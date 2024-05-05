package software.galaniberico.navigator.navigation

import android.app.Activity
import android.util.Log
import software.galaniberico.moduledroid.facade.Facade
import software.galaniberico.navigator.configuration.LandAnnotationSearch
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import software.galaniberico.navigator.data.NavigateData
import software.galaniberico.navigator.data.ParentData
import software.galaniberico.navigator.exceptions.DataTypeMismatchException
import software.galaniberico.navigator.facade.Navigate
import software.galaniberico.navigator.tags.Land
import java.lang.reflect.Field

internal object LandManager {
    internal fun land(newActivity: Activity) {
        val activityId = Facade.getId(newActivity) //TODO checked
            ?: return //doesn't have id
        Navigate.navigating = false

        val apn = NavigateData.of(newActivity)

        if (apn == null) {
            Log.w(PLUGIN_LOG_TAG, "A not expected activity with id '$activityId' is creating. Maybe is something creating an activity external to the plugin?")
            return
        }

        if (NavigatorConfigurations.landAnnotationSearch != LandAnnotationSearch.NONE)
            setAnnotationsData(activityId, newActivity, apn.parentData)

    }

    private fun setAnnotationsData(
        activityId: String,
        newActivity: Activity,
        parentData: ParentData,
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
                        parentData,
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
                                parentData,
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
                                parentData,
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
        parentData: ParentData,
        annotation: Land,
        attribute: Field,
        newActivity: Activity
    ): Boolean {
        try {
            val (value, found) = parentData.get(annotation.oldField)
            if (!found) return false
            val newAccessibility = attribute.isAccessible
            attribute.isAccessible = true
            attribute.set(newActivity, value)
            attribute.isAccessible = newAccessibility
        } catch (e: IllegalArgumentException) {
            throw DataTypeMismatchException("The retrieved data for id \"${annotation.oldField}\" is not of the expected type.")

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
            val (value, found) = NavigateData.of(newActivity)?.get(annotation.oldField, true) ?: Pair(null, false)
            if (!found) return false
            val newAccessibility = attribute.isAccessible
            attribute.isAccessible = true
            attribute.set(newActivity, value)
            attribute.isAccessible = newAccessibility

        } catch (e: IllegalArgumentException) {
            throw DataTypeMismatchException("The retrieved data for id \"${annotation.oldField}\" is not of the expected type.")
        } catch (e: Exception) {
            return false
        }
        return true
    }

}