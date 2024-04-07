package software.galaniberico.navigator.navigation

import software.galaniberico.navigator.exceptions.BlankIdFieldException
import software.galaniberico.navigator.exceptions.UnexpectedFunctionCallException


object ResultDataManager {
    private val pile = mutableListOf<ResultData>()

    var currentOutputResult: ResultData? = null

    fun put(data: ResultData) {
        pile.add(data)
    }

    private fun pop(): ResultData? {
        if (pile.isEmpty())
            return null
        return pile.removeLast()
    }

    fun top(): ResultData? {
        if(pile.isEmpty())
            return null
        return pile.last()
    }

    fun loadOutput() {
        currentOutputResult = pop()
    }

    fun getResult(id: String): Pair<Any?, Boolean> {
        if(id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value.")
        if(currentOutputResult == null) throw UnexpectedFunctionCallException("This method cannot be invoked outside of an OnResult method; that is, the method expected to be executed after a 'Back' method call.")
        return currentOutputResult!!.get(id)

    }

    fun executeOnResult(){
        currentOutputResult!!.method()
        currentOutputResult = null
    }
}


class ResultData (val parentId: String?, var method: () -> Unit){
    private var data: MutableMap<String, Any?> = mutableMapOf()

    fun add(id: String, value: Any?){
        data[id] = value
    }

    fun get(id: String): Pair<Any?, Boolean>{
        if (!data.containsKey(id)) return Pair(null, false)
        return Pair(data[id], true)
    }
}