package software.galaniberico.navigator.navigation

import android.accessibilityservice.GestureDescription.StrokeDescription
import android.app.Activity
import android.content.Context
import android.view.View
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.configuration.NavigatorConfigurations

class ParentData() {

    var id: String? = null
    var activity: Activity? = null
    private var data: MutableMap<String, Any?>? = null

    private fun add(id: String, value: Any?) {
        if (data == null) data = mutableMapOf()
        data!![id] = value
    }

    fun get(id: String): Pair<Any?, Boolean> {
        if (activity != null) {
            try {
                activity!!::class.java.getDeclaredField(id).let {
                    val a = it.isAccessible
                    it.isAccessible = true
                    val value = it.get(activity!!)
                    it.isAccessible = a
                    return Pair(value, true)
                }
            } catch (e: NoSuchElementException) {
                return Pair(null, false)
            }
        } else {
            if (data != null) {
                if (data!!.containsKey(id))
                    return Pair(data!![id], true)
                return Pair(null, false)
            } else {
                return Pair(null, false) //Strange case. Should not get up to here.
            }
        }
    }

    fun saveData() {
        if (activity == null) return
        id = Facade.getId(activity!!)
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