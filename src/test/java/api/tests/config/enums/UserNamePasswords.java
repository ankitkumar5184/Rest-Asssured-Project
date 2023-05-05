package api.tests.config.enums;

// enum for storing UserName and Passwords used in tests
public enum UserNamePasswords {

    FIRSTNAME_TEXT("firstname"),
    DRXTEST123_TEXT("drxtest123"),
    BMAGENT1003_TEXT("BMAgent1003"),
    BMDRXTEST1234_TEXT("BMDrxTest1234"),
    LASTNAME_TEXT("lastname");

    public final String value;
    UserNamePasswords(String value) {
        this.value = value;
    }
}
