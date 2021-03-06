package com.example.android.advancebakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.advancebakingapp.Activity.RecipeActivity;
import com.example.android.advancebakingapp.Activity.RecipeFragment;
import com.example.android.advancebakingapp.Adapter.RecipeAdapter;
import com.example.android.advancebakingapp.Api.ApiActivity;
import com.example.android.advancebakingapp.Api.RecipeApi;
import com.example.android.advancebakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.advancebakingapp.Model.Ingredient;
import com.example.android.advancebakingapp.Model.Recipe;
import com.example.android.advancebakingapp.Model.Step;
import com.example.android.advancebakingapp.Widget.BakingAppWidgetProvider;
import com.example.android.advancebakingapp.Widget.WidgetUpdateService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeOnClickHandler {

    private ArrayList<Recipe> mRecipes;
    public RecyclerView rvRecipe;
    public LinearLayoutManager mLinearLayoutManager;
    public ArrayList<Recipe> arrayList;
    public RecipeAdapter recipeAdapter;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    int positionIndex;
    int lastFirstVisiblePosition;
   public final ArrayList<Ingredient> ingredients = new ArrayList<>();
   public final ArrayList<Step> steps = new ArrayList<>();
   public ArrayList <Recipe> arr;
    private static final String KEY_RECIPES = "recipes";
    public static final String BUNDLE = "bundle";
    public static final String INGREDIENTS = "ingredients";
    private Recipe[] mRecipesArr;
    public static final String SHARED_PREFS_KEY = "SHARED_PREFS_KEY";
    Gson gson;
    String json;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;



    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }



    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                mLinearLayoutManager.onSaveInstanceState());
        lastFirstVisiblePosition = ((LinearLayoutManager)rvRecipe.getLayoutManager())
                .findFirstVisibleItemPosition();
        savedInstanceState.putInt("INT_VALUE",lastFirstVisiblePosition);
        savedInstanceState.putParcelableArray(KEY_RECIPES, mRecipesArr);
        super.onSaveInstanceState(savedInstanceState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState);
            positionIndex = savedInstanceState.getInt("INT_VALUE");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null){
            Parcelable[] recipes = savedInstanceState.getParcelableArray(KEY_RECIPES);
            if (recipes != null)
            {
                mRecipesArr = new Recipe[recipes.length];
                for (int i = 0; i < recipes.length; i++)
                {
                    mRecipesArr[i] = (Recipe) recipes[i];
                }
            }
        }

        rvRecipe = (RecyclerView) findViewById(R.id.recyclerView_recipes);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        ApiActivity rB = new ApiActivity();
        RecipeApi service =rB.retrofit.create(RecipeApi.class);

        Call<List<Recipe>> call2 = service.GetRecipe();
        call2.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if(response.isSuccessful())
                {
                    mRecipes = (ArrayList<Recipe>) response.body();
                    if (mRecipes != null)
                    {
                        final List<Recipe> movies = mRecipes;
                        mRecipesArr = mRecipes.toArray(new Recipe[mRecipes.size()]);
                        arrayList = new ArrayList<>();//create a list to store the objects
                        arrayList.addAll(movies);
                        configureRecyclerView(arrayList);

                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"no Data..",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                String tt = t.getMessage();

                Toast.makeText(getApplicationContext(),tt,Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void configureRecyclerView(ArrayList mRecipes) {
        rvRecipe = (RecyclerView) findViewById(R.id.recyclerView_recipes);
        rvRecipe.setHasFixedSize(true);
        rvRecipe.setLayoutManager( new LinearLayoutManager(getApplicationContext()));
        recipeAdapter = new RecipeAdapter(this);
        recipeAdapter.setRecipesData(mRecipes);
        rvRecipe.setAdapter(recipeAdapter);
        ((LinearLayoutManager)rvRecipe.getLayoutManager()).scrollToPosition(positionIndex);
    }


    @Override
    public void onClickRecipe(Recipe recipe) {
       //ingredients = new ArrayList<>();
       arr = new ArrayList<>();
        arr.add(recipe);
        if(ingredients.size()!=0 || steps.size()!=0){
            ingredients.clear();
            steps.clear();
        }
        ingredients.addAll(arr.get(0).getIngredients());
        steps.addAll(arr.get(0).getSteps());



        Intent intent1 = new Intent(getApplicationContext(), BakingAppWidgetProvider.class);
        Bundle args1 = new Bundle();
        args1.putSerializable("INGREDIENTS",ingredients);
        intent1.putExtra("BUNDLE", args1);
        this.sendBroadcast(intent1);

        SharedPrefListner(ingredients);

        Intent intent = new Intent(getApplicationContext(), RecipeFragment.class);
        Bundle args = new Bundle();
        args.putSerializable("ingredients_list",ingredients);
        args.putSerializable("steps_list",steps);
        intent.putExtra("BUNDLE", args);
        startActivity(intent);

    }

    public void SharedPrefListner(ArrayList<Ingredient> ingredients){
        gson = new Gson();
        json = gson.toJson(ingredients);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = prefs.edit();
        editor.putString(SHARED_PREFS_KEY, json).commit();


    }



}
