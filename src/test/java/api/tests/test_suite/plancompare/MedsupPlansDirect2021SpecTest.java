package api.tests.test_suite.plancompare;

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

public class MedsupPlansDirect2021SpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    private String medsupPlansDirect2021SessionId, requestUrl, requestBody;

    @Test(priority = 1)
    public void testVerifyGetEnrollUrl() {
        commonMethods.usePropertyFileData("PC-API");
        String GetEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingsNoBrokerData.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedsupPlansDirect2021_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanToEnroll}", dataProp.getProperty("MEDSUPPLANSDIRECT2021_PlanToEnroll"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollUrlEndpoint, getMedsupPlansDirect2021SessionId()) + dataProp.getProperty("MedsupPlansDirect2021_GetEnrollUrlData"), fileReader.getTestJsonFile(GetEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, medsupPlansDirect2021SessionId) + dataProp.getProperty("MedsupPlansDirect2021_GetEnrollUrlData");
        requestBody = fileReader.getTestJsonFile(GetEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedsupPlansDirect2021SpecTest get enroll url case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String url = response.jsonPath().get("Url");
        assertTrue(url instanceof String);
        assertTrue(url.length() > 5);
        String parameters = response.jsonPath().get("Parameters");
        assertTrue(parameters instanceof String);
        assertTrue(parameters.length() > 5);
    }

    @Test(priority = 2)
    public void testVerifyGetEnrollUrlWithInvalidSessionId() {
        String GetEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingsNoBrokerData.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedsupPlansDirect2021_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanToEnroll}", dataProp.getProperty("MEDSUPPLANSDIRECT2021_PlanToEnroll"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollUrlEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedsupPlansDirect2021_GetEnrollUrlData"), fileReader.getTestJsonFile(GetEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("MedsupPlansDirect2021_GetEnrollUrlData");
        requestBody = fileReader.getTestJsonFile(GetEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedsupPlansDirect2021SpecTest get enroll url with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyGetEnrollUrlWithBlankSessionId() {
        String GetEnrollUrlData = commonMethods.getTestDataPath() + "enrollmentSettingsNoBrokerData.xml";
        String getEnrollUrlEndpoint = prop.getProperty("MedsupPlansDirect2021_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanToEnroll}", dataProp.getProperty("MEDSUPPLANSDIRECT2021_PlanToEnroll"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollUrlEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedsupPlansDirect2021_GetEnrollUrlData"), fileReader.getTestJsonFile(GetEnrollUrlData));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("MedsupPlansDirect2021_GetEnrollUrlData");
        requestBody = fileReader.getTestJsonFile(GetEnrollUrlData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedsupPlansDirect2021SpecTest get enroll url with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 4)
    public void testVerifyGetEnrollUrlWithBlankBodyData() {
        String getEnrollUrlEndpoint = prop.getProperty("MedsupPlansDirect2021_GetEnrollUrl").replace("{SessionID}", "%s").replace("{PlanToEnroll}", dataProp.getProperty("MEDSUPPLANSDIRECT2021_PlanToEnroll"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                String.format(getEnrollUrlEndpoint, medsupPlansDirect2021SessionId) + dataProp.getProperty("MedsupPlansDirect2021_GetEnrollUrlData"), CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getEnrollUrlEndpoint, medsupPlansDirect2021SessionId) + dataProp.getProperty("MedsupPlansDirect2021_GetEnrollUrlData");
        requestBody = CommonValues.BLANK_BODY_REQUEST_WITH_XML.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Test MedsupPlansDirect2021SpecTest get enroll url with blank body data case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.enrollmentSettings[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }

    private String getMedsupPlansDirect2021SessionId() {
        medsupPlansDirect2021SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), CommonValues.BLANK_BODY_VALID_REQUEST.value);
        return medsupPlansDirect2021SessionId;
    }
}