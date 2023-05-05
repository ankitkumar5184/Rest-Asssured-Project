package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.UserNamePasswords;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GetPlansForSessionBrokerSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    private String getPlansForSessionBrokerSessionId;
    private String sessionFroBrokerSessionID;
    private String requestUrl, requestBody;

    @Test(priority = 1)
    public void UpdateSessionTestViaAddBLUserNameUpdateBLUserNameRemoveBLUserName() {
        commonMethods.usePropertyFileData("PC-API");

        // Get created session
        String getSessionEndpoint = prop.getProperty("GetPlansForSessionBroker_GetSession").replace("{SessionID}", "%s");
        Response responseGetSession = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId()));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker get session response:- " + responseGetSession.asPrettyString());
        }
        assertEquals(responseGetSession.getStatusCode(), 200);

        // Update session by adding TestUser45
        String updateSessionMinimalBrokerTestUser45Data = commonMethods.getTestDataPath() + "updateSessionMinimalBrokerTestUser45Data.json";
        requestBody = fileReader.getTestJsonFile(updateSessionMinimalBrokerTestUser45Data);
        Response responseUpdateSessionByAddingTestUser45 = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId), requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test GetPlansForSessionBroker update session by adding TestUser45 response:- " + responseUpdateSessionByAddingTestUser45.asPrettyString());
        }
        assertEquals(responseUpdateSessionByAddingTestUser45.getStatusCode(), 200);

        // Get updated session TestUser45
        Response responseGetUpdatedSessionTestUser45 = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker get updated session after adding TestUser45 response:- " + responseGetUpdatedSessionTestUser45.asPrettyString());
        }
        assertEquals(responseGetUpdatedSessionTestUser45.getStatusCode(), 200);
        assertEquals(responseGetUpdatedSessionTestUser45.jsonPath().get("Profile.FirstName"), UserNamePasswords.DRXTEST123_TEXT.value);
        assertEquals(responseGetUpdatedSessionTestUser45.jsonPath().get("Profile.LastName"), UserNamePasswords.DRXTEST123_TEXT.value);

        // Update session by adding TestUser12793
        String updateSessionMinimalBrokerTestUser12793Data = commonMethods.getTestDataPath() + "createSessionMinimalBrokerTestUser12793DataBLUuser.json";
        requestBody = fileReader.getTestJsonFile(updateSessionMinimalBrokerTestUser12793Data);
        Response responseUpdateSessionByAddingTestUser12793 = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),"JSON",
                prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId), requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test GetPlansForSessionBroker update session by adding TestUser12793 response:- " + responseUpdateSessionByAddingTestUser12793.asPrettyString());
        }
        assertEquals(responseUpdateSessionByAddingTestUser12793.getStatusCode(), 200);

        // Get updated session TestUser12793
        Response responseGetUpdatedSessionTestUser12793 = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionEndpoint, getPlansForSessionBrokerSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker get updated session after adding TestUser12793 response:- " + responseGetUpdatedSessionTestUser12793.asPrettyString());
        }
        assertEquals(responseGetUpdatedSessionTestUser12793.getStatusCode(), 200);
        assertNotNull(responseGetUpdatedSessionTestUser12793.jsonPath().get("Profile.FirstName"));
        assertNotNull(responseGetUpdatedSessionTestUser12793.jsonPath().get("Profile.LastName"));
    }

    @Test(priority = 2)
    public void testVerifyCheckPlanTypesInCaForNoBrokerCase() {
        String checkPlanTypesInCaForNoBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCa").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(checkPlanTypesInCaForNoBrokerCaseEndpoint, getPlansForSessionBrokerSessionId) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkPlanTypesInCaForNoBrokerCaseEndpoint, getPlansForSessionBrokerSessionId) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check plan types in CA for no broker case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void testVerifyCheckPlanTypesInCaForNoBrokerCaseWithInvalidSessionId() {
        String checkPlanTypesInCaForNoBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCa").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(checkPlanTypesInCaForNoBrokerCaseEndpoint, GeneralErrorMessages.INVALID_TEXT.value) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkPlanTypesInCaForNoBrokerCaseEndpoint, GeneralErrorMessages.INVALID_TEXT.value) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check plan types in CA for no broker case with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 4)
    public void testVerifyCheckPlanTypesInCaForNoBrokerCaseWithBlankSessionId() {
        String checkPlanTypesInCaForNoBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCa").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(checkPlanTypesInCaForNoBrokerCaseEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkPlanTypesInCaForNoBrokerCaseEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check plan types in CA for no broker case with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 5)
    public void testVerifyCheckLimitedPlanTypesInCaForBrokerCase() {
        String CheckLimitedPlanTypesInCaForBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCa").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(CheckLimitedPlanTypesInCaForBrokerCaseEndpoint, getPlansForSessionBrokerSessionId) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(CheckLimitedPlanTypesInCaForBrokerCaseEndpoint, getPlansForSessionBrokerSessionId) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check limited plan types in CA for broker case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testVerifyCheckLimitedPlanTypesInCaForBrokerCaseWithInvalidSessionId() {
        String CheckLimitedPlanTypesInCaForBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCa").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(CheckLimitedPlanTypesInCaForBrokerCaseEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(CheckLimitedPlanTypesInCaForBrokerCaseEndpoint, GeneralErrorMessages.INVALID_TEXT.value) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check limited plan types in CA for broker case with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 7)
    public void testVerifyCheckLimitedPlanTypesInCaForBrokerCaseWithBlankSessionId() {
        String CheckLimitedPlanTypesInCaForBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCa").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(CheckLimitedPlanTypesInCaForBrokerCaseEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(CheckLimitedPlanTypesInCaForBrokerCaseEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInCaData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check limited plan types in CA for broker case with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 8)
    public void testVerifyCheckPlanTypesInAzForNoBrokerCase() {
        String checkPlanTypesInAzForNoBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAz").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(checkPlanTypesInAzForNoBrokerCaseEndpoint, getPlansForSessionBrokerSessionId) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkPlanTypesInAzForNoBrokerCaseEndpoint, getPlansForSessionBrokerSessionId) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 9)
    public void testVerifyCheckPlanTypesInAzForNoBrokerCaseWithInvalidSessionId() {
        String checkPlanTypesInAzForNoBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAz").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(checkPlanTypesInAzForNoBrokerCaseEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkPlanTypesInAzForNoBrokerCaseEndpoint, GeneralErrorMessages.INVALID_TEXT.value) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check plan types in AZ for no broker case with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 10)
    public void testVerifyCheckPlanTypesInAzForNoBrokerCaseWithBlankSessionId() {
        String checkPlanTypesInAzForNoBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAz").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(checkPlanTypesInAzForNoBrokerCaseEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkPlanTypesInAzForNoBrokerCaseEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check plan types in AZ for no broker case with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 11)
    public void testVerifyCheckLimitedPlanTypesInAzForBrokerCase() {
        String checkLimitedPlanTypesInAzForBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAz").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(checkLimitedPlanTypesInAzForBrokerCaseEndpoint, getPlansForSessionBrokerSessionId) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkLimitedPlanTypesInAzForBrokerCaseEndpoint, getPlansForSessionBrokerSessionId) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 12)
    public void testVerifyCheckLimitedPlanTypesInAzForBrokerCaseWithInvalidSessionId() {
        String checkLimitedPlanTypesInAzForBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAz").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(checkLimitedPlanTypesInAzForBrokerCaseEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkLimitedPlanTypesInAzForBrokerCaseEndpoint, GeneralErrorMessages.INVALID_TEXT.value) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check limited plan types in AZ for broker case with invalid SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 13)
    public void testVerifyCheckLimitedPlanTypesInAzForBrokerCaseWithBlankSessionId() {
        String checkLimitedPlanTypesInAzForBrokerCaseEndpoint = prop.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAz").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(checkLimitedPlanTypesInAzForBrokerCaseEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(checkLimitedPlanTypesInAzForBrokerCaseEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("GetPlansForSessionBroker_CheckPlanTypesInAzData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test GetPlansForSessionBroker check limited plan types in AZ for broker case with blank SessionID response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    private String getSessionForBroker() {
        String postCreateSessionData = commonMethods.getTestDataPath() + "createSessionMinimalBrokerTestUser12793Data.json";
        sessionFroBrokerSessionID = sessionIdGeneration.getSessionId(tokens.get("host"), "[ " + fileReader.getTestJsonFile(postCreateSessionData) + " ]");
        return sessionFroBrokerSessionID;
    }

    private String getPlansForSessionBrokerSessionId() {
        getPlansForSessionBrokerSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return getPlansForSessionBrokerSessionId;
    }
}