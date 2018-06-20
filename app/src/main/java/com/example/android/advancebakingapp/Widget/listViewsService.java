package com.example.android.advancebakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.advancebakingapp.MainActivity;
import com.example.android.advancebakingapp.Model.Ingredient;
import com.example.android.advancebakingapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class listViewsService extends RemoteViewsService
{
    public static final String TAG = "ListViewsService";


    /**
     * @param intent intent that triggered this service
     * @return new ListViewsFactory Object with the appropriate implementation
     */
    public ListViewsFactory onGetViewFactory(Intent intent)
    {
        return new ListViewsFactory(this.getApplicationContext());
    }

}

class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    private Context mContext;
    private ArrayList<Ingredient> ingredients;

    public ListViewsFactory(Context mContext)
    {
        this.mContext = mContext;
    }

    @Override
    public void onCreate()
    {

    }

    //Very Important,this is the place where the data is being changed each time by the adapter.
    @Override
    public void onDataSetChanged()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String json = preferences.getString(MainActivity.SHARED_PREFS_KEY, "");
        if (!json.equals("")) {
            Gson gson = new Gson();
            ingredients = gson.fromJson(json, new TypeToken<ArrayList<Ingredient>>() {
            }.getType());
        }
    }

    @Override
    public void onDestroy()
    { }

    @Override
    public int getCount()
    {
        if (ingredients == null)
            return 0;
        return ingredients.size();
    }

    /**
     * @param position position of current view in the ListView
     * @return a new RemoteViews object that will be one of many in the ListView
     */
    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget_ingrediant);
        views.setTextViewText(R.id.text_view_recipe_widget,
                ingredients.get(position).getIngredient() + " ("
                   + ingredients.get(position).getQuantity() +" "
                   + ingredients.get(position).getMeasure() + ").");
        return views;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}
