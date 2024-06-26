package software.galaniberico.navigator.configuration

internal object NavigatorConfigurations {
    var parentActivityMapProtocol: ParentActivityMapProtocol = ParentActivityMapProtocol.NO_VIEWS_NO_CONTEXT
    var parentActivityDataAccess: ParentActivityDataAccess = ParentActivityDataAccess.ACTIVITY_ACCESS_OR_MAP_COPY
    var landAnnotationSearch: LandAnnotationSearch = LandAnnotationSearch.OLD_FIELDS_THEN_NAVIGATE_DATA
    var landGetterSearch: LandGetterSearch = LandGetterSearch.OLD_FIELDS_THEN_NAVIGATE_DATA
    var multipleNavigationIdTargets: MultipleNavigationIdTargets = MultipleNavigationIdTargets.SEND_ERROR
    var multipleOnResultIdTargets: MultipleOnResultIdTargets = MultipleOnResultIdTargets.SEND_ERROR

    internal fun currentConfiguration(option: ConfigurationField) : OptionEnum{
        return when(option) {
            ConfigurationField.LAND_ANNOTATION_SEARCH -> landAnnotationSearch
            ConfigurationField.LAND_GETTER_SEARCH -> landGetterSearch
            ConfigurationField.MULTIPLE_NAVIGATION_ID_TARGETS -> multipleNavigationIdTargets
            ConfigurationField.MULTIPLE_ON_RESULT_ID_TARGETS -> multipleOnResultIdTargets
            ConfigurationField.PARENT_ACTIVITY_DATA_ACCESS -> parentActivityDataAccess
            ConfigurationField.PARENT_ACTIVITY_MAP_PROTOCOL -> parentActivityMapProtocol
        }
    }

    var landingErrorHandler: (Exception) -> Unit = {throw it}
    var onResultErrorHandler: (Exception) -> Unit = {throw it}
}