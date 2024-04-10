package software.galaniberico.myapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.navigator.exceptions.DataTypeMismatchException
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class Land {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    // ### @Land ###

    @Test
    fun toBadType() {
        Navigator.setLandingErrorHandler {
            assert(it::class == DataTypeMismatchException::class)
        }

        activityRule.scenario.onActivity {
            Navigate.to("withBadType")
        }

        Navigator.setLandingErrorHandler {}
    }

    @Test
    fun toOldFieldNotFound() {
        activityRule.scenario.onActivity {
            Navigate.to("withNoOldField")
        }

        onView(withId(R.id.tvNoOldField)).check(matches(withText("no value")))
        onView(withId(R.id.tvA)).check(matches(withText("0")))
    }

    @Test
    fun toLandNotId() {
        activityRule.scenario.onActivity {
            Navigate.to("withTag2")
        }

        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
        onView(withId(R.id.tvA)).check(matches(withText("0")))
        onView(isRoot()).perform(ViewActions.pressBack())

        // close MainActivity2

        activityRule.scenario.onActivity {
            Navigate.to("withTag")
        }

        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
        onView(withId(R.id.tvA)).check(matches(withText("45")))
    }

}