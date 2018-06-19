package com.example.android.advancebakingapp;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.advancebakingapp.Activity.RecipeActivity;
import com.example.android.advancebakingapp.Adapter.RecipeAdapter;
import com.example.android.advancebakingapp.Api.ApiActivity;
import com.example.android.advancebakingapp.Api.RecipeApi;
import com.example.android.advancebakingapp.Model.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.Espresso.onView;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityTest {

    private ArrayList<Recipe> mRecipes;
    public RecyclerView rvRecipe;
    // public GridLayoutManager mGridLayoutManager;
    public LinearLayoutManager mLinearLayoutManager;
    public ArrayList<Recipe> arrayList;
    public RecipeAdapter recipeAdapter;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    private static final int ITEM_BELOW_THE_FOLD = 1;
    @Test
    public void scrollToItemBelowFold_checkItsText() {
        // First, scroll to the position that needs to be matched and click on it.
       /* onView(ViewMatchers.withId(R.id.recyclerView_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_BELOW_THE_FOLD,
                        click()));

        // Match the text in an item below the fold and check that it's displayed.
        String itemElementText = mActivityTestRule.getActivity().getResources()
                .getString(R.string.item_element_text)
                + String.valueOf(ITEM_BELOW_THE_FOLD);
        onView(withText(itemElementText)).check(matches(isDisplayed()));*/

       /* onView(withRecyclerView(R.id.recyclerView_recipes)
                .atPositionOnView(1, R.id.tv_recipe_name))
                .check(matches(withText(R.string.item_element_text)));*/

       onView(withRecyclerView(R.id.recyclerView_recipes).atPosition(0))
                .check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.recyclerView_recipes).atPositionOnView(0, R.id.tv_recipe_name))
                .check(matches(isDisplayed()))
                .check(matches(withText("Nutella Pie")));


    }



    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
