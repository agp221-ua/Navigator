package software.galaniberico.navigator.exceptions

import android.util.Log
import software.galaniberico.navigator.configuration.PLUGIN_LOG_TAG
import java.lang.IllegalArgumentException


class TooManyTargetsException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message) {
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause) {
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class NoTargetsException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class NullActivityException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class DataTypeMismatchException : IllegalStateException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class EnumTypeMismatchException : IllegalStateException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class ConfigurationConflictException : IllegalStateException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class InvalidActivityIdException : IllegalArgumentException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class BlankIdFieldException : IllegalArgumentException {
    constructor() : super()
    constructor(message: String?) : super(message) {
        if (message != null) Log.e(PLUGIN_LOG_TAG, message)
    }

    constructor(message: String?, cause: Throwable?) : super(message, cause) {
        if (message != null) Log.e(PLUGIN_LOG_TAG, message)
    }

    constructor(cause: Throwable?) : super(cause)
}
class ConcurrentNavigationLoadException : IllegalStateException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class UnexpectedFunctionCallException : IllegalStateException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class MissingLoadedDataException : IllegalStateException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class ConcurrentNavigationException : IllegalStateException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(message: String?, cause: Throwable?) : super(message, cause){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
    constructor(cause: Throwable?) : super(cause)
}
class MissingNavigateDataException : NoSuchElementException {
    constructor() : super()
    constructor(message: String?) : super(message){
        if(message != null) Log.e(PLUGIN_LOG_TAG, message)
    }
}
