package com.example.android.advancebakingapp.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.advancebakingapp.Model.Ingredient;
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
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import android.support.v4.content.ContextCompat;

import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.app.NotificationCompat;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity implements View.OnClickListener, ExoPlayer.EventListener{
    ArrayList<Step> stepList;
    ArrayList<Step> stepsArrayList= new ArrayList<>();
    Step step;
    ImageView noVideoImageView;
    String SELECTED_POSITION;
    int stepIndex;
    ArrayList<Step> saveSteps = new ArrayList<>();
    public long position;
    public TextView videoDescription;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private static final String TAG = RecipeActivity.class.getSimpleName();
    public Uri mMediaUri;
    ImageView nextButton;
    ImageView previousButton;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(SELECTED_POSITION,position);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState!=null) {
            super.onRestoreInstanceState(savedInstanceState);
            position = savedInstanceState.getLong(SELECTED_POSITION);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        mPlayerView = (SimpleExoPlayerView) findViewById(R.id.playerView);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.ic_launcher_background));
        startActivity();
    }


    public void startActivity(){
        Intent intent = getIntent();
        final Bundle args = intent.getBundleExtra("BUNDLE");
        if(args != null) {
            stepsArrayList = (ArrayList<Step>) args.getSerializable("stepsArrayList");
            saveSteps.addAll(stepsArrayList);
            stepIndex = args.getInt("SELECTED_INDEX", 0);
            if (saveSteps.size() != 0) {
                step = saveSteps.get(stepIndex);
                if(step.getDescription() != null) {
                    videoDescription = (TextView) findViewById(R.id.stepDescriptionTextView);
                    videoDescription.setText(step.getDescription());
                }
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
                    noVideoImageView = (ImageView) findViewById(R.id.noVideoImageView);
                    noVideoImageView.setVisibility(View.VISIBLE);
                    mPlayerView.setVisibility(View.GONE);
                   // noVideoImageView.setImageResource(R.drawable.ic_launcher_background);
                }
            }
            nextButton = (ImageView) findViewById(R.id.iv_nextStep);
            if (stepIndex == saveSteps.size() - 1)
                nextButton.setVisibility(View.GONE);
            else {
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextButton();
                    }
                });
            }
            previousButton = (ImageView)findViewById(R.id.iv_backStep);
            if(stepIndex == 0)
                previousButton.setVisibility(View.GONE);
            else {
                previousButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        backButton();
                    }
                });
            }
        }
    }
    public void nextButton(){
        Intent intent = new Intent(getApplicationContext(), StepActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("stepsArrayList",stepsArrayList);
        args.putInt("SELECTED_INDEX",stepIndex + 1);
        intent.putExtra("BUNDLE", args);
        finish();
        startActivity(intent);
    }

    public void backButton(){
        Intent intent = new Intent(getApplicationContext(), StepActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("stepsArrayList",stepsArrayList);
        args.putInt("SELECTED_INDEX",stepIndex - 1);
        intent.putExtra("BUNDLE", args);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
        }
    }

    /**
     * Shows Media Style notification, with actions that depend on the current MediaSession
     * PlaybackState.
     * @param state The PlaybackState of the MediaSession.
     */
    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

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
                MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, StepActivity.class), 0);

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


        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }
    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            Toast.makeText(getApplicationContext(),"yess", Toast.LENGTH_LONG).show();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "AdvanceBakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
           /* if (position != C.TIME_UNSET){
               mExoPlayer.seekTo(position);
            }
            long tt = position;*/
            if (position != C.TIME_UNSET)
                mExoPlayer.seekTo(position);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
        else {
            Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_LONG).show();
        }
    }


    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, TAG);

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
        mNotificationManager.cancelAll();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
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
      //  position = mExoPlayer.getCurrentPosition();
       /* if (position != C.TIME_UNSET){
            mExoPlayer.seekTo(position);
        }*/
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
