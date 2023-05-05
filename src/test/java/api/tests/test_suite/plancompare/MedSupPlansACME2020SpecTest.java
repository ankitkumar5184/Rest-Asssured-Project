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
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class MedSupPlansACME2020SpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    String sessionId;
    String getMedGapId;
    String requestUrl, requestBody;

    @Test(priority = 1)
    public void testGetSession() {
        commonMethods.usePropertyFileData("PC-API");
        String getSessionEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, getMedSupPlansACME2020SpecId()));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, sessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testGetSessionWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testGetSessionWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Session With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testGetPlansBeforeQuestions() {
        String getPlansBeforeQuestionsEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansBeforeQuestionsEndpoint, sessionId) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansBeforeQuestionsEndpoint, sessionId) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testGetPlansBeforeQuestionsWithInvalidSessionId() {
        String getPlansBeforeQuestionsEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansBeforeQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansBeforeQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Plans Before Questions With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testGetPlansBeforeQuestionsWithBlankSessionId() {
        String getPlansBeforeQuestionsEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansBeforeQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansBeforeQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Plans Before Questions With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testGetQuestions() {
        String getQuestionsEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getQuestionsEndpoint, sessionId) + dataProp.getProperty("MedSupPlansACME2020_Questions"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, sessionId) + dataProp.getProperty("MedSupPlansACME2020_Questions");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Questions response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 8)
    public void testGetQuestionsWithInvalidSessionId() {
        String getQuestionsEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansACME2020_Questions"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansACME2020_Questions");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Questions With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testGetQuestionsWithBlankSessionId() {
        String getQuestionsEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedSupPlansACME2020_Questions"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getQuestionsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedSupPlansACME2020_Questions");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Questions With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }
    
    @Test(priority = 10)
    public void testSubmitAnswers() {
        String getSubmitAnswersEndPoint = prop.getProperty("MedSupPlansACME2020_Submit_Answers").replace("{SessionID}", "%s");
        String getSubmitAnswersData = commonMethods.getTestDataPath() + "medsupSubmitAnswersCA.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSubmitAnswersEndPoint, sessionId), fileReader.getTestJsonFile(getSubmitAnswersData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSubmitAnswersEndPoint, sessionId);
        requestBody = fileReader.getTestJsonFile(getSubmitAnswersData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Submit Answers response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 11)
    public void testSubmitAnswersWithInvalidSessionId() {
        String getSubmitAnswersEndPoint = prop.getProperty("MedSupPlansACME2020_Submit_Answers").replace("{SessionID}", "%s");
        String getSubmitAnswersData = commonMethods.getTestDataPath() + "medsupSubmitAnswersCA.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSubmitAnswersEndPoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(getSubmitAnswersData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSubmitAnswersEndPoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(getSubmitAnswersData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Submit Answers With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 12)
    public void testSubmitAnswersWithBlankSessionId() {
        String getSubmitAnswersEndPoint = prop.getProperty("MedSupPlansACME2020_Submit_Answers").replace("{SessionID}", "%s");
        String getSubmitAnswersData = commonMethods.getTestDataPath() + "medsupSubmitAnswersCA.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSubmitAnswersEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), fileReader.getTestJsonFile(getSubmitAnswersData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSubmitAnswersEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(getSubmitAnswersData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Submit Answers With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 13)
    public void testSubmitAnswersWithBlankBodyData() {
        String getSubmitAnswersEndPoint = prop.getProperty("MedSupPlansACME2020_Submit_Answers").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                String.format(getSubmitAnswersEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), CommonValues.BLANK_BODY_REQUEST.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSubmitAnswersEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Submit Answers With Blank Body Data response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 14)
    public void testReviewSession() {
        String getReviewSessionEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getReviewSessionEndpoint, sessionId));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getReviewSessionEndpoint, sessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get Session Review response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 15)
    public void testReviewSessionWithInvalidSessionId() {
        String getReviewSessionEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getReviewSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getReviewSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Session Review with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 16)
    public void testReviewSessionWithBlankSessionId() {
        String getReviewSessionEndpoint = prop.getProperty("MedSupPlansACME2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getReviewSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getReviewSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Session Review With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 17)
    public void testGetMedGapPlans() {
        String getMedGapPlansEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getMedGapPlansEndpoint, sessionId) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getMedGapPlansEndpoint, sessionId) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        getMedGapId = response.jsonPath().getString("MedicarePlans[0].ID");
    }


    @Test(priority = 18)
    public void testGetMedGapPlansWithInvalidSessionId() {
        String getMedGapPlansEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getMedGapPlansEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getMedGapPlansEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get MedGap Plans With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 19)
    public void testGetMedGapPlansWithBlankSessionId() {
        String getMedGapPlansEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getMedGapPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getMedGapPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedSupPlansACME2020_GetPlans_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get MedGap Plans With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 20)
    public void testGetPlansDetails() {
        String getPlansDetailsEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansDetailsEndpoint, sessionId) + dataProp.getProperty("MedSupPlansACME2020_GetPlan_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansDetailsEndpoint, sessionId) + dataProp.getProperty("MedSupPlansACME2020_GetPlan_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 21)
    public void testGetPlansDetailsWithInvalidSessionId() {
        String getPlansDetailsEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansDetailsEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansACME2020_GetPlan_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansDetailsEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansACME2020_GetPlan_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Plans Details With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 22)
    public void testGetPlansDetailsWithBlankSessionId() {
        String getPlansDetailsEndpoint = prop.getProperty("MedSupPlansACME2020_GetPlan").replace("{SessionID}", "%s")
                .replace("{PlanID}", getMedGapId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansDetailsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedSupPlansACME2020_GetPlan_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansDetailsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedSupPlansACME2020_GetPlan_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test Get Plans Details With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 23)
    public void testGetEnrollUrl() {
        String getEnrollUrlEndPoint = prop.getProperty("MedSupPlansACME2020_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        String getEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingNoBroker.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollUrlEndPoint, sessionId), fileReader.getTestJsonFile(getEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndPoint, sessionId);
        requestBody = fileReader.getTestJsonFile(getEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Get Enroll Url response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 24)
    public void testGetEnrollUrlWithInvalidSessionId() {
        String getEnrollUrlEndPoint = prop.getProperty("MedSupPlansACME2020_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        String getEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingNoBroker.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollUrlEndPoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(getEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndPoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(getEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
        }
        ExtentLogger.pass("Test Get Enroll Url With Invalid Session Id response:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 25)
    public void testGetEnrollUrlWithBlankSessionId() {
        String getEnrollUrlEndPoint = prop.getProperty("MedSupPlansACME2020_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        String getEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingNoBroker.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollUrlEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), fileReader.getTestJsonFile(getEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(getEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Get Enroll Url With Blank Session Id response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 26)
    public void testGetEnrollUrlWithBlankBodyData() {
        String getEnrollUrlEndPoint = prop.getProperty("MedSupPlansACME2020_EnrollRider").replace("{SessionID}", "%s")
                .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollUrlEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = CommonValues.BLANK_BODY_REQUEST_WITH_XML.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test Get Enroll Url With Blank Body Data response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    private String getMedSupPlansACME2020SpecId() {
        sessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return sessionId;
    }
}