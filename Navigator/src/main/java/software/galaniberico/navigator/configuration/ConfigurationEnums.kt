package software.galaniberico.navigator.configuration

import kotlin.reflect.KClass

sealed interface OptionEnum

enum class MultipleNavigationIdTargets : OptionEnum {
    SEND_ERROR,
    PICK_FIRST
}
enum class MultipleOnResultIdTargets : OptionEnum {
    SEND_ERROR,
    PICK_FIRST
}
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
    MULTIPLE_NAVIGATION_ID_TARGETS(
        MultipleNavigationIdTargets::class,
        {NavigatorConfigurations.multipleNavigationIdTargets = it as MultipleNavigationIdTargets}),
    MULTIPLE_ON_RESULT_ID_TARGETS(
        MultipleOnResultIdTargets::class,
        {NavigatorConfigurations.multipleOnResultIdTargets = it as MultipleOnResultIdTargets}),
    PARENT_ACTIVITY_DATA_ACCESS(
        ParentActivityDataAccess::class,
        {NavigatorConfigurations.parentActivityDataAccess = it as ParentActivityDataAccess}),
    PARENT_ACTIVITY_MAP_PROTOCOL(
        ParentActivityMapProtocol::class,
        {NavigatorConfigurations.parentActivityMapProtocol = it as ParentActivityMapProtocol});

}

enum class LandAnnotationSearch : OptionEnum {
    NONE,
    OLD_FIELDS,
    NAVIGATE_DATA,
    OLD_FIELDS_THEN_NAVIGATE_DATA,
    NAVIGATE_DATA_THEN_OLD_FIELDS;
}

enum class LandGetterSearch : OptionEnum {
    NONE,
    OLD_FIELDS,
    NAVIGATE_DATA,
    OLD_FIELDS_THEN_NAVIGATE_DATA,
    NAVIGATE_DATA_THEN_OLD_FIELDS;
}


enum class ParentActivityDataAccess(val saveActivity: Boolean) : OptionEnum {
    NEVER(false), //When try to access sth at parentActivity will ignore it and return default or not change if attribute with @Land
    ACTIVITY_ACCESS_OR_DEFAULT(true), //does not use maps, only if activity is not accessible, just use default
    ACTIVITY_ACCESS_OR_MAP_COPY(true), //if activity is not accessible, before being inaccessible the info will be copied into a map (just the reference, no deep copy will be made)
    MAP_COPY(false); //all attributes will be copied (just the reference, no deep copy will be made) into a map and no reference will be made to the activity
}

enum class ParentActivityMapProtocol(val view: Boolean, val context: Boolean) : OptionEnum {
    ALL(false, false),
    NO_VIEWS(true, false),
    NO_CONTEXT(false, true),
    NO_VIEWS_NO_CONTEXT(true, true);

}