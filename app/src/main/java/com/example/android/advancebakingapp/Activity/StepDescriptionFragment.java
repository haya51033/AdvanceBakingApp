package com.example.android.advancebakingapp.Activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.advancebakingapp.Model.Ingredient;
import com.example.android.advancebakingapp.Model.Step;
import com.example.android.advancebakingapp.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

public class StepDescriptionFragment extends Fragment {
    ArrayList<Step> stepsArrayList= new ArrayList<>();
    Step step;
    ImageView noVideoImageView;
    int stepIndex;
    ArrayList<Step> saveSteps = new ArrayList<>();
    public TextView videoDescription;
    public Uri mMediaUri;
    ImageView nextButton;
    ImageView previousButton;
    View rootView;
    ArrayList<Ingredient> ingredients = new ArrayList<>();

    public StepDescriptionFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_step_description, container, false);
        //root
        final FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.step_desc_frame_root);

        startActivity();
        return rootView;
    }


    public void startActivity(){
        Intent intent = getActivity().getIntent();
        final Bundle args = intent.getBundleExtra("BUNDLE");
        if(args != null) {
           // stepsArrayList = (ArrayList<Step>) args.getSerializable("stepsArrayList");
            //steps_list
            stepsArrayList = (ArrayList<Step>) args.getSerializable("steps_list");
            ingredients = (ArrayList<Ingredient>) args.getSerializable("ingredients_list");

            saveSteps.addAll(stepsArrayList);
           stepIndex = args.getInt("SELECTED_INDEX", 0);
            if (saveSteps.size() != 0) {
                step = saveSteps.get(stepIndex);
               // step = saveSteps.get(0);
                if(step.getDescription() != null) {
                    videoDescription = (TextView) rootView.findViewById(R.id.stepDescriptionTextView);
                    videoDescription.setText(step.getDescription());
                }
            }
            nextButton = (ImageView) rootView.findViewById(R.id.iv_nextStep);
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
            previousButton = (ImageView) rootView.findViewById(R.id.iv_backStep);
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
      //  Intent intent = new Intent(getActivity(), RecipeFragment.class);
        Intent intent = new Intent(getActivity(), StepContainerActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("steps_list",stepsArrayList);
        args.putInt("SELECTED_INDEX",stepIndex + 1);
        args.putSerializable("ingredients_list",ingredients);
        intent.putExtra("BUNDLE", args);
        getActivity().finish();
        startActivity(intent);
    }

    public void backButton(){
       // Intent intent = new Intent(getActivity(), RecipeFragment.class);
        Intent intent = new Intent(getActivity(), StepContainerActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("steps_list",stepsArrayList);
        args.putInt("SELECTED_INDEX",stepIndex - 1);
        args.putSerializable("ingredients_list",ingredients);
        intent.putExtra("BUNDLE", args);
        getActivity().finish();
        startActivity(intent);
    }
}
