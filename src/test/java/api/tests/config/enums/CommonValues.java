package api.tests.config.enums;

// enum for storing common values used in tests
public enum CommonValues {

    BLANK_BODY_REQUEST("{}"),
    BLANK_BODY_VALID_REQUEST("[{}]"),
    BLANK_BODY_REQUEST_WITH_XML("<>"),
    GENDER_TEXT("Gender"),
    UPDATED_TEXT("Updated"),
    FALSE_VALUE("false"),
    CREATED_TEXT("Created"),
    TRUE_VALUE("true");

    public final String value;
    CommonValues(String value) {
        this.value = value;
    }
}
