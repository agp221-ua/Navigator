package software.galaniberico.myapplication.navigate

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.myapplication.MainActivity
import software.galaniberico.myapplication.MainActivity2
import software.galaniberico.myapplication.R
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
import software.galaniberico.navigator.facade.Navigate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class With {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun withOutBounds() {
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.with("id", "Fail please")
            }
        }
    }

    @Test
    fun withOutBoundsAfterException() {
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                try {
                    Navigate.to(MainActivity2::class) {
                        Navigate.with("", "dasdf")
                    }
                } catch (_: Exception) {
                }
                Navigate.with("id", "Fail please")
            }
        }
    }

    @Test
    fun withEmpty() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to(MainActivity2::class) {
                    Navigate.with("", "sdaffasdf")
                }
            }
        }
    }

    @Test
    fun withBlank() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to(MainActivity2::class) {
                    Navigate.with("     ", "sdaffasdf")
                }
            }
        }
    }

    @Test
    fun withMultipleValues() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("anyid4", 34)
                Navigate.with("anyid4_", 34445)
                Navigate.with("anyid4__", 354)
                Navigate.with("anyid4___", 3466)
            }
        }

    }

    @Test
    fun withDifferentValueTypes() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("anyid3", 34)
                Navigate.with("anyid3_", "34445")
                Navigate.with("anyid3__", Pair(34,"asd"))
            }
        }

    }
    @Test
    fun withSameId() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("anyid", 34)
                Navigate.with("anyid", "34445")
            }
        }

    }

    @Test
    fun withNull() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("anyid2", null)
            }
        }

    }



    // ### .to(id) ###


    @Test
    fun toIdWith() {
        activityRule.scenario.onActivity {
            Navigate.to("withNavigateData")
        }

        onView(withId(R.id.tvWith)).check(matches(withText("47")))
    }

    // ### .to(clazz, lambda) ###

    @Test
    fun toClassWith() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("value1", 47)
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("47")))
    }


    // ### .to(id, clazz, lambda) ###

    @Test
    fun toIdClassWith() {
        activityRule.scenario.onActivity {
            Navigate.to("withNavigateData", MainActivity2::class){
                Navigate.with("value1", 47)
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("47")))
    }

    // ### WITH LOADED ###

    @Test
    fun withLoadedEmpty() {
        activityRule.scenario.onActivity {
            Navigate.to("withLoadedEmpty", MainActivity2::class){
                Navigate.with("withLoaded", 48)
                Navigate.with("withLoaded2", "Hello")
            }
        }
        onView(withId(R.id.tvWithLoaded1)).check(matches(withText("no data")))
        onView(withId(R.id.tvWithLoaded2)).check(matches(withText("no data")))
    }
    @Test
    fun withLoadedOneElement() {
        activityRule.scenario.onActivity {
            Navigate.to("withLoadedOneElement", MainActivity2::class){
                Navigate.with("withLoaded", 48)
                Navigate.with("withLoaded2", "Hello")
            }
        }
        onView(withId(R.id.tvWithLoaded1)).check(matches(withText("48")))
        onView(withId(R.id.tvWithLoaded2)).check(matches(withText("no data")))
    }
    @Test
    fun withLoadedSeveralElements() {
        activityRule.scenario.onActivity {
            Navigate.to("withLoadedSeveralElements", MainActivity2::class){
                Navigate.with("withLoaded", 48)
                Navigate.with("withLoaded2", "Hello")
            }
        }
        onView(withId(R.id.tvWithLoaded1)).check(matches(withText("48")))
        onView(withId(R.id.tvWithLoaded2)).check(matches(withText("Hello")))
    }

}