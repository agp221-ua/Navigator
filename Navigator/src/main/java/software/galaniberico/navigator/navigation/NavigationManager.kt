package software.galaniberico.navigator.navigation

import android.app.Activity
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.util.ErrorMsgTemplate
import software.galaniberico.navigator.configuration.MultipleNavigationIdTargets
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.exceptions.NoTargetsException
import software.galaniberico.navigator.exceptions.NullActivityException
import software.galaniberico.navigator.exceptions.TooManyTargetsException
import software.galaniberico.navigator.lifecicle.ComingActivityPile
import software.galaniberico.navigator.tags.Navigation
import java.lang.reflect.Method
import kotlin.reflect.KClass

class NavigationManager internal constructor(var activity: Activity?) {
    fun to(id: String) {
        checkId(id)

        if(activity == null)
            activity = currentActivity() ?: throw NullActivityException("You are trying to navigate from a null Activity. Maybe is not already started.")

        val annotatedMethods: MutableList<Method> = mutableListOf()
        var target: KClass<out Activity>? = null

        for (member in activity!!.javaClass.declaredMethods) {
            if (member.isAnnotationPresent(Navigation::class.java) && member.getAnnotation(
                    Navigation::class.java
                )?.id == id
            ) {
                annotatedMethods.add(member)
                if (target == null)
                    target = member.getAnnotation(Navigation::class.java)?.target
            }
        }
        checkTargets(annotatedMethods, activity!!, id)

        callPreExecutionMethod(annotatedMethods[0], activity!!)
        target?.let {
            Facade.startActivity(activity!!, it.java, id)
            ComingActivityPile.put(id, it)
        }
    }

    fun to(clazz: KClass<out Activity>, lambda: () -> Unit = {}) {
        lambda()
        clazz.let {
            val id = if (activity != null) Facade.startActivity(activity!!, it.java) else Facade.startActivity(it.java)
            ComingActivityPile.put(id, it)
        }
    }

    fun to(id: String, clazz: KClass<out Activity>, lambda: () -> Unit = {}) {
        checkId(id)

        lambda()
        clazz.let {
            if (activity != null) Facade.startActivity(activity!!, it.java, id) else Facade.startActivity(it.java, id)
            ComingActivityPile.put(id, it)
        }
    }

    private fun checkId(id: String) {
        if (id.isBlank()) throw IllegalArgumentException("The id field cannot be blank. Please revise the parameter value or if you prefer not to set an id, you can use to(KClass<out Activity>) method instead")
    }

    private fun callPreExecutionMethod(method: Method, currentActivity: Activity) {
        method.invoke(currentActivity)
    }

    private fun currentActivity(): Activity? {
        val currentActivity: Activity? = Facade.getCurrentActivity()
        if (currentActivity == null) {
            Log.w(
                PLUGIN_LOG_TAG,
                ErrorMsgTemplate.CURRENT_ACTIVITY_NULL_WARNING.with(
                    "navigate to another activity",
                    "attempting navigation"
                )
            )
        }
        return currentActivity
    }

    private fun checkTargets(
        annotatedMethods: MutableList<Method>,
        activity: Activity,
        id: String
    ) {
        if (annotatedMethods.size == 0) {
            Log.e(
                PLUGIN_LOG_TAG,
                "The current activity ($activity) has no method with the annotation ${Navigation::class.simpleName} or not with the ID field with value \"$id\"."
            )
            throw NoTargetsException("The current activity ($activity) has no method with the annotation ${Navigation::class.simpleName} or not with the ID field with value \"$id\".")
        }
        if (annotatedMethods.size > 1) {
            if (NavigatorConfigurations.multipleNavigationIdTargets == MultipleNavigationIdTargets.SEND_ERROR) {
                Log.e(
                    PLUGIN_LOG_TAG,
                    "The current activity ($activity) has several annotations with the ID field with value \"$id\"."
                )
                throw TooManyTargetsException("The current activity ($activity) has several annotations with the ID field with value \"$id\".")
            } else {
                Log.w(
                    PLUGIN_LOG_TAG,
                    "The current activity ($activity) has several annotations with the ID field with value \"$id\". Picking the first matching method"
                )
            }
        }
    }
}