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

public class GetPlansForSessionQuoterOnlyBrokerSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    private String createSessionData;
    private String getSessionPlansBrokerSpecId;
    private String getSessionPlansBrokerSpecEndPoint;
    private String postSessionPlansBrokerSpecEndPoint;
    private String invalidSessionIdResponse;
    private String invalidSessionIdErrorCode;
    private String forbiddenPlanIdErrorCode;
    private String enrollForCaForbiddenPlanType1DataPath, enrollForCaForbiddenPlanType2DataPath, requestUrl, requestBody;

    @Test(priority = 1)
    public void testCheckLimitedPlanTypesInCaForBrokerCase() {
        commonMethods.usePropertyFileData("PC-API");
        getSessionPlansBrokerSpecEndPoint = prop.getProperty("PlansQuoterOnly_BrokerSpec").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")),  prop.getProperty("sessionId-Endpoint") + String.format(getSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId())
                + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData"));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId)
                + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testCheckLimitedPlanTypesInCaForBrokerCaseInvalidSession() {
        getSessionPlansBrokerSpecEndPoint = prop.getProperty("PlansQuoterOnly_BrokerSpec").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData"));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 3)
    public void testCheckLimitedPlanTypesInCaForBrokerCaseBlankSession() {
        getSessionPlansBrokerSpecEndPoint = prop.getProperty("PlansQuoterOnly_BrokerSpec").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData"));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("PlansQuoterOnly_BrokerSpecData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 4)
    public void testEnrollForCaForbiddenPlanType1() {
        enrollForCaForbiddenPlanType1DataPath = commonMethods.getTestDataPath() + "enrollmentSettings.xml";
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_1"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId),
                fileReader.getTestJsonFile(enrollForCaForbiddenPlanType1DataPath));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId);
        requestBody = fileReader.getTestJsonFile(enrollForCaForbiddenPlanType1DataPath);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test enroll for CA forbidden plan type 1 response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        int internalPlanID = response.jsonPath().get("InternalPlanID");
        assertNotNull(internalPlanID);
        String url = response.jsonPath().get("Url");
        assertNotNull(url);
    }

    @Test(priority = 5)
    public void testEnrollForCaForbiddenPlanType1InvalidSession() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_1"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value),
                fileReader.getTestJsonFile(enrollForCaForbiddenPlanType1DataPath));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(enrollForCaForbiddenPlanType1DataPath);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 6)
    public void testEnrollForCaForbiddenPlanType1BlankSession() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_1"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(enrollForCaForbiddenPlanType1DataPath));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(enrollForCaForbiddenPlanType1DataPath);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 7)
    public void testEnrollForCaForbiddenPlanType2() {
        enrollForCaForbiddenPlanType2DataPath = commonMethods.getTestDataPath() + "enrollmentSettings.xml";
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_2"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId),
                fileReader.getTestJsonFile(enrollForCaForbiddenPlanType2DataPath));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, getSessionPlansBrokerSpecId);
        requestBody = fileReader.getTestJsonFile(enrollForCaForbiddenPlanType2DataPath);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        int internalPlanID = response.jsonPath().get("InternalPlanID");
        assertNotNull(internalPlanID);
        String url = response.jsonPath().get("Url");
        assertNotNull(url);
    }

    @Test(priority = 8)
    public void testEnrollForCaForbiddenPlanType2InvalidSession() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_2"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value),
                fileReader.getTestJsonFile(enrollForCaForbiddenPlanType2DataPath));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(enrollForCaForbiddenPlanType2DataPath);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 9)
    public void testEnrollForCaForbiddenPlanType2BlankSession() {
        postSessionPlansBrokerSpecEndPoint = prop.getProperty("ForbiddenPlanType_EnrollID").replace("{SessionID}", "%s")
                .replace("{PlanId}", dataProp.getProperty("ForbiddenPlanType_EnrollID_2"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(enrollForCaForbiddenPlanType2DataPath));
        requestUrl = prop.getProperty("sessionId-Endpoint") + String.format(postSessionPlansBrokerSpecEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(enrollForCaForbiddenPlanType2DataPath);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    private String getSessionPlansBrokerSpecId() {
        createSessionData = commonMethods.getTestDataPath() + "createSessionMinimalBrokerTestUser12793Data.json";
        getSessionPlansBrokerSpecId = sessionIdGeneration.getSessionId(tokens.get("host"), "[ " + fileReader.getTestJsonFile(createSessionData) + " ]");
        return getSessionPlansBrokerSpecId;
    }
}