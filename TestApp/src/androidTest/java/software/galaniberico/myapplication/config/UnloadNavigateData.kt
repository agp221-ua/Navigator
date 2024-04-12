package software.galaniberico.myapplication.config

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.myapplication.MainActivity
import software.galaniberico.myapplication.MainActivity4
import software.galaniberico.myapplication.R
import software.galaniberico.navigator.configuration.ConfigurationField
import software.galaniberico.navigator.configuration.UnloadNavigateData
import software.galaniberico.navigator.exceptions.ConfigurationConflictException
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.facade.Navigator

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UnloadNavigateData {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @After
    fun after(){
        Navigator.config(ConfigurationField.UNLOAD_NAVIGATEDATA, UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY)
        Navigate.nullify()
    }
    @Test
    fun never() {
        Navigator.config(ConfigurationField.UNLOAD_NAVIGATEDATA, UnloadNavigateData.NEVER)
        Navigator.setLandingErrorHandler {}

        activityRule.scenario.onActivity {
            Navigate.to("neverLoad", MainActivity4::class)
        }

        onView(withId(R.id.tvError)).check(matches(withText("ConfigurationConflictException")))
    }

    @Test
    fun manualLoadNotCalled() {
        Navigator.config(ConfigurationField.UNLOAD_NAVIGATEDATA, UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY)
        Navigator.setLandingErrorHandler {}

        activityRule.scenario.onActivity {
            Navigate.to("manualLoadNotCalled", MainActivity4::class)
        }

        onView(withId(R.id.tvError)).check(matches(withText("ConfigurationConflictException")))
    }

    @Test
    fun manualLoad() {
        Navigator.config(ConfigurationField.UNLOAD_NAVIGATEDATA, UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY)
        Navigator.setLandingErrorHandler {}

        activityRule.scenario.onActivity {
            Navigate.to("manualLoad", MainActivity4::class)
        }

        onView(withId(R.id.tvError)).check(matches(withText("all ok")))
    }
    @Test
    fun autoLoadNotNulled() {
        Navigator.config(ConfigurationField.UNLOAD_NAVIGATEDATA, UnloadNavigateData.FROM_LAND_UNTIL_MANUAL_NULLIFY)
        Navigator.setLandingErrorHandler { Assert.assertEquals(it::class.java, ConfigurationConflictException::class.java) }
        activityRule.scenario.onActivity {
            Navigate.to("autoLoadNotNulled", MainActivity4::class)
        }
    }

    @Test
    fun autoLoad() {
        Navigator.config(ConfigurationField.UNLOAD_NAVIGATEDATA, UnloadNavigateData.FROM_LAND_UNTIL_MANUAL_NULLIFY)
        Navigator.setLandingErrorHandler {}
        activityRule.scenario.onActivity {
            Navigate.to("autoLoad", MainActivity4::class)
        }

        onView(withId(R.id.tvError)).check(matches(withText("all ok")))
    }
    @Test
    fun autoLoadNotToNullifyNullified() {
        Navigator.config(ConfigurationField.UNLOAD_NAVIGATEDATA, UnloadNavigateData.FROM_LAND_UNTIL_OTHER_LAND)
        Navigator.setLandingErrorHandler {}

        activityRule.scenario.onActivity {
            Navigate.to("autoLoad2", MainActivity4::class)
        }

        onView(withId(R.id.tvError)).check(matches(withText("ConfigurationConflictException")))
    }

    @Test
    fun autoLoadNotToNullify() {
        Navigator.config(ConfigurationField.UNLOAD_NAVIGATEDATA, UnloadNavigateData.FROM_LAND_UNTIL_OTHER_LAND)
        Navigator.setLandingErrorHandler {}

        activityRule.scenario.onActivity {
            Navigate.to("autoLoadNotToNullify", MainActivity4::class)
        }

        onView(withId(R.id.tvError)).check(matches(withText("all ok")))
    }

}