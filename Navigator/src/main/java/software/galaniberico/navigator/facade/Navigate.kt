package software.galaniberico.navigator.facade

import android.app.Activity
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.util.ErrorMsgTemplate
import software.galaniberico.navigator.data.NavigateData
import software.galaniberico.navigator.data.NavigateDataActions
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.DataTypeMismatchException
import software.galaniberico.navigator.exceptions.NullActivityException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
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
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value")
        NavigateData.navigateIncome?.set(id, value)
            ?: throw UnexpectedFunctionCallException("You cannot add data outside of a pre-Navigate function. This method should only be called inside a method annotated with the @Navigation tag (executed by a NavigateProcess) or within a Navigate.to() lambda.")
    }

    fun withPrevious(vararg ids: String) {
        val data = NavigateData.of(Facade.getPreferredActivityOrFail()) ?: throw UnexpectedFunctionCallException("You cannot call this method if the current activity has no Navigation data.")
        for (id in ids) {
            val (value, found) = data.get(id)
            if (found)
                with(id, value)
        }
    }

    fun withResult(id: String, value: Any?) {
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value.")
        val resultData = NavigateData.of(Facade.getPreferredActivityOrFail())?.resultData ?: throw UnexpectedFunctionCallException("You cannot add data to return outside of a \"for result\" Activity.")
        resultData.add(id, value)
    }


    inline fun <reified T : Any?> get(id: String, default: T? = null): T? {
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value.")
        val (value, found) = NavigateDataActions.get(id)
        if (!found) return default
        if (value == null) return null
        if (value !is T) throw DataTypeMismatchException("The retrieved data for id \"$id\" is not of the expected type.")
        return value
    }

    inline fun <reified T : Any?> getResult(id: String, default: T? = null): T? {
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value.")
        val (value, found) = NavigateDataActions.getResult(id)
        if (!found) return default
        if (value == null) return null
        if (value !is T) throw DataTypeMismatchException("The retrieved data for id \"$id\" is not of the expected type.")
        return value
    }

    @NavigateProcess
    fun back(currentActivity: Activity? = null) {
        val activity: Activity = currentActivity ?: Facade.getPreferredActivity()
        ?: throw NullActivityException(
            ErrorMsgTemplate.CURRENT_ACTIVITY_NULL_WARNING.with(
                "navigate back to parent activity",
                "attempting navigation"
            )
        )
        if (NavigateData.of(activity)?.isForResult() == true){
            NavigateData.of(activity)?.resultData?.hasResult = true
        }
        activity.finish()
    }

    fun id(activity: Activity? = null): String?{
        val a = activity ?: Facade.getPreferredActivity() ?: return null
        return Facade.getId(a) //TODO checked
    }

}