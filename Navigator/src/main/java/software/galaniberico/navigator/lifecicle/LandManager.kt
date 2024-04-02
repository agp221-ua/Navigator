package software.galaniberico.navigator.lifecicle

import android.app.Activity
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.configuration.LandAnnotationSearch
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.UnloadNavigateData
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.navigation.NavigateDataManager
import software.galaniberico.navigator.tags.Land
import java.lang.reflect.Field
import kotlin.RuntimeException

object LandManager {
    internal fun land(newActivity: Activity) {
        val activityId =
            newActivity.intent.getStringExtra(Facade.getIdKey()) ?: return //doesn't have id

        val apn = ComingActivityPile.get(activityId, newActivity::class)
            ?: return //TODO activity not expected (strange case)

        if (NavigatorConfigurations.unloadNavigateData != UnloadNavigateData.NEVER) {

            NavigateDataManager.storeNavigateData(activityId, apn.navigateData)

            val oldActivity = Facade.getCurrentActivity() ?: return //TODO maybe add some log

            setNavigateData(activityId)

            if (NavigatorConfigurations.landAnnotationSearch != LandAnnotationSearch.NONE)
                setAnnotationsData(activityId, newActivity, oldActivity)
        }
        Navigate.navigating = false
    }

    private fun setNavigateData(activityId: String) {
        NavigateDataManager.loadStoredNavigateData(activityId)
    }

    private fun setAnnotationsData(
        activityId: String,
        newActivity: Activity,
        oldActivity: Activity
    ) {
        for (attribute in newActivity.javaClass.declaredFields.filter {
            it.isAnnotationPresent(
                Land::class.java
            )
        }) {
            val annotation = attribute.getAnnotation(Land::class.java)
            if (annotation?.oldField?.isNotBlank() == true && (annotation.id.isBlank() || annotation.id == activityId)) {
                when (NavigatorConfigurations.landAnnotationSearch) {
                    LandAnnotationSearch.OLD_FIELDS -> setDataFromOldField(
                        oldActivity,
                        annotation,
                        attribute,
                        newActivity
                    )

                    LandAnnotationSearch.NAVIGATE_DATA -> setDataFromNavigateData(
                        annotation,
                        attribute,
                        newActivity
                    )

                    LandAnnotationSearch.OLD_FIELDS_THEN_NAVIGATE_DATA -> {
                        if (!setDataFromOldField(
                                oldActivity,
                                annotation,
                                attribute,
                                newActivity
                            )
                        )
                            setDataFromNavigateData(annotation, attribute, newActivity)
                    }

                    LandAnnotationSearch.NAVIGATE_DATA_THEN_OLD_FIELDS -> {
                        if (!setDataFromNavigateData(annotation, attribute, newActivity))
                            setDataFromOldField(
                                oldActivity,
                                annotation,
                                attribute,
                                newActivity
                            )
                    }

                    LandAnnotationSearch.NONE -> throw RuntimeException("Impossible case. The code should not been able to reach this")
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
                .apply {
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

    private fun setDataFromNavigateData(
        annotation: Land,
        attribute: Field,
        newActivity: Activity
    ): Boolean {
        try {
            val (value, found) = NavigateDataManager.get(annotation.oldField)
            if (!found) return false
            val newAccessibility = attribute.isAccessible
            attribute.isAccessible = true
            attribute.set(newActivity, value)
            attribute.isAccessible = newAccessibility

        } catch (e: Exception) {
            return false
        }
        return true
    }

}