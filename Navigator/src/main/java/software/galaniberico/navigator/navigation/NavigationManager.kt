package software.galaniberico.navigator.navigation

import android.app.Activity
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.moduledroid.util.ErrorMsgTemplate
import software.galaniberico.navigator.configuration.MultipleNavigationIdTargets
import software.galaniberico.navigator.configuration.MultipleOnResultIdTargets
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.configuration.ParentActivityDataAccess
import software.galaniberico.navigator.data.ComingActivityPile
import software.galaniberico.navigator.data.NavigateDataManager
import software.galaniberico.navigator.data.ParentData
import software.galaniberico.navigator.data.ResultData
import software.galaniberico.navigator.data.ResultDataManager
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.ConcurrentNavigationException
import software.galaniberico.navigator.exceptions.InvalidActivityIdException
import software.galaniberico.navigator.exceptions.NoTargetsException
import software.galaniberico.navigator.exceptions.NullActivityException
import software.galaniberico.navigator.exceptions.TooManyTargetsException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.tags.Navigation
import software.galaniberico.navigator.tags.OnResult
import java.lang.reflect.Method
import kotlin.reflect.KClass

class NavigationManager internal constructor(var activity: Activity?) {
    private var navigateData: Map<String, Any?>? = null
    private var parentData: ParentData? = null
    private var target: KClass<out Activity>? = null
    private var id: String? = null

    fun to(id: String) {
        checkId(id)
        checkUnique(id)
        getActivity()

        if (Navigate.navigating) throw ConcurrentNavigationException("Nested Navigation is not allowed.")
        if (ResultDataManager.top() != null)
            throw UnexpectedFunctionCallException("Attempting to navigate to an activity that is not intended to be returned from one that does, what is not allowed.")
        Navigate.navigating = true
        val annotatedMethods: MutableList<Method> = mutableListOf()
        var target: KClass<out Activity>? = null

        for (member in activity!!.javaClass.declaredMethods) {
            if (member.isAnnotationPresent(Navigation::class.java)
                && member.getAnnotation(Navigation::class.java)?.id == id
            ) {
                annotatedMethods.add(member)
                if (target == null)
                    target = member.getAnnotation(Navigation::class.java)?.target
            }
        }
        checkNavigationTargets(annotatedMethods, activity!!, id)

        NavigateDataManager.prepareIncome()
        annotatedMethods[0].apply {
            val a = this.isAccessible
            isAccessible = true
            invoke(activity!!)
            isAccessible = a
        }
        val navigateData = NavigateDataManager.resolveIncome()

        val parentData = resolveParentData(activity!!)

        target?.let {
            Facade.startActivity(activity!!, it.java, id)
            ComingActivityPile.put(id, it, navigateData, parentData)
        }
    }

    fun to(clazz: KClass<out Activity>, lambda: () -> Unit = {}) {
        if (Navigate.navigating) throw ConcurrentNavigationException("Nested Navigation is not allowed.")
        if (ResultDataManager.top() != null) throw UnexpectedFunctionCallException("Attempting to navigate to an activity that is not intended to be returned from one that does, what is not allowed.")
        getActivity()
        Navigate.navigating = true

        NavigateDataManager.prepareIncome()
        try {
            lambda()
        }catch (e: Exception){
            NavigateDataManager.nullifyCurrentIncomeNavigateData()
            throw e
        }
        val navigateData = NavigateDataManager.resolveIncome()
        val parentData = resolveParentData(activity!!)

        val id = Facade.startActivity(activity!!, clazz.java)
        ComingActivityPile.put(id, clazz, navigateData, parentData)
    }

    fun to(id: String, clazz: KClass<out Activity>, lambda: () -> Unit = {}) {
        if (Navigate.navigating) throw ConcurrentNavigationException("Nested Navigation is not allowed.")
        if (ResultDataManager.top() != null) throw UnexpectedFunctionCallException("Attempting to navigate to an activity that is not intended to be returned from one that does, what is not allowed.")
        getActivity()
        Navigate.navigating = true
        checkId(id)
        checkUnique(id)
        NavigateDataManager.prepareIncome()
        lambda()
        val navigateData = NavigateDataManager.resolveIncome()
        val parentData = resolveParentData(activity!!)

        Facade.startActivity(activity!!, clazz.java, id)

        ComingActivityPile.put(id, clazz, navigateData, parentData)
    }

