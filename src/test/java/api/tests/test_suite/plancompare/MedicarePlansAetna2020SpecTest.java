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
import static org.testng.Assert.assertTrue;

public class MedicarePlansAetna2020SpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    private String medicarePlansAetna2020SessionId;
    private String ID, requestUrl, requestBody;

    @Test(priority = 1)
    public void testVerifyGetSession() {
        commonMethods.usePropertyFileData("PC-API");
        String getSessionEndpoint = prop.getProperty("MedicarePlansAetna2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, getMedicarePlansAetna2020SessionId()));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, medicarePlansAetna2020SessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get session response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionWithInvalidSessionId() {
        String getSessionEndpoint = prop.getProperty("MedicarePlansAetna2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get session with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyGetSessionWithBlankSessionId() {
        String getSessionEndpoint = prop.getProperty("MedicarePlansAetna2020_GetSession").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get session with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 405);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyGetPlans() {
        String getPlansEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, medicarePlansAetna2020SessionId) + dataProp.getProperty("MedicarePlansAetna2020_GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, medicarePlansAetna2020SessionId) + dataProp.getProperty("MedicarePlansAetna2020_GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        ID = response.jsonPath().get("MedicarePlans[0].ID");
        assertTrue(ID.length() > 2);
        String planID = response.jsonPath().get("MedicarePlans[0].PlanID");
        assertTrue(planID.length() > 1);
        String planName = response.jsonPath().get("MedicarePlans[0].PlanName");
        assertTrue(planName.length() > 1);
        String carrierName = response.jsonPath().get("MedicarePlans[0].CarrierName");
        assertTrue(carrierName.length() > 2);
    }

    @Test(priority = 5)
    public void testVerifyGetPlansWithInvalidSessionId() {
        String getPlansEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedicarePlansAetna2020_GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedicarePlansAetna2020_GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get plans with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyGetPlansWithBlankSessionId() {
        String getPlansEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))  + dataProp.getProperty("MedicarePlansAetna2020_GetPlansData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))  + dataProp.getProperty("MedicarePlansAetna2020_GetPlansData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get plans with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testVerifyGetPlanDetailsForMedicarePlan() {
        String getPlansEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlan").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansEndpoint, medicarePlansAetna2020SessionId) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlanData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, medicarePlansAetna2020SessionId) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlanData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("ID"), ID);
    }

    @Test(priority = 8)
    public void testVerifyGetPlanDetailsForMedicarePlanWithInvalidSessionId() {
        String getPlansEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlan").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlanData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlanData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get plan details for medicare plan with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyGetPlanDetailsForMedicarePlanWithBlankSessionId() {
        String getPlansEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlan").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlanData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlansEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsForMedicarePlanData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get plan details for medicare plan with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 10)
    public void testVerifyGetPlanDetailsWithoutEffectiveDate() {
        String getPlanDetailsWithoutEffectiveDateEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDate").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsWithoutEffectiveDateEndpoint, medicarePlansAetna2020SessionId) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlanDetailsWithoutEffectiveDateEndpoint, medicarePlansAetna2020SessionId) +
                dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDateData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("ID"), ID);
    }

    @Test(priority = 11)
    public void testVerifyGetPlanDetailsWithoutEffectiveDateWithInvalidSessionId() {
        String getPlanDetailsWithoutEffectiveDateEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDate").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsWithoutEffectiveDateEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlanDetailsWithoutEffectiveDateEndpoint, GeneralErrorMessages.INVALID_TEXT.value) +
                dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDateData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get plan details without effective date with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 12)
    public void testVerifyGetPlanDetailsWithoutEffectiveDateWithBlankSessionId() {
        String getPlanDetailsWithoutEffectiveDateEndpoint = prop.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDate").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getPlanDetailsWithoutEffectiveDateEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDateData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getPlanDetailsWithoutEffectiveDateEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("MedicarePlansAetna2020_GetPlanDetailsWithoutEffectiveDateData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get plan details without effective date with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 13)
    public void testVerifyGetEnrollUrl() {
        String getEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingsData.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedicarePlansAetna2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollUrlEndpoint, medicarePlansAetna2020SessionId), fileReader.getTestJsonFile(getEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, medicarePlansAetna2020SessionId);
        requestBody = fileReader.getTestJsonFile(getEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get enroll url response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String url = response.jsonPath().get("Url");
        String parameters = response.jsonPath().get("Parameters");
        assertTrue(url instanceof String);
        assertTrue(parameters instanceof String);
        assertTrue(url.length() > 5);
        assertTrue(parameters.length() > 5);
    }

    @Test(priority = 14)
    public void testVerifyGetEnrollUrlWithInvalidSessionId() {
        String getEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingsData.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedicarePlansAetna2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollUrlEndpoint, GeneralErrorMessages.INVALID_TEXT.value), fileReader.getTestJsonFile(getEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(getEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get enroll url with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 15)
    public void testVerifyGetEnrollUrlWithBlankSessionId() {
        String getEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingsData.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedicarePlansAetna2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                        + String.format(getEnrollUrlEndpoint, dataProp.getProperty("BLANK_SESSION_ID")), fileReader.getTestJsonFile(getEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(getEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get enroll url with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 16)
    public void testVerifyGetEnrollUrlWithBlankBodyData() {
        String getEnrollUrlEndpoint = prop.getProperty("MedicarePlansAetna2020_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanId}", ID);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint")
                + String.format(getEnrollUrlEndpoint, medicarePlansAetna2020SessionId), CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, medicarePlansAetna2020SessionId) ;
        requestBody = CommonValues.BLANK_BODY_REQUEST_WITH_XML.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedicarePlansAetna2020 get enroll url with blank body data response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.enrollmentSettings[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    private String getMedicarePlansAetna2020SessionId() {
        medicarePlansAetna2020SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return medicarePlansAetna2020SessionId;
    }
}