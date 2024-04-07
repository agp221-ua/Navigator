package software.galaniberico.navigator.facade

import android.app.Activity
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.util.ErrorMsgTemplate
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.UnloadNavigateData
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.ConcurrentNavigationLoadException
import software.galaniberico.navigator.exceptions.ConfigurationConflictException
import software.galaniberico.navigator.exceptions.InvalidActivityIdException
import software.galaniberico.navigator.exceptions.DataTypeMismatchException
import software.galaniberico.navigator.exceptions.NullActivityException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
import software.galaniberico.navigator.data.NavigateDataManager
import software.galaniberico.navigator.navigation.NavigationManager
import software.galaniberico.navigator.data.ResultDataManager
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

    @NavigateProcess
    fun toReturn(id: String): NavigationManager {
        return NavigationManager(null).toReturn(id)
    }

    @NavigateProcess
    fun toReturn(
        clazz: KClass<out Activity>,
        preNavigateFunction: () -> Unit = {}
    ): NavigationManager {
        return NavigationManager(null).toReturn(clazz, preNavigateFunction)
    }

    @NavigateProcess
    fun toReturn(
        id: String,
        clazz: KClass<out Activity>,
        preNavigateFunction: () -> Unit = {}
    ): NavigationManager {
        return NavigationManager(null).toReturn(id, clazz, preNavigateFunction)
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

    fun withResult(id: String, value: Any?) {
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value.")
        ResultDataManager.top()?.add(id, value)
            ?: throw UnexpectedFunctionCallException("This method cannot be called out of an Activity for result started with 'toReturn().andThen()'")
    }


    inline fun <reified T : Any?> get(id: String, default: T? = null): T? {
        val (value, found) = NavigateDataManager.get(id)
        if (!found) return default
        if (value !is T) throw DataTypeMismatchException("The retrieved data for id \"$id\" is not of the expected type.")
        return value
    }

    inline fun <reified T : Any?> getResult(id: String, default: T? = null): T? {
        val (value, found) = ResultDataManager.getResult(id)
        if (!found) return default
        if (value !is T) throw DataTypeMismatchException("The retrieved data for id \"$id\" is not of the expected type.")
        return value
    }

    @NavigateProcess
    fun back(currentActivity: Activity? = null) {
        val activity: Activity = currentActivity ?: Facade.getCurrentActivity()
        ?: throw NullActivityException(
            ErrorMsgTemplate.CURRENT_ACTIVITY_NULL_WARNING.with(
                "navigate back to parent activity",
                "attempting navigation"
            )
        )
        if (ResultDataManager.top() == null)
            activity.finish()
        else {
            ResultDataManager.loadOutput()
            activity.finish()
        }

    }

    fun load(currentActivity: Activity) {
        if (NavigatorConfigurations.unloadNavigateData != UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY)
            throw ConfigurationConflictException("Calling this method with the current configuration is not allowed.")
        val activityId = Facade.getId(currentActivity)
            ?: throw InvalidActivityIdException("The provided Activity does not contain a valid ID. It is possible that it was not started by this plugin.")
        if (NavigateDataManager.isLoaded())
            throw ConcurrentNavigationLoadException("Attempting to load navigation data while another one is still loaded. Please nullify the previous one before loading a new one.")

        NavigateDataManager.loadStoredNavigateData(activityId)
    }

    fun nullify() {
        if (NavigatorConfigurations.unloadNavigateData != UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY
            && NavigatorConfigurations.unloadNavigateData != UnloadNavigateData.FROM_LAND_UNTIL_MANUAL_NULLIFY
        )
            throw ConfigurationConflictException("Calling this method with the current configuration is not allowed.")
        NavigateDataManager.nullifyCurrentOutcomeNavigateData()
    }


}