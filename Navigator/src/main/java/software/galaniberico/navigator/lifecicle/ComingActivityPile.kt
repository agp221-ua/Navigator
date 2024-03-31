package software.galaniberico.navigator.lifecicle

import android.app.Activity
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

internal object ComingActivityPile {
    val pile: MutableList<ActivityPileNode> = mutableListOf()

    fun put(id: String, clazz: KClass<out Activity>, extraData: Map<String, Any>? = null){
        if (id.isBlank()) throw IllegalArgumentException("The id field cannot be blank. Please revise the parameter value")
        pile.add(ActivityPileNode(id, clazz, extraData))
    }

    fun pop(){
        pile.removeLast()
    }

    fun get(id: String, clazz: KClass<out Activity>): ActivityPileNode? {
        for (i in pile.size-1 downTo 0)
            if (id == pile[i].id && clazz == pile[i].clazz)
                return pile.removeAt(i)
        return null
    }
}

internal class ActivityPileNode(val id: String, val clazz: KClass<out Activity>, val extraData: Map<String, Any>?)