    fun toReturn(id: String): NavigationManager {
        this.id = checkId(id)
        checkUnique(id)

        getActivity()

        if (Navigate.navigating) throw ConcurrentNavigationException("Nested Navigation is not allowed.")
        Navigate.navigating = true
        val annotatedMethods: MutableList<Method> = mutableListOf()

        for (member in activity!!.javaClass.declaredMethods) {
            if (member.isAnnotationPresent(Navigation::class.java)
                && member.getAnnotation(Navigation::class.java)?.id == id
            ) {
                val idddd = member.getAnnotation(Navigation::class.java)?.id
                annotatedMethods.add(member)
                if (target == null)
                    target = member.getAnnotation(Navigation::class.java)?.target
            }
        }

        checkNavigationTargets(annotatedMethods, activity!!, id)

        NavigateDataManager.prepareIncome()
        annotatedMethods[0].apply {
            val a = this.isAccessible
            isAccessible = true
            invoke(activity!!)
            isAccessible = a
        }
        navigateData = NavigateDataManager.resolveIncome()

        parentData = resolveParentData(activity!!)

        return this
    }

    fun toReturn(clazz: KClass<out Activity>, lambda: () -> Unit = {}): NavigationManager {
        if (Navigate.navigating) throw ConcurrentNavigationException("Nested Navigation is not allowed.")
        getActivity()
        Navigate.navigating = true
        NavigateDataManager.prepareIncome()
        lambda()
        navigateData = NavigateDataManager.resolveIncome()
        parentData = resolveParentData(activity!!)
        target = clazz

        return this

    }

    fun toReturn(
        id: String,
        clazz: KClass<out Activity>,
        lambda: () -> Unit = {}
    ): NavigationManager {
        if (Navigate.navigating) throw ConcurrentNavigationException("Nested Navigation is not allowed.")
        getActivity()
        Navigate.navigating = true
        this.id = checkId(id)
        checkUnique(id)
        NavigateDataManager.prepareIncome()
        lambda()
        navigateData = NavigateDataManager.resolveIncome()
        parentData = resolveParentData(activity!!)
        target = clazz

        return this
    }

    fun andThen() {
        checkToReturnIsCalled()
        if (id == null) {
            Navigate.navigating = false
            throw UnexpectedFunctionCallException("This method cannot be called after calling a 'toReturn' method without providing an 'id' parameter.")
        }
        val onResultId = id!!
        val resultData = getResultDataFromTag(onResultId)

        Facade.startActivity(activity!!, target!!.java, id)
        ComingActivityPile.put(id!!, target!!, navigateData!!, parentData!!, resultData)
    }


    fun andThen(onResultId: String) {
        checkToReturnIsCalled()
        val resultData = getResultDataFromTag(onResultId)
        if (id == null) {
            val idd = Facade.startActivity(activity!!, target!!.java)
            ComingActivityPile.put(idd, target!!, navigateData!!, parentData!!, resultData)
        } else {
            Facade.startActivity(activity!!, target!!.java, id)
            ComingActivityPile.put(id!!, target!!, navigateData!!, parentData!!, resultData)
        }
    }

    fun andThen(lambda: () -> Unit) {
        checkToReturnIsCalled()
        val resultData = ResultData(parentData!!.id!!, lambda)
        if (id == null) {
            val idd = if (activity != null)
                Facade.startActivity(activity!!, target!!.java)
            else
                Facade.startActivity(target!!.java)
            ComingActivityPile.put(idd, target!!, navigateData!!, parentData!!, resultData)
        } else {
            if (activity != null)
                Facade.startActivity(activity!!, target!!.java, id)
            else
                Facade.startActivity(target!!.java, id)
            ComingActivityPile.put(id!!, target!!, navigateData!!, parentData!!, resultData)
        }
    }

