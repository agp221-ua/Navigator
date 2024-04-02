package software.galaniberico.navigator.configuration

internal object NavigatorConfigurations {
    var landAnnotationSearch: LandAnnotationSearch = LandAnnotationSearch.OLD_FIELDS_THEN_NAVIGATE_DATA
    var landGetterSearch: LandGetterSearch = LandGetterSearch.OLD_FIELDS_THEN_NAVIGATE_DATA
    var unloadNavigateData: UnloadNavigateData = UnloadNavigateData.FROM_LAND_UNTIL_OTHER_LAND
    var multipleNavigationIdTargets: MultipleNavigationIdTargets = MultipleNavigationIdTargets.SEND_ERROR

    fun currentConfiguration(option: ConfigurationField) : OptionEnum{
        return when(option) {
            ConfigurationField.LAND_ANNOTATION_SEARCH -> landAnnotationSearch
            ConfigurationField.LAND_GETTER_SEARCH -> landGetterSearch
            ConfigurationField.UNLOAD_NAVIGATEDATA -> unloadNavigateData
            ConfigurationField.MULTIPLE_NAVIGATION_ID_TARGETS -> multipleNavigationIdTargets
        }
    }
}