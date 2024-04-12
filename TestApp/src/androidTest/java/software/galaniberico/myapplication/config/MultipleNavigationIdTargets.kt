package software.galaniberico.myapplication.config

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.myapplication.MainActivity
import software.galaniberico.navigator.configuration.ConfigurationField
import software.galaniberico.navigator.configuration.MultipleNavigationIdTargets
import software.galaniberico.navigator.exceptions.TooManyTargetsException
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MultipleNavigationIdTargets {



    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun sendExceptions() {
        Navigator.config(ConfigurationField.MULTIPLE_NAVIGATION_ID_TARGETS, MultipleNavigationIdTargets.SEND_ERROR)

        Assert.assertThrows(TooManyTargetsException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.to("withTooTargets")
            }
        }
    }
    @Test
    fun noSendExceptions() {
        Navigator.config(ConfigurationField.MULTIPLE_NAVIGATION_ID_TARGETS, MultipleNavigationIdTargets.PICK_FIRST)
            activityRule.scenario.onActivity {
                Navigate.to("withTooTargets")
            }
    }

    

}