    private fun checkToReturnIsCalled() {
        if (navigateData == null || parentData == null || target == null) {
            Navigate.navigating = false
            throw UnexpectedFunctionCallException("This method cannot be called before calling the 'toReturn' method.")
        }
    }
    private fun getResultDataFromTag(onResultId: String): ResultData {
        getActivity()

        val annotatedMethods: MutableList<Method> = mutableListOf()

        for (member in activity!!.javaClass.declaredMethods) {
            if (member.isAnnotationPresent(OnResult::class.java)
                && member.getAnnotation(OnResult::class.java)?.id == onResultId
            ) {
                annotatedMethods.add(member)
            }
        }

        checkOnResultTargets(annotatedMethods, activity!!, onResultId)

        val resultData = ResultData(parentData!!.id!!) {
            annotatedMethods[0].apply {
                val a = this.isAccessible
                this.isAccessible = true
                this.invoke(activity!!)
                this.isAccessible = a
            }
        }
        return resultData
    }

    private fun resolveParentData(activity: Activity): ParentData {
        val parentData = ParentData()
        parentData.activity = activity
        if (NavigatorConfigurations.parentActivityDataAccess != ParentActivityDataAccess.MAP_COPY)
            return parentData
        parentData.saveData()
        return parentData
    }

    private fun checkId(id: String): String {
        if (id.isBlank()){
            Navigate.navigating = false
            throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value or if you prefer not to set an id, you can use to(KClass<out Activity>) method instead")
        }
        return id
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

    private fun checkNavigationTargets(
        annotatedMethods: MutableList<Method>,
        activity: Activity,
        id: String
    ) {
        if (annotatedMethods.size == 0) {
            Log.e(
                PLUGIN_LOG_TAG,
                "The current activity ($activity) has no method with the annotation ${Navigation::class.simpleName} or not with the ID field with value \"$id\"."
            )
            Navigate.navigating = false
            throw NoTargetsException("The current activity ($activity) has no method with the annotation ${Navigation::class.simpleName} or not with the ID field with value \"$id\".")
        }
        if (annotatedMethods.size > 1) {
            if (NavigatorConfigurations.multipleNavigationIdTargets == MultipleNavigationIdTargets.SEND_ERROR) {
                Log.e(
                    PLUGIN_LOG_TAG,
                    "The current activity ($activity) has several annotations with the ID field with value \"$id\"."
                )
                Navigate.navigating = false
                throw TooManyTargetsException("The current activity ($activity) has several annotations with the ID field with value \"$id\".")
            } else {
                Log.w(
                    PLUGIN_LOG_TAG,
                    "The current activity ($activity) has several annotations with the ID field with value \"$id\". Picking the first matching method"
                )
            }
        }
    }

    private fun checkOnResultTargets(
        annotatedMethods: MutableList<Method>,
        activity: Activity,
        id: String
    ) {
        if (annotatedMethods.size == 0) {
            Log.e(
                PLUGIN_LOG_TAG,
                "The current activity ($activity) has no method with the annotation ${OnResult::class.simpleName} or not with the ID field with value \"$id\"."
            )
            Navigate.navigating = false
            throw NoTargetsException("The current activity ($activity) has no method with the annotation ${OnResult::class.simpleName} or not with the ID field with value \"$id\".")
        }
        if (annotatedMethods.size > 1) {
            if (NavigatorConfigurations.multipleOnResultIdTargets == MultipleOnResultIdTargets.SEND_ERROR) {
                Log.e(
                    PLUGIN_LOG_TAG,
                    "The current activity ($activity) has several annotations with the ID field with value \"$id\"."
                )
                Navigate.navigating = false
                throw TooManyTargetsException("The current activity ($activity) has several annotations with the ID field with value \"$id\".")
            } else {
                Log.w(
                    PLUGIN_LOG_TAG,
                    "The current activity ($activity) has several annotations with the ID field with value \"$id\". Picking the first matching method"
                )
            }
        }
    }

    private fun checkUnique(id: String){
        if (ComingActivityPile.has(id)) {
            Navigate.navigating = false
            throw InvalidActivityIdException("The ID provided is already in use. Please choose a different ID to avoid conflicts.")
        }
    }

    private fun getActivity() {
        if (activity == null)
            activity = currentActivity()
        if(activity == null){
            Navigate.navigating = false
            throw NullActivityException("You are trying to navigate from a null Activity. Maybe is not already started.")
        }
    }


}