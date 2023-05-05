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
import static org.testng.Assert.assertNotNull;

@Listeners(value = ConditionalSkipTestAnalyzer.class)
public class SessionPreferencesTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    private String preferencesSessionId, requestURL, requestBody;

    @Test
    public void testGetPreferencesQuestions() {
        commonMethods.usePropertyFileData("PC-API");
        String getPreferenceEndpoint = prop.getProperty("Preferences_GetPreferenceQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getPreferenceEndpoint, getPreferencesSessionId()));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPreferenceEndpoint, preferencesSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Preferences Questions response body :- " + response.asPrettyString());
        }
        String getQuestionKey = response.jsonPath().getString("[0].QuestionKey");
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(getQuestionKey);
    }

    @Test(priority = 1)
    public void testGetPreferencesQuestionsWithInvalidSessionId() {
        String getPreferenceEndpoint = prop.getProperty("Preferences_GetPreferenceQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getPreferenceEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPreferenceEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Preferences Questions With Invalid SessionId response body:- " + response.asPrettyString());
        }
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testGetPreferencesQuestionsWithBlankSessionId() {
        String getPreferenceEndpoint = prop.getProperty("Preferences_GetPreferenceQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(getPreferenceEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPreferenceEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Preferences Questions With Blank SessionId response:- " + response.asPrettyString());
        }
        String blankErrorMessage = response.jsonPath().getString("Message");
        String blankErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(blankErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(blankErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 4)
    public void testPostPreferencesQuestions(){
        String addPreferenceQuestionsData = commonMethods.getTestDataPath() + "addPreferencesQuestions.json";
        String addPreferenceQuestionsEndpoint = prop.getProperty("Preferences_PostPreferenceQuestions").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addPreferenceQuestionsEndpoint, preferencesSessionId), fileReader.getTestJsonFile(addPreferenceQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addPreferenceQuestionsEndpoint, preferencesSessionId);
        requestBody = fileReader.getTestJsonFile(addPreferenceQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Preferences Questions response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testPostPreferencesQuestionsWithInvalidSessionId(){
        String addPreferenceQuestionsData = commonMethods.getTestDataPath() + "addPreferencesQuestions.json";
        String addPreferenceQuestionsEndpoint = prop.getProperty("Preferences_PostPreferenceQuestions").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addPreferenceQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(addPreferenceQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addPreferenceQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(addPreferenceQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Preferences Questions With Invalid SessionId response:- " + response.asPrettyString());
        }
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 7)
    public void testPostPreferencesQuestionsWithBlankSessionId(){
        String addPreferenceQuestionsData = commonMethods.getTestDataPath() + "addPreferencesQuestions.json";
        String addPreferenceQuestionsEndpoint = prop.getProperty("Preferences_PostPreferenceQuestions").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addPreferenceQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")), fileReader.getTestJsonFile(addPreferenceQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addPreferenceQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(addPreferenceQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Preferences Questions With Blank SessionId response body :- " + response.asPrettyString());
        }
        String blankSessionIdMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorMessage = response.jsonPath().getString("ModelState.session[0]");
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 8)
    public void testPostPreferencesQuestionsWithBlankBodyData(){
        String addPreferenceQuestionsEndpoint = prop.getProperty("Preferences_PostPreferenceQuestions").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addPreferenceQuestionsEndpoint, preferencesSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addPreferenceQuestionsEndpoint, preferencesSessionId);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Preferences Questions With Blank Body Data response body:- " + response.asPrettyString());
        }
        String blankBodyDataMessage = response.jsonPath().getString("Message");
        String blankBodyDataErrorMessage = response.jsonPath().getString("ModelState.answers[0]");
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    private String getPreferencesSessionId() {
        String createSessionData = commonMethods.getTestDataPath() + "createSessionData.json";
        preferencesSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return preferencesSessionId;
    }
}