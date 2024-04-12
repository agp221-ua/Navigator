package software.galaniberico.myapplication.config

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.myapplication.MainActivity
import software.galaniberico.myapplication.MainActivity5
import software.galaniberico.myapplication.R
import software.galaniberico.navigator.configuration.ConfigurationField
import software.galaniberico.navigator.configuration.LandAnnotationSearch
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LandAnnotationSearch {



    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun never() {
        Navigator.config(ConfigurationField.LAND_ANNOTATION_SEARCH, LandAnnotationSearch.NONE)

        activityRule.scenario.onActivity {
            Navigate.to("neverLoad", MainActivity5::class){
                Navigate.with("noAttribute", 85)
                Navigate.with("both", 86)
            }
        }

        onView(withId(R.id.tvNoData)).check(matches(withText("0")))
        onView(withId(R.id.tvNoAttribute)).check(matches(withText("1")))
        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("2")))
        onView(withId(R.id.tvBoth)).check(matches(withText("3")))
    }

    @Test
    fun oldFields() {
        Navigator.config(ConfigurationField.LAND_ANNOTATION_SEARCH, LandAnnotationSearch.OLD_FIELDS)

        activityRule.scenario.onActivity {
            Navigate.to("oldFields", MainActivity5::class){
                Navigate.with("noAttribute", 85)
                Navigate.with("both", 86)
            }
        }

        onView(withId(R.id.tvNoData)).check(matches(withText("0")))
        onView(withId(R.id.tvNoAttribute)).check(matches(withText("1")))
        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("45")))
        onView(withId(R.id.tvBoth)).check(matches(withText("46")))
    }

    @Test
    fun navigateData() {
        Navigator.config(ConfigurationField.LAND_ANNOTATION_SEARCH, LandAnnotationSearch.NAVIGATE_DATA)

        activityRule.scenario.onActivity {
            Navigate.to("navigateData", MainActivity5::class){
                Navigate.with("noAttribute", 85)
                Navigate.with("both", 86)
            }
        }

        onView(withId(R.id.tvNoData)).check(matches(withText("0")))
        onView(withId(R.id.tvNoAttribute)).check(matches(withText("85")))
        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("2")))
        onView(withId(R.id.tvBoth)).check(matches(withText("86")))
    }

    @Test
    fun bothOldFieldNavigateData() {
        Navigator.config(ConfigurationField.LAND_ANNOTATION_SEARCH, LandAnnotationSearch.OLD_FIELDS_THEN_NAVIGATE_DATA)

        activityRule.scenario.onActivity {
            Navigate.to("bothOldFieldNavigateData", MainActivity5::class){
                Navigate.with("noAttribute", 85)
                Navigate.with("both", 86)
            }
        }

        onView(withId(R.id.tvNoData)).check(matches(withText("0")))
        onView(withId(R.id.tvNoAttribute)).check(matches(withText("85")))
        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("45")))
        onView(withId(R.id.tvBoth)).check(matches(withText("46")))
    }
    @Test
    fun bothNavigateDataOldField() {
        Navigator.config(ConfigurationField.LAND_ANNOTATION_SEARCH, LandAnnotationSearch.NAVIGATE_DATA_THEN_OLD_FIELDS)

        activityRule.scenario.onActivity {
            Navigate.to("bothNavigateDataOldField", MainActivity5::class){
                Navigate.with("noAttribute", 85)
                Navigate.with("both", 86)
            }
        }

        onView(withId(R.id.tvNoData)).check(matches(withText("0")))
        onView(withId(R.id.tvNoAttribute)).check(matches(withText("85")))
        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("45")))
        onView(withId(R.id.tvBoth)).check(matches(withText("86")))
    }


}