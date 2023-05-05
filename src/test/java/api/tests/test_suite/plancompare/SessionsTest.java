package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.FipsValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.ZipCodeValues;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class SessionsTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String , String> tokens = envInstanceHelper.getEnvironment();
    private static String getCreateSessionSSOValue;
    private  String invalidErrorMessage;
    private String invalidErrorCode;
    private String getSessionId;
    private String postSessionEditData;
    private String getPostSessionEditEndPoint;
    private String requestURL, requestBody;

    @Test
    public void testVerifyCreateSession(){
        commonMethods.usePropertyFileData("PC-API");
        String postCreateSessionData = commonMethods.getTestDataPath() + "createSessionApiData.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test create session response body : " + response.asPrettyString());
        }
        getSessionId = response.jsonPath().getString("[0].SessionID");
        String getReqId = response.jsonPath().getString("[0].reqid");
        String getStatus = response.jsonPath().getString("[0].Status");
        getCreateSessionSSOValue = response.jsonPath().getString("[0].SSOValue");
        assertEquals(response.getStatusCode(), 200);
        assertEquals(getReqId, dataProp.getProperty("REQ_ID"));
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
    }

    @Test(priority = 1)
    public void testVerifyCreateSessionWithBlankBody() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test create session when blank body response body: " + response.asPrettyString());
        }
        String emptyBodyResponseMessage = response.jsonPath().getString("Message");
        String emptyBodyErrorMessage = response.jsonPath().getString("ModelState.sessions[0]");
        assertEquals(emptyBodyResponseMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(emptyBodyErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 2)
    public void testVerifyGetSession(){
        String getSessionEndPoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndPoint, getSessionId));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndPoint, getSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test verify get session response body :- " + response.asPrettyString());
        }
        String getSSOValue = response.jsonPath().getString("SSOValue");
        assertEquals(response.getStatusCode(), 200);
        assertEquals(getSSOValue, getCreateSessionSSOValue);
    }

    @Test(priority = 3)
    public void testVerifyGetSessionWithInvalidSessionId(){
        String getSessionEndPoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndPoint, dataProp.getProperty("INVALID_TEXT")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndPoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test get Session with invalid SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 4)
    public void testVerifyGetSessionWithBlankSessionId(){
        String getSessionEndPoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(getSessionEndPoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test get Session with blank SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
        assertEquals(response.getStatusCode(), 405);
    }

    @Test(priority = 5)
    public void testVerifyPostSessionEdit() {
        postSessionEditData = commonMethods.getTestDataPath() + "postSessionEditApiData.json";
        getPostSessionEditEndPoint = prop.getProperty("Sessions_PostSessionEdit").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(getPostSessionEditEndPoint, getSessionId),
                fileReader.getTestJsonFile(postSessionEditData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPostSessionEditEndPoint, getSessionId);
        requestBody = fileReader.getTestJsonFile(postSessionEditData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test post session edit response body :- " + response.asPrettyString());
        }
        String getZipCode = response.jsonPath().getString("Zip");
        String getFipsValue = response.jsonPath().getString("Fips");
        String getBirthDate = response.jsonPath().getString("Birthdate");
        assertEquals(getZipCode, ZipCodeValues.Zip_90011.value);
        assertEquals(getFipsValue, FipsValues.Fips_06037.value);
        assertEquals(getBirthDate, dataProp.getProperty("BIRTHDATE"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testVerifyPostSessionEditWithBlankSessionId(){
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(getPostSessionEditEndPoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(postSessionEditData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPostSessionEditEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(postSessionEditData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test post session Edit when blank sessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ModelState['sessions.Zip'][0]");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 7)
    public void testVerifyPostSessionEditWithInvalidSessionId(){
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(getPostSessionEditEndPoint, dataProp.getProperty("INVALID_TEXT")),
                fileReader.getTestJsonFile(postSessionEditData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPostSessionEditEndPoint, dataProp.getProperty("INVALID_TEXT"));
        requestBody = fileReader.getTestJsonFile(postSessionEditData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test post session edit when invalid SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }
}