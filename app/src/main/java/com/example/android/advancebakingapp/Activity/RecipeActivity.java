package com.example.android.advancebakingapp.Activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.advancebakingapp.Adapter.StepAdapter;
import com.example.android.advancebakingapp.Model.Ingredient;
import com.example.android.advancebakingapp.Model.Step;
import com.example.android.advancebakingapp.R;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity
        implements StepAdapter.StepOnClickHandler{

    public ArrayList<Ingredient> ingredients;
    public ArrayList<Step> steps;
    public ArrayList<Step> step1;

    String videoURL = "";
    String thumbnailURL = "";
    String description = "";

    TextView tv_ingredients;
    Button stepButton;

    LinearLayout mLinearLayout;
    LinearLayout mLinearLayoutHeader;

    LinearLayout mLinearLayout2;
    LinearLayout mLinearLayoutHeader2;


    RecyclerView stepsRecyclerView;
    StepAdapter stepAdapter ;
    String collaps = "true";
    String collaps1 = "true";

    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    private static final String STRING_VALUE = "string_value";
    private static final String STRING_VALUE1 = "string_value1";

    int positionIndex;
    int lastFirstVisiblePosition;
    public LinearLayoutManager mLinearLayoutManager;
    public ArrayList<Step> stepArrayList =new ArrayList<>();
    int stepIndex=0;

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STRING_VALUE, collaps);
        savedInstanceState.putString(STRING_VALUE1, collaps1);

        savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                mLinearLayoutManager.onSaveInstanceState());
        lastFirstVisiblePosition = ((LinearLayoutManager)stepsRecyclerView.getLayoutManager())
                .findFirstCompletelyVisibleItemPosition();
        savedInstanceState.putInt("INT_VALUE",lastFirstVisiblePosition);
        super.onSaveInstanceState(savedInstanceState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState);
            stepsRecyclerView = (RecyclerView)findViewById(R.id.recipe_details_steps);
            mLinearLayout = (LinearLayout) findViewById(R.id.expandedLayout);
            mLinearLayoutHeader = (LinearLayout) findViewById(R.id.header);
            mLinearLayout2 = (LinearLayout) findViewById(R.id.expandedLayout2);
            mLinearLayoutHeader2 = (LinearLayout) findViewById(R.id.header2);
            collaps = savedInstanceState.getString(STRING_VALUE);
            collaps1 = savedInstanceState.getString(STRING_VALUE1);
            positionIndex = savedInstanceState.getInt("INT_VALUE");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        stepsRecyclerView = (RecyclerView)findViewById(R.id.recipe_details_steps);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayout = (LinearLayout) findViewById(R.id.expandedLayout);
        mLinearLayoutHeader = (LinearLayout) findViewById(R.id.header);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.expandedLayout2);
        mLinearLayoutHeader2 = (LinearLayout) findViewById(R.id.header2);
        if(savedInstanceState != null)
        {
            if (collaps.equals("true"))
            {
               collapse();
            }
            else if (collaps.equals("false")){
                expand();
            }
            else
            {
                collapse();
            }

            if (collaps1.equals("true"))
            {

               // collapse1();
                expand1();
                expand();
                Toast.makeText(getApplicationContext(),"truee",Toast.LENGTH_LONG).show();

            }
            else if (collaps1.equals("false")){
                Toast.makeText(getApplicationContext(),"faalse",Toast.LENGTH_LONG).show();
                expand1();
            }
            else
            {
                collapse1();
            }

        }
        else
        {
            collapse();
            collapse1();
        }

        //set visibility to GONE
        if(mLinearLayout != null) {
            mLinearLayout.setVisibility(View.GONE);

        }


        mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout.getVisibility()==View.GONE){
                    expand();
                }else{
                    collapse();
                }
            }
        });

        /////////////

        //set visibility to GONE
        if(mLinearLayout2 != null) {
            mLinearLayout2.setVisibility(View.GONE);
        }

        mLinearLayoutHeader2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout2.getVisibility()==View.GONE){
                    expand1();
                }else{
                    collapse1();
                }
            }
        });

        Intent intent = getIntent();
        final Bundle args = intent.getBundleExtra("BUNDLE");
        if(args != null)
        {
            ingredients = (ArrayList<Ingredient>) args.getSerializable("ingredients_list");
            if (ingredients.size() != 0)
            {
                tv_ingredients = (TextView) findViewById(R.id.recipe_details_ingredients);
                tv_ingredients.setText("");
                for(Ingredient i:ingredients){
                    tv_ingredients.append("- "+i.getIngredient() +" ("+i.getQuantity()+" "+i.getMeasure()+")\n");
                }

            }
            steps= (ArrayList<Step>) args.getSerializable("steps_list");
            if(steps.size()!=0){
                configureRecyclerView(steps);
            }
        }
    }

    private void configureRecyclerView(ArrayList steps) {
        stepsRecyclerView = (RecyclerView) findViewById(R.id.recipe_details_steps);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setLayoutManager( new LinearLayoutManager(this));
        stepAdapter = new StepAdapter(this);
        stepAdapter.setStepsData(steps);
        stepsRecyclerView.setAdapter(stepAdapter);
        ((LinearLayoutManager)stepsRecyclerView.getLayoutManager()).scrollToPosition(positionIndex);
    }


    @Override
    public void onClickStep(Step step) {
        stepArrayList.addAll(steps);
        stepIndex = stepArrayList.indexOf(step);
        step1 = new ArrayList<>();
        step1.add(step);
        Intent intent = new Intent(getApplicationContext(), StepActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("step_list",step1);
        args.putSerializable("stepsArrayList",stepArrayList);
        args.putInt("SELECTED_INDEX",stepIndex);
        intent.putExtra("BUNDLE", args);
        startActivity(intent);
    }

    private void expand() {
        //set Visible
        collaps="false";
        mLinearLayout.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mLinearLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
        mAnimator.start();
    }

     private void collapse() {
        collaps = "true";
        int finalHeight = mLinearLayout.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

            @Override
            public void onAnimationStart(Animator animator) {

            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    ///////////////
    private void expand1() {
        collaps1="false";

        //set Visible
        mLinearLayout2.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mLinearLayout2.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator1(0, mLinearLayout2.getMeasuredHeight());
        mAnimator.start();
    }

    private void collapse1() {
        collaps1="true";
        int finalHeight = mLinearLayout2.getHeight();
        ValueAnimator mAnimator = slideAnimator1(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mLinearLayout2.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

            @Override
            public void onAnimationStart(Animator animator) {

            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator1(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mLinearLayout2.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout2.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


}
