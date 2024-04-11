package software.galaniberico.myapplication

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
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
import software.galaniberico.navigator.exceptions.NoTargetsException
import software.galaniberico.navigator.exceptions.TooManyTargetsException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
import software.galaniberico.navigator.facade.Navigate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NavigateToReturn {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun toNeedsAndThen() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("withTag")
        }
        onView(withId(R.id.tvA)).check(doesNotExist())
    }

    @Test
    fun toEmpty() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.toReturn("")
            }
        }
    }

    @Test
    fun toBlank() {
        Assert.assertThrows(BlankIdFieldException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.toReturn("              ")
            }
        }
    }

    @Test
    fun toNoTarget() {
        Assert.assertThrows(NoTargetsException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.toReturn("to2")
            }
        }
    }

    @Test
    fun toTooTargets() {
        Assert.assertThrows(TooManyTargetsException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.toReturn("withTooTargets")
            }
        }
    }

    @Test
    fun andThenFromNoToReturn() {
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.from(it).andThen()
            }
        }
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.from(it).andThen("sth")
            }
        }
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.from(it).andThen {}
            }
        }
    }

    @Test
    fun toCreateNoReturnActivityFromAndThen() {

        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn("onResult").andThen {
                Navigate.from(it).to("withTag")
            }
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvA)).check(matches(withText("45")))
    }

    @Test
    fun toCreateReturnActivityFromAndThen() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("onResult").andThen {
                Navigate.toReturn("onResult").andThen {
                    it.findViewById<TextView>(R.id.tvMain).text = "returned"
                }
            }
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned")))
    }

    @Test
    fun toCreateReturnActivityAndCallNoReturnActivity() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("onResultNavigationNested", MainActivity2::class) {
                Navigate.with("to do", {
                    Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
                        Navigate.to("withTag")
                    }
                    Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
                        Navigate.to(MainActivity2::class)
                    }
                    Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
                        Navigate.to("something", MainActivity2::class)
                    }
                })
            }.andThen {
                Assert.fail("Should have failed")
            }
        }

    }


    // ### .to(id) ###
    @Test
    fun toAndThen() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("onResult").andThen()
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned")))
    }

    @Test
    fun toAndThenId() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("onResult").andThen("onResult2")
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned2")))
    }

    @Test
    fun toAndThenLambda() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("onResult").andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))

    }


    @Test
    fun to2From() {
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn("onResult").andThen()
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned")))
    }

    @Test
    fun to2Repeated() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("onResult").andThen()
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned")))
        activityRule.scenario.onActivity {
            Navigate.toReturn("onResult").andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))
    }

    @Test
    fun to2FromRepeated() {
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn("onResult").andThen()
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned")))
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn("onResult").andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))
    }

    // ### .to(clazz, lambda) ###

    @Test
    fun toClassAndThen() {
        Assert.assertThrows(UnexpectedFunctionCallException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.toReturn(MainActivity2::class) {
                    Navigate.with("return", true)
                }.andThen()
            }
        }
    }

    @Test
    fun toClassAndThenId() {
        activityRule.scenario.onActivity {
            Navigate.toReturn(MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen("onResult2")
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned2")))
    }

    @Test
    fun toClassAndThenLambda() {
        activityRule.scenario.onActivity {
            Navigate.toReturn(MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))

    }

    @Test
    fun toClass2From() {
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn(MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))
    }

    @Test
    fun toClass2Repeated() {
        activityRule.scenario.onActivity {
            Navigate.toReturn(MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))
        activityRule.scenario.onActivity {
            Navigate.toReturn(MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned4"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned4")))
    }

    @Test
    fun toClass2FromRepeated() {
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn(MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn(MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned4"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned4")))
    }

    // ### .to(id, clazz, lambda) ###
    @Test
    fun toIdClassAndThenNoTarget() {
        Assert.assertThrows(NoTargetsException::class.java) {
            activityRule.scenario.onActivity {
                Navigate.toReturn("withTag").andThen()
            }
        }
    }

    @Test
    fun toIdClassAndThen() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("onResult", MainActivity2::class){
                Navigate.with("return", true)
            }.andThen()
        }

        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned")))
    }

    @Test
    fun toIdClassAndThenId() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("idCustom", MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen("onResult2")
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned2")))
    }

    @Test
    fun toIdClassAndThenLambda() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("idCustom", MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))

    }

    @Test
    fun toIdClass2From() {
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn("idCustom", MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))
    }

    @Test
    fun toIdClass2Repeated() {
        activityRule.scenario.onActivity {
            Navigate.toReturn("idCustom", MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))
        activityRule.scenario.onActivity {
            Navigate.toReturn("idCustom", MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned4"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned4")))
    }

    @Test
    fun toIdClass2FromRepeated() {
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn("idCustom", MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned3"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned3")))
        activityRule.scenario.onActivity {
            Navigate.from(it).toReturn("idCustom", MainActivity2::class) {
                Navigate.with("return", true)
            }.andThen {
                it.findViewById<TextView>(R.id.tvMain).text = "returned4"
            }
        }
        Thread.sleep(1000)
        onView(withId(R.id.tvMain)).check(matches(withText("returned4")))
    }


}