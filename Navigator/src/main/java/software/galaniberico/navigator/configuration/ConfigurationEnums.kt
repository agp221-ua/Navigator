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
    LOAD_EXTRADATA(
        LoadExtradata::class,
        { NavigatorConfigurations.loadExtraData = it as LoadExtradata }),

}

enum class LandAttributeSearch(val oldFields: Boolean, val extraData: Boolean) : OptionEnum {
    NONE(false, false),
    OLD_FIELDS(true, false),
    EXTRA_DATA(false, true),
    OLD_FIELDS_THEN_EXTRA_DATA(true, true),
    EXTRA_DATA_THEN_OLD_FIELDS(true, true);
}

enum class LoadExtradata : OptionEnum {
    NEVER,
    WHEN_LAND_UNTIL_OTHER_LAND,
    WHEN_LAND_UNTIL_ACTIVITY_START;
}