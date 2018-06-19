package com.example.android.advancebakingapp.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;


import com.example.android.advancebakingapp.Model.Ingredient;

import java.util.ArrayList;

public class WidgetUpdateService extends IntentService
{
    public static final String WIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    private Ingredient[]mIngredients;
    ArrayList<Ingredient> parcelable = new ArrayList<>();


    public WidgetUpdateService()
    {
        super("WidgetUpdateService");
    }



    @Override
    protected void onHandleIntent( Intent intent)
    {

        if (intent != null && intent.getAction().equals(WIDGET_UPDATE_ACTION)) {

            final Bundle bundle = intent.getBundleExtra("BUNDLE");
            parcelable =(ArrayList<Ingredient>) bundle.getSerializable("INGREDIENTS");

            if (parcelable != null)
            {
                mIngredients = new Ingredient[parcelable.size()];
                for (int i = 0; i < parcelable.size(); i++)
                {
                    mIngredients[i] = (Ingredient) parcelable.get(i);
                }
            }


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            BakingAppWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetIds,mIngredients);
       }
    }
}
