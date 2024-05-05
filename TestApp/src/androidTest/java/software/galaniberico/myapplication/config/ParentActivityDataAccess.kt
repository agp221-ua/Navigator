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
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ParentActivityDataAccess {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun never() {
        Navigator.config(ConfigurationField.PARENT_ACTIVITY_DATA_ACCESS, ParentActivityDataAccess.NEVER)

        activityRule.scenario.onActivity {
            Navigate.to("bothOldFieldNavigateDataGet", MainActivity5::class)
        }

        onView(withId(R.id.tvNoData)).check(matches(withText("0")))
        onView(withId(R.id.tvNoAttribute)).check(matches(withText("1")))
        onView(withId(R.id.tvNoNavigateData)).check(matches(withText("2")))
        onView(withId(R.id.tvBoth)).check(matches(withText("3")))
    }

//    @Test
//    fun activityOrDefaultCheckNullifyDataWhenBack() {
//        Navigator.config(ConfigurationField.PARENT_ACTIVITY_DATA_ACCESS, ParentActivityDataAccess.ACTIVITY_ACCESS_OR_DEFAULT)
//
//        activityRule.scenario.onActivity {
//            Navigate.to(MainActivity2::class) {
//                Navigate.with("id", "value")
//
//            }
//        }
//
//        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())
//
//        Assert.assertThrows(MissingLoadedDataException::class.java) {
//            activityRule.scenario.onActivity {
//                Navigate.get("id", "default")
//            }
//        }
//    }



}