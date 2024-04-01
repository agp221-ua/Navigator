package software.galaniberico.navigator.facade

import android.app.Activity
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.util.ErrorMsgTemplate
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.lifecicle.ComingActivityPile
import software.galaniberico.navigator.navigation.NavigationManager
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
    fun from(activity: Activity): NavigationManager{
        return NavigationManager(activity)
    }

    fun to(id: String) {
        NavigationManager(null).to(id)
    }

    fun to(clazz: KClass<out Activity>, lambda: () -> Unit = {}) {
        NavigationManager(null).to(clazz, lambda)
    }

    fun to(id: String, clazz: KClass<out Activity>, lambda: () -> Unit = {}) {
        NavigationManager(null).to(id, clazz, lambda)
    }

}