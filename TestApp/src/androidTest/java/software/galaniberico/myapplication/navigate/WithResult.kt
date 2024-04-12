package software.galaniberico.myapplication.navigate

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import software.galaniberico.myapplication.MainActivity
import software.galaniberico.myapplication.MainActivity2
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
import software.galaniberico.navigator.facade.Navigate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class WithResult {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun withResultOutBounds() {
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.withResult("id", "Fail please")
            }
        }
    }

    @Test
    fun withResultEmpty() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("withResultEmpty", MainActivity2::class) {
                Navigate.with("to do", {
                    Assert.assertThrows(BlankIdFieldException::class.java) {
                        Navigate.withResult("", "sdaffasdf")
                    }
                })
            }
        }
    }

    @Test
    fun withResultBlank() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("withResultBlank", MainActivity2::class) {
                Navigate.with("to do", {
                    Assert.assertThrows(BlankIdFieldException::class.java) {
                        Navigate.withResult("  ", "sdaffasdf")
                    }
                })
            }
        }
    }


    @Test
    fun withResultMultipleValues() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("withResultMultipleValues", MainActivity2::class) {
                Navigate.with("to do", {
                    Navigate.withResult("res1", "sdaffasdf")
                    Navigate.withResult("res2", "sdafdf")
                    Navigate.withResult("res3", "sdaffasdfdsfasdf")
                    Navigate.withResult("res4", "df")

                })
                Navigate.with("return", true)
            }.andThen {}
        }
    }



    @Test
    fun withResultDifferentValueTypes() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("withResultDifferentValueTypes", MainActivity2::class) {
                Navigate.with("to do", {
                    Navigate.withResult("res1", "sdaffasdf")
                    Navigate.withResult("res2", 34)
                    Navigate.withResult("res3", Pair(2,"dsf"))

                })
                Navigate.with("return", true)
            }.andThen {}
        }

    }
    @Test
    fun withResultSameId() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("withResultSameId", MainActivity2::class) {
                Navigate.with("to do", {
                    Navigate.withResult("res1", 45)
                    Navigate.withResult("res1", "sdaffasdf")

                })
                Navigate.with("return", true)
            }.andThen {}
        }

    }

    @Test
    fun withResultNull() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("withResultNull", MainActivity2::class) {
                Navigate.with("to do", {
                    Navigate.withResult("res1", null)

                })
                Navigate.with("return", true)
            }.andThen {}
        }

    }

}