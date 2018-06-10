package com.example.android.advancebakingapp.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.advancebakingapp.R;

public class StepContainerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_container);

        // Only create new fragments when there is no previously saved state
        if(savedInstanceState == null) {
            StepFragment stepFragment = new StepFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.stepContainer, stepFragment)
                    .commit();


            StepDescriptionFragment stepDescriptionFragment= new StepDescriptionFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.stepContainer2,stepDescriptionFragment)
                    .commit();
        }
    }
}
