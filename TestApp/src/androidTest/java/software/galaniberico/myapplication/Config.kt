package software.galaniberico.myapplication

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.myapplication.MainActivity3.Companion.loaded
import software.galaniberico.navigator.configuration.ConfigurationField
import software.galaniberico.navigator.configuration.LandAnnotationSearch
import software.galaniberico.navigator.exceptions.EnumTypeMismatchException
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class Config {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        loaded = false
    }

    // ### @Land ###

    @Test
    fun configBadType() {

        activityRule.scenario.onActivity {
            Assert.assertThrows(EnumTypeMismatchException::class.java){
                Navigator.config(
                    ConfigurationField.UNLOAD_NAVIGATEDATA,
                    LandAnnotationSearch.OLD_FIELDS
                )
            }
        }

    }

}