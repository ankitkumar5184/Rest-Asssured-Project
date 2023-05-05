package api.tests.test_suite.plancompare;

import api.base.core.DeleteRequest;
import api.base.core.PostRequest;
import api.base.core.PutRequest;
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

public class ProvidersTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    PutRequest putRequest = new PutRequest();
    DeleteRequest deleteRequest = new DeleteRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    PostmanSessionsScenariosTest postmanSessionsScenariosTest = new PostmanSessionsScenariosTest();
    String testRemoveProvidersFromSessionData;
    private String providersSessionId;
    private String requestURL, requestBody;

    @Test(priority = 1)
    public void testVerifyAddProvidersToSession() {
        commonMethods.usePropertyFileData("PC-API");
        String addProvidersToSessionData = commonMethods.getTestDataPath() + "addProvidersToSessionData.json";
        String addProvidersToSessionEndpoint = prop.getProperty("Providers_AddProvidersToSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint")
                        + String.format(addProvidersToSessionEndpoint, getProvidersSessionId()), fileReader.getTestJsonFile(addProvidersToSessionData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addProvidersToSessionEndpoint, providersSessionId);
        requestBody = fileReader.getTestJsonFile(addProvidersToSessionData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test add providers to session response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyAddProvidersToSessionWithInvalidSessionId() {
        String addProvidersToSessionData = commonMethods.getTestDataPath() + "addProvidersToSessionData.json";
        String addProvidersToSessionEndpoint = prop.getProperty("Providers_AddProvidersToSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint")
                        + String.format(addProvidersToSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value),
                fileReader.getTestJsonFile(addProvidersToSessionData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addProvidersToSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = fileReader.getTestJsonFile(addProvidersToSessionData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test add providers to session with invalid SessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 3)
    public void testVerifyAddProvidersToSessionWithBlankSessionId() {
        String addProvidersToSessionData = commonMethods.getTestDataPath() + "addProvidersToSessionData.json";
        String addProvidersToSessionEndpoint = prop.getProperty("Providers_AddProvidersToSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint")
                        + String.format(addProvidersToSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(addProvidersToSessionData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addProvidersToSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(addProvidersToSessionData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test add providers to session with blank SessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 4)
    public void testVerifyAddProvidersToSessionWithBlankBodyData() {
        String addProvidersToSessionEndpoint = prop.getProperty("Providers_AddProvidersToSession").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint")
                + String.format(addProvidersToSessionEndpoint, providersSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(addProvidersToSessionEndpoint, providersSessionId);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test add providers to session with blank BodyData response body :- " + response.asPrettyString());
        }
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.providerIdentifiersList[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 5)
    public void testVerifyReplaceProvidersInSession() {
        String replaceProvidersInSessionData = commonMethods.getTestDataPath() + "replaceProvidersInSessionData.json";
        String replaceProvidersInSessionEndpoint = prop.getProperty("Providers_ReplaceProvidersInSession").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(replaceProvidersInSessionEndpoint, providersSessionId), fileReader.getTestJsonFile(replaceProvidersInSessionData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(replaceProvidersInSessionEndpoint, providersSessionId);
        requestBody = fileReader.getTestJsonFile(replaceProvidersInSessionData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test replace providers in session response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testVerifyReplaceProvidersInSessionWithInvalidSessionId() {
        String replaceProvidersInSessionData = commonMethods.getTestDataPath() + "replaceProvidersInSessionData.json";
        String replaceProvidersInSessionEndpoint = prop.getProperty("Providers_ReplaceProvidersInSession").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                        + String.format(replaceProvidersInSessionEndpoint, GeneralErrorMessages.INVALID_TEXT.value),
                fileReader.getTestJsonFile(replaceProvidersInSessionData));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Test replace providers in session with invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 7)
    public void testVerifyReplaceProvidersInSessionWithBlankSessionId() {
        String replaceProvidersInSessionData = commonMethods.getTestDataPath() + "replaceProvidersInSessionData.json";
        String replaceProvidersInSessionEndpoint = prop.getProperty("Providers_ReplaceProvidersInSession").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                        + String.format(replaceProvidersInSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                fileReader.getTestJsonFile(replaceProvidersInSessionData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(replaceProvidersInSessionEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = fileReader.getTestJsonFile(replaceProvidersInSessionData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test replace providers in session with blank SessionId response body :- " + response.asPrettyString());
        }
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 8)
    public void testVerifyReplaceProvidersInSessionWithBlankBodyData() {
        String replaceProvidersInSessionEndpoint = prop.getProperty("Providers_ReplaceProvidersInSession").replace("{SessionID}", "%s");
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(replaceProvidersInSessionEndpoint, providersSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(replaceProvidersInSessionEndpoint, providersSessionId);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test replace providers in session with blank BodyData response body :- " + response.asPrettyString());
        }
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.providerIdentifiersList[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 9)
    public void testDeleteProvidersToSession() {
        String deleteProvidersFromSessionEndPoint = prop.getProperty("Providers_RemoveProvidersFromSession").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(deleteProvidersFromSessionEndPoint, getProvidersSessionId()), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(deleteProvidersFromSessionEndPoint, providersSessionId);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Delete Providers From session response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 10)
    public void testDeleteProvidersToSessionWithInvalidSessionId() {
        String deleteProvidersFromSessionEndPoint = prop.getProperty("Providers_RemoveProvidersFromSession").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                prop.getProperty("sessionId-Endpoint") + String.format(deleteProvidersFromSessionEndPoint, GeneralErrorMessages.INVALID_TEXT.value), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(deleteProvidersFromSessionEndPoint, GeneralErrorMessages.INVALID_TEXT.value);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Delete Providers From session with invalid session id response body :- " + response.asPrettyString());
        }
        String invalidSessionIdResponse = response.jsonPath().get("Message");
        String invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 11)
    public void testDeleteProvidersToSessionWithBlankSessionId() {
        String deleteProvidersFromSessionEndPoint = prop.getProperty("Providers_RemoveProvidersFromSession").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(deleteProvidersFromSessionEndPoint, dataProp.getProperty("BLANK_SESSION_ID")), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(deleteProvidersFromSessionEndPoint, dataProp.getProperty("BLANK_SESSION_ID"));
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Delete Providers From session with blank session id response body :- " + response.asPrettyString());
        }
        String blankSessionIdResponse = response.jsonPath().get("Message");
        assertEquals(blankSessionIdResponse, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_DELETE.value);
        assertEquals(response.getStatusCode(), 405);
    }

    @Test(priority = 11)
    public void testDeleteSingleProviderToSession() {
        testRemoveProvidersFromSessionData = commonMethods.getTestDataPath() + "removeProvidersFromSession.json";
        String deleteProvidersFromSessionEndPoint = prop.getProperty("Providers_RemoveSingleProviderFromSession").replace("{SessionID}", "%s").replace("{NPI}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(deleteProvidersFromSessionEndPoint, providersSessionId, postmanSessionsScenariosTest.getNpiNumber),
                fileReader.getTestJsonFile(testRemoveProvidersFromSessionData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(deleteProvidersFromSessionEndPoint, providersSessionId, postmanSessionsScenariosTest.getNpiNumber);
        requestBody = fileReader.getTestJsonFile(testRemoveProvidersFromSessionData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Delete Providers From session response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 12)
    public void testDeleteSingleProviderToSessionWithBlankSessionId() {
        String deleteProvidersFromSessionEndPoint = prop.getProperty("Providers_RemoveSingleProviderFromSession").replace("{SessionID}", "%s")
                .replace("{NPI}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(deleteProvidersFromSessionEndPoint, dataProp.getProperty("BLANK_SESSION_ID"), postmanSessionsScenariosTest.getNpiNumber), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(deleteProvidersFromSessionEndPoint, dataProp.getProperty("BLANK_SESSION_ID"), postmanSessionsScenariosTest.getNpiNumber);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Delete Providers From session with blank session id response body :- " + response.asPrettyString());
        }
        String blankSessionIdResponse = response.jsonPath().getString("Message");
        assertEquals(blankSessionIdResponse, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_DELETE.value);
        assertEquals(response.getStatusCode(), 405);
    }

    @Test(priority = 13)
    public void testDeleteSingleProviderToSessionWithInvalidSessionId() {
        String deleteProvidersFromSessionEndPoint = prop.getProperty("Providers_RemoveSingleProviderFromSession").replace("{SessionID}", "%s")
                .replace("{NPI}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(deleteProvidersFromSessionEndPoint, GeneralErrorMessages.INVALID_TEXT.value, postmanSessionsScenariosTest.getNpiNumber), CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(deleteProvidersFromSessionEndPoint, GeneralErrorMessages.INVALID_TEXT.value, postmanSessionsScenariosTest.getNpiNumber);
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Delete Providers From session with invalid session id response body :- " + response.asPrettyString());
        }
        String invalidSessionIdResponse = response.jsonPath().getString("Message");
        String invalidSessionIdErrorCode = response.jsonPath().getString("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    private String getProvidersSessionId() {
        String createSessionData = commonMethods.getTestDataPath() + "createSessionWithProviders.json";
        providersSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return providersSessionId;
    }
}