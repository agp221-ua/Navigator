package software.galaniberico.myapplication.navigate

import android.app.Activity
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.myapplication.MainActivity
import software.galaniberico.myapplication.MainActivity2
import software.galaniberico.myapplication.R
import software.galaniberico.navigator.configuration.ConfigurationField
import software.galaniberico.navigator.configuration.UnloadNavigateData
import software.galaniberico.navigator.exceptions.ConcurrentNavigationLoadException
import software.galaniberico.navigator.exceptions.InvalidActivityIdException
import software.galaniberico.navigator.exceptions.MissingNavigateDataException
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class Load {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        Navigator.config(
            ConfigurationField.UNLOAD_NAVIGATEDATA,
            UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY
        )
    }


    @Test
    fun nothingToLoad() {
        Assert.assertThrows(MissingNavigateDataException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.load(it)
            }
        }
    }

    @Test
    fun loadInvalidActivityID() {
        Assert.assertThrows(InvalidActivityIdException::class.java) {
            activityRule.scenario.onActivity {
                it.intent.putExtra("###ModuleDroid_IntentManager@ID###", null as String?)
                Navigate.load(it)
            }
        }
    }

    @Test
    fun loadAlreadyLoaded() {
        activityRule.scenario.onActivity {
            Navigate.to("loadAlreadyLoaded", MainActivity2::class) {
                Navigate.with("to do", { activity: Activity ->
                    Assert.assertThrows(ConcurrentNavigationLoadException::class.java) {
                        Navigate.load(
                            activity
                        )
                    }
                })
            }
        }
    }


    @Test
    fun loadOk() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("loadOk", MainActivity2::class) {
                Navigate.with("to do", {
                    Navigate.withResult("res1", "result ok")
                    Navigate.withResult("res2", 45)

                })
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = Navigate.getResult("res1", "default")
                it.findViewById<TextView>(R.id.tvMain).text = "${Navigate.getResult("res2", 0)}"
            }


        }
        onView(withId(R.id.tvMain)).check(matches(withText("45")))
    }



}