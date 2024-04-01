package software.galaniberico.navigator.configuration

import kotlin.reflect.KClass

sealed interface OptionEnum
enum class ConfigurationField(
    val enumClass: KClass<out OptionEnum>,
    val update: (OptionEnum) -> Unit
) {
    LAND_ATTRIBUTE_SEARCH(
        LandAttributeSearch::class,
        { NavigatorConfigurations.landAttributeSearch = it as LandAttributeSearch }),
    UNLOAD_NAVIGATEDATA(
        UnloadNavigateData::class,
        { NavigatorConfigurations.unloadNavigateData = it as UnloadNavigateData }),

}

enum class LandAttributeSearch(val oldFields: Boolean, val navigateData: Boolean) : OptionEnum {
    NONE(false, false),
    OLD_FIELDS(true, false),
    NAVIGATE_DATA(false, true),
    OLD_FIELDS_THEN_NAVIGATE_DATA(true, true),
    NAVIGATE_DATA_THEN_OLD_FIELDS(true, true);
}

enum class UnloadNavigateData : OptionEnum {
    NEVER,
    FROM_MANUAL_LOAD_UNTIL_MANUAL_ANNUL,
    FROM_LAND_UNTIL_MANUAL_ANNUL,
    FROM_LAND_UNTIL_OTHER_LAND,
    FROM_LAND_UNTIL_ACTIVITY_START;
}