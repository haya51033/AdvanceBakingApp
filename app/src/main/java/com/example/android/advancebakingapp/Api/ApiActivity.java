package com.example.android.advancebakingapp.Api;

import android.support.v7.app.AppCompatActivity;

import com.example.android.advancebakingapp.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiActivity extends AppCompatActivity{
    public static final String BASE_URL= "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

}
