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
import software.galaniberico.navigator.data.NavigateData
import software.galaniberico.navigator.data.ParentData
import software.galaniberico.navigator.data.ResultData
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.ConcurrentNavigationException
import software.galaniberico.navigator.exceptions.NoTargetsException
import software.galaniberico.navigator.exceptions.TooManyTargetsException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.tags.Navigation
import software.galaniberico.navigator.tags.OnResult
import java.lang.reflect.Method
import kotlin.reflect.KClass

class NavigationManager internal constructor(var activity: Activity?) {
    private var navigateData: NavigateData? = null
    private var target: KClass<out Activity>? = null
    private var id: String? = null

    private fun commonTo(lambdaGetter: () -> (Pair<KClass<out Activity>, () -> Unit>)): Pair<KClass<out Activity>, NavigateData> {
        if (Navigate.navigating) throw ConcurrentNavigationException("Nested Navigation is not allowed.")
        activity = activity ?: Facade.getPreferredActivityOrFail()
        if (NavigateData.of(activity!!)?.isForResult() == true)
            throw UnexpectedFunctionCallException("Attempting to navigate to an activity that is not intended to be returned from one that does, what is not allowed.")
        Navigate.navigating = true

        val (target, lambda) = lambdaGetter()
        val parentData = resolveParentData(activity!!)
        val navigateData = NavigateData(parentData)
        NavigateData.prepareIncome()
        try {
        lambda()
        } finally {
            NavigateData.resolveIncome(navigateData)
        }

        return Pair(target, navigateData)
    }

    private fun start(
        target: KClass<out Activity>,
        navigateData: NavigateData,
        _id: String? = null
    ) {
        Facade.startActivity(activity!!, target.java, _id) { _, _, internalId ->
            NavigateData.set(internalId, navigateData)
        }
    }

    fun to(id: String) {
        checkId(id)

        val (target, navigateData) = commonTo {
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
            return@commonTo Pair(target!!) {
                annotatedMethods[0].apply {
                    val a = this.isAccessible
                    isAccessible = true
                    invoke(activity!!)
                    isAccessible = a
                }
            }
        }
        start(target, navigateData, id)
    }

    fun to(clazz: KClass<out Activity>, lambda: () -> Unit = {}) {
        val (target, navigateData) = commonTo { return@commonTo Pair(clazz, lambda) }
        start(target, navigateData)
    }

    fun to(id: String, clazz: KClass<out Activity>, lambda: () -> Unit = {}) {
        val (target, navigateData) = commonTo { return@commonTo Pair(clazz, lambda) }
        start(target, navigateData, id)
    }

    //TODO check configuration
    //TODO the toReturns below must just add the return data to the navigation data and then in the
    // andThen() do the necessary actions to get the onActivityResult actions and init the navigation, maybe with a to() call
    //TODO just see if something else is needed
    fun toReturn(id: String): NavigationManager {
        checkId(id)

        val (_target, _navigateData) = commonTo {
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
            return@commonTo Pair(target!!) {
                annotatedMethods[0].apply {
                    val a = this.isAccessible
                    isAccessible = true
                    invoke(activity!!)
                    isAccessible = a
                }
            }
        }
        this.target = _target
        this.navigateData = _navigateData
        this.id = id

        return this
    }

    fun toReturn(clazz: KClass<out Activity>, lambda: () -> Unit = {}): NavigationManager {
        val (_target, _navigateData) = commonTo { return@commonTo Pair(clazz, lambda) }
        this.target = _target
        this.navigateData = _navigateData
        return this

    }

    fun toReturn( id: String, clazz: KClass<out Activity>, lambda: () -> Unit = {}): NavigationManager {
        checkId(id)
        val (_target, _navigateData) = commonTo { return@commonTo Pair(clazz, lambda) }

        this.target = _target
        this.navigateData = _navigateData
        this.id = id
        return this
    }

    fun andThen() {
        checkToReturnIsCalled(id == null)
        navigateData!!.resultData = getResultDataFromTag(id!!)
        start(target!!, navigateData!!, id)
    }

    fun andThen(onResultId: String) {
        checkToReturnIsCalled()
        navigateData!!.resultData = getResultDataFromTag(onResultId)
        start(target!!, navigateData!!, id)
    }

    fun andThen(lambda: () -> Unit) {
        checkToReturnIsCalled()
        navigateData!!.resultData = ResultData(lambda)

        start(target!!, navigateData!!, id)
    }

    private fun checkToReturnIsCalled(extraCondition: Boolean = false) {
        if (navigateData == null || target == null || extraCondition) {
            Navigate.navigating = false
            throw UnexpectedFunctionCallException("This method cannot be called before calling the 'toReturn' method.")
        }
    }

    private fun getResultDataFromTag(onResultId: String): ResultData {
        val annotatedMethods: MutableList<Method> = mutableListOf()

        for (member in activity!!.javaClass.declaredMethods) {
            if (member.isAnnotationPresent(OnResult::class.java)
                && member.getAnnotation(OnResult::class.java)?.id == onResultId
            ) {
                annotatedMethods.add(member)
            }
        }

        checkOnResultTargets(annotatedMethods, activity!!, onResultId)

        val resultData = ResultData {
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
        val parentData = ParentData(Facade.getInternalId(activity))
        parentData.activity = activity
        if (NavigatorConfigurations.parentActivityDataAccess != ParentActivityDataAccess.MAP_COPY)
            return parentData
        parentData.saveData()
        return parentData
    }

    private fun checkId(id: String): String {
        if (id.isBlank()) {
            Navigate.navigating = false
            throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value or if you prefer not to set an id, you can use to(KClass<out Activity>) method instead")
        }
        return id
    }

    private fun currentActivity(): Activity? {
        val currentActivity: Activity? = Facade.getPreferredActivity()
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



}