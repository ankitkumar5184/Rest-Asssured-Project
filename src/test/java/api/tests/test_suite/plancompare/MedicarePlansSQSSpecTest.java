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
import api.tests.config.enums.PlanCompareErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class MedicarePlansSQSSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    CommonDateTimeMethods commonDateTimeMethods = new CommonDateTimeMethods();
    private String medicarePlansSQSSpecSessionId;
    private String medicarePlansSQSSpecEndpoint;
    private String medicarePlansId, requestUrl, requestBody;

    @Test(priority = 1)
    public void testVerifyGetSession() {
        commonMethods.usePropertyFileData("PC-API");
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, getMedicarePlansSQSSPecSessionId()));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, medicarePlansSQSSpecSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get session response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get session with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyGetSessionWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("Sessions_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get session with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyGetPlans() {
        medicarePlansSQSSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansSQSSpecEndpoint, medicarePlansSQSSpecSessionId) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlans"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSQSSpecEndpoint, medicarePlansSQSSpecSessionId) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlans");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("MedicarePlans[0].PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("MedicarePlans[0].PlanType").isEmpty());
        Assert.assertTrue(response.jsonPath().getString("MedicarePlans").length() > 2);
        medicarePlansId = response.jsonPath().getString("MedicarePlans[0].ID");
    }

    @Test(priority = 5)
    public void testVerifyGetPlansWithInvalidSessionId() {
        medicarePlansSQSSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansSQSSpecEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlans"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSQSSpecEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlans");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get plans with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyGetPlansWithBlankSessionId() {
        medicarePlansSQSSpecEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansSQSSpecEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlans"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSQSSpecEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlans");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get plans with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testVerifyGetPlanDetails() {
        medicarePlansSQSSpecEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansSQSSpecEndpoint, medicarePlansSQSSpecSessionId, medicarePlansId) +
                dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlansDetails"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSQSSpecEndpoint, medicarePlansSQSSpecSessionId, medicarePlansId) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlansDetails");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details for Medicare plan response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("PlanType").isEmpty());
    }

    @Test(priority = 8)
    public void testVerifyGetPlanDetailsWithInvalidSessionId() {
        medicarePlansSQSSpecEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansSQSSpecEndpoint, GeneralErrorMessages.INVALID_TEXT.value, medicarePlansId) +
                dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlansDetails"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSQSSpecEndpoint, GeneralErrorMessages.INVALID_TEXT.value, medicarePlansId) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlansDetails");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get plan details for medicare plan with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyGetPlanDetailsForWithBlankSessionId() {
        medicarePlansSQSSpecEndpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s")
                .replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(medicarePlansSQSSpecEndpoint, dataProp.getProperty("BLANK_SESSION_ID"), medicarePlansId) +
                dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlansDetails"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSQSSpecEndpoint, dataProp.getProperty("BLANK_SESSION_ID"), medicarePlansId) + dataProp.getProperty("MedicarePlansSQSSpecTest_GetPlansDetails");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get plan details for medicare plan with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 13)
    public void testVerifyEnrollWithRider() {
        String getEnrollWithRiderData = commonMethods.getTestDataPath() + "enrollmentSettings.xml";
        String getEnrollUrlEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollUrlEndpoint, medicarePlansSQSSpecSessionId) + dataProp.getProperty("PlanEnrollWithRiders")
                        .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                fileReader.getTestJsonFile(getEnrollWithRiderData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, medicarePlansSQSSpecSessionId) + dataProp.getProperty("PlanEnrollWithRiders").replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        requestBody = fileReader.getTestJsonFile(getEnrollWithRiderData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test get enroll with Rider response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("Url"));
        Assert.assertFalse(response.jsonPath().getString("EnrollActionType").isEmpty());
    }

    @Test(priority = 14)
    public void testVerifyGetEnrollWithRiderWithInvalidSessionId() {
        String getEnrollWithRiderData = commonMethods.getTestDataPath() + "enrollmentSettingsData.xml";
        String getEnrollWithRiderEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollWithRiderEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PlanEnrollWithRiders")
                        .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                fileReader.getTestJsonFile(getEnrollWithRiderData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollWithRiderEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PlanEnrollWithRiders").replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        requestBody = fileReader.getTestJsonFile(getEnrollWithRiderData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get enroll url with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 15)
    public void testVerifyGetEnrollWithRiderWithBlankSessionId() {
        String getEnrollWithRiderData = commonMethods.getTestDataPath() + "enrollmentSettingsData.xml";
        String getEnrollWithRiderEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollWithRiderEndpoint, dataProp.getProperty("BLANK_SESSION_ID") + dataProp.getProperty("PlanEnrollWithRiders"))
                        .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                fileReader.getTestJsonFile(getEnrollWithRiderData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollWithRiderEndpoint, dataProp.getProperty("BLANK_SESSION_ID") + dataProp.getProperty("PlanEnrollWithRiders")).replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        requestBody = fileReader.getTestJsonFile(getEnrollWithRiderData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get enroll url with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 16)
    public void testVerifyGetEnrollWithRiderWithBlankBodyData() {
        String getEnrollWithRiderEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollWithRiderEndpoint, medicarePlansSQSSpecSessionId) + dataProp.getProperty("PlanEnrollWithRiders")
                        .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollWithRiderEndpoint, medicarePlansSQSSpecSessionId) + dataProp.getProperty("PlanEnrollWithRiders").replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        requestBody = CommonValues.BLANK_BODY_REQUEST_WITH_XML.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get enroll url with blank body data response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.enrollmentSettings[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 17)
    public void testVerifyGetEnrollWithRiderWithInvalidPlanId() {
        String getEnrollWithRiderData = commonMethods.getTestDataPath() + "enrollmentSettingsData.xml";
        String getEnrollWithRiderEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollWithRiderEndpoint, medicarePlansSQSSpecSessionId) +
                        dataProp.getProperty("PlanEnrollWithRiders").replace("{PlansID}", dataProp.getProperty("INVALID_TEXT"))
                                .replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                fileReader.getTestJsonFile(getEnrollWithRiderData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollWithRiderEndpoint, medicarePlansSQSSpecSessionId) + dataProp.getProperty("PlanEnrollWithRiders").replace("{PlansID}", dataProp.getProperty("INVALID_TEXT")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        requestBody = fileReader.getTestJsonFile(getEnrollWithRiderData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get enroll url with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
    }

    @Test(priority = 17)
    public void testVerifyGetEnrollWithRiderWithInvalidRiderId() {
        String getEnrollWithRiderData = commonMethods.getTestDataPath() + "enrollmentSettingsData.xml";
        String getEnrollWithRiderEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollWithRiderEndpoint, medicarePlansSQSSpecSessionId) +
                        dataProp.getProperty("PlanEnrollWithRiders").replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID"))
                                .replace("{RiderID}", dataProp.getProperty("INVALID_TEXT")),
                fileReader.getTestJsonFile(getEnrollWithRiderData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollWithRiderEndpoint, medicarePlansSQSSpecSessionId) + dataProp.getProperty("PlanEnrollWithRiders").replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("INVALID_TEXT"));
        requestBody = fileReader.getTestJsonFile(getEnrollWithRiderData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansSQSSpec get enroll url with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), PlanCompareErrorMessages.AT_LEAST_ONE_RIDER_MUST_BE_SELECTED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    private String getMedicarePlansSQSSPecSessionId() {
        String postCreateSessionData = commonMethods.getTestDataPath() + "createSessionJsonOriginal.json";
        medicarePlansSQSSpecSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return medicarePlansSQSSpecSessionId;
    }
}