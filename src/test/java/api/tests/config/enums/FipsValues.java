package api.tests.config.enums;

// enum for storing FIPS values used in tests
public enum FipsValues {

    Fips_04013("04013"),
    Fips_06037("06037"),
    Fips_06073("06073");

    public final String value;
    FipsValues(String value) {
        this.value = value;
    }
}