package software.galaniberico.navigator.facade

import android.app.Activity
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.UnloadNavigateData
import software.galaniberico.navigator.exceptions.ConcurrentNavigationLoadException
import software.galaniberico.navigator.exceptions.ConfigurationConflictException
import software.galaniberico.navigator.exceptions.InvalidActivityIdException
import software.galaniberico.navigator.exceptions.DataTypeMismatchException
import software.galaniberico.navigator.navigation.NavigateDataManager
import software.galaniberico.navigator.navigation.NavigationManager
import software.galaniberico.navigator.tags.NavigateProcess
import kotlin.reflect.KClass

object Navigate {
    internal var navigating = false
    fun from(activity: Activity): NavigationManager {
        return NavigationManager(activity)
    }

    @NavigateProcess
    fun to(id: String) {
        NavigationManager(null).to(id)
    }

    @NavigateProcess
    fun to(clazz: KClass<out Activity>, preNavigateFunction: () -> Unit = {}) {
        NavigationManager(null).to(clazz, preNavigateFunction)
    }

    @NavigateProcess
    fun to(id: String, clazz: KClass<out Activity>, preNavigateFunction: () -> Unit = {}) {
        NavigationManager(null).to(id, clazz, preNavigateFunction)
    }

    fun with(id: String, value: Any?) {
        NavigateDataManager.with(id, value)
    }

    fun withLoaded(vararg ids: String) {
        for (id in ids) {
            val (value, found) = NavigateDataManager.get(id)
            if (found)
                NavigateDataManager.with(id, value)
        }
    }

    inline fun <reified T : Any?> get(id: String, default: T? = null): T? {
        val (value, found) = NavigateDataManager.get(id)
        if (!found) return default
        if (value !is T) throw DataTypeMismatchException("The retrieved data is not of the expected type.")
        return value
    }

    fun load(currentActivity: Activity){
        if(NavigatorConfigurations.unloadNavigateData != UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY)
            throw ConfigurationConflictException("Calling this method with the current configuration is not allowed.")
        val activityId = currentActivity.intent.getStringExtra(Facade.getIdKey())
            ?: throw InvalidActivityIdException("The provided Activity does not contain a valid ID. It is possible that it was not started by this plugin.")
        if (NavigateDataManager.isLoaded())
            throw ConcurrentNavigationLoadException("Attempting to load navigation data while another one is still loaded. Please nullify the previous one before loading a new one.")

        NavigateDataManager.loadStoredNavigateData(activityId)
    }

    fun nullify(){
        if(NavigatorConfigurations.unloadNavigateData != UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY
            && NavigatorConfigurations.unloadNavigateData != UnloadNavigateData.FROM_LAND_UNTIL_MANUAL_NULLIFY)
            throw ConfigurationConflictException("Calling this method with the current configuration is not allowed.")
        NavigateDataManager.nullifyCurrentOutcomeNavigateData()
    }


}