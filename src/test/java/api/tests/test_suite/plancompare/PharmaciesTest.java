package api.tests.test_suite.plancompare;

import api.base.core.DeleteRequest;
import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.core.PutRequest;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
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

public class PharmaciesTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    PutRequest putRequest = new PutRequest();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    DeleteRequest deleteRequest = new DeleteRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    String createSessionData;
    private String emptyBodyData;
    private String pharmacySessionId;
    private String postPharmacyId;
    private int pharmacyRecordID;
    private String invalidSessionId;
    private String blankSessionId;
    private String invalidSessionIdErrorCode;
    private String blankSessionIdResponseError;
    private String blankSessionIdMessage;
    private String PharmacyNABPValue;
    private String blankBodyDataMessage;
    private String blankBodyDataErrorMessage;
    private String invalidSessionIdResponse;
    private String requestURL, requestBody;

    @Test(priority = 1)
    public void testAddSessionPharmacy() {
        commonMethods.usePropertyFileData("PC-API");
        String addSessionToPharmacyData = commonMethods.getTestDataPath() + "addSessionToPharmacy.json";
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(addSessionToPharmacyEndpoint, getSessionPharmacySessionId());
        requestBody = fileReader.getTestJsonFile(addSessionToPharmacyData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add session pharmacy response:- " + response.asPrettyString());
        }
        Boolean isMailOrder = Boolean.valueOf(CommonValues.FALSE_VALUE.value);
        String postPharmacyNABPValue = dataProp.getProperty("Post_PharmacyNABP");
        Boolean isMailOrderResponseValue = response.jsonPath().get("[0].IsMailOrder");
        String pharmacyNABPValue = response.jsonPath().get("[0].PharmacyNABP");
        postPharmacyId = response.jsonPath().get("[0].PharmacyID");
        assertEquals(isMailOrderResponseValue, isMailOrder);
        assertEquals(pharmacyNABPValue, postPharmacyNABPValue);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testAddSessionPharmacyWithInvalidSessionId() {
        String addSessionToPharmacyData = commonMethods.getTestDataPath() + "addSessionToPharmacy.json";
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        invalidSessionId = GeneralErrorMessages.INVALID_TEXT.value;
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(addSessionToPharmacyEndpoint, invalidSessionId);
        requestBody = fileReader.getTestJsonFile(addSessionToPharmacyData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session To Pharmacy With Invalid SessionId response:- " + response.asPrettyString());
        }
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 3)
    public void testAddSessionPharmacyWithBlankSessionId() {
        String addSessionToPharmacyData = commonMethods.getTestDataPath() + "addSessionToPharmacy.json";
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        blankSessionId = dataProp.getProperty("BLANK_SESSION_ID");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(addSessionToPharmacyEndpoint, blankSessionId);
        requestBody = fileReader.getTestJsonFile(addSessionToPharmacyData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session To Pharmacy With Blank SessionId response:- " + response.asPrettyString());
        }
        blankSessionIdMessage = response.jsonPath().get("Message");
        blankSessionIdResponseError = response.jsonPath().get("ModelState.session[0]");
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdResponseError, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 4)
    public void testAddSessionPharmacyWithBlankBodyData() {
        emptyBodyData = CommonValues.BLANK_BODY_REQUEST.value;
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(addSessionToPharmacyEndpoint, pharmacySessionId);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, emptyBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + emptyBodyData);
            ExtentLogger.pass("Add Session To Pharmacy With Blank BodyData response:- " + response.asPrettyString());
        }
        blankBodyDataMessage = response.jsonPath().get("Message");
        blankBodyDataErrorMessage = response.jsonPath().get("ModelState.pharmacies[0]");
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 5)
    public void testGetSessionPharmacies() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacies").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, pharmacySessionId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get session pharmacies response:- " + response.asPrettyString());
        }
        String getPharmacyId = response.jsonPath().get("[0].PharmacyID");
        String getPharmacyNABP = response.jsonPath().get("[0].PharmacyNABP");
        assertEquals(getPharmacyId, postPharmacyId);
        assertEquals(getPharmacyNABP, dataProp.getProperty("Post_PharmacyNABP"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testGetSessionPharmaciesWithInvalidSessionId() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacies").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, invalidSessionId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session Pharmacies With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 7)
    public void testGetSessionPharmaciesWithBlankSessionId() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacies").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, blankSessionId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session Pharmacies With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdResponseError, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 8)
    public void testPutPharmaciesSession() {
        String putPharmacyData = commonMethods.getTestDataPath() + "putPharmacySession.json";
        String putPharmaciesEndpoint = prop.getProperty("Pharmacies_PutPharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(putPharmaciesEndpoint, pharmacySessionId);
        requestBody = fileReader.getTestJsonFile(putPharmacyData);
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Put pharmacies to session response:- " + response.asPrettyString());
        }
        pharmacyRecordID = response.jsonPath().getInt("[0].PharmacyRecordID");
        String putPharmacyNABPValueResponse = response.jsonPath().get("[0].PharmacyNABP");
        Boolean getIsMailOrder = response.jsonPath().get("[0].IsMailOrder");
        PharmacyNABPValue = dataProp.getProperty("Put_PharmacyNABP");
        assertEquals(putPharmacyNABPValueResponse, PharmacyNABPValue);
        assertEquals(getIsMailOrder, Boolean.valueOf(CommonValues.FALSE_VALUE.value));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 9)
    public void testPutPharmaciesSessionWithInvalidSessionId() {
        String putPharmacyData = commonMethods.getTestDataPath() + "putPharmacySession.json";
        String putPharmaciesEndpoint = prop.getProperty("Pharmacies_PutPharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(putPharmaciesEndpoint, invalidSessionId);
        requestBody = fileReader.getTestJsonFile(putPharmacyData);
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Put Pharmacies To Session With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 10)
    public void testPutPharmaciesSessionWithBlankSessionId() {
        String putPharmacyData = commonMethods.getTestDataPath() + "putPharmacySession.json";
        String putPharmaciesEndpoint = prop.getProperty("Pharmacies_PutPharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(putPharmaciesEndpoint, blankSessionId);
        requestBody = fileReader.getTestJsonFile(putPharmacyData);
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Put Pharmacies To Session With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdResponseError, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 11)
    public void testPutPharmaciesSessionWithBlankBodyData() {
        String putPharmaciesEndpoint = prop.getProperty("Pharmacies_PutPharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(putPharmaciesEndpoint, pharmacySessionId);
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL, emptyBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + emptyBodyData);
            ExtentLogger.pass("Put Pharmacies To Session With Blank Body Data response:- " + response.asPrettyString());
        }
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 12)
    public void testGetSessionPharmacy() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacy").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, pharmacySessionId, pharmacyRecordID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get session Pharmacy response:- " + response.asPrettyString());
        }
        int getPharmacyRecordID = response.jsonPath().getInt("PharmacyRecordID");
        assertEquals(getPharmacyRecordID, pharmacyRecordID);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 13)
    public void testGetSessionPharmacyWithInvalidSessionId() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacy").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, invalidSessionId, pharmacyRecordID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get session Pharmacy With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 14)
    public void testGetSessionPharmacyWithBlankSessionId() {
        String getSessionPharmacyEndpoint = prop.getProperty("Pharmacies_GetSessionPharmacy").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionPharmacyEndpoint, blankSessionId, pharmacyRecordID);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get session Pharmacy With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 15)
    public void testPostPharmacySessionModify() {
        String postPharmacyToSessionModifyData = commonMethods.getTestDataPath() + "postPharmacyToSessionModify.json";
        String postPharmacyToSessionModifyEndpoint = prop.getProperty("Pharmacies_PostPharmacyToSessionModify").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(postPharmacyToSessionModifyEndpoint, pharmacySessionId);
        requestBody = fileReader.getTestJsonFile(postPharmacyToSessionModifyData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Pharmacy to session modify response:- " + response.asPrettyString());
        }
        String postPharmacyModifyNABP = response.jsonPath().get("[0].PharmacyNABP");
        assertEquals(postPharmacyModifyNABP, PharmacyNABPValue);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 16)
    public void testPostPharmacySessionModifyWithInvalidSessionId() {
        String postPharmacyToSessionModifyData = commonMethods.getTestDataPath() + "postPharmacyToSessionModify.json";
        String postPharmacyToSessionModifyEndpoint = prop.getProperty("Pharmacies_PostPharmacyToSessionModify").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(postPharmacyToSessionModifyEndpoint, invalidSessionId);
        requestBody = fileReader.getTestJsonFile(postPharmacyToSessionModifyData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Pharmacy to session modify With Invalid SessionId response:- " + response.asPrettyString());
        }
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 17)
    public void testPostPharmacySessionModifyWithBlankSessionId() {
        String postPharmacyToSessionModifyData = commonMethods.getTestDataPath() + "postPharmacyToSessionModify.json";
        String postPharmacyToSessionModifyEndpoint = prop.getProperty("Pharmacies_PostPharmacyToSessionModify").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(postPharmacyToSessionModifyEndpoint, blankSessionId);
        requestBody = fileReader.getTestJsonFile(postPharmacyToSessionModifyData);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, requestBody);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Pharmacy to session modify With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(blankSessionIdMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankSessionIdResponseError, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 18)
    public void testPostPharmacySessionModifyWithBlankBodyData() {
        String addSessionToPharmacyEndpoint = prop.getProperty("Pharmacies_AddPharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(addSessionToPharmacyEndpoint, pharmacySessionId);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", requestURL, emptyBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + emptyBodyData);
            ExtentLogger.pass("Post Pharmacy To Session Modify With Blank Body Data response:- " + response.asPrettyString());
        }
        assertEquals(blankBodyDataMessage, GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(blankBodyDataErrorMessage, GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 19)
    public void testDeletePharmacySession() {
        String deletePharmacyToSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmacyToSession").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(deletePharmacyToSessionEndpoint, pharmacySessionId, pharmacyRecordID);
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL, CommonValues.BLANK_BODY_REQUEST.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Delete pharmacy to session response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 20)
    public void testDeletePharmacySessionWithInvalidSessionId() {
        String deletePharmacyToSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmacyToSession").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(deletePharmacyToSessionEndpoint, invalidSessionId, pharmacyRecordID);
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL, CommonValues.BLANK_BODY_REQUEST.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Delete pharmacy to session response:- " + response.asPrettyString());
        }
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 21)
    public void testDeletePharmacySessionWithBlankSessionId() {
        String deletePharmacyToSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmacyToSession").replace("{SessionID}", "%s")
                .replace("PharmacyRecordID", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(deletePharmacyToSessionEndpoint, blankSessionId, pharmacyRecordID);
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                requestURL, CommonValues.BLANK_BODY_REQUEST.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Delete pharmacy to session response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 22)
    public void testDeletePharmaciesSession() {
        String deletePharmaciesTosSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(deletePharmaciesTosSessionEndpoint, pharmacySessionId);
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                requestURL, CommonValues.BLANK_BODY_REQUEST.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Delete pharmacies to session response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 23)
    public void testDeletePharmaciesSessionWithInvalidSessionId() {
        String deletePharmaciesTosSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(deletePharmaciesTosSessionEndpoint, invalidSessionId);
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL, CommonValues.BLANK_BODY_REQUEST.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Delete pharmacies to session response:- " + response.asPrettyString());
        }
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 24)
    public void testDeletePharmaciesSessionWithBlankSessionId() {
        String deletePharmaciesTosSessionEndpoint = prop.getProperty("Pharmacies_DeletePharmaciesToSession").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(deletePharmaciesTosSessionEndpoint, blankSessionId);
        Response response = deleteRequest.deleteMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL, CommonValues.BLANK_BODY_REQUEST.value);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Request Body is :- " + CommonValues.BLANK_BODY_REQUEST.value);
            ExtentLogger.pass("Delete pharmacies to session response:- " + response.asPrettyString());
        }
        String blankSessionIdMessageDeleteRequest = response.jsonPath().get("Message");
        assertEquals(blankSessionIdMessageDeleteRequest, GeneralErrorMessages.REQUEST_RESOURCE_UNSUPPORTED_DELETE.value);
        assertEquals(response.getStatusCode(), 405);
    }

    private String getSessionPharmacySessionId() {
        createSessionData = commonMethods.getTestDataPath() + "createSessionPharmacyData.json";
        pharmacySessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return pharmacySessionId;
    }
}