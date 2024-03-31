package software.galaniberico.navigator.facade

import android.app.Activity
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.util.ErrorMsgTemplate
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.lifecicle.ComingActivityPile
import software.galaniberico.navigator.tags.Navigation
import java.lang.reflect.Method
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter

object Navigate {
    /**
     *
     */
    fun to(id: String) {

        if (id.isBlank()) throw IllegalArgumentException("The id field cannot be blank. Please revise the parameter value or if you prefer not to set an id, you can use Navigate.to(KClass<out Activity>) method instead")

        val currentActivity: Activity? = Facade.getCurrentActivity()
        if (currentActivity == null) {
            Log.w(
                PLUGIN_LOG_TAG,
                ErrorMsgTemplate.CURRENT_ACTIVITY_NULL_WARNING.with(
                    "navigate to another activity",
                    "attempting navigation"
                )
            )
            return
        }

        val annotatedMethods: MutableList<Method> = mutableListOf()
        var target: KClass<out Activity>? = null

        val declaredFunctions = currentActivity.javaClass.declaredMethods
        for (member in declaredFunctions) {
            if (member.isAnnotationPresent(Navigation::class.java) && member.getAnnotation(Navigation::class.java)?.id == id) {
                annotatedMethods.add(member)
                target = member.getAnnotation(Navigation::class.java)?.target
                break
            }
        }
        if (annotatedMethods.size == 0)
            Log.i(
                PLUGIN_LOG_TAG,
                "The current activity ($currentActivity) has no method with the annotation ${Navigation::class.simpleName} or not with the ID field with value \"$id\". No pre-execution method will be called"
            )

        if (annotatedMethods.size > 1) {
            //TODO maybe add some plugin configuration to change behaviour to send error instead of pick the first
            Log.w(
                PLUGIN_LOG_TAG,
                "The current activity ($currentActivity) has several annotations with the ID field with value \"$id\". Picking the first matching method"
            )
        }

        callPreExecutionMethod(annotatedMethods[0], currentActivity)
        target?.let {
            startActivity(it, id)
            ComingActivityPile.put(id, it)
        }
    }

    fun to(clazz: KClass<out Activity>, lambda: () -> Unit = {}) {

        val currentActivity: Activity? = Facade.getCurrentActivity()
        if (currentActivity == null) {
            Log.w(
                PLUGIN_LOG_TAG,
                ErrorMsgTemplate.CURRENT_ACTIVITY_NULL_WARNING.with(
                    "navigate to another activity",
                    "attempting navigation"
                )
            )
            return
        }

        lambda()
        clazz.let {
            val id = startActivity(it)
            ComingActivityPile.put(id, it)
        }
    }

    private fun callPreExecutionMethod(method: Method, currentActivity: Activity) {
        method.invoke(currentActivity)
    }

    private fun startActivity(target: KClass<out Activity>, id: String? = null): String {
        return Facade.startActivity(target.java, id)
    }

}