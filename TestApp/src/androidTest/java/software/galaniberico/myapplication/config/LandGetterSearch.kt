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
import software.galaniberico.navigator.configuration.LandGetterSearch
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LandGetterSearch {



    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun neverGet() {
        Navigator.config(ConfigurationField.LAND_GETTER_SEARCH, LandGetterSearch.NONE)

        activityRule.scenario.onActivity {
            Navigate.to("neverLoadGet", MainActivity5::class){
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
    fun oldFieldsGet() {
        Navigator.config(ConfigurationField.LAND_GETTER_SEARCH, LandGetterSearch.OLD_FIELDS)

        activityRule.scenario.onActivity {
            Navigate.to("oldFieldsGet", MainActivity5::class){
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
    fun navigateDataGet() {
        Navigator.config(ConfigurationField.LAND_GETTER_SEARCH, LandGetterSearch.NAVIGATE_DATA)

        activityRule.scenario.onActivity {
            Navigate.to("navigateDataGet", MainActivity5::class){
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
    fun bothOldFieldNavigateDataGet() {
        Navigator.config(ConfigurationField.LAND_GETTER_SEARCH, LandGetterSearch.OLD_FIELDS_THEN_NAVIGATE_DATA)

        activityRule.scenario.onActivity {
            Navigate.to("bothOldFieldNavigateDataGet", MainActivity5::class){
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
    fun bothNavigateDataOldFieldGet() {
        Navigator.config(ConfigurationField.LAND_GETTER_SEARCH, LandGetterSearch.NAVIGATE_DATA_THEN_OLD_FIELDS)

        activityRule.scenario.onActivity {
            Navigate.to("bothNavigateDataOldFieldGet", MainActivity5::class){
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