package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.*;

public class PlansSpecPharmacyDetailsTest {
    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    CommonMethods commonMethods = new CommonMethods();
    FileReader fileReader = new FileReader();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();

    String createSessionData, createSessionPharmacyDetailsNoPharmacyData;
    protected String plansSpecPharmacyDetailsSessionId;
    protected String plansSpecPharmacyDetailsEndpoint;
    protected String requestURL;

    @Test(priority = 1)
    public void testVerifyCreateSessionWithPharmacyData() {
        commonMethods.usePropertyFileData("PC-API");
        createSessionData = commonMethods.getTestDataPath() + "createSessionPharmacyDetails.json";
        plansSpecPharmacyDetailsEndpoint = prop.getProperty("PlansSpecPharmacyDetails_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, getPlansSpecPharmacyDetailsSessionId(createSessionData)));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, plansSpecPharmacyDetailsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Create session with pharmacy data response body :- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("Pharmacies[0].PharmacyID"), dataProp.getProperty("PharmaciesFirst_PharmacyID"));
        Assert.assertTrue(response.jsonPath().getInt("Pharmacies[0].PharmacyRecordID") > 0);
        Assert.assertNotNull(response.jsonPath().getString("Pharmacies[0].Name"));
        Assert.assertNotNull(response.jsonPath().getString("Pharmacies[0].City"));
    }

    @Test(priority = 2)
    public void testVerifyCreateSessionWithPharmacyDataWithInvalidSessionId() {
        plansSpecPharmacyDetailsEndpoint = prop.getProperty("PlansSpecPharmacyDetails_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, dataProp.getProperty("INVALID_TEXT")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, dataProp.getProperty("INVALID_TEXT"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Create session with pharmacy data response with invalid sessionId response body :- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        Assert.assertEquals(response.jsonPath().getString("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyCreateSessionWithPharmacyDataWithBlankSessionId() {
        plansSpecPharmacyDetailsEndpoint = prop.getProperty("PlansSpecPharmacyDetails_GetSessionRequest").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, dataProp.getProperty("BLANK_SESSION_ID")));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, dataProp.getProperty("BLANK_SESSION_ID"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Create session with pharmacy data response with empty sessionId response body :- " + response.asPrettyString());
        }
        Assert.assertEquals(response.getStatusCode(), 405);
        Assert.assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_GET.value);
    }

    @Test(priority = 4)
    public void testVerifyCreateSessionWithNoPharmacyData() {
        // Get session Id
        createSessionPharmacyDetailsNoPharmacyData = commonMethods.getTestDataPath() + "createSessionPharmacyDetailsNoPharmacy.json";
        Response responseWithNoPharmacyData = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, getPlansSpecPharmacyDetailsSessionId(createSessionPharmacyDetailsNoPharmacyData)));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansSpecPharmacyDetailsEndpoint, plansSpecPharmacyDetailsSessionId);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Create session with no pharmacy data response body :- " + responseWithNoPharmacyData.asPrettyString());
        }
        Assert.assertEquals(responseWithNoPharmacyData.getStatusCode(), 200);
        Assert.assertNull(responseWithNoPharmacyData.jsonPath().getString("[0].Pharmacies"));

        // Get created session
        Response responseGetSession = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + plansSpecPharmacyDetailsSessionId);
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + plansSpecPharmacyDetailsSessionId;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test Get Create session response body :- " + responseGetSession.asPrettyString());
        }
        Assert.assertEquals(responseGetSession.getStatusCode(), 200);
        Assert.assertNull(responseGetSession.jsonPath().getString("[0].Pharmacies"));
    }

    private String getPlansSpecPharmacyDetailsSessionId(String fileName) {
        plansSpecPharmacyDetailsSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(fileName));
        ExtentLogger.pass("PlansSpec pharmacy details sessionId:- " + plansSpecPharmacyDetailsSessionId);
        return plansSpecPharmacyDetailsSessionId;
    }
}