package software.galaniberico.myapplication

import android.widget.TextView
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
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
import software.galaniberico.navigator.facade.Navigate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class GetResult {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun getResultOutBounds() {
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.getResult("id", "Fail please")
            }
        }
    }

    @Test
    fun getResultEmpty() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("getResultEmpty", MainActivity2::class) {
                Navigate.with("to do", {
                    Navigate.withResult("res1", "return")
                })
                Navigate.with("return", true)
            }.andThen {
                Assert.assertThrows(BlankIdFieldException::class.java){
                    Navigate.getResult<Any?>("", null)
                }
            }
        }
    }

    @Test
    fun getResultBlank() {
        activityRule.scenario.onActivity {
                Navigate.toReturn("getResultBlank", MainActivity2::class) {
                    Navigate.with("to do", {
                        Navigate.withResult("res1", "return")
                    })
                    Navigate.with("return", true)
                }.andThen {
                    Assert.assertThrows(BlankIdFieldException::class.java){
                        Navigate.getResult<Any?>("    ", null)
                    }
                }

        }
    }


    @Test
    fun getResultMultipleValues() {
        activityRule.scenario.onActivity {
                Navigate.toReturn("getResultMultipleValues", MainActivity2::class) {
                    Navigate.with("to do", {
                        Navigate.withResult("res1", "result ok")
                        Navigate.withResult("res2", "sdafdf")
                        Navigate.withResult("res3", "sdaffasdfdsfasdf")
                        Navigate.withResult("res4", "df")

                    })
                    Navigate.with("return", true)
                }.andThen {
                    it.findViewById<TextView>(R.id.tvMain).text = Navigate.getResult("res1", "default")

            }

        }
        onView(withId(R.id.tvMain)).check(matches(withText("result ok")))
    }



    @Test
    fun getResultDifferentValueTypes() {
        activityRule.scenario.onActivity {
                Navigate.toReturn("getResultDifferentValueTypes", MainActivity2::class) {
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
    @Test
    fun getResultSameId() {
        activityRule.scenario.onActivity {
                Navigate.toReturn("getResultSameId", MainActivity2::class) {
                    Navigate.with("to do", {
                        Navigate.withResult("res2", "result ok")
                        Navigate.withResult("res2", 45)

                    })
                    Navigate.with("return", true)
                }.andThen {
                    it.findViewById<TextView>(R.id.tvMain).text = "${Navigate.getResult("res2", 0)}"
                }


        }
        onView(withId(R.id.tvMain)).check(matches(withText("45")))
    }

    @Test
    fun getResultNull() {
            activityRule.scenario.onActivity {
                Navigate.toReturn("getResultNull", MainActivity2::class) {
                    Navigate.with("to do", {
                        Navigate.withResult("res2", null)

                    })
                    Navigate.with("return", true)
                }.andThen {
                    it.findViewById<TextView>(R.id.tvMain).text = Navigate.getResult("res2", "0") ?: "default"
                }


        }
        onView(withId(R.id.tvMain)).check(matches(withText("default")))
    }
    @Test
    fun getResultDefault() {
        activityRule.scenario.onActivity {
                Navigate.toReturn("getResultDefault", MainActivity2::class) {
                    Navigate.with("to do", {
                        Navigate.withResult("res2", "value found")

                    })
                    Navigate.with("return", true)
                }.andThen {
                    it.findViewById<TextView>(R.id.tvMain).text = Navigate.getResult("res1", "0000") ?: "found null"
                }


        }
        onView(withId(R.id.tvMain)).check(matches(withText("0000")))
    }

}