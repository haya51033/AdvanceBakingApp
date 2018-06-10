package com.example.android.advancebakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.advancebakingapp.Model.Ingredient;
import com.example.android.advancebakingapp.Model.Step;
import com.example.android.advancebakingapp.R;

import java.util.ArrayList;

public class StepAdapter extends
        RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {
    private Context context;
    private ArrayList<Step> mSteps;
  //  private StepAdapter.StepOnClickHandler mStepOnClickHandler;
    private StepOnClickHandler mStepOnClickHandler;


    public StepAdapter(StepOnClickHandler stepOnClickHandler) {
        mStepOnClickHandler = stepOnClickHandler;

    }

    public void setStepsData(ArrayList<Step> stepData) {
        mSteps = stepData;
        notifyDataSetChanged();
    }



    @Override
    public StepAdapter.StepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_step, parent, false);

        // Return a new holder instance
        StepAdapterViewHolder viewHolder = new StepAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepAdapter.StepAdapterViewHolder viewHolder, int position) {
        Step step = mSteps.get(position);
         //Button button = viewHolder.mButton;
        TextView button = viewHolder.mButton;
        button.setText(step.getShortDescription());
    }


    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mSteps == null) {
            return 0;
        }

        return mSteps.size();
    }


    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //  public final Button mButton;
        public final TextView mButton;
          public StepAdapterViewHolder(View view) {
            super(view);

              // mButton = (Button) view.findViewById(R.id.step_button);
              mButton = (TextView) view.findViewById(R.id.step_button);
            view.setOnClickListener(this);
        }


        @Override

        public void onClick(View view) {
            int position = getAdapterPosition();
            Step selectedStep = mSteps.get(position);
            mStepOnClickHandler.onClickStep(selectedStep);
        }

    }

    public interface StepOnClickHandler {

        void onClickStep(Step step);
    }



}
