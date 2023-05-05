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
import static api.base.helpers.CommonMethods.*;

public class MedicarePlansSPAFlexcare2021SpecTest {

    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    CommonMethods commonMethods = new CommonMethods();
    CommonDateTimeMethods commonDateTimeMethods = new CommonDateTimeMethods();
    FileReader fileReader = new FileReader();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();

    String testEnrollmentSettingsData;
    protected String medicarePlansSPAFlexcare2021SessionId;
    protected String medicarePlansSPAFlexcare2021Endpoint;
    protected String requestUrl, requestBody;

    @Test(priority = 1)
    public void testVerifyGetSessionRequest() {
        commonMethods.usePropertyFileData("PC-API");
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("MedicarePlansSPAFlexcare2021_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, getMedicarePlansSPAFlexcare2021SessionId()));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session request response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionRequestWithInvalidSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("MedicarePlansSPAFlexcare2021_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session request response when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyGetSessionRequestWithBlankSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("MedicarePlansSPAFlexcare2021_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session request response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyGetPlans() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId) + dataProp.getProperty("GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId) + dataProp.getProperty("GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("MedicarePlans[0].PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertTrue(response.jsonPath().get("MedicarePlans[0].PlanType") instanceof Integer);
        Assert.assertTrue(response.jsonPath().getString("MedicarePlans[0].PlanName").length() > 1);
        Assert.assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanName"));
        Assert.assertNotNull(response.jsonPath().getString("MedicarePlans[0].CarrierName"));
        Assert.assertTrue(response.jsonPath().getString("MedicarePlans[0].CarrierName").length() > 2);
        Assert.assertTrue(response.jsonPath().getFloat("MedicarePlans[0].MedicalPremium") >= 0.0);
        Assert.assertTrue(response.jsonPath().getFloat("MedicarePlans[0].MaximumOutOfPocketCost") >= 0.0);
    }

    @Test(priority = 5)
    public void testVerifyGetPlansWithInvalidSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT")) + dataProp.getProperty("GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT")) + dataProp.getProperty("GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan response when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyGetPlansWithBlankSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testVerifyGetPlanDetailsForMedicarePlan() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId, dataProp.getProperty("MedicarePlansId")) + dataProp.getProperty("GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId, dataProp.getProperty("MedicarePlansId")) + dataProp.getProperty("GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details for Medicare plan response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("PlanType").isEmpty());
    }

    @Test(priority = 8)
    public void testVerifyGetPlanDetailsForMedicarePlanWithInvalidSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlansId")) + dataProp.getProperty("GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlansId")) + dataProp.getProperty("GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyGetPlanDetailsForMedicarePlanWithBlankSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlansId")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlansId"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details for Medicare plan when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 10)
    public void testVerifyGetPlanDetailsForMedicarePlanWithInvalidPlanId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", dataProp.getProperty("INVALID_TEXT"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId) + dataProp.getProperty("GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId) + dataProp.getProperty("GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details for Medicare plan response when invalid planId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
    }

    @Test(priority = 11)
    public void testVerifyGetPlanDetailsWithoutEffectiveDate() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId, dataProp.getProperty("MedicarePlansId")) +
                dataProp.getProperty("MedicarePlansSPAFlexcare2021_GetPlanNoDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId, dataProp.getProperty("MedicarePlansId")) +
                dataProp.getProperty("MedicarePlansSPAFlexcare2021_GetPlanNoDateData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("PlanType").isEmpty());
    }

    @Test(priority = 12)
    public void testVerifyGetPlanDetailsWithoutEffectiveDateWithInvalidSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlansId")) + dataProp.getProperty("MedicarePlansSPAFlexcare2021_GetPlanNoDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlansId")) + dataProp.getProperty("MedicarePlansSPAFlexcare2021_GetPlanNoDateData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details without effective date response when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 13)
    public void testVerifyGetPlanDetailsWithoutEffectiveDateWithBlankSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlansId")) + dataProp.getProperty("MedicarePlansSPAFlexcare2021_GetPlanNoDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlansId")) + dataProp.getProperty("MedicarePlansSPAFlexcare2021_GetPlanNoDateData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details without effective date response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 14)
    public void testVerifyGetPlanDetailsWithoutEffectiveDateWithInvalidPlanId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_GetPLanDetail").replace("{SessionID}", "%s").replace("{PlanID}", dataProp.getProperty("INVALID_TEXT"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId) + dataProp.getProperty("MedicarePlansSPAFlexcare2021_GetPlanNoDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId) + dataProp.getProperty("MedicarePlansSPAFlexcare2021_GetPlanNoDateData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details without effective date response when invalid planId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
    }

    @Test(priority = 15)
    public void testVerifyGetEnrollUrl() {
        testEnrollmentSettingsData = commonMethods.getTestDataPath() + "enrollmentSettings.xml";
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "XML",
                prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId, dataProp.getProperty("MedicarePlansId")), fileReader.getTestJsonFile(testEnrollmentSettingsData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId, dataProp.getProperty("MedicarePlansId"));
        requestBody = fileReader.getTestJsonFile(testEnrollmentSettingsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test get enroll Url response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("Url"));
        Assert.assertTrue(response.jsonPath().getString("Url").length() > 5);
        Assert.assertNotNull(response.jsonPath().getString("Url"));
        Assert.assertNotNull(response.jsonPath().getString("Parameters"));
        Assert.assertTrue(response.jsonPath().getString("Parameters").length() > 5);
    }

    @Test(priority = 16)
    public void testVerifyGetEnrollUrlWithInvalidSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlansId")),
                fileReader.getTestJsonFile(testEnrollmentSettingsData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlansId"));
        requestBody = fileReader.getTestJsonFile(testEnrollmentSettingsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test get enroll Url response when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 17)
    public void testVerifyGetEnrollUrlWithBlankSessionId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlansId")),
                fileReader.getTestJsonFile(testEnrollmentSettingsData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlansId"));
        requestBody = fileReader.getTestJsonFile(testEnrollmentSettingsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test get enroll Url response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 18)
    public void testVerifyGetEnrollUrlWithInvalidPlanId() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", dataProp.getProperty("INVALID_TEXT"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId),
                fileReader.getTestJsonFile(testEnrollmentSettingsData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId);
        requestBody = fileReader.getTestJsonFile(testEnrollmentSettingsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test get enroll Url response when invalid planId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
    }

    @Test(priority = 19)
    public void testVerifyGetEnrollUrlWithEmptyBody() {
        medicarePlansSPAFlexcare2021Endpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", dataProp.getProperty("INVALID_TEXT"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId),
                CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansSPAFlexcare2021Endpoint, medicarePlansSPAFlexcare2021SessionId);
        requestBody = CommonValues.BLANK_BODY_REQUEST_WITH_XML.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test get enroll Url response when empty body:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.enrollmentSettings[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    private String getMedicarePlansSPAFlexcare2021SessionId() {
        medicarePlansSPAFlexcare2021SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return medicarePlansSPAFlexcare2021SessionId;
    }
}