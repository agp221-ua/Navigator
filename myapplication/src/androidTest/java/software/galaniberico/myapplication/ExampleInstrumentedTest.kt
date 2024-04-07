package software.galaniberico.myapplication

import android.app.Activity
import android.util.Log
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import software.galaniberico.myapplication.util.TEST_TAG
import software.galaniberico.navigator.facade.Navigate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("software.galaniberico.myapplication", appContext.packageName)

        activityRule.scenario.onActivity {
            Navigate.to(MainActivity2::class) {
                Log.i(TEST_TAG, "GOING TO 2")
            }
        }

        Espresso.onView(ViewMatchers.withId(R.id.tvPrueba)).check(ViewAssertions.matches(
            ViewMatchers.withText("45")))
    }
}