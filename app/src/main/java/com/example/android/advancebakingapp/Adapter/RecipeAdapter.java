package com.example.android.advancebakingapp.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.advancebakingapp.Model.Recipe;
import com.example.android.advancebakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends
        RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private Context context;
    private ArrayList<Recipe> mRecipes;
    private RecipeOnClickHandler mRecipeOnClickHandler;


    public RecipeAdapter(RecipeOnClickHandler recipeOnClickHandler) {
        mRecipeOnClickHandler = recipeOnClickHandler;

    }

    public void setRecipesData(ArrayList<Recipe> recipeData) {
        mRecipes = recipeData;
        notifyDataSetChanged();
    }



    @Override

    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_recipe, parent, false);

        // Return a new holder instance
        RecipeAdapterViewHolder viewHolder = new RecipeAdapterViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeAdapterViewHolder viewHolder, int position) {

        Recipe recipe = mRecipes.get(position);
        String imageUrl = mRecipes.get(position).getImage();
        TextView tv = viewHolder.mTextView;
        tv.setText(recipe.getName());

        if (imageUrl.equals("") && imageUrl.isEmpty())
        {
            Picasso.with(context).load(R.drawable.baking_app_ico).into(viewHolder.mImageView);
        }
        else
        {
            Picasso.with(context).load(imageUrl).into(viewHolder.mImageView);
        }

    }



    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        if(mRecipes == null) {
            return 0;
        }

        return mRecipes.size();
    }


    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;
        public final TextView mTextView;
        public RecipeAdapterViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.iv_recipe_image);
            mTextView = (TextView) view.findViewById(R.id.tv_recipe_name);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Recipe selectedRecipe = mRecipes.get(position);
            mRecipeOnClickHandler.onClickRecipe(selectedRecipe);
        }

    }

    public interface RecipeOnClickHandler {
        void onClickRecipe(Recipe recipe);
    }



}
