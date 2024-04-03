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
}