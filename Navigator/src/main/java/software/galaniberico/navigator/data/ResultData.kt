package software.galaniberico.navigator.data

import software.galaniberico.navigator.exceptions.BlankIdFieldException

class ResultData (var method: () -> Unit){
    private var data: MutableMap<String, Any?> = mutableMapOf()
    var hasResult = false

    internal fun add(id: String, value: Any?){
        if(id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value.")
        data[id] = value
    }

    internal fun get(id: String): Pair<Any?, Boolean>{
        if(id.isBlank()) throw BlankIdFieldException("The id field cannot be blank. Please revise the parameter value.")
        if (!data.containsKey(id)) return Pair(null, false)
        return Pair(data[id], true)
    }

}