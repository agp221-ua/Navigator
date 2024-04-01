package software.galaniberico.navigator.lifecicle

import android.app.Activity
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.tags.Land

object LandManager {
    fun land(newActivity: Activity) {
        val activityId = newActivity.intent.getStringExtra(Facade.getIdKey()) ?: return //TODO don't have id

        val apn = ComingActivityPile.get(activityId, newActivity::class) ?: return //TODO activity not expected (strange case)

        val oldActivity = Facade.getCurrentActivity() ?: return //TODO maybe add some log

        for (attribute in newActivity.javaClass.declaredFields.filter { it.isAnnotationPresent(Land::class.java) }){
            val annotation = attribute.getAnnotation(Land::class.java)
            if (annotation?.oldField?.isNotBlank() == true && (annotation.id.isBlank() || annotation.id == activityId)){
                oldActivity::class.java.getDeclaredField(annotation.oldField).apply { //TODO catch NoSuchFieldException and search in navigatedata
                    val oldAccessibility = isAccessible
                    val newAccessibility = attribute.isAccessible
                    isAccessible = true
                    attribute.isAccessible = true
                    attribute.set(newActivity, get(oldActivity)!!)
                    attribute.isAccessible = newAccessibility
                    isAccessible = oldAccessibility
                }
            }
        }
    }

}