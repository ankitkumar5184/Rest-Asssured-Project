package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.*;
import api.base.helpers.env_annotations.ExecuteOnProd;
import api.base.helpers.env_annotations.SkipOnProd;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.PlanCompareErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.*;

@Listeners(value = ConditionalSkipTestAnalyzer.class)
public class MedicarePlansACME2020SpecTest {

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
    protected String medicarePlansACME2020SessionId;
    protected String medicarePlansACME2020Endpoint;
    protected String  requestUrl, requestBody;

    @Test(priority = 1)
    public void testVerifyGetSessionRequest() {
        commonMethods.usePropertyFileData("PC-API");
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, getMedicarePlansACME2020SessionId()));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session request response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionRequestWithInvalidSessionId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"));
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
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session request response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyGetPlans() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId)
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId)
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("MedicarePlans[0].PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("MedicarePlans[0].PlanType").isEmpty());
        Assert.assertTrue(response.jsonPath().getString("MedicarePlans").length() > 2);
    }


    @Test(priority = 5)
    public void testVerifyGetPlansWithInvalidSessionId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansData");
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
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansData");
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
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlansForMedicare").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("MedicarePlanID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansForMedicareData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("MedicarePlanID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansForMedicareData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test verify get plan details for medicare plan response body :- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("PlanType").isEmpty());
    }

    @Test(priority = 8)
    public void testVerifyGetPlanDetailsForMedicarePlanWithInvalidSessionId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlansForMedicare").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlanID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansForMedicareData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlanID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansForMedicareData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details for Medicare plan when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyGetPlanDetailsForMedicarePlanWithBlankSessionId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlansForMedicare").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlanID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansForMedicareData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlanID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansForMedicareData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details for Medicare plan when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 10)
    public void testVerifyGetPlanDetailsForMedicarePlanWithInvalidPlanId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlansForMedicare").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("INVALID_TEXT"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansForMedicareData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("INVALID_TEXT"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlansForMedicareData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details for Medicare plan response when invalid planId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
    }

    @Test(priority = 11)
    public void testVerifyGetPlanDetailsWithoutEffectiveDate() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlanNoDate").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("MedicarePlanID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlanNoDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("MedicarePlanID")) +
                dataProp.getProperty("MedicarePlansACME2020_GetPlanNoDateData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test verify get plan details without effective date response body :- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getInt("PlanYear"), commonDateTimeMethods.currentYear());
        Assert.assertFalse(response.jsonPath().getString("PlanType").isEmpty());
    }

    @Test(priority = 12)
    public void testVerifyGetPlanDetailsWithoutEffectiveDateWithInvalidSessionId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlanNoDate").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlanID")) + dataProp.getProperty("MedicarePlansACME2020_GetPlanNoDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlanID")) +
                dataProp.getProperty("MedicarePlansACME2020_GetPlanNoDateData");
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
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlanNoDate").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlanID"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlanNoDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlanID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get plan details without effective date response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 14)
    public void testVerifyGetPlanDetailsWithoutEffectiveDateWithInvalidPlanId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetPlanNoDate").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("INVALID_TEXT"))
                + dataProp.getProperty("MedicarePlansACME2020_GetPlanNoDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("INVALID_TEXT"));
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
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint,medicarePlansACME2020SessionId, dataProp.getProperty("MedicarePlanID")),
                fileReader.getTestJsonFile(testEnrollmentSettingsData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("MedicarePlanID"));
        requestBody = fileReader.getTestJsonFile(testEnrollmentSettingsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertNotNull(response.jsonPath().getString("Url"));
        Assert.assertFalse(response.jsonPath().getString("EnrollActionType").isEmpty());
    }

    @Test(priority = 17)
    public void testVerifyGetEnrollUrlWithInvalidSessionId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlanID")),
                fileReader.getTestJsonFile(testEnrollmentSettingsData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("INVALID_TEXT"), dataProp.getProperty("MedicarePlanID"));
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

    @Test(priority = 18)
    public void testVerifyGetEnrollUrlWithBlankSessionId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlanID")),
                fileReader.getTestJsonFile(testEnrollmentSettingsData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, dataProp.getProperty("BLANK_SESSION_ID"), dataProp.getProperty("MedicarePlanID"));
        requestBody = fileReader.getTestJsonFile(testEnrollmentSettingsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test get enroll Url response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 19)
    public void testVerifyGetEnrollUrlWithInvalidPlanId() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("INVALID_TEXT")),
                fileReader.getTestJsonFile(testEnrollmentSettingsData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("INVALID_TEXT"));
        requestBody = fileReader.getTestJsonFile(testEnrollmentSettingsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test get enroll Url response when invalid planId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), PlanCompareErrorMessages.PLAN_ID_NOT_IN_VALID_FORMAT.value);
    }

    @Test(priority = 20)
    public void testVerifyGetEnrollUrlWithEmptyBody() {
        medicarePlansACME2020Endpoint = prop.getProperty("MedicarePlansACME2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("INVALID_TEXT")),
                CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(medicarePlansACME2020Endpoint, medicarePlansACME2020SessionId, dataProp.getProperty("INVALID_TEXT"));
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

    private String getMedicarePlansACME2020SessionId() {
        medicarePlansACME2020SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return medicarePlansACME2020SessionId;
    }
}