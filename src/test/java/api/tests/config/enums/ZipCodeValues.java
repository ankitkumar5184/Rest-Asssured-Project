package api.tests.config.enums;

// enum for storing ZIP codes used in tests
public enum ZipCodeValues {

    ZIP_45("45"),
    ZIP_27713("27713"),
    Zip_85004("85004"),
    ZIP_90010("90010"),
    Zip_90011("90011"),
    Zip_92129("92129"),
    ZIP_90404("90404");

    public final String value;
    ZipCodeValues(String value) {
        this.value = value;
    }
}