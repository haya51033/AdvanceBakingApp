package com.example.android.advancebakingapp.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.advancebakingapp.Model.Step;
import com.example.android.advancebakingapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.app.NotificationCompat;

import android.widget.Toast;

import java.util.ArrayList;

public class StepFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener{
    ArrayList<Step> stepsArrayList= new ArrayList<>();
    Step step;
    ImageView noVideoImageView;
    String SELECTED_POSITION;
    String SELECTED_STATE;
    int stepIndex;
    ArrayList<Step> saveSteps = new ArrayList<>();
    public long position = -1;
    static long pausePosition = -1;
    public SimpleExoPlayer mExoPlayer;
    public SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private static final String TAG = RecipeActivity.class.getSimpleName();
    public Uri mMediaUri;
    View rootView;
    String returnedScreen;
    FrameLayout frameLayout;
     public static boolean playWhenReady = true;
    @Override
    public void onStop()
    {
        super.onStop();
        if (Util.SDK_INT > 23)
        {
            releasePlayer();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        pausePosition = -1;
        playWhenReady = true;

    }

    @Override
    public void onDestroyView()
    {
        if (mExoPlayer != null)
        {
            position = mExoPlayer.getCurrentPosition();


        }
        super.onDestroyView();
    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(SELECTED_POSITION,pausePosition);

        if(mExoPlayer != null){
            playWhenReady = mExoPlayer.getPlayWhenReady();
        }
        savedInstanceState.putBoolean(SELECTED_STATE, playWhenReady);

        super.onSaveInstanceState(savedInstanceState);

    }


    public StepFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            super.onActivityCreated(savedInstanceState);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_step, container, false);
        //root
        frameLayout = (FrameLayout) rootView.findViewById(R.id.step_frame_root);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.ic_launcher_background));


        if(savedInstanceState != null) {
            boolean testState = savedInstanceState.getBoolean(SELECTED_STATE);
                playWhenReady =testState;

            long testPosition =savedInstanceState.getLong(SELECTED_POSITION);
            if(testPosition >=0){
                pausePosition = testPosition;
            }
        }

        enterFullScreen();

        Intent intent = getActivity().getIntent();
        final Bundle args = intent.getBundleExtra("BUNDLE");
        if(args != null) {
            stepsArrayList = (ArrayList<Step>) args.getSerializable("steps_list");
            saveSteps.addAll(stepsArrayList);

            stepIndex = args.getInt("SELECTED_INDEX", 0);
            if (saveSteps.size() != 0) {
                step = saveSteps.get(stepIndex);
                if(!step.getVideoURL().equals("") || !step.getThumbnailURL().equals("")){

                    initializeMediaSession();
                    if(step.getVideoURL().equals(""))
                        mMediaUri = Uri.parse(step.getThumbnailURL());
                    if(step.getThumbnailURL().equals(""))
                        mMediaUri = Uri.parse(step.getVideoURL());
                    if(!step.getVideoURL().equals("") && !step.getThumbnailURL().equals(""))
                        mMediaUri = Uri.parse(step.getVideoURL());
                  initializePlayer(mMediaUri);
                }
                else {
                    noVideoImageView = (ImageView) rootView.findViewById(R.id.noVideoImageView);
                    noVideoImageView.setVisibility(View.VISIBLE);
                    mPlayerView.setVisibility(View.GONE);
                    Picasso.with(getContext()).load(R.drawable.baking_app_ico).into(noVideoImageView);
                    playWhenReady =true;
                }
            }

        }





        return rootView;
    }

    public boolean isLandscape()
    {
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        return false;
    }
    static boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public boolean enterFullScreen(){
        if(isLandscape() && !isTablet(getContext())){
            Toast.makeText(getActivity(),"Full Screen Mode." , Toast.LENGTH_SHORT).show();
            frameLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


            getActivity().getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            returnedScreen = "H";
            return true;
        }

        else
        {
            returnedScreen = "V";
            return false;}
    }


    @Override
    public void onPause() {
        if (mExoPlayer != null){
            pausePosition = mExoPlayer.getCurrentPosition();
            playWhenReady = mExoPlayer.getPlayWhenReady();
        }

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        super.onPause();
        if (Util.SDK_INT <= 23)
        {
            releasePlayer();
        }
    }

    /**
     * Shows Media Style notification, with actions that depend on the current MediaSession
     * PlaybackState.
     * @param state The PlaybackState of the MediaSession.
     */
    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());

        int icon;
        String play_pause;
        if(state.getState() == PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(),
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (getContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (getContext(), 0, new Intent(getContext(), StepContainerActivity.class), 0);

        builder.setContentTitle(getString(R.string.guess))
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.baking_app_ico)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(0,1));


        mNotificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }
    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */

    private void initializePlayer(Uri mediaUri)
    {
            if (mExoPlayer == null) {
                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
                mPlayerView.setPlayer(mExoPlayer);
                if (pausePosition != -1) {
                    mExoPlayer.seekTo(pausePosition);
                    pausePosition = -1;
                }
                else {
                    mExoPlayer.seekTo(0);
                }
                // Set the ExoPlayer.EventListener to this activity.
                mExoPlayer.addListener(this);

                // Prepare the MediaSource.
                String userAgent = Util.getUserAgent(getContext(), "AdvanceBakingApp");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);


                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(playWhenReady);
                playWhenReady = true;

            }
            else {
            }
        }



    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    @Override
    public void onClick(View v) {
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null)
        {
            mNotificationManager.cancelAll();
           // playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.stop();
            mExoPlayer.release();
        }

        mExoPlayer = null;

    }
    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        if(mExoPlayer != null){
            releasePlayer();
            mMediaSession.setActive(false);


        }
        super.onDestroy();
    }

    // ExoPlayer Event Listeners

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);

        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }



    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
            //      mExoPlayer.seekTo(position);

        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            MediaButtonReceiver.handleIntent(mMediaSession, intent);




        }
    }


}

