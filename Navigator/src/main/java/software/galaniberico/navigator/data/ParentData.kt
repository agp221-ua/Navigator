package software.galaniberico.navigator.data

import android.app.Activity
import android.content.Context
import android.view.View
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.ParentActivityDataAccess

internal class ParentData(internal var id: String){
    internal var activity: Activity? = null
    private var data: MutableMap<String, Any?> = mutableMapOf()

    private fun add(id: String, value: Any?) {
        data[id] = value
    }

    internal fun get(id: String): Pair<Any?, Boolean> {
        if (NavigatorConfigurations.parentActivityDataAccess == ParentActivityDataAccess.NEVER) {
            return Pair(null, false)
        }
        if (activity != null) {
            try {
                activity!!::class.java.getDeclaredField(id).let {
                    val a = it.isAccessible
                    it.isAccessible = true
                    val value = it.get(activity!!)
                    it.isAccessible = a
                    return Pair(value, true)
                }
            } catch (e: NoSuchFieldException) {
                return Pair(null, false)
            }
        } else {
            if (NavigatorConfigurations.parentActivityDataAccess == ParentActivityDataAccess.ACTIVITY_ACCESS_OR_DEFAULT)
                return Pair(null, false)
            if (data.containsKey(id))
                return Pair(data[id], true)
            return Pair(null, false)
        }
    }

    internal fun saveData() {
        if (activity == null) return
        for (field in activity!!::class.java.declaredFields) {
            if (NavigatorConfigurations.parentActivityMapProtocol.view && View::class.java.isAssignableFrom(
                    field.type
                )
            ) continue
            if (NavigatorConfigurations.parentActivityMapProtocol.context && Context::class.java.isAssignableFrom(
                    field.type
                )
            ) continue

            val a = field.isAccessible
            field.isAccessible = true
            val value = field.get(activity)
            field.isAccessible = a
            add(field.name, value)
        }
        activity = null
    }
}