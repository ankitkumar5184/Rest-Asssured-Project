package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonDateTimeMethods;
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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MedSupPlansInitiallyEligibleSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    CommonDateTimeMethods commonDateTimeMethods = new CommonDateTimeMethods();
    private String medSupPlansInitiallyEligibleSpecSessionId;
    String medSupPlansInitiallyEligibleSpecEndpoint;
    String ageAsOfPartBQuestionId = "1";
    boolean isAgeAsOfPartBPriorQuestionCalculatedFactorActual;
    private String requestURL, requestBody;

    @Test(priority = 1)
    public void testVerifyGetSession() {
        commonMethods.usePropertyFileData("PC-API");
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getMedSupPlansInitiallyEligibleSpecSessionId()));
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getMedSupPlansInitiallyEligibleSpecSessionId());
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetSession response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetSessionWithInvalidSessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyGetSessionWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetSessionWithBlankSessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyGetPlans() {
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, medSupPlansInitiallyEligibleSpecSessionId)
                + dataProp.getProperty("GetPlansWithPlanType").replace("&planType={PlanType}", "");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetPlans response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ContractID"));
        assertEquals(response.jsonPath().getInt("MedicarePlans[0].PlanYear"), commonDateTimeMethods.currentYear());
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
    }

    @Test(priority = 5)
    public void testVerifyGetPlansWithInvalidSessionId() {
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("GetPlansWithPlanType").replace("&planType={PlanType}", "");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetPlansWithInvalidSessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyGetPlansWithBlankSessionId() {
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("GetPlansWithPlanType").replace("&planType={PlanType}", "");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetPlansWithBlankSessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testVerifyGetQuestions() {
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetQuestions").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, medSupPlansInitiallyEligibleSpecSessionId)
                + dataProp.getProperty("MedSupPlansInitiallyEligibleSpecTest_GetQuestions");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetQuestions response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 8)
    public void testVerifyGetQuestionsWithBlankSessionID() {
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedSupPlansInitiallyEligibleSpecTest_GetQuestions");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetQuestionsWithBlankSessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyGetQuestionsWithInvalidSessionID() {
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("MedSupPlansInitiallyEligibleSpecTest_GetQuestions");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetQuestionsWithInvalidSessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 10)
    public void testVerifySubmitAnswers() {
        String postAddAnswersData = commonMethods.getTestDataPath() + "medsupSubmitAnswersMIunder65.json";
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetQuestions").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, medSupPlansInitiallyEligibleSpecSessionId);
        requestBody = fileReader.getTestJsonFile(postAddAnswersData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("SubmitAnswers response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 11)
    public void testVerifySubmitAnswersWithBlankBody() {
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetQuestions").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, medSupPlansInitiallyEligibleSpecSessionId);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, CommonValues.BLANK_BODY_REQUEST.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("SubmitAnswersWithBlankBody response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.answers[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 12)
    public void testVerifySubmitAnswersWithBlankSessionID() {
        String postAddAnswersData = commonMethods.getTestDataPath() + "medsupSubmitAnswersMIunder65.json";
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetQuestions").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(postAddAnswersData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("SubmitAnswersWithBlankSessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 13)
    public void testVerifySubmitAnswersWithInvalidSessionID() {
        String postAddAnswersData = commonMethods.getTestDataPath() + "medsupSubmitAnswersMIunder65.json";
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetQuestions").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, dataProp.getProperty("INVALID_TEXT"));
        requestBody = fileReader.getTestJsonFile(postAddAnswersData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("SubmitAnswersWithInvalidSessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 14)
    public void testVerifyGetSessionAfterSubmittingAnswers() {
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, medSupPlansInitiallyEligibleSpecSessionId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Test MedSupPlansInitiallyEligibleSpec After Submitting Answers get session response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("Questions[0].QuestionID"), ageAsOfPartBQuestionId);
        if (response.jsonPath().getString("Questions[0].QuestionID").equals(ageAsOfPartBQuestionId)) {
            String ageAsOfPartBQuestionCalculatedString = response.jsonPath().getString("Questions[0].Value");
            if (ageAsOfPartBQuestionCalculatedString.equalsIgnoreCase("Male")) {
                isAgeAsOfPartBPriorQuestionCalculatedFactorActual = true;
            } else if (ageAsOfPartBQuestionCalculatedString.equalsIgnoreCase("Female")) {
                isAgeAsOfPartBPriorQuestionCalculatedFactorActual = false;
            } else {
                throw new RuntimeException("Could not parse: " + ageAsOfPartBQuestionCalculatedString);
            }
        }
        boolean isPartBDatePriorAge65BdayCalculatedExpected = true;
        assert isAgeAsOfPartBPriorQuestionCalculatedFactorActual == isPartBDatePriorAge65BdayCalculatedExpected;
    }

    @Test(priority = 15)
    public void testVerifyGetSessionAfterSubmittingAnswersWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetSessionAfterSubmittingAnswersWithInvalidSessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 16)
    public void testVerifyGetSessionAfterSubmittingAnswersWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("GetSessionAfterSubmittingAnswersWithBlankSessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 17)
    public void testVerifySearchAvailablePlansForSession(){
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, medSupPlansInitiallyEligibleSpecSessionId)
                + dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("SearchAvailablePlansForSession response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 18)
    public void testVerifySearchAvailablePlansForSessionWithBlankSessionID(){
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("SearchAvailablePlansForSessionWithBlankSessionID response :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 19)
    public void testVerifySearchAvailablePlansForSessionWithInvalidSessionID(){
        medSupPlansInitiallyEligibleSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(medSupPlansInitiallyEligibleSpecEndpoint, dataProp.getProperty("INVALID_TEXT"))
                + dataProp.getProperty("GetPlansWithPlanType").replace("{PlanType}", dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("SearchAvailablePlansForSessionWithInvalidSessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    private String getMedSupPlansInitiallyEligibleSpecSessionId() {
        String postCreateSessionData = commonMethods.getTestDataPath() + "createSessionMI.json";
        medSupPlansInitiallyEligibleSpecSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return medSupPlansInitiallyEligibleSpecSessionId;
    }
}