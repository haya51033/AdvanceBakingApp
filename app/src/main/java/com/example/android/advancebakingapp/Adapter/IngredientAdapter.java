package com.example.android.advancebakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.android.advancebakingapp.Model.Ingredient;
import com.example.android.advancebakingapp.R;

import java.util.ArrayList;

public class IngredientAdapter extends
        RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {
    private Context context;
    private ArrayList<Ingredient> mIngredients;
    private IngredientAdapter.IngredientOnClickHandler mIngredientOnClickHandler;


    public IngredientAdapter(IngredientAdapter.IngredientOnClickHandler ingredientOnClickHandler) {
        mIngredientOnClickHandler = ingredientOnClickHandler;

    }

    public void setIngredientsData(ArrayList<Ingredient> ingredientData) {
        mIngredients = ingredientData;
        notifyDataSetChanged();
    }



    @Override
    public IngredientAdapter.IngredientAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.activity_ingredient, parent, false);

        // Return a new holder instance
        IngredientAdapter.IngredientAdapterViewHolder viewHolder =
                new IngredientAdapter.IngredientAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.IngredientAdapterViewHolder viewHolder, int position) {
        Ingredient ingredient  = mIngredients.get(position);
       // Button button = viewHolder.mButton;
        //button.setText(ingredient.getIngredient());
    }



    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mIngredients == null) {
            return 0;
        }

        return mIngredients.size();
    }


    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      //  public final Button mButton;
        public IngredientAdapterViewHolder(View view) {
            super(view);

         //   mButton = (Button) view.findViewById(R.id.ingredient_button);
            view.setOnClickListener(this);
        }


        @Override

        public void onClick(View view) {
            int position = getAdapterPosition();
            Ingredient selectedIngredient = mIngredients.get(position);
            mIngredientOnClickHandler.onClickRecipe(selectedIngredient);
        }

    }

    public interface IngredientOnClickHandler {
        void onClickRecipe(Ingredient ingredient);
    }



}
