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
import software.galaniberico.navigator.configuration.ParentActivityDataAccess
import software.galaniberico.navigator.configuration.ParentActivityMapProtocol
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ParentActivityMapProtocol {



    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun all() {
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_DATA_ACCESS, ParentActivityDataAccess.MAP_COPY)
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_MAP_PROTOCOL, ParentActivityMapProtocol.ALL)

        activityRule.scenario.onActivity {
            Navigate.to("allMapProtocol", MainActivity5::class)
        }

        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("45")))
        onView(withId(R.id.tvContext)).check(matches(withText("MainActivity")))
        onView(withId(R.id.tvView)).check(matches(withText("MaterialTextView")))
    }

    @Test
    fun noContext() {
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_DATA_ACCESS, ParentActivityDataAccess.MAP_COPY)
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_MAP_PROTOCOL, ParentActivityMapProtocol.NO_CONTEXT)

        activityRule.scenario.onActivity {
            Navigate.to("allMapProtocol", MainActivity5::class)
        }

        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("45")))
        onView(withId(R.id.tvContext)).check(matches(withText("no value")))
        onView(withId(R.id.tvView)).check(matches(withText("MaterialTextView")))
    }

    @Test
    fun noView() {
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_DATA_ACCESS, ParentActivityDataAccess.MAP_COPY)
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_MAP_PROTOCOL, ParentActivityMapProtocol.NO_VIEWS)

        activityRule.scenario.onActivity {
            Navigate.to("allMapProtocol", MainActivity5::class)
        }

        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("45")))
        onView(withId(R.id.tvContext)).check(matches(withText("MainActivity")))
        onView(withId(R.id.tvView)).check(matches(withText("no value")))
    }

    @Test
    fun noBoth() {
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_DATA_ACCESS, ParentActivityDataAccess.MAP_COPY)
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_MAP_PROTOCOL, ParentActivityMapProtocol.NO_VIEWS_NO_CONTEXT)

        activityRule.scenario.onActivity {
            Navigate.to("allMapProtocol", MainActivity5::class)
        }

        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("45")))
        onView(withId(R.id.tvContext)).check(matches(withText("no value")))
        onView(withId(R.id.tvView)).check(matches(withText("no value")))
    }


    

}