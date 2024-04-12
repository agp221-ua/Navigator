package software.galaniberico.myapplication.navigate

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.myapplication.MainActivity
import software.galaniberico.myapplication.MainActivity2
import software.galaniberico.myapplication.R
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.NoTargetsException
import software.galaniberico.navigator.exceptions.TooManyTargetsException
import software.galaniberico.navigator.facade.Navigate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigateTo {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    // ### .to(id) ###

    @Test
    fun toEmpty() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to("")
            }
        }
    }

    @Test
    fun toBlank() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to("              ")
            }
        }
    }

    @Test
    fun toNoTarget() {
        Assert.assertThrows(NoTargetsException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to("to2")
            }
        }
    }
    @Test
    fun toTooTargets() {
        Assert.assertThrows(TooManyTargetsException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to("withTooTargets")
            }
        }
    }

    @Test
    fun to2() {
        activityRule.scenario.onActivity {
            Navigate.to("withTag")
        }

        onView(withId(R.id.tvA)).check(matches(withText("45")))
        onView(withId(R.id.tvB)).check(matches(withText("46 b")))
        onView(withId(R.id.tvNull)).check(matches(withText("null")))
        onView(withId(R.id.tvId)).check(matches(withText("withTag")))
    }

    @Test
    fun to2From() {
        activityRule.scenario.onActivity {
            Navigate.from(it).to("withTag")
        }

        onView(withId(R.id.tvA)).check(matches(withText("45")))
        onView(withId(R.id.tvB)).check(matches(withText("46 b")))
        onView(withId(R.id.tvNull)).check(matches(withText("null")))
        onView(withId(R.id.tvId)).check(matches(withText("withTag")))
    }
    @Test
    fun to2Repeated() {
        activityRule.scenario.onActivity {
            Navigate.to("withTag")
        }

        onView(withId(R.id.tvA)).check(matches(withText("45")))
        onView(withId(R.id.tvB)).check(matches(withText("46 b")))
        onView(withId(R.id.tvNull)).check(matches(withText("null")))
        onView(withId(R.id.tvId)).check(matches(withText("withTag")))

        onView(isRoot()).perform(ViewActions.pressBack())

        activityRule.scenario.onActivity {
            Navigate.to("withTag")
        }

        onView(withId(R.id.tvA)).check(matches(withText("45")))
        onView(withId(R.id.tvB)).check(matches(withText("46 b")))
        onView(withId(R.id.tvNull)).check(matches(withText("null")))
        onView(withId(R.id.tvId)).check(matches(withText("withTag")))
    }

    @Test
    fun to2FromRepeated() {
        activityRule.scenario.onActivity {
            Navigate.from(it).to("withTag")
        }

        onView(withId(R.id.tvA)).check(matches(withText("45")))
        onView(withId(R.id.tvB)).check(matches(withText("46 b")))
        onView(withId(R.id.tvNull)).check(matches(withText("null")))
        onView(withId(R.id.tvId)).check(matches(withText("withTag")))

        onView(isRoot()).perform(ViewActions.pressBack())

        activityRule.scenario.onActivity {
            Navigate.from(it).to("withTag")
        }

        onView(withId(R.id.tvA)).check(matches(withText("45")))
        onView(withId(R.id.tvB)).check(matches(withText("46 b")))
        onView(withId(R.id.tvNull)).check(matches(withText("null")))
        onView(withId(R.id.tvId)).check(matches(withText("withTag")))

    }

    // ### .to(clazz, lambda) ###

    @Test
    fun toWithClass() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class)
        }

        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
    }

    @Test
    fun toWithClassAndLambda() {
        var lambdaExecuted = false
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class) {
                lambdaExecuted = true
            }
        }

        assertTrue(lambdaExecuted)
        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
    }

    @Test
    fun toWithClassAndLambdaFrom() {
        var lambdaExecuted = false
        activityRule.scenario.onActivity {
            Navigate.from(it).to(MainActivity2::class) {
                lambdaExecuted = true
            }
        }

        assertTrue(lambdaExecuted)
        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
    }

    // ### .to(id, clazz, lambda) ###
    @Test
    fun toIdEmptyClassLambda() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to("")
            }
        }
    }

    @Test
    fun toIdBlankClassLambda() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to("              ")
            }
        }
    }
    @Test
    fun toWithIdClass() {
        activityRule.scenario.onActivity {
            Navigate.to("custom Id", MainActivity2::class)
        }

        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
        onView(withId(R.id.tvId)).check(matches(withText("custom Id")))
    }

    @Test
    fun toWithIdClassAndLambda() {
        var lambdaExecuted = false
        activityRule.scenario.onActivity {
            Navigate.to("custom Id", MainActivity2::class) {
                lambdaExecuted = true
            }
        }

        assertTrue(lambdaExecuted)
        onView(withId(R.id.tvId)).check(matches(withText("custom Id")))
        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
    }
    @Test
    fun toWithIdClassAndLambdaFrom() {
        var lambdaExecuted = false
        activityRule.scenario.onActivity {
            Navigate.from(it).to("custom Id", MainActivity2::class) {
                lambdaExecuted = true
            }
        }

        assertTrue(lambdaExecuted)
        onView(withId(R.id.tvId)).check(matches(withText("custom Id")))
        onView(withId(R.id.tvAlways)).check(matches(withText("45")))
    }


}