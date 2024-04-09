package software.galaniberico.myapplication

import android.util.Log
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.junit.Assert
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule
import software.galaniberico.navigator.configuration.NavigatorConfigurator
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.DataTypeMismatchException
import software.galaniberico.navigator.exceptions.MissingLoadedDataException
import software.galaniberico.navigator.exceptions.NoTargetsException
import software.galaniberico.navigator.exceptions.TooManyTargetsException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigateToWith {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun toWithOutBounds() {
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.with("id", "Fail please")
            }
        }
    }

    @Test
    fun toGetOutBounds() {
        Assert.assertThrows(MissingLoadedDataException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.get("id", "Fail please")
            }
        }
    }
    @Test
    fun toGetEmptyId() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.get("", "Fail please")
            }
        }
    }
    @Test
    fun toGetBlankId() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.get("       ", "Fail please")
            }
        }
    }
    @Test
    fun toGetNullifyWhenBackToParent() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("id", "value")
            }
        }

        onView(isRoot()).perform(ViewActions.pressBack())

        Assert.assertThrows(MissingLoadedDataException::class.java){
            activityRule.scenario.onActivity {
                Navigate.get("id", "default")
            }
        }
    }
    // ### .to(id) ###


    @Test
    fun toIdWith() {
        activityRule.scenario.onActivity {
            Navigate.to("withTag")
        }

        onView(withId(R.id.tvA)).check(matches(withText("45")))
        onView(withId(R.id.tvB)).check(matches(withText("46 b")))
        onView(withId(R.id.tvNull)).check(matches(withText("null")))
        onView(withId(R.id.tvId)).check(matches(withText("withTag")))
    }

    // ### .to(clazz, lambda) ###

    @Test
    fun toClassWith() {
        var lambdaExecuted = false
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class) {
                lambdaExecuted = true
            }
        }

        assertTrue(lambdaExecuted)
        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
    }


    // ### .to(id, clazz, lambda) ###

    @Test
    fun toIdClassWith() {
        activityRule.scenario.onActivity {
            Navigate.to("custom Id", MainActivity2::class)
        }

        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
        onView(withId(R.id.tvId)).check(matches(withText("custom Id")))
    }



}