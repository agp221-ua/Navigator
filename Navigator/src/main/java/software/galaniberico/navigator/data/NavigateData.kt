package software.galaniberico.navigator.data

import android.app.Activity
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.configuration.LandGetterSearch
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.exceptions.BlankIdFieldException

internal class NavigateData(val parentData: ParentData){
    private var data = mutableMapOf<String, Any?>()
    var resultData: ResultData? = null

    fun isForResult(): Boolean{
        return resultData != null
    }

    fun get(id: String, navigateDataOnly: Boolean = false): Pair<Any?, Boolean> {
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value")
        if (navigateDataOnly) {
            if (!data.containsKey(id))
                return Pair(null, false)
            return Pair(data[id], true)
        }
        when(NavigatorConfigurations.landGetterSearch){
            LandGetterSearch.NONE -> return Pair(null, false)
            LandGetterSearch.OLD_FIELDS -> return parentData.get(id)
            LandGetterSearch.NAVIGATE_DATA -> {
                if (!data.containsKey(id)) return Pair(null, false)
                return Pair(data[id], true)
            }
            LandGetterSearch.OLD_FIELDS_THEN_NAVIGATE_DATA -> {
                val (value, found) = parentData.get(id)
                if (found) return Pair(value, true)
                if (!data.containsKey(id)) return Pair(null, false)
                return Pair(data[id], true)
            }
            LandGetterSearch.NAVIGATE_DATA_THEN_OLD_FIELDS -> {
                if (data.containsKey(id)) return Pair(
                    data[id], true)
                return parentData.get(id)
            }
        }
    }

    companion object {
        private val datas = TwoKeyMutableMap<String, String, NavigateData>()
        var resultData: NavigateData? = null
        internal fun of(activity: Activity): NavigateData? {
            return datas.getK(Facade.getInternalId(activity))
        }

        internal fun ofChild(activity: Activity): NavigateData? {
            return datas.getP(Facade.getInternalId(activity))
        }

        internal fun set(id: String, data: NavigateData) {
            datas.put(id, data.parentData.id,data)
        }

        internal fun remove(activity: Activity) {
            val id = Facade.getInternalId(activity)
            if(datas.getK(id)?.resultData?.hasResult == true){
                return
            }
            datas.removeK(id)
        }

        internal fun saveParentData(parentActivity: Activity) {
            val id = Facade.getInternalId(parentActivity)
            datas.getP(id)?.parentData?.saveData()
        }

        internal fun executeOnResult(parentActivity: Activity) {
            val id = Facade.getInternalId(parentActivity)
            val data = datas.getP(id)
            if(data?.resultData?.hasResult == true){
                resultData = datas.removeP(id)
                data.resultData!!.method()
                resultData = null
                return
            }

        }

        internal var navigateIncome: MutableMap<String, Any?>? = null

        internal fun prepareIncome(){
            navigateIncome = mutableMapOf()
        }

        internal fun resolveIncome(navigateData: NavigateData) {
            val data = navigateIncome
            navigateIncome = null
            navigateData.data = data ?: mutableMapOf()
        }

        fun has(id: String): Boolean {
            return datas.getKeyset().contains(id)
        }
    }
}

object NavigateDataActions {
    fun get(id: String): Pair<Any?, Boolean> {
        val data = NavigateData.of(Facade.getPreferredActivityOrFail()) ?: return Pair(null, false)
        return data.get(id)
    }

    fun getResult(id: String): Pair<Any?, Boolean> {
        val data = NavigateData.resultData ?: return Pair(null, false)
        return data.resultData?.get(id) ?: Pair(null, false)
    }
}