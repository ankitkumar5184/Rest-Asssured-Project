package api.tests.config.enums;

// enum for storing General Error Messages used in tests
public enum GeneralErrorMessages {

    INVALID_TEXT("invalid"),
    RESUME_SESSION_FAILED("ResumeSession failed."),
    INTERNAL_SERVER_ERROR("InternalServerError"),
    THE_REQUEST_IS_INVALID("The request is invalid."),
    AN_ERROR_HAS_OCCURRED("An error has occurred."),
    INVALID_VALUE_INT64("The value 'invalid' is not valid for Int64."),
    REQUEST_RESOURCE_UNSUPPORTED_DELETE("The requested resource does not support http method 'DELETE'."),
    REQUEST_RESOURCE_UNSUPPORTED_GET("The requested resource does not support http method 'GET'."),
    REQUEST_RESOURCE_UNSUPPORTED_POST("The requested resource does not support http method 'POST'."),
    REQUEST_RESOURCE_UNSUPPORTED_PUT("The requested resource does not support http method 'PUT'."),
    SESSION_INFORMATION_NOT_PROVIDED("Session Information must be provided"),
    SESSION_COULD_NOT_BE_FOUND("Session could not be found");

    public final String value;
    GeneralErrorMessages(String value) {
        this.value = value;
    }
}