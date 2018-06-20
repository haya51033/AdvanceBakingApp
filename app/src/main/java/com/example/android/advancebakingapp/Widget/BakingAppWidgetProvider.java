package com.example.android.advancebakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.advancebakingapp.BuildConfig;
import com.example.android.advancebakingapp.MainActivity;
import com.example.android.advancebakingapp.Model.Ingredient;
import com.example.android.advancebakingapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider
{
    public static Ingredient[] mIngredients;
    public  ArrayList<Ingredient> parcelables;
    public static ArrayList<Ingredient> ingredients1= new ArrayList<>();
    public static final String WIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    private static final String TAG = "WidgetProvider";

    public BakingAppWidgetProvider()
    {

    }

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        super.onReceive(context, intent);
        final Bundle bundle = intent.getBundleExtra("BUNDLE");
        if(bundle != null) {
            parcelables = (ArrayList<Ingredient>) bundle.getSerializable("INGREDIENTS");
            ingredients1.addAll(parcelables);
        }
        else {
        }

        intent.setAction(WIDGET_UPDATE_ACTION);
        if (BakingAppWidgetProvider.WIDGET_UPDATE_ACTION.equals(intent.getAction()))
        {
            if (BuildConfig.DEBUG)
                Log.d(BakingAppWidgetProvider.TAG, "onReceive "
                        + BakingAppWidgetProvider.WIDGET_UPDATE_ACTION);
            final AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            final ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), BakingAppWidgetProvider.class.getName());
            final int[] appWidgetIds = appWidgetManager
                    .getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    /**
     * method will update the ListView each time the user opens the IngredientsActivity,
     * meaning that the widget will always show the last IngredientsActivity Ingredients[] that the user seen.
     * @param context app context
     * @param appWidgetManager  app WidgetManger
     * @param appWidgetIds ids which will be updated
     * @param ingredients the ingredients that will fill the ListView
     *
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetIds[], Ingredient[] ingredients)
    {
        mIngredients = ingredients;
        for (int appWidgetId : appWidgetIds)
        {
            Intent intent = new Intent(context, listViewsService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
            views.setRemoteAdapter(R.id.list_view_widget, intent);
            ComponentName component = new ComponentName(context, BakingAppWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
            appWidgetManager.updateAppWidget(component, views);

        }
    }

    /**
     * the widget will update itself each time the IngredientsActivity will open,meaning that this method
     * is unnecessary in our implementation.
     * @param context app context
     * @param appWidgetManager the application WidgetManager
     * @param appWidgetIds ids which will be updated
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        if (BuildConfig.DEBUG)
            Log.d(BakingAppWidgetProvider.TAG, "onUpdate");

        Intent serviceIntent = new Intent(context,WidgetUpdateService.class);
        Bundle args1 = new Bundle();
        args1.putSerializable("INGREDIENTS",parcelables);
        serviceIntent.putExtra("BUNDLE", args1);
        context.sendBroadcast(serviceIntent);
        serviceIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        context.startService(serviceIntent);
    }


    @Override
    public void onEnabled(Context context)
    { }

    @Override
    public void onDisabled(Context context)
    { }




}

