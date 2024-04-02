package software.galaniberico.navigator.configuration

import kotlin.reflect.KClass

sealed interface OptionEnum
enum class ConfigurationField(
    val enumClass: KClass<out OptionEnum>,
    val update: (OptionEnum) -> Unit
) {
    LAND_ANNOTATION_SEARCH(
        LandAnnotationSearch::class,
        { NavigatorConfigurations.landAnnotationSearch = it as LandAnnotationSearch }),
    LAND_GETTER_SEARCH(
        LandGetterSearch::class,
        { NavigatorConfigurations.landGetterSearch = it as LandGetterSearch }),
    UNLOAD_NAVIGATEDATA(
        UnloadNavigateData::class,
        { NavigatorConfigurations.unloadNavigateData = it as UnloadNavigateData }),
    MULTIPLE_NAVIGATION_ID_TARGETS(
        MultipleNavigationIdTargets::class,
        {NavigatorConfigurations.multipleNavigationIdTargets = it as MultipleNavigationIdTargets})

}

enum class LandAnnotationSearch(val oldFields: Boolean, val navigateData: Boolean) : OptionEnum {
    NONE(false, false),
    OLD_FIELDS(true, false),
    NAVIGATE_DATA(false, true),
    OLD_FIELDS_THEN_NAVIGATE_DATA(true, true),
    NAVIGATE_DATA_THEN_OLD_FIELDS(true, true);
}

enum class LandGetterSearch(val oldFields: Boolean, val navigateData: Boolean) : OptionEnum {
    NONE(false, false),
    OLD_FIELDS(true, false),
    NAVIGATE_DATA(false, true),
    OLD_FIELDS_THEN_NAVIGATE_DATA(true, true),
    NAVIGATE_DATA_THEN_OLD_FIELDS(true, true);
}

enum class UnloadNavigateData : OptionEnum {
    NEVER, //No navigatedata. All access to this, will cause an exception.
    FROM_MANUAL_LOAD_UNTIL_MANUAL_NULLIFY, //if not load, no data. if load and load without nullify, exception.
    FROM_LAND_UNTIL_MANUAL_NULLIFY, //If land start and not nullify, no new navigate data will be loaded and exception (avoid bugs)
    FROM_LAND_UNTIL_OTHER_LAND, //Each new land removes the previous one and loads the new one
}
enum class MultipleNavigationIdTargets : OptionEnum {
    SEND_ERROR,
    PICK_FIRST
}