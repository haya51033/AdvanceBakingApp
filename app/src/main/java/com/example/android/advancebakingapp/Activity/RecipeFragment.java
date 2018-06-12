package com.example.android.advancebakingapp.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.advancebakingapp.R;


public class RecipeFragment extends AppCompatActivity {
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(findViewById(R.id.details_linear_layout) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {
                StepFragment stepFragment = new StepFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.stepContainer, stepFragment)
                        .commit();


                StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.stepContainer2, stepDescriptionFragment)
                        .commit();

            }
        }else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }

        if(savedInstanceState == null) {

            RecipeActivity recipeFragment = new RecipeActivity();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipeContainer, recipeFragment)
                    .commit();
        }
        else {


            setContentView(R.layout.step_container);
            StepFragment stepFragment = new StepFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.stepContainer, stepFragment)
                    .commit();


            StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.stepContainer2, stepDescriptionFragment)
                    .commit();

        }
    }
}
