package software.galaniberico.navigator.data

import android.app.Activity
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.ParentActivityDataAccess
import software.galaniberico.navigator.exceptions.BlankIdFieldException
import kotlin.reflect.KClass

internal object ComingActivityPile {
    private val pile: MutableList<ActivityPileNode> = mutableListOf()

    internal fun put(
        id: String,
        clazz: KClass<out Activity>,
        navigateData: Map<String, Any?>,
        parentData: ParentData,
        resultData: ResultData? = null
    ){
        if (id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value")
        pile.add(ActivityPileNode(id, clazz, navigateData, parentData, resultData))
    }

    internal fun get(id: String, clazz: KClass<out Activity>): ActivityPileNode? {
        for (i in pile.size-1 downTo 0)
            if (id == pile[i].id && clazz == pile[i].clazz)
                return pile.removeAt(i)
        return null
    }

    internal fun saveParentData(activity: Activity){
        if (NavigatorConfigurations.parentActivityDataAccess != ParentActivityDataAccess.ACTIVITY_ACCESS_OR_MAP_COPY) return
        for (node in pile){
            if (node.parentData.activity == activity) {
                node.parentData.saveData()
                break
            }
        }
    }

    internal fun has(id: String): Boolean {
        for (node in pile)
            if (node.id == id)
                return true
        return false
    }
}

internal class ActivityPileNode(
    val id: String,
    val clazz: KClass<out Activity>,
    val navigateData: Map<String, Any?>,
    val parentData: ParentData,
    val resultData: ResultData? = null
)