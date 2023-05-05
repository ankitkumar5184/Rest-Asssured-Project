package api.tests.test_suite.plancompare;

import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.FipsValues;
import api.tests.config.enums.ZipCodeValues;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PostmanSessionsScenariosTest {
    PostRequest postRequest = new PostRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    protected String SessionId;
    protected String requestURL, requestBody;
    protected String getMedGapSessionId;
    protected String getDentalVisionSessionId;
    protected String getNpiNumber;

    @Test
    public void testCreateSession() {
        commonMethods.usePropertyFileData("PC-API");
        String createSessionData = commonMethods.getTestDataPath() + "createSessionsData.json";
        String createSessionEndpoint = prop.getProperty("Sessions_CreateSession_Postman").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession") + String.format(createSessionEndpoint, getSessionId()),
                fileReader.getTestJsonFile(createSessionData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession") + String.format(createSessionEndpoint, SessionId);
        requestBody = fileReader.getTestJsonFile(createSessionData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session Response body :- " + response.asPrettyString());
        }
        String getMBI = response.jsonPath().get("Profile.MBI");
        String getZip = response.jsonPath().get("Zip");
        String getFips = response.jsonPath().get("Fips");
        String getGenericLabelName = response.jsonPath().get("Dosages[0].GenericLabelName");
        String getBirthDate = response.jsonPath().get("Birthdate");
        assertEquals(getZip, ZipCodeValues.Zip_90011.value);
        assertEquals(getFips, FipsValues.Fips_06037.value);
        assertEquals(getMBI, dataProp.getProperty("MBI"));
        assertEquals(getBirthDate, dataProp.getProperty("BIRTH_DATE"));
        assertEquals(getGenericLabelName, dataProp.getProperty("GENERIC_LABEL_NAME"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 1)
    public void testCreateSessionWithProfile() {
        String postCreateSessionWithProfileData = commonMethods.getTestDataPath() + "createSessionWithProfile.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithProfileData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithProfileData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With Profile Response body :- " + response.asPrettyString());
        }
        String getReqId = response.jsonPath().get("[0].reqid");
        String getSubmittedNDC = response.jsonPath().getString("[0].Dosages[1].SubmittedNDC");
        assertEquals(getSubmittedNDC, dataProp.getProperty("SubmittedNDC"));
        assertEquals(getReqId, dataProp.getProperty("REQ_ID"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testCreateSessionWithNoProfile() {
        String postCreateSessionWithNoProfileData = commonMethods.getTestDataPath() + "createSessionWithNoProfile.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithNoProfileData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithNoProfileData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With No Profile Response body :- " + response.asPrettyString());
        }
        String getReqId = response.jsonPath().get("[0].reqid");
        String getSubmittedNDC = response.jsonPath().getString("[0].Dosages[1].SubmittedNDC");
        assertEquals(getReqId, dataProp.getProperty("REQ_ID"));
        assertEquals(getSubmittedNDC, dataProp.getProperty("SubmittedNDC"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void testCreateSessionWithQuestions() {
        String postCreateSessionWithQuestionsData = commonMethods.getTestDataPath() + "createSessionWithQuestions.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With Questions Response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void testCreateSessionWithSSOValueOnly() {
        String postCreateSessionWithSSOValueOnlyData = commonMethods.getTestDataPath() + "createSessionWithSSOValueOnly.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithSSOValueOnlyData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithSSOValueOnlyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With SSOValue Only Response body :- " + response.asPrettyString());
        }
        String getSSOValue = response.jsonPath().get("[0].SSOValue");
        assertEquals(getSSOValue, dataProp.getProperty("SSOValue"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testCreateSessionWithXML() {
        String postCreateSessionWithXMLData = commonMethods.getTestDataPath() + "createSessionWithXML.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithXMLData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithXMLData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With XML Response body :- " + response.asPrettyString());
        }
        String getStatus = response.jsonPath().get("[0].Status");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testCreateSessionWithXMLWithBlankBodyData() {
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("Sessions_CreateSession"),
                CommonValues.BLANK_BODY_REQUEST_WITH_XML.value);
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = CommonValues.BLANK_BODY_REQUEST_WITH_XML.value;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With XML Blank Body Data Response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 7)
    public void testCreateSessionWithProfilePlus() {
        String postCreateSessionWithProfilePlusData = commonMethods.getTestDataPath() + "createSessionWithProfile+.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithProfilePlusData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithProfilePlusData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With Profile Plus Response body : " + response.asPrettyString());
        }
        String getStatus = response.jsonPath().get("[0].Status");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 8)
    public void testCreateSessionWithProviders() {
        String postCreateSessionWithProvidersData = commonMethods.getTestDataPath() + "createSessionWithProviders.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithProvidersData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithProvidersData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With Providers Response body :- " + response.asPrettyString());
        }
        String getAddressId = response.jsonPath().get("[0].Providers[0].AddressId");
        getNpiNumber = response.jsonPath().get("[0].Providers[0].NPI");
        assertEquals(getAddressId, dataProp.getProperty("ADDRESS_ID"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 9)
    public void testCreateSessionWithOneBrandDrug() {
        String postCreateSessionWithOneBrandDrugData = commonMethods.getTestDataPath() + "createSessionWithOneBrandDrug.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithOneBrandDrugData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithOneBrandDrugData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With One Brand Drug Response body :- " + response.asPrettyString());
        }
        String getNDC = response.jsonPath().get("[0].Dosages[0].SubmittedNDC");
        assertEquals(getNDC, dataProp.getProperty("NDC"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 10)
    public void testCreateSessionWithMedGapQuestions() {
        String postCreateSessionWithMedGapQuestionsData = commonMethods.getTestDataPath() + "createSessionWithMedGapQuestions.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithMedGapQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithMedGapQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With MedGap Questions Response body :- " + response.asPrettyString());
        }
        getMedGapSessionId = response.jsonPath().get("[0].SessionID");
        String getStatus = response.jsonPath().get("[0].Status");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 11)
    public void testCreateSessionWithDentalVisionQuestions() {
        String postCreateSessionWithDentalVisionQuestionsData = commonMethods.getTestDataPath() + "createSessionWithDentalVisionQuestions.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession"),
                fileReader.getTestJsonFile(postCreateSessionWithDentalVisionQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession");
        requestBody = fileReader.getTestJsonFile(postCreateSessionWithDentalVisionQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With MedGap Questions Response body :- " + response.asPrettyString());
        }
        String getStatus = response.jsonPath().get("[0].Status");
        getDentalVisionSessionId = response.jsonPath().get("[0].SessionID");
        assertEquals(getStatus, dataProp.getProperty("UPDATED_TEXT"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 12)
    public void testUpdateSessionsWithQuestions() {
        String postUpdateSessionsWithQuestionsData = commonMethods.getTestDataPath() + "updateSessionWithQuestions.json";
        String updateSessionQuestionsEndpoint = prop.getProperty("Sessions_CreateSession_Postman").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", prop.getProperty("Sessions_CreateSession") +
                String.format(updateSessionQuestionsEndpoint, SessionId), fileReader.getTestJsonFile(postUpdateSessionsWithQuestionsData));
        requestURL = tokens.get("host") + prop.getProperty("Sessions_CreateSession") + String.format(updateSessionQuestionsEndpoint, SessionId);
        requestBody = fileReader.getTestJsonFile(postUpdateSessionsWithQuestionsData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Create Session With MedGap Questions Response body :- " + response.asPrettyString());
        }
        String getPharmacyId = response.jsonPath().get("Pharmacies[1].PharmacyID");
        String getNDC = response.jsonPath().get("Dosages[1].NDC");
        assertEquals(getPharmacyId, dataProp.getProperty("SESSION_PHARMACY_ID"));
        assertNotNull(getNDC);
        assertEquals(response.getStatusCode(), 200);
    }

    private String getSessionId() {
        String createSessionData = commonMethods.getTestDataPath() + "createSessionData.json";
        SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return SessionId;
    }
}