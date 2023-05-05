package api.tests.config.enums;

// enum for storing Tools API Error Messages used in tests
public enum ToolsApiErrorMessages {
    MISSING_SEARCH_TERM_PARAM_VALUE("Missing SearchTerm param value. SearchTerm is required unless NPIs are provided."),
    CANNOT_FIND_PHARMACY_BY_THE_GIVEN_ID("Can not find Phamacy by the given ID!"),
    AGENT_ID_MISSING("AgentID Missing"),
    USERNAME_MISSING("UserName Missing"),
    FIRST_NAME_MISSING("FirstName Missing"),
    LAST_NAME_MISSING("LastName Missing"),
    AGENT_ROLE_MISSING("AgentRole Missing"),
    IS_ACTIVE_MISSING("IsActive Missing"),
    INVALID_AGENCY_ID_ERROR_MESSAGE("No active agency found for the specified agencyID"),
    THE_AGENCY_FIELD_IS_REQUIRED("The AgencyID field is required."),
    THE_AGENCY_NAME_IS_REQUIRED("The AgencyName field is required.");

    public final String value;
    ToolsApiErrorMessages(String value) {
        this.value = value;
    }
}
