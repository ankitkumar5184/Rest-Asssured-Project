package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.ConditionalSkipTestAnalyzer;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Map;

import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

@Listeners(value = ConditionalSkipTestAnalyzer.class)
public class PreferenceQuestionsSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    private String preferenceQuestionsSpecSessionId;
    private String requestURL, requestBody;

    @Test(priority = 1)
    public void testSendGetSessionRequest() {
        commonMethods.usePropertyFileData("PC-API");
        String getSessionEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, getPreferenceQuestionsSpecSessionId()));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, preferenceQuestionsSpecSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test get session response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testSendGetSessionRequestWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test get session with invalid SessionID response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testSendGetSessionRequestWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Get Session With Blank SessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testGetPreferenceQuestions() {
        String getPreferenceQuestionsEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPreferenceQuestionsEndpoint, preferenceQuestionsSpecSessionId) + dataProp.getProperty("PreferenceQuestionsSpec_GetQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPreferenceQuestionsEndpoint, preferenceQuestionsSpecSessionId) + dataProp.getProperty("PreferenceQuestionsSpec_GetQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Get Preference Questions response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testGetPreferenceQuestionsWithInvalidSessionId() {
        String getPreferenceQuestionsEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPreferenceQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PreferenceQuestionsSpec_GetQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPreferenceQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("PreferenceQuestionsSpec_GetQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Get Preference Questions With Invalid SessionID response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testGetPreferenceQuestionsWithBlankSessionId() {
        String getPreferenceQuestionsEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPreferenceQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PreferenceQuestionsSpec_GetQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPreferenceQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PreferenceQuestionsSpec_GetQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Get Preference Questions With Blank SessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 8)
    public void testSubmitAnswers() {
        String addAnswersToQuestionsData = commonMethods.getTestDataPath() + "submitAnswers.json";
        String addAnswersEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetQuestions").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, preferenceQuestionsSpecSessionId), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, preferenceQuestionsSpecSessionId);
        requestBody = fileReader.getTestJsonFile(addAnswersToQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Submit Answers response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 9)
    public void testSubmitAnswersWithInvalidSessionId() {
        String addAnswersToQuestionsData = commonMethods.getTestDataPath() + "submitAnswers.json";
        String addAnswersEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetQuestions").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(addAnswersToQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Submit Answers With Invalid SessionId response body :- " + response.asPrettyString());
        }
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 11)
    public void testSubmitAnswersWithBlankSessionId() {
        String addAnswersToQuestionsData = commonMethods.getTestDataPath() + "submitAnswers.json";
        String addAnswersEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetQuestions").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, dataProp.getProperty("BLANK_SESSION_ID")), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(addAnswersToQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Submit Answers With Blank SessionId response body :- " + response.asPrettyString());
        }
        String blankSessionIdMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorMessage = response.jsonPath().getString("ModelState.session[0]");
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 12)
    public void testSubmitAnswersWithBlankBodyData() {
        String addAnswersEndpoint = prop.getProperty("PreferenceQuestionsSpec_GetQuestions").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, preferenceQuestionsSpecSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, preferenceQuestionsSpecSessionId);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Submit Answers With Blank Body Data response body :- " + response.asPrettyString());
        }
        String blankBodyDataMessage = response.jsonPath().getString("Message");
        String blankBodyDataErrorMessage = response.jsonPath().getString("ModelState.answers[0]");
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    private String getPreferenceQuestionsSpecSessionId() {
        String postCreateSessionData = commonMethods.getTestDataPath() + "createSessionJsonOriginal.json";
        preferenceQuestionsSpecSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return preferenceQuestionsSpecSessionId;
    }
}
