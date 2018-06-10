package com.example.android.advancebakingapp.Api;

import com.example.android.advancebakingapp.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RecipeApi {
    @Headers("Content-Type: application/json")
    @GET("baking.json")
    Call<List<Recipe>> GetRecipe();

}
