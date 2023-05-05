package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MedSupPlansPreloadACMESpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    String postCreateSessionWithLoadedQuestionsData, postCreateSessionWithoutLoadedQuestionsData;
    private String medSupPlansPreloadACMESessionId;
    private String medSupPlansPreloadACMESpecSessionEndpoint;
    protected String newlyEligibleQuestionId = "1";
    boolean newlyEligibleQ23AnswerActual;
    private String getSessionEndpoint;
    private String getPlanId, requestURL, requestBody;

    @Test
    public void testVerifyGetCreatedSessionWithLoadedQuestions() {
        commonMethods.usePropertyFileData("PC-API");
        postCreateSessionWithLoadedQuestionsData = commonMethods.getTestDataPath() + "createSessionCAWithQuestions.json";
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + getCreateSession(postCreateSessionWithLoadedQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + medSupPlansPreloadACMESessionId;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Test get created session response :- " + response.asPrettyString());
        }
        assertEquals(response.jsonPath().getString("Questions[0].QuestionID"), newlyEligibleQuestionId);
        if (response.jsonPath().getString("Questions[0].QuestionID").equals(newlyEligibleQuestionId)) {
            String newlyEligibleQ23AnswerString = response.jsonPath().getString("Questions[0].Value");
            if (newlyEligibleQ23AnswerString.equalsIgnoreCase("Male")) {
                newlyEligibleQ23AnswerActual = true;
            } else if (newlyEligibleQ23AnswerString.equalsIgnoreCase("Female")) {
                newlyEligibleQ23AnswerActual = false;
            } else {
                throw new RuntimeException("Could not parse: " + newlyEligibleQ23AnswerString);
            }
            boolean newlyEligibleQ23AnswerExpected = true;
            assert newlyEligibleQ23AnswerActual == newlyEligibleQ23AnswerExpected;
        }
    }

    @Test(priority = 1)
    public void testVerifyGetSessionWithLoadedQuestionsInvalidSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session With Invalid SessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionWithLoadedQuestionsBlankSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session With Blank SessionId response :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 3)
    public void testVerifySearchAvailablePlansForSession(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansPreloadACMESpecSessionEndpoint, medSupPlansPreloadACMESessionId) +
                String.format(dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE")));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
        }
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedigapPlans"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].ID"));
        assertEquals(response.jsonPath().getString("MedigapPlans[0].PlanType"), dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].AnnualPlanPremium"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].AnnualCalculatedPlanPremium"));
    }

    @Test(priority = 4)
    public void testVerifySearchAvailablePlansForSessionWithBlankSessionID(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Search available plans for session with blank sessionID response :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 5)
    public void testVerifySearchAvailablePlansForSessionWithInvalidSessionID(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, dataProp.getProperty("INVALID_TEXT")) +
                dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Search available plans for session with invalid sessionID response :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyGetCreatedSessionWithoutLoadedQuestions() {
        postCreateSessionWithoutLoadedQuestionsData = commonMethods.getTestDataPath() + "createSessionCA.json";
        requestURL = prop.getProperty("sessionId-Endpoint") + getCreateSession(postCreateSessionWithoutLoadedQuestionsData);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get created session without loaded questions response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 7)
    public void testVerifyGetSessionWithoutLoadedQuestionsInvalidSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get session with invalid sessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 8)
    public void testVerifyGetSessionWithoutLoadedQuestionsBlankSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get session with blank sessionId response body:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 9)
    public void testVerifyUpdateSessionWithQuestions(){
        String updateSessionCAWithQuestionsData = commonMethods.getTestDataPath() + "updateSessionCAWithQuestions.json";
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, medSupPlansPreloadACMESessionId);
        requestBody = fileReader.getTestJsonFile(updateSessionCAWithQuestionsData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Update session with questions response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("Birthdate").replace("T00:00:00", ""), dataProp.getProperty("BIRTH_DATE_UPDATE_SESSION"));
        assertEquals(response.jsonPath().getString("HealthStatus"), dataProp.getProperty("HEALTH_STATUS"));
    }

    @Test(priority = 10)
    public void testVerifyUpdateSessionWithQuestionsWithInvalidSessionID(){
        String updateSessionCAWithQuestionsData = commonMethods.getTestDataPath() + "updateSessionCAWithQuestions.json";
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("INVALID_TEXT"));
        requestBody = fileReader.getTestJsonFile(updateSessionCAWithQuestionsData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Update session with questions response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 11)
    public void testVerifyUpdateSessionWithQuestionsWithBlankSessionID(){
        String updateSessionCAWithQuestionsData = commonMethods.getTestDataPath() + "updateSessionCAWithQuestions.json";
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(updateSessionCAWithQuestionsData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Update session with questions response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState['sessions.Birthdate'][0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 12)
    public void testVerifyGetCreatedSessionAfterUpdatingSessionWithQuestions() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, medSupPlansPreloadACMESessionId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Test verify get created session after updating session with questions response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("Questions[0].QuestionID"), newlyEligibleQuestionId);
        if (response.jsonPath().getString("Questions[0].QuestionID").equals(newlyEligibleQuestionId)) {
            String newlyEligibleQ23AnswerString = response.jsonPath().getString("Questions[0].Value");
            if (newlyEligibleQ23AnswerString.equalsIgnoreCase("Male")) {
                newlyEligibleQ23AnswerActual = true;
            } else if (newlyEligibleQ23AnswerString.equalsIgnoreCase("Female")) {
                newlyEligibleQ23AnswerActual = false;
            } else {
                throw new RuntimeException("Could not parse: " + newlyEligibleQ23AnswerString);
            }
            boolean newlyEligibleQ23AnswerExpected = true;
            assert newlyEligibleQ23AnswerActual == newlyEligibleQ23AnswerExpected;
        }
    }

    @Test(priority = 13)
    public void testVerifyGetCreatedSessionAfterUpdatingSessionWithQuestionsWithInvalidSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get session with invalid sessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 14)
    public void testVerifyCreatedGetSessionAfterUpdatingSessionWithQuestionsWithBlankSessionId() {
        getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get session with blank sessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 15)
    public void testVerifySearchAvailablePlansAfterUpdatingSessionWithQuestions(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansPreloadACMESpecSessionEndpoint, medSupPlansPreloadACMESessionId) +
                String.format(dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE")));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Search available plans after updating session with questions response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        getPlanId = response.jsonPath().getString("MedigapPlans[0].ID");
        assertNotNull(response.jsonPath().getString("MedigapPlans"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].ID"));
        assertEquals(response.jsonPath().getString("MedigapPlans[0].PlanType"), dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].AnnualPlanPremium"));
        assertNotNull(response.jsonPath().getString("MedigapPlans[0].AnnualCalculatedPlanPremium"));
    }

    @Test(priority = 16)
    public void testVerifySearchAvailablePlansAfterUpdatingSessionWithQuestionsWithBlankSessionID(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Search available plans after updating session with questions with blank sessionID response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 17)
    public void testVerifySearchAvailablePlansAfterUpdatingSessionWithQuestionsWithInvalidSessionID(){
        medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, dataProp.getProperty("INVALID_TEXT")) +
                dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Search available plans after updating session with questions with invalid sessionID response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 18)
    public void testVerifyGetPlanDetails() {
        String medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint")
                + String.format(medSupPlansPreloadACMESpecSessionEndpoint, medSupPlansPreloadACMESessionId, getPlanId) + dataProp.getProperty
                ("MedSupPlansPreloadACMESpec_GetPlansDetails");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Test get plan details for Medicare plan response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("PlanType"), dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        assertNotNull(response.jsonPath().getString("CarrierName"));
        assertNotNull(response.jsonPath().getString("AnnualPlanPremium"));
        assertNotNull(response.jsonPath().getString("AnnualCalculatedPlanPremium"));
    }

    @Test(priority = 19)
    public void testVerifyGetPlanDetailsWithInvalidSessionId() {
        String medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansPreloadACMESpecSessionEndpoint,
                GeneralErrorMessages.INVALID_TEXT.value, getPlanId) + dataProp.getProperty("MedSupPlansPreloadACMESpec_GetPlansDetails");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Plan details with invalid sessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 20)
    public void testVerifyGetPlanDetailsForWithBlankSessionId() {
        String medSupPlansPreloadACMESpecSessionEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansPreloadACMESpecSessionEndpoint,
                dataProp.getProperty("BLANK_SESSION_ID"), getPlanId) + dataProp.getProperty("MedSupPlansPreloadACMESpec_GetPlansDetails");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Plan details for with blank sessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    private String getCreateSession(String filePath) {
        medSupPlansPreloadACMESessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(filePath));
        return medSupPlansPreloadACMESessionId;
    }
}