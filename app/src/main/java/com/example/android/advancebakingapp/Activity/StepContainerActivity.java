package com.example.android.advancebakingapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.advancebakingapp.Adapter.StepAdapter;
import com.example.android.advancebakingapp.Model.Step;
import com.example.android.advancebakingapp.R;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import static java.security.AccessController.getContext;

public class StepContainerActivity extends AppCompatActivity {

    private boolean mTwoPane;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


            if(findViewById(R.id.details_linear_layout) != null) {
                if(savedInstanceState==null) {

                    FragmentManager fragmentManager = getSupportFragmentManager();

                    mTwoPane = true;
                    RecipeActivity recipeFragment = new RecipeActivity();

                    fragmentManager.beginTransaction()
                            .add(R.id.recipeContainer, recipeFragment)
                            .commit();

                    StepFragment stepFragment = new StepFragment();
                    fragmentManager.beginTransaction()
                            .add(R.id.stepContainer, stepFragment)
                            .commit();


                    StepDescriptionFragment stepDescriptionFragment= new StepDescriptionFragment();
                    fragmentManager.beginTransaction()
                            .add(R.id.stepContainer2,stepDescriptionFragment)
                            .commit();
                }
                else {

                }

            }

            // Only create new fragments when there is no previously saved state
            else {
                    setContentView(R.layout.step_container);
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    if((!isTablet(getApplicationContext()))  && isLandscape()){
                        StepFragment stepFragment = new StepFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.stepContainer, stepFragment)
                                .commit();


                   }
                    else {
                        StepFragment stepFragment = new StepFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.stepContainer, stepFragment)
                                .commit();

                        StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();
                        fragmentManager.beginTransaction()
                                .add(R.id.stepContainer2, stepDescriptionFragment)
                                .commit();

                       // exitFullscreen();
                    }




            }

    }


    public boolean isLandscape()
    {

        int orientation = getApplicationContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        return false;
    }

    public boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);

        return (xlarge || large);
    }


}



