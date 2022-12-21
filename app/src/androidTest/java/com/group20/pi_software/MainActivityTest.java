package com.group20.pi_software;


import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.JMock1Matchers.equalTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.group20.pi_software.ui.bottomSheet.BottomSheetFragment;
import com.group20.pi_software.ui.feed.FeedFragment;
import com.group20.pi_software.ui.summary.SummaryFragment;
import com.group20.pi_software.ui.trend.TrendFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.SimpleFormatter;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        mActivityRule.getActivity().getSupportFragmentManager().beginTransaction();
        mActivityRule.getActivity().getSharedPreferences("Settings", MODE_PRIVATE).edit().putBoolean("track", false).apply();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.group20.pi_software", appContext.getPackageName());
    }

    @Test
    public void testSpec07_3() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction summaryPage = onView(withId(R.id.navigation_summary));
        summaryPage.check(matches(isDisplayed()));
        summaryPage.perform(click());

        ViewInteraction textView = onView(withId(R.id.text_stepsSummary));
        textView.check(matches(isDisplayed()));
        String count = readText(withId(R.id.text_stepsSummary));

        ViewInteraction floatingActionButton = onView(withId(R.id.floatingActionButton_add));
        floatingActionButton.check(matches(isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction stepsEditText = onView(withId(R.id.editText_steps));
        stepsEditText.check(matches(isDisplayed()));
        stepsEditText.perform(replaceText("100"), closeSoftKeyboard());

        ViewInteraction doneButton = onView(withId(R.id.button_done));
        doneButton.check(matches(isDisplayed()));
        doneButton.perform(click());

        textView.check(matches(allOf(withText(String.valueOf(Integer.parseInt(count) + 100)), isDisplayed())));
    }

    @Test
    public void testSpec08_1() throws Exception {
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());

        FragmentScenario<TrendFragment> trendScenario = FragmentScenario.launchInContainer(TrendFragment.class, null, R.style.Theme_PIsoftware, (FragmentFactory) null);

        trendScenario.onFragment(fragment -> {
                    navController.setGraph(R.navigation.mobile_navigation);

                    Navigation.setViewNavController(fragment.requireView(), navController);
                }
        );
        trendScenario.moveToState(Lifecycle.State.RESUMED);

        // Spec 6.1
        onView(withId(R.id.barChart_sleep)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.barChart_study)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.barChart_steps)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.barChart_activity)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void testSpec08_2() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadSamples();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction trendPage = onView(allOf(withId(R.id.navigation_trend), isDisplayed()));
        trendPage.perform(click());

        // Spec 6.2
        onView(withId(R.id.scatter_correlation)).check(matches(isCompletelyDisplayed()));

    }

    @Test
    public void testSpec09_123() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction profilePage = onView(allOf(withId(R.id.navigation_profile), isDisplayed()));
        profilePage.perform(click());

        ViewInteraction goalsCard = onView(allOf(withId(R.id.cardGoals), isDisplayed()));
        goalsCard.check(matches(isDisplayed()));
        goalsCard.perform(scrollTo(), click());

        ViewInteraction stepGoalEditText = onView(allOf(withId(R.id.goal_card_input), isDisplayed()));
        stepGoalEditText.perform(replaceText("20000"));
        stepGoalEditText.check(matches(withText("20000")));
    }

    @Test
    public void testSpec09_4() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction profilePage = onView(allOf(withId(R.id.navigation_profile), isDisplayed()));
        profilePage.perform(click());

        ViewInteraction goalsCard = onView(allOf(withId(R.id.cardGoals), isDisplayed()));
        goalsCard.check(matches(isDisplayed()));
        goalsCard.perform(scrollTo(), click());

        ViewInteraction goalProgressBar = onView(withId(R.id.goals_progress_bar));
        goalProgressBar.check(matches(isDisplayed()));
    }

    @Test
    public void testSpec10_1() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction trendPage = onView(allOf(withId(R.id.navigation_trend), isDisplayed()));
        trendPage.perform(click());

        onView(withId(R.id.text_sleepAverage)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.text_studyAverage)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.text_stepsAverage)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.text_activityRange)).perform(scrollTo()).check(matches(isDisplayed()));

    }

    @Test
    public void testSpec10_2() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction summaryPage = onView(allOf(withId(R.id.navigation_summary), isDisplayed()));
        summaryPage.perform(click());

        onView(withId(R.id.text_sleepSummary)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.text_studySummary)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.text_stepsSummary)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.text_workoutSummary)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void testSpec10_3() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadSamples();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction trendPage = onView(allOf(withId(R.id.navigation_trend), isDisplayed()));
        trendPage.perform(click());

        onView(withId(R.id.text_correlationDescription)).check(matches(isDisplayed()));
    }

    @Test
    public void testSpec10_4() throws Exception {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reset();

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.feed)).check(matches(atPosition(1, hasDescendant(withText("You have not logged study today, yet!\nLet's study!")))));
    }

    @Test
    public void testSpec10_5() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction summaryPage = onView(allOf(withId(R.id.navigation_summary), isDisplayed()));
        summaryPage.perform(click());

        ViewInteraction addFAB = onView(allOf(withId(R.id.floatingActionButton_add), isDisplayed()));
        addFAB.perform(click());


        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Steps"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText("Steps")));

        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Sleep Hours"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText("Sleep Hours")));

        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Study Hours"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText("Study Hours")));

        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Exercise"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.spinner)).check(matches(withSpinnerText("Exercise")));
    }

    @Test
    public void testSpec11_1() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction summaryPage = onView(withId(R.id.navigation_summary));
        summaryPage.check(matches(isDisplayed()));
        summaryPage.perform(click());

        ViewInteraction textView = onView(withId(R.id.text_stepsSummary));
        textView.check(matches(isDisplayed()));
        String count = readText(withId(R.id.text_stepsSummary));

        ViewInteraction floatingActionButton = onView(withId(R.id.floatingActionButton_add));
        floatingActionButton.check(matches(isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction stepsEditText = onView(withId(R.id.editText_steps));
        stepsEditText.check(matches(isDisplayed()));
        stepsEditText.perform(replaceText("100"), closeSoftKeyboard());

        ViewInteraction doneButton = onView(withId(R.id.button_done));
        doneButton.check(matches(isDisplayed()));
        doneButton.perform(click());

        textView.check(matches(allOf(withText(String.valueOf(Integer.parseInt(count) + 100)), isDisplayed())));
    }

    @Test
    public void testSpec11_2() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction summaryPage = onView(withId(R.id.navigation_summary));
        summaryPage.check(matches(isDisplayed()));
        summaryPage.perform(click());

        ViewInteraction floatingActionButton = onView(withId(R.id.floatingActionButton_add));
        floatingActionButton.check(matches(isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction stepsEditText = onView(withId(R.id.editText_steps));
        stepsEditText.check(matches(isDisplayed()));
        stepsEditText.perform(replaceText("100"), closeSoftKeyboard());

        ViewInteraction doneButton = onView(withId(R.id.button_done));
        doneButton.check(matches(isDisplayed()));
        doneButton.perform(click());

        ViewInteraction feedPage = onView(allOf(withId(R.id.navigation_feed), isDisplayed()));
        feedPage.perform(click());

        onView(withId(R.id.feed)).check(matches(atPosition(0, hasDescendant(withText("You just logged 100 Steps!")))));
    }

    @Test
    public void testSpec11_3() throws Exception {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reset();

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.feed)).check(matches(atPosition(1, hasDescendant(withText("You have not logged study today, yet!\nLet's study!")))));
    }

    @Test
    public void testSpec11_4() throws Exception {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reset();

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction profilePage = onView(allOf(withId(R.id.navigation_profile), isDisplayed()));
        profilePage.perform(click());

        ViewInteraction goalsCard = onView(allOf(withId(R.id.cardGoals), isDisplayed()));
        goalsCard.check(matches(isDisplayed()));
        goalsCard.perform(scrollTo(), click());

        ViewInteraction stepGoalEditText = onView(allOf(withId(R.id.goal_card_input), isDisplayed()));
        stepGoalEditText.check(matches(withText("10000")));
    }

    @Test
    public void testSpec11_5() throws Exception {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reset();
        mActivityRule.getActivity().getSharedPreferences("Logs", MODE_PRIVATE).edit().putString("last_goals_modified", "01/01/2000").apply();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.navigation_summary), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.navigation_feed), isDisplayed())).perform(click());

        onView(withId(R.id.feed)).check(matches(atPosition(0, hasDescendant(withText("You have not changed goals these days.\nMaybe, it is good time to review?")))));
    }

    @Test
    public void testSpec12_1() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction summaryPage = onView(withId(R.id.navigation_summary));
        summaryPage.check(matches(isDisplayed()));
        summaryPage.perform(click());

        onView(withId(R.id.action_share)).check(matches(isDisplayed()));

    }

    @Test
    public void testSpec13_2() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction summaryPage = onView(withId(R.id.navigation_summary));
        summaryPage.check(matches(isDisplayed()));
        summaryPage.perform(click());

        onView(withId(R.id.progressRing_steps)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void testSpec13_3() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.navigation_summary), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.navigation_trend), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.navigation_profile), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.navigation_feed), isDisplayed())).perform(click());
    }

    @Test
    public void testSpec13_4() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.navigation_trend), isDisplayed())).perform(click());

        ViewInteraction rangeText = onView(withId(R.id.text_stepsRange));
        rangeText.perform(scrollTo());

        String range = readText(withId(R.id.text_stepsRange));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar date = Calendar.getInstance();
        date.setTime(formatter.parse(range));
        date.add(Calendar.DAY_OF_MONTH, -1);

        onView(withId(R.id.barChart_steps)).check(matches(isDisplayed()));

        onView(withId(R.id.button_stepsRangeBefore)).perform(scrollTo(), click());


        rangeText.check(matches(withText(formatter.format(date.getTime()))));
    }

    @Test
    public void testSpec13_6() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadSamples();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction trendPage = onView(allOf(withId(R.id.navigation_trend), isDisplayed()));
        trendPage.perform(click());

        // Spec 6.2
        onView(withId(R.id.scatter_correlation)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testSpec13_7() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadSamples();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.navigation_profile), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.cardProfile), isDisplayed())).perform(click());

        onView(withId(R.id.pd_name_input)).perform(scrollTo()).check(matches(allOf(isDisplayed(), withText("John"))));
        onView(withId(R.id.pd_height_input)).perform(scrollTo()).check(matches(allOf(isDisplayed(), withText("186.0"))));
        onView(withId(R.id.pd_weight_input)).perform(scrollTo()).check(matches(allOf(isDisplayed(), withText("180.0"))));
    }

    @Test
    public void testSpec13_8() throws Exception {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        loadSamples();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.navigation_profile), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.cardProfile), isDisplayed())).perform(click());

        onView(withId(R.id.pd_name_input)).perform(scrollTo(), replaceText("Josh")).check(matches(allOf(isDisplayed(), withText("Josh"))));

        onView(allOf(withContentDescription("Navigate up"),isDisplayed())).perform(click());

        onView(allOf(withId(R.id.cardSettings), isDisplayed())).perform(click());

        onView(withId(R.id.switch_feedNotifications)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.switch_loggingNotifications)).perform(scrollTo()).check(matches(isDisplayed()));
        onView(withId(R.id.switch_trendsNotifications)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    private void reset(){
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_profile), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction cardView = onView(
                allOf(withId(R.id.cardSettings), isDisplayed()));
        cardView.perform(scrollTo(), click());

        ViewInteraction materialButton = onView(withId(R.id.button_reset));
        materialButton.perform(scrollTo(), click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Yes"), isDisplayed()));
        materialButton2.perform(scrollTo(), click());
    }

    private void loadSamples(){
        ViewInteraction bottomNavigationItemView = onView(allOf(withId(R.id.navigation_profile), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction cardView = onView(allOf(withId(R.id.cardSettings), isDisplayed()));
        cardView.perform(scrollTo(), click());

        ViewInteraction materialTextView = onView(withId(R.id.text_aboutApp));
        materialTextView.perform(scrollTo());
        for (int i = 0; i < 10; i++){
            materialTextView.perform(click());
        }

        ViewInteraction materialButton = onView(allOf(withId(R.id.button_sample), isDisplayed()));
        materialButton.perform(scrollTo(), click());

        ViewInteraction materialButton2 = onView(allOf(withId(android.R.id.button1), withText("Yes"), isDisplayed()));
        materialButton2.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private String readText(Matcher<View> matcher){
        final String[] strings = {null};
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "Get text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                strings[0] = textView.getText().toString();
            }
        });

        return strings[0];
    }

    /*
    From https://stackoverflow.com/questions/31394569/how-to-assert-inside-a-recyclerview-in-espresso
     */
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

}