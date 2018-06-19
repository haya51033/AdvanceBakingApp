package com.example.android.advancebakingapp;

import android.os.SystemClock;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringContains.containsString;
import static android.support.test.espresso.Espresso.onView;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityTest {

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

    @Test
    public void scrollToItemBelowFold_checkItsText() {
        SystemClock.sleep(1000);
       onView(withRecyclerView(R.id.recyclerView_recipes).atPosition(1))
                .check(matches(isDisplayed()));

        onView(withRecyclerView(R.id.recyclerView_recipes).atPositionOnView(1, R.id.tv_recipe_name))
                .check(matches(isDisplayed()))
                .check(matches(withText("Brownies")));

        onView(withId(R.id.recyclerView_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        SystemClock.sleep(1000);

        onView((withId(R.id.ingredientsTextView)))
                .check(matches(isDisplayed()))
                .check(matches(withText("ingredients")))
                .perform(click());
        SystemClock.sleep(1000);

        onView((withId(R.id.recipe_details_ingredients)))
                .check(matches(isDisplayed()))
                .check(matches(withText(containsString("- Bittersweet chocolate (60-70% cacao) (350.0 G)"))));

        onView((withId(R.id.stepsTextView)))
                .check(matches(isDisplayed()))
                .check(matches(withText("Steps")))
                .perform(click());

        SystemClock.sleep(1000);

        onView(withRecyclerView(R.id.recipe_details_steps).atPositionOnView(1, R.id.step_button))
                .check(matches(isDisplayed()))
                .check(matches(withText(containsString("Starting prep"))));
        SystemClock.sleep(1000);

        onView(withId(R.id.recipe_details_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        SystemClock.sleep(1000);
    }



    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
