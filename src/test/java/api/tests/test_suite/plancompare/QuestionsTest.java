package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class QuestionsTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    private String questionsSessionId;
    private String invalidQuestionSessionId;
    private String blankQuestionsSessionId;
    private String blankBody;
    private String requestURL, requestBody;

    @Test(priority = 1)
    public void testGetQuestions() {
        commonMethods.usePropertyFileData("PC-API");
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, getQuestionsSessionId()) + dataProp.getProperty("Questions_GetQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, questionsSessionId) + dataProp.getProperty("Questions_GetQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get questions response body :- " + response.asPrettyString());
        }
        String getName = response.jsonPath().getString("[0].Name");
        String getQuestionId = response.jsonPath().getString("[0].QuestionID");
        assertEquals(getName, CommonValues.GENDER_TEXT.value);
        assertEquals(getQuestionId, dataProp.getProperty("QUESTION_ID"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testGetQuestionsWithInvalidSessionId() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestions").replace("{SessionID}", "%s");
        invalidQuestionSessionId = dataProp.getProperty("INVALID_TEXT");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Questions With Invalid SessionId response body :- " + response.asPrettyString());
        }
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testGetQuestionsWithBlankSessionId() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestions").replace("{SessionID}", "%s");
        blankQuestionsSessionId = dataProp.getProperty("BLANK_SESSION_ID");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Questions With Blank SessionId response body :- " + response.asPrettyString());
        }
        String blankErrorMessage = response.jsonPath().getString("Message");
        String blankErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(blankErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(blankErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 4)
    public void testAddAnswers() {
        String addAnswersToQuestionsData = commonMethods.getTestDataPath() + "addAnswersToQuestion.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, questionsSessionId), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, questionsSessionId);
        requestBody = fileReader.getTestJsonFile(addAnswersToQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Answers response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testAddAnswersWithInvalidSessionId() {
        String addAnswersToQuestionsData = commonMethods.getTestDataPath() + "addAnswersToQuestion.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, invalidQuestionSessionId), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, invalidQuestionSessionId);
        requestBody = fileReader.getTestJsonFile(addAnswersToQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Answers With Invalid SessionId response body :- " + response.asPrettyString());
        }
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 6)
    public void testAddAnswersWithBlankSessionId() {
        String addAnswersToQuestionsData = commonMethods.getTestDataPath() + "addAnswersToQuestion.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, blankQuestionsSessionId), fileReader.getTestJsonFile(addAnswersToQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, blankQuestionsSessionId);
        requestBody = fileReader.getTestJsonFile(addAnswersToQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Answers With Blank SessionId response body :- " + response.asPrettyString());
        }
        String blankSessionIdMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorMessage = response.jsonPath().getString("ModelState.session[0]");
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 7)
    public void testAddAnswersWithBlankBodyData() {
        blankBody = CommonValues.BLANK_BODY_REQUEST.value;
        String addAnswersEndpoint = prop.getProperty("Questions_AddAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, questionsSessionId), blankBody);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, questionsSessionId);
        requestBody = blankBody;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Answers With Blank Body Data response body :- " + response.asPrettyString());
        }
        String blankBodyDataMessage = response.jsonPath().getString("Message");
        String blankBodyDataErrorMessage = response.jsonPath().getString("ModelState.answers[0]");
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 8)
    public void testGetDentalQuestions() {
        String getDentalQuestionsEndpoint = prop.getProperty("Questions_GetDentalQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, questionsSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, questionsSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Dental Questions response body :- " + response.asPrettyString());
        }
        String getName = response.jsonPath().getString("[0].Name");
        String getQuestionId = response.jsonPath().getString("[0].QuestionID");
        assertEquals(getName, CommonValues.GENDER_TEXT.value);
        assertEquals(getQuestionId, dataProp.getProperty("QUESTION_ID"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 9)
    public void testGetDentalQuestionsWithInvalidSessionId() {
        String getDentalQuestionsEndpoint = prop.getProperty("Questions_GetDentalQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Dental Questions With Invalid SessionId response body :- " + response.asPrettyString());
        }
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(response.getStatusCode(), 500);
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 10)
    public void testGetDentalQuestionsWithBlankSessionId() {
        String getDentalQuestionsEndpoint = prop.getProperty("Questions_GetDentalQuestions").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getDentalQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetDentalQuestionsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Dental Questions With Blank SessionId response body :- " + response.asPrettyString());
        }
        String blankSessionIdErrorMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(blankSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 11)
    public void testAddDentalVisionAnswers() {
        String addDentalVisionAnswersData = commonMethods.getTestDataPath() + "addDentalVisionAnswers.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddDentalVisionAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, questionsSessionId), fileReader.getTestJsonFile(addDentalVisionAnswersData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, questionsSessionId);
        requestBody = fileReader.getTestJsonFile(addDentalVisionAnswersData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Dental Vision Answers response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 12)
    public void testAddDentalVisionAnswersWithInvalidSessionId() {
        String addDentalVisionAnswersData = commonMethods.getTestDataPath() + "addDentalVisionAnswers.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddDentalVisionAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, invalidQuestionSessionId), fileReader.getTestJsonFile(addDentalVisionAnswersData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, invalidQuestionSessionId);
        requestBody = fileReader.getTestJsonFile(addDentalVisionAnswersData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Dental Vision Answers With Invalid SessionId response body :- " + response.asPrettyString());
        }
        String invalidSessionIdMessage = response.jsonPath().getString("Message");
        String invalidSessionIdErrorMessage = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidSessionIdMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorMessage, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 13)
    public void testAddDentalVisionAnswersWithBlankSessionId() {
        String addDentalVisionAnswersData = commonMethods.getTestDataPath() + "addDentalVisionAnswers.json";
        String addAnswersEndpoint = prop.getProperty("Questions_AddDentalVisionAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, blankQuestionsSessionId), fileReader.getTestJsonFile(addDentalVisionAnswersData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, blankQuestionsSessionId);
        requestBody = fileReader.getTestJsonFile(addDentalVisionAnswersData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Dental Vision Answers With Blank SessionId response body :- " + response.asPrettyString());
        }
        String blankSessionIdMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorMessage = response.jsonPath().getString("ModelState.session[0]");
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 14)
    public void testAddDentalVisionAnswersWithBlankBodyData() {
        String addAnswersEndpoint = prop.getProperty("Questions_AddDentalVisionAnswers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(addAnswersEndpoint, questionsSessionId), blankBody);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addAnswersEndpoint, questionsSessionId);
        requestBody = blankBody;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Dental Vision Answers With Blank SessionId response body :- " + response.asPrettyString());
        }
        String blankBodyDataMessage = response.jsonPath().getString("Message");
        String blankBodyDataErrorMessage = response.jsonPath().getString("ModelState.answers[0]");
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 15)
    public void testGetQuestionsALT() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestionsALT").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, questionsSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, questionsSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Question ALT response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String getName = response.jsonPath().getString("[0].Name");
        String getQuestionId = response.jsonPath().getString("[0].QuestionID");
        assertEquals(getName, CommonValues.GENDER_TEXT.value);
        assertEquals(getQuestionId, dataProp.getProperty("QUESTION_ID"));
    }

    @Test(priority = 16)
    public void testGetQuestionsALTWithInvalidSessionId() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestionsALT").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, invalidQuestionSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Question ALT With Invalid SessionId response body :- " + response.asPrettyString());
        }
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 17)
    public void testGetQuestionsALTWithBlankSessionId() {
        String getQuestionsEndpoint = prop.getProperty("Questions_GetQuestionsALT").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, blankQuestionsSessionId) + dataProp.getProperty("Questions_GetQuestionsALTData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Question ALT With Blank SessionId response body :- " + response.asPrettyString());
        }
        String blankSessionIdErrorMessage = response.jsonPath().getString("Message");
        String blankSessionIdErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(blankSessionIdErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(blankSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    private String getQuestionsSessionId() {
        String createSessionData = commonMethods.getTestDataPath() + "createSessionData.json";
        questionsSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return questionsSessionId;
    }
}