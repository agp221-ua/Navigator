package software.galaniberico.navigator.facade

import android.app.Activity
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.util.ErrorMsgTemplate
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.tags.Navigation
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.instanceParameter

object Navigate {
    /**
     *
     */
    fun to(id: String) {
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

        val annotatedMethods: MutableList<KCallable<*>> = mutableListOf()
        var target: KClass<out Activity>? = null

        currentActivity::class.members.forEach {
            val annotation = it.findAnnotation<Navigation>() ?: return@forEach
            if (annotation.id == id) {
                annotatedMethods.add(it)
                if (target == null)
                    target = annotation.target
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
        target?.let { startActivity(it, id) }
    }

    private fun callPreExecutionMethod(kCallable: KCallable<*>, currentActivity: Activity) {
        if (kCallable.instanceParameter == null)
            throw IllegalStateException("The instanceParameter of the KCallable obtained from the current Activity is null")

        kCallable.call(currentActivity)
    }

    private fun startActivity(target: KClass<out Activity>, id: String? = null) {
        Facade.startActivity(target.java, id)
    }

}