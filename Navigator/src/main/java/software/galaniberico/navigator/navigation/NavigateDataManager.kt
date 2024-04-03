package software.galaniberico.navigator.navigation

import android.util.Log
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.configuration.UnloadNavigateData
import software.galaniberico.navigator.exceptions.ConfigurationConflictException
import software.galaniberico.navigator.exceptions.MissingLoadedDataException
import software.galaniberico.navigator.exceptions.MissingNavigateDataException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException

object NavigateDataManager {
    private var currentIncomeNavigateData: MutableMap<String, Any?>? = null

    private val navigateDataStorage: MutableMap<String, Map<String, Any?>> = mutableMapOf()

    private var currentOutcomeNavigateData: Map<String, Any?>? = null
    fun prepareIncome() {
        currentIncomeNavigateData = mutableMapOf()
    }

    fun resolveIncome(): Map<String, Any?> {
        val res = currentIncomeNavigateData ?: mutableMapOf()
        currentIncomeNavigateData = null
        return res
    }

    fun with(id: String, value: Any?) {
        if (NavigatorConfigurations.unloadNavigateData == UnloadNavigateData.NEVER)
            throw ConfigurationConflictException("You are attempting to add data to a Navigation, but the current configuration does not allow it.")
        currentIncomeNavigateData?.set(id, value)
            ?: throw UnexpectedFunctionCallException("You cannot add data outside of a pre-Navigate function. This method should only be called inside a method annotated with the @Navigation tag (executed by a NavigateProcess) or within a Navigate.to() lambda.")
    }


    fun get(id: String): Pair<Any?, Boolean> {
        if (NavigatorConfigurations.unloadNavigateData == UnloadNavigateData.NEVER)
            throw ConfigurationConflictException("You are attempting to retrieve data from a Navigation, but the current configuration does not allow it.")

        if (currentOutcomeNavigateData == null) {
            if (NavigatorConfigurations.unloadNavigateData == UnloadNavigateData.FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY)
                throw ConfigurationConflictException("You are attempting to retrieve data from a Navigation, but according to the current configuration, you must manually load it before using Navigate.load().")
            throw MissingLoadedDataException("You are attempting to retrieve data from a Navigation, but there is no loaded data available. This may occur because no landing process has occurred or, as per the current configuration, the data has been nullified.")
        }
        if (!currentOutcomeNavigateData!!.containsKey(id)) return Pair(null, false)
        return Pair(currentOutcomeNavigateData!![id], true)
    }

    fun storeNavigateData(id: String, navigateData: Map<String, Any?>){
        if (navigateDataStorage.containsKey(id))
            Log.w(PLUGIN_LOG_TAG, "A stored navigate data with the provided ID already exists. The previous value will be overwritten. If this behavior is unexpected, ensure unique IDs are used across different navigations.")
        navigateDataStorage[id] = navigateData
    }

    fun loadStoredNavigateData(id: String){
        currentOutcomeNavigateData = navigateDataStorage.remove(id)
            ?: throw MissingNavigateDataException("There is no stored navigate data associated to given id parameter. Maybe it has been already loaded.")
    }

    fun nullifyCurrentOutcomeNavigateData(){
        currentOutcomeNavigateData = null
    }

    fun isLoaded(): Boolean {
        return currentOutcomeNavigateData != null
    }
    

}