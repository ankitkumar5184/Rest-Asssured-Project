package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.PlanCompareErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class SessionPlansTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String , String> tokens = envInstanceHelper.getEnvironment();
    private String sessionPlansSmartSessionId, medicarePlansID, planSessionId, getPlansEndpoint,
            invalidErrorMessage, invalidErrorCode, getPlanYear, postPlansEnrollmentBodyData, requestURL, requestBody;

    /* Plan Smart scripts to be disabled as the functionality is not operational for PC API.*/
    @Test(enabled = false)
    public void testVerifyCreateSessionPlanSmart() {
        commonMethods.usePropertyFileData("PC-API");
        String postCreateSessionForPlanSmartData = commonMethods.getTestDataPath() + "createSessionPlanSmartData.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Plans_PlanSmartCreateSession"),
                        fileReader.getTestJsonFile(postCreateSessionForPlanSmartData));
        requestURL = tokens.get("host") + prop.getProperty("Plans_PlanSmartCreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionForPlanSmartData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test create session plan smart response body : " + response.asPrettyString());
        }
        sessionPlansSmartSessionId = response.jsonPath().getString("[0].SessionID");
        String getSSoValue = response.jsonPath().getString("[0].SSOValue");
        String getStatusUpdate = response.jsonPath().getString("[0].Status");
        assertEquals(getSSoValue, dataProp.getProperty("CREATE_PLAN_SMART_SSO_VALUE"));
        assertEquals(getStatusUpdate, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(enabled = false)
    public void testVerifyCreateSessionPlanSmartWithBlankBody() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Plans_PlanSmartCreateSession"),
                CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("Plans_PlanSmartCreateSession");
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test create session plan smart when empty body response body : " + response.asPrettyString());
        }
        String emptyBodyResponseMessage = response.jsonPath().getString("Message");
        String emptyBodyErrorMessage = response.jsonPath().getString("ModelState.sessions[0]");
        assertEquals(emptyBodyResponseMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(emptyBodyErrorMessage,GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(enabled = false)
    public void testVerifyPlanSmartGetPlans(){
        String planSmartGetPlansEndpoint = prop.getProperty("Plans_PlanSmartGetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, sessionPlansSmartSessionId));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, sessionPlansSmartSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test plan smart get session plans response body :- " + response.asPrettyString());
        }
        String getContractID = response.jsonPath().getString("MedicarePlans[0].ContractID");
        String getID = response.jsonPath().getString("MedicarePlans[0].ID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertFalse(getContractID.isEmpty());
        assertFalse(getID.isEmpty());
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(enabled = false)
    public void testVerifyPlanSmartGetPlansWithInvalidSessionId(){
        String planSmartGetPlansEndpoint = prop.getProperty("Plans_PlanSmartGetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, dataProp.getProperty("INVALID_TEXT")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PlanSmart Get Plans With Invalid SessionId response body :- " + response.asPrettyString());
        }
        String invalidErrorMessage = response.jsonPath().getString("Message");
        String invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(enabled = false)
    public void testVerifyPlanSmartGetPlansWithBlankSessionId(){
        String planSmartGetPlansEndpoint = prop.getProperty("Plans_PlanSmartGetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(planSmartGetPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PlanSmart Get Plans With Blank SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test
    public void testVerifyGetPlans(){
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        getPlansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, getSessionPlansSessionId()));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, planSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test get plans response body :- " + response.asPrettyString());
        }
        medicarePlansID = response.jsonPath().getString("MedicarePlans[0].ID");
        String getContractID = response.jsonPath().getString("MedicarePlans[0].ContractID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertFalse(getContractID.isEmpty());
        assertFalse(medicarePlansID.isEmpty());
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 1)
    public void testVerifyGetPlansWithInvalidSessionId(){
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, dataProp.getProperty("INVALID_TEXT")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plans With Invalid SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 2)
    public void testVerifyGetPlansWithBlankSessionId(){
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint,  dataProp.getProperty("BLANK_SESSION_ID")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint,  dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plans With Blank SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 3)
    public void testVerifyPostPlansEnroll(){
        postPlansEnrollmentBodyData = commonMethods.getTestDataPath() + "planEnrollData.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, planSessionId, medicarePlansID), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId, medicarePlansID);
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test post plans enroll response body :- " + response.asPrettyString());
        }
        String getUrl = response.jsonPath().getString("Url");
        String getMethod = response.jsonPath().getString("Method");
        assertFalse(getUrl.isEmpty());
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void testVerifyPostPlansEnrollWithInvalidMedicarePlanId(){
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, getSessionPlansSessionId(), dataProp.getProperty("INVALID_TEXT")), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId, dataProp.getProperty("INVALID_TEXT"));
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Plans Enroll With Invalid Medicare PlanId response body :-" + response.asPrettyString());
        }
        String invalidPlanIdErrorMessage = response.jsonPath().getString("Message");
        assertEquals(invalidPlanIdErrorMessage, PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 5)
    public void testVerifyPostPlansEnrollWithInvalidSessionId(){
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, dataProp.getProperty("INVALID_TEXT"), medicarePlansID), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, dataProp.getProperty("INVALID_TEXT"), medicarePlansID);
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Plans Enroll With Invalid SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 6)
    public void testVerifyPostPlansEnrollWithBlankBody(){
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, planSessionId, medicarePlansID), CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId, medicarePlansID);
        requestBody = CommonValues.BLANK_BODY_REQUEST_WITH_XML.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Plans Enroll With Invalid SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ModelState.enrollmentSettings[0]");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(invalidErrorCode,GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 7)
    public void testVerifyPostPlansEnrollWithBlankSessionId(){
        String postPlansEnrollmentBodyData =commonMethods.getTestDataPath() + "planEnrollData.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, dataProp.getProperty("BLANK_SESSION_ID"), medicarePlansID), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, dataProp.getProperty("BLANK_SESSION_ID"), medicarePlansID);
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Plans Enroll With Blank SessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 8)
    public void testVerifyPostPlansEnrollWithBlankMedicarePlanId(){
        String postPlansEnrollmentBodyData =commonMethods.getTestDataPath() + "planEnrollData.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML" ,prop.getProperty("sessionId-Endpoint") +
                String.format(plansEnrollEndpoint, getSessionPlansSessionId(), dataProp.getProperty("BLANK_MEDICARE_PLAN_ID")), fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId, dataProp.getProperty("BLANK_MEDICARE_PLAN_ID"));
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Plans Enroll With Blank Medicare PlanId response body :- " + response.asPrettyString());
        }
        String blankPlanIdErrorMessage = response.jsonPath().getString("Message");
        assertEquals(blankPlanIdErrorMessage, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_POST.value);
        assertEquals(response.getStatusCode(), 405);
    }

    @Test(priority = 9)
    public void testVerifyGetPlansCount(){
        String planCountEndpoint = prop.getProperty("Plans_PlanCount").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planCountEndpoint, planSessionId) +
                dataProp.getProperty("Plans_PlanCountData"));
        requestURL = tokens.get("host") + String.format(planCountEndpoint, planSessionId) + dataProp.getProperty("Plans_PlanCountData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test get plans count response body :- " + response.asPrettyString());
        }
        String getPlanType = response.jsonPath().getString("[0].PlanType");
        String getPlanType256Count = response.jsonPath().getString("[0].Count");
        assertEquals(getPlanType, dataProp.getProperty("PLAN_TYPE"));
        assertNotNull(getPlanType256Count);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 10)
    public void testVerifyGetPlansCountWithInvalidSessionId(){
        String planCountEndpoint = prop.getProperty("Plans_PlanCount").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planCountEndpoint, dataProp.getProperty("INVALID_TEXT")) +
                dataProp.getProperty("Plans_PlanCountData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(planCountEndpoint, dataProp.getProperty("INVALID_TEXT"))
                + dataProp.getProperty("Plans_PlanCountData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plans With Invalid SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 11)
    public void testVerifyGetPlansCountWithBlankSessionId(){
        String planCountEndpoint = prop.getProperty("Plans_PlanCount").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(planCountEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("Plans_PlanCountData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(planCountEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("Plans_PlanCountData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plans With Blank SessionId response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 12)
    public void testVerifyGetPlanDetails(){
        String plansEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId, medicarePlansID));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId, medicarePlansID);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details response body :- " + response.asPrettyString());
        }
        String getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 13)
    public void testVerifyGetPlanDetailsWithBlankSessionID(){
        String plansEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"),
                medicarePlansID));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"), medicarePlansID);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details with blank session ID response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 14)
    public void testVerifyGetPlanDetailsWithInvalidSessionID(){
        String plansEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, dataProp.getProperty("INVALID_TEXT"),
                        medicarePlansID));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, dataProp.getProperty("INVALID_TEXT"), medicarePlansID);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details with invalid session ID response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 15)
    public void testVerifyGetPlanDetailsWithInvalidPlanID(){
        String plansEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId,
                dataProp.getProperty("INVALID_TEXT")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details with invalid session ID response body :- " + response.asPrettyString());
        }
        invalidErrorMessage = response.jsonPath().getString("Message");
        invalidErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidErrorMessage, PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
        assertEquals(response.getStatusCode(), 400);
    }

    private String getSessionPlansSessionId(){
        String postCreateSessionData = commonMethods.getTestDataPath() + "createSessionData.json";
        planSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return planSessionId;
    }
}