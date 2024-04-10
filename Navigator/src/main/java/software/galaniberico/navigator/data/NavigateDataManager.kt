package software.galaniberico.navigator.data

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.configuration.LandGetterSearch
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.configuration.UnloadNavigateData
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.ConfigurationConflictException
import software.galaniberico.navigator.exceptions.MissingLoadedDataException
import software.galaniberico.navigator.exceptions.MissingNavigateDataException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException

object NavigateDataManager {
    private var currentIncomeNavigateData: MutableMap<String, Any?>? = null

    private val navigateDataStorage: MutableMap<String, Map<String, Any?>> = mutableMapOf()
    private val parentDataStorage: MutableMap<String, ParentData> = mutableMapOf()

    private var currentOutcomeNavigateData: Map<String, Any?>? = null
    @SuppressLint("StaticFieldLeak")
    private var currentOutcomeParentData: ParentData? = null
    internal fun prepareIncome() {
        currentIncomeNavigateData = mutableMapOf()
    }

    internal fun resolveIncome(): Map<String, Any?> {
        val res = currentIncomeNavigateData ?: mutableMapOf()
        currentIncomeNavigateData = null
        return res
    }

    internal fun with(id: String, value: Any?) {
        if (NavigatorConfigurations.unloadNavigateData == UnloadNavigateData.NEVER)
            throw ConfigurationConflictException("You are attempting to add data to a Navigation, but the current configuration does not allow it.")
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value")
        currentIncomeNavigateData?.set(id, value)
            ?: throw UnexpectedFunctionCallException("You cannot add data outside of a pre-Navigate function. This method should only be called inside a method annotated with the @Navigation tag (executed by a NavigateProcess) or within a Navigate.to() lambda.")
    }


    fun get(id: String, navigateDataOnly: Boolean = false): Pair<Any?, Boolean> {
        if (NavigatorConfigurations.unloadNavigateData == UnloadNavigateData.NEVER)
            throw ConfigurationConflictException("You are attempting to retrieve data from a Navigation, but the current configuration does not allow it.")
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value")
        if (currentOutcomeNavigateData == null) {
            if (NavigatorConfigurations.unloadNavigateData == UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY)
                throw ConfigurationConflictException("You are attempting to retrieve data from a Navigation, but according to the current configuration, you must manually load it before using Navigate.load().")
            throw MissingLoadedDataException("You are attempting to retrieve data from a Navigation, but there is no loaded data available. This may occur because no landing process has occurred or, as per the current configuration, the data has been nullified.")
        }
        if (navigateDataOnly) {
            if (!currentOutcomeNavigateData!!.containsKey(id)) return Pair(null, false)
            return Pair(currentOutcomeNavigateData!![id], true)
        }
        when(NavigatorConfigurations.landGetterSearch){
            LandGetterSearch.NONE -> return Pair(null, false)
            LandGetterSearch.OLD_FIELDS -> return currentOutcomeParentData!!.get(id)
            LandGetterSearch.NAVIGATE_DATA -> {
                if (!currentOutcomeNavigateData!!.containsKey(id)) return Pair(null, false)
                return Pair(currentOutcomeNavigateData!![id], true)
            }
            LandGetterSearch.OLD_FIELDS_THEN_NAVIGATE_DATA -> {
                val (value, found) = currentOutcomeParentData!!.get(id)
                if (found) return Pair(value, true)
                if (!currentOutcomeNavigateData!!.containsKey(id)) return Pair(null, false)
                return Pair(currentOutcomeNavigateData!![id], true)
            }
            LandGetterSearch.NAVIGATE_DATA_THEN_OLD_FIELDS -> {
                if (currentOutcomeNavigateData!!.containsKey(id)) return Pair(
                    currentOutcomeNavigateData!![id], true)
                return currentOutcomeParentData!!.get(id)
            }
        }
    }

    internal fun storeNavigateData(id: String, navigateData: Map<String, Any?>, parentData: ParentData){
        if (navigateDataStorage.containsKey(id))
            Log.w(PLUGIN_LOG_TAG, "A stored navigate data with the provided ID already exists. The previous value will be overwritten. If this behavior is unexpected, ensure unique IDs are used across different navigations.")
        navigateDataStorage[id] = navigateData
        parentDataStorage[id] = parentData
    }

    internal fun loadStoredNavigateData(id: String){
        currentOutcomeNavigateData = navigateDataStorage.remove(id)
            ?: throw MissingNavigateDataException("There is no stored navigate data associated to given id parameter. Maybe it has been already loaded.")
        currentOutcomeParentData = parentDataStorage.remove(id)
            ?: throw MissingNavigateDataException("There is no stored parent data associated to given id parameter. Maybe it has been already loaded.")
    }

    internal fun nullifyCurrentOutcomeNavigateData(){
        currentOutcomeNavigateData = null
        currentOutcomeParentData = null
    }

    internal fun nullifyCurrentIncomeNavigateData(){
        currentIncomeNavigateData = null
    }

    internal fun isLoaded(): Boolean {
        return currentOutcomeNavigateData != null
    }

    internal fun isParent(activity: Activity): Boolean {
        if (!isLoaded()) return false
        val id = Facade.getId(activity)
        return id == currentOutcomeParentData?.id
    }
    

}