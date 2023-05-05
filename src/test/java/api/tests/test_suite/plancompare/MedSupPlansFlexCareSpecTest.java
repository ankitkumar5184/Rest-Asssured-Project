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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class MedSupPlansFlexCareSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    private String memberSearchSessionId;
    private String invalidSessionIdResponse;
    private String invalidSessionIdErrorCode;
    private String getPlansQuestion;
    private String createSubmitAnswerData;
    private String submitAnswerEndPoint;
    private String enrollRequestEndPoint;
    private String createEnrollRequestData;
    private String requestUrl, requestBody;

    //  Get Plans Before Question
    @Test(priority = 1)
    public void testGetPlansBeforeQuestions() {
        commonMethods.usePropertyFileData("PC-API");
        getPlansQuestion = prop.getProperty("MedSupPlansFlexCareSpecTest_Plans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansQuestion, getSessionPlansBrokerSpecId()) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_PlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansQuestion, memberSearchSessionId) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_PlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testGetPlansBeforeQuestionsWithInvalidSession() {
        getPlansQuestion = prop.getProperty("MedSupPlansFlexCareSpecTest_Plans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansQuestion, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_PlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansQuestion, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_PlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session For Plans Before Question :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    //  Get Questions
    @Test(priority = 3)
    public void testGetQuestions() {
        getPlansQuestion = prop.getProperty("MedSupPlansFlexCareSpecTest_Question").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansQuestion, memberSearchSessionId) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_QuestionData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansQuestion, memberSearchSessionId) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_QuestionData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass(" Get Session For Questions :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void testGetQuestionsWithInvalidSession() {
        getPlansQuestion = prop.getProperty("MedSupPlansFlexCareSpecTest_Question").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansQuestion, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_QuestionData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansQuestion, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_QuestionData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass(" Get Session For Questions :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    //  Submit Answers
    @Test(priority = 5)
    public void testSubmitAnswers() {
        submitAnswerEndPoint = prop.getProperty("MedSupPlansFlexCareSpecTest_SubmitAnswer").replace("{SessionID}", "%s");
        createSubmitAnswerData = commonMethods.getTestDataPath() + "medsupSubmitAnswers.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("sessionId-Endpoint") + String.format(submitAnswerEndPoint, memberSearchSessionId), fileReader.getTestJsonFile(createSubmitAnswerData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(submitAnswerEndPoint, memberSearchSessionId);
        requestBody = fileReader.getTestJsonFile(createSubmitAnswerData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Submit Answer For Full Cost For Certain Drug And Plan:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testSubmitAnswersBlankBody() {
        submitAnswerEndPoint = prop.getProperty("MedSupPlansFlexCareSpecTest_SubmitAnswer").replace("{SessionID}", "%s");
        createSubmitAnswerData = commonMethods.getTestDataPath() + "medsupSubmitAnswers.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("sessionId-Endpoint") + String.format(submitAnswerEndPoint, memberSearchSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(submitAnswerEndPoint, memberSearchSessionId);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Submit Answer For Full Cost For Certain Drug And Plan:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 7)
    public void testSubmitAnswersWithInvalidSession() {
        submitAnswerEndPoint = prop.getProperty("MedSupPlansFlexCareSpecTest_SubmitAnswer").replace("{SessionID}", "%s");
        createSubmitAnswerData = commonMethods.getTestDataPath() + "medsupSubmitAnswers.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("sessionId-Endpoint") + String.format(submitAnswerEndPoint, GeneralErrorMessages.INVALID_TEXT.value),
                fileReader.getTestJsonFile(createSubmitAnswerData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(submitAnswerEndPoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(createSubmitAnswerData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Submit Answer For Full Cost For Certain Drug And Plan:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    //  Get MeDiGap Plans
    @Test(priority = 8)
    public void testGetMedGapPlans() {
        getPlansQuestion = prop.getProperty("MedSupPlansFlexCareSpecTest_PlanID").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansQuestion, memberSearchSessionId) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_PlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansQuestion, memberSearchSessionId) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_PlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        String mediCarePlanId = response.jsonPath().getString("MedicarePlans[0].ID");
        assertNotNull(mediCarePlanId);
    }

    //  Get Plan Details
    @Test(priority = 9)
    public void testGetPlansDetails() {
        String getPlans = prop.getProperty("MedSupPlansFlexCareSpecTest_GetPlanDetails").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlans, memberSearchSessionId, dataProp.getProperty("PlanAcmePlanID")) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_GetPlanDetailsData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlans, memberSearchSessionId, dataProp.getProperty("PlanAcmePlanID")) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_GetPlanDetailsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session For Plans Before Question :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 10)
    public void testGetPlansDetailsWithInvalidSession() {
        getPlansQuestion = prop.getProperty("MedSupPlansFlexCareSpecTest_GetPlanDetails").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansQuestion, GeneralErrorMessages.INVALID_TEXT.value, dataProp.getProperty("PlanAcmePlanID")) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_GetPlanDetailsData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansQuestion, GeneralErrorMessages.INVALID_TEXT.value, dataProp.getProperty("PlanAcmePlanID")) + dataProp.getProperty("MedSupPlansFlexCareSpecTest_GetPlanDetailsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session For Plans Before Question :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    //  Enroll Request
    @Test(priority = 11)
    public void testEnrollRequest() {
        enrollRequestEndPoint = prop.getProperty("MedSupPlansFlexCareSpecTest_EnrollRequest").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        createEnrollRequestData = commonMethods.getTestDataPath() + "enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "XML",
                prop.getProperty("sessionId-Endpoint") + String.format(enrollRequestEndPoint, memberSearchSessionId, dataProp.getProperty("PlanAcmePlanID")), fileReader.getTestJsonFile(createEnrollRequestData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(enrollRequestEndPoint, memberSearchSessionId, dataProp.getProperty("PlanAcmePlanID"));
        requestBody = fileReader.getTestJsonFile(createEnrollRequestData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Submit Answer For Full Cost For Certain Drug And Plan:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 12)
    public void testEnrollRequestWithInvalidSession() {
        enrollRequestEndPoint = prop.getProperty("MedSupPlansFlexCareSpecTest_EnrollRequest").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        createEnrollRequestData = commonMethods.getTestDataPath() + "enrollmentSettings.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "XML",
                prop.getProperty("sessionId-Endpoint") + String.format(enrollRequestEndPoint, GeneralErrorMessages.INVALID_TEXT.value, dataProp.getProperty("PlanAcmePlanID")), fileReader.getTestJsonFile(createEnrollRequestData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(enrollRequestEndPoint, GeneralErrorMessages.INVALID_TEXT.value, dataProp.getProperty("PlanAcmePlanID"));
        requestBody = fileReader.getTestJsonFile(createEnrollRequestData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Submit Answer For Full Cost For Certain Drug And Plan:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    private String getSessionPlansBrokerSpecId() {
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return memberSearchSessionId;
    }
}