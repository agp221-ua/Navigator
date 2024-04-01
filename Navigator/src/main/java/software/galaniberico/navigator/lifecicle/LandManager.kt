package software.galaniberico.navigator.lifecicle

import android.app.Activity
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.configuration.LandAttributeSearch
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.tags.Land
import java.lang.reflect.Field
import kotlin.RuntimeException

object LandManager {
    fun land(newActivity: Activity) {
        val activityId =
            newActivity.intent.getStringExtra(Facade.getIdKey()) ?: return //TODO don't have id

        val apn = ComingActivityPile.get(activityId, newActivity::class)
            ?: return //TODO activity not expected (strange case)

        val oldActivity = Facade.getCurrentActivity() ?: return //TODO maybe add some log

        if (NavigatorConfigurations.landAttributeSearch != LandAttributeSearch.NONE)
            for (attribute in newActivity.javaClass.declaredFields.filter {
                it.isAnnotationPresent(
                    Land::class.java
                )
            }) {
                val annotation = attribute.getAnnotation(Land::class.java)
                if (annotation?.oldField?.isNotBlank() == true && (annotation.id.isBlank() || annotation.id == activityId)) {
                    when (NavigatorConfigurations.landAttributeSearch) {
                        LandAttributeSearch.OLD_FIELDS -> setDataFromOldField(
                            oldActivity,
                            annotation,
                            attribute,
                            newActivity
                        )

                        LandAttributeSearch.NAVIGATE_DATA -> TODO("waiting #10")
                        LandAttributeSearch.OLD_FIELDS_THEN_NAVIGATE_DATA -> {
                            if (!setDataFromOldField(
                                    oldActivity,
                                    annotation,
                                    attribute,
                                    newActivity
                                )
                            )
                                TODO("waiting #10")
                        }

                        LandAttributeSearch.NAVIGATE_DATA_THEN_OLD_FIELDS -> {
                            TODO("waiting #10")
                            setDataFromOldField(
                                oldActivity,
                                annotation,
                                attribute,
                                newActivity
                            )
                        }

                        LandAttributeSearch.NONE -> throw RuntimeException("Impossible case. The code should not been able to reach this")
                    }
                }
            }
    }

    private fun setDataFromOldField(
        oldActivity: Activity,
        annotation: Land,
        attribute: Field,
        newActivity: Activity
    ): Boolean {
        try {
            oldActivity::class.java.getDeclaredField(annotation.oldField)
                .apply { //TODO catch NoSuchFieldException and search in navigatedata
                    val oldAccessibility = isAccessible
                    val newAccessibility = attribute.isAccessible
                    isAccessible = true
                    attribute.isAccessible = true
                    attribute.set(newActivity, get(oldActivity)!!)
                    attribute.isAccessible = newAccessibility
                    isAccessible = oldAccessibility
                }
        } catch (e: Exception) {
            return false
        }
        return true
    }

}