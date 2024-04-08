package software.galaniberico.navigator.facade

import software.galaniberico.navigator.configuration.ConfigurationField
import software.galaniberico.navigator.configuration.NavigatorConfigurations
import software.galaniberico.navigator.configuration.OptionEnum
import software.galaniberico.navigator.exceptions.EnumTypeMismatchException

object Navigator {

    inline fun <reified T : OptionEnum> config(option: ConfigurationField, to: T) {
        if (option.enumClass != T::class) throw EnumTypeMismatchException("The enum type (${T::class.simpleName}) does not match the expected type (${option.enumClass.simpleName}) for the provided configuration field (${option.name}).")
        option.update(to)
    }

    fun currentConfiguration(option: ConfigurationField) : OptionEnum{
        return NavigatorConfigurations.currentConfiguration(option)
    }

    /**
     * Sets the handler that will be executed when an Exception is thrown inside any Landing.
     * This method is intended to be used primarily for tests and exceptional cases where you want
     * to manage the exception without potentially terminating the application, but
     * displaying a custom error message.
     *
     * If no handler is explicitly provided, the default behavior is to throw the
     * exception.
     *
     * @param handler The lambda function to be executed, taking the thrown exception as a parameter.
     *
     */
    fun setLandingErrorHandler(handler: (Exception) -> Unit){
        NavigatorConfigurations.landingErrorHandler = handler
    }

    /**
     * Sets the handler that will be executed when an Exception is thrown inside any onResult method.
     * This method is intended to be used primarily for tests and exceptional cases where you want
     * to manage the exception without potentially terminating the application, but
     * displaying a custom error message.
     *
     * If no handler is explicitly provided, the default behavior is to throw the
     * exception.
     *
     * @param handler The lambda function to be executed, taking the thrown exception as a parameter.
     *
     */
    fun setOnResultErrorHandler(handler: (Exception) -> Unit){
        NavigatorConfigurations.onResultErrorHandler = handler
    }
}