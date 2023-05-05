package api.tests.config.enums;

// enum for storing PC API Error Messages used in tests
public enum PlanCompareErrorMessages {

    // Health Cost Benefits Error Messages
    BENEFIT_CATEGORIES_FIELD_REQUIRED("The BenefitCategories field is required."),

    // Session Plan Error Messages
    PLAN_ID_NOT_IN_VALID_FORMAT("Plan ID is not in a valid format"),

    // Plans For Session Quoter Only Broker Error Message
    COULD_NOT_FIND_MAPPINGS_FOR_ENROLLMENT("Could not find mappings for enrollment"),

    AT_LEAST_ONE_RIDER_MUST_BE_SELECTED("At least one rider must be selected"),
    NFEN_ROLL("NFENROLL");

    public final String value;
    PlanCompareErrorMessages(String value) {
        this.value = value;
    }
}
