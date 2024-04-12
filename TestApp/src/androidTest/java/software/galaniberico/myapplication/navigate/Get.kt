package software.galaniberico.myapplication.navigate

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
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
import software.galaniberico.navigator.exceptions.MissingLoadedDataException
import software.galaniberico.navigator.facade.Navigate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class Get {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun getOutBounds() {
        Assert.assertThrows(MissingLoadedDataException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.get("id", "Fail please")
            }
        }
    }

    @Test
    fun getEmptyId() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.get("", "Fail please")
            }
        }
    }

    @Test
    fun getBlankId() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.get("       ", "Fail please")
            }
        }
    }

    @Test
    fun getNullifyWhenBackToParent() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class) {
                Navigate.with("id", "value")
            }
        }

        onView(ViewMatchers.isRoot()).perform(ViewActions.pressBack())

        Assert.assertThrows(MissingLoadedDataException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.get("id", "default")
            }
        }
    }


    @Test
    fun getMultipleValues() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("value11", 34445)
                Navigate.with("value111", 345)
                Navigate.with("value1", 34945)
            }
        }
        onView(withId(R.id.tvWith)).check(matches(withText("34945")))
        onView(withId(R.id.tvWith2)).check(matches(withText("default")))

    }

    @Test
    fun getDifferentValueTypes() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("value1", 49)
                Navigate.with("value2", "34445")
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("49")))
        onView(withId(R.id.tvWith2)).check(matches(withText("34445")))
    }
    @Test
    fun getSameId() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("value1", "34445")
                Navigate.with("value1", 34)
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("34")))
        onView(withId(R.id.tvWith2)).check(matches(withText("default")))

    }

    @Test
    fun getNull() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("value1", null)
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("default")))
        onView(withId(R.id.tvWith2)).check(matches(withText("default")))
    }

    @Test
    fun getNoIntroducedValue() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("anyid2", null)
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("default")))
        onView(withId(R.id.tvWith2)).check(matches(withText("default")))
    }
    // ### .to(id) ###

    @Test
    fun getId() {
        activityRule.scenario.onActivity {
            Navigate.to("getId")
        }

        onView(withId(R.id.tvWith)).check(matches(withText("49")))
        onView(withId(R.id.tvWith2)).check(matches(withText("34445")))
    }

    // ### .to(clazz, lambda) ###


    @Test
    fun getClass() {
        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class){
                Navigate.with("value1", 49)
                Navigate.with("value2", "34445")
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("49")))
        onView(withId(R.id.tvWith2)).check(matches(withText("34445")))
    }

    // ### .to(id, clazz, lambda) ###


    @Test
    fun getIdClass() {
        activityRule.scenario.onActivity {
            Navigate.to("getId", MainActivity2::class){
                Navigate.with("value1", 49)
                Navigate.with("value2", "34445")
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("49")))
        onView(withId(R.id.tvWith2)).check(matches(withText("34445")))
    }
    @Test
    fun getIdClassRecreate() {
        activityRule.scenario.onActivity {
            Navigate.to("landscape", MainActivity2::class){
                Navigate.with("value1", 4555)
                Navigate.with("value2", "123")
            }
        }

        onView(withId(R.id.tvWith)).check(matches(withText("4555")))
        onView(withId(R.id.tvWith2)).check(matches(withText("123")))
    }

}