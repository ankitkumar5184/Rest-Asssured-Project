package api.tests.test_suite.plancompare;

import api.base.core.DeleteRequest;
import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.core.PutRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.FipsValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.ZipCodeValues;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;

public class DependentsSpecTest {

    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    PutRequest putRequest = new PutRequest();
    DeleteRequest deleteRequest = new DeleteRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();

    private String testAddDependentsData;
    protected String dependentsSessionId;
    protected String dependentsEndpoint, requestUrl, requestBody;

    @Test(priority = 1)
    public void testVerifyGetSessionRequest() {
        commonMethods.usePropertyFileData("PC-API");
        dependentsEndpoint = prop.getProperty("Dependent_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, getDependentsSpecSessionId()));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session request response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testVerifyGetSessionRequestWithInvalidSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT"));
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
        dependentsEndpoint = prop.getProperty("Dependent_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get session request response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyAddDependents() {
        testAddDependentsData = commonMethods.getTestDataPath() + "dependents.json";
        dependentsEndpoint = prop.getProperty("Dependent_AddDependents").replace("{SessionID}", "%s");
        requestBody = fileReader.getTestJsonFile(testAddDependentsData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test add dependents response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("[0].Status"), CommonValues.CREATED_TEXT.value);
    }

    @Test(priority = 5)
    public void testVerifyAddDependentsWithInvalidSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_AddDependents").replace("{SessionID}", "%s");
        requestBody = fileReader.getTestJsonFile(testAddDependentsData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test add dependents response when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.SESSION_COULD_NOT_BE_FOUND.value);
    }

    @Test(priority = 6)
    public void testVerifyAddDependentsWithBlankSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_AddDependents").replace("{SessionID}", "%s");
        requestBody = fileReader.getTestJsonFile(testAddDependentsData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test add dependents response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 7)
    public void testVerifyAddDependentsWithEmptyBody() {
        dependentsEndpoint = prop.getProperty("Dependent_AddDependents").replace("{SessionID}", "%s");
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test add dependents response when empty body:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.dependents[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 8)
    public void testVerifyGetDependents() {
        dependentsEndpoint = prop.getProperty("Dependent_GetDependents").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get dependents response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("[0].Zip"), ZipCodeValues.Zip_92129.value);
        Assert.assertEquals(response.jsonPath().getString("[0].Fips"), FipsValues.Fips_06073.value);
    }

    @Test(priority = 9)
    public void testVerifyGetDependentsWithInvalidSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_GetDependents").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get dependents response when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.SESSION_COULD_NOT_BE_FOUND.value);
    }

    @Test(priority = 10)
    public void testVerifyGetDependentsWithBlankSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_GetDependents").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") +  String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test get dependents response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 11)
    public void testVerifyUpdateDependents() {
        dependentsEndpoint = prop.getProperty("Dependent_UpdateDependents").replace("{SessionID}", "%s");
        requestBody = fileReader.getTestJsonFile(testAddDependentsData);
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test update dependents response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertFalse(response.jsonPath().getString("[0].DependentID").isEmpty());
        Assert.assertEquals(response.jsonPath().getString("[0].Status"), CommonValues.CREATED_TEXT.value);
    }

    @Test(priority = 12)
    public void testVerifyUpdateDependentsWithInvalidSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_UpdateDependents").replace("{SessionID}", "%s");
        requestBody = fileReader.getTestJsonFile(testAddDependentsData);
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test update dependents response when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.SESSION_COULD_NOT_BE_FOUND.value);
    }

    @Test(priority = 13)
    public void testVerifyUpdateDependentsWithBlankSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_UpdateDependents").replace("{SessionID}", "%s");
        requestBody = fileReader.getTestJsonFile(testAddDependentsData);
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test update dependents response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.session[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 14)
    public void testVerifyUpdateDependentsWithEmptyBody() {
        dependentsEndpoint = prop.getProperty("Dependent_UpdateDependents").replace("{SessionID}", "%s");
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test update dependents response when empty body:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        Assert.assertEquals(response.jsonPath().getString("ModelState.dependents[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    @Test(priority = 15)
    public void testVerifyRemoveDependents() {
        dependentsEndpoint = prop.getProperty("Dependent_RemoveDependents").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")),prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId), CommonValues.BLANK_BODY_REQUEST.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dependentsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Test remove dependents response:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 16)
    public void testVerifyRemoveDependentsWithInvalidSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_RemoveDependents").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT")), CommonValues.BLANK_BODY_REQUEST.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Test remove dependents response when invalid sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.SESSION_COULD_NOT_BE_FOUND.value);
    }

    @Test(priority = 17)
    public void testVerifyRemoveDependentsWithBlankSessionId() {
        dependentsEndpoint = prop.getProperty("Dependent_RemoveDependents").replace("{SessionID}", "%s");
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")), CommonValues.BLANK_BODY_REQUEST.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(dependentsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Test remove dependents response when empty sessionId:- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_DELETE.value);
    }

    private String getDependentsSpecSessionId() {
        String createSessionData = commonMethods.getTestDataPath() + "createSessionJsonOriginal.json";
        dependentsSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return dependentsSessionId;
    }
}