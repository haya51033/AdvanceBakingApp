package com.example.android.advancebakingapp.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.advancebakingapp.Adapter.StepAdapter;
import com.example.android.advancebakingapp.Model.Step;
import com.example.android.advancebakingapp.R;

public class StepContainerActivity extends AppCompatActivity {

    private boolean mTwoPane;
    public TextView textViewStep;
    StepAdapter.StepOnClickHandler mCallBack;
    StepAdapter.StepAdapterViewHolder mCall;




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
                    Toast.makeText(getApplicationContext(), "else.", Toast.LENGTH_LONG).show();

                }

            }

            // Only create new fragments when there is no previously saved state
            else {
                if (savedInstanceState == null) {
                    setContentView(R.layout.step_container);
                    Toast.makeText(getApplicationContext(), "else.", Toast.LENGTH_LONG).show();
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    StepFragment stepFragment = new StepFragment();
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


    }






/*  textViewStep = (TextView) findViewById(R.id.step_button);

        textViewStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTwoPane){
                    StepFragment stepFragment = new StepFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.stepContainer, stepFragment)
                            .commit();


                    StepDescriptionFragment stepDescriptionFragment= new StepDescriptionFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.stepContainer2,stepDescriptionFragment)
                            .commit();

                }

            }
        });*/