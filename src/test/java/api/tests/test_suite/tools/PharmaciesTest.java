package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.ToolsApiErrorMessages;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import static org.testng.Assert.assertEquals;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;
import java.util.Map;

public class PharmaciesTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();

    private String errorMessage, requestURL;

    @Test(priority = 1)
    public void testGetPharmacyDrxId(){
        commonMethods.usePropertyFileData("TOOLS-API");
        String pharmacyDrx_EndPoint = prop.getProperty("Pharmacies_GetPharmacyDRXID").replace("{DrxId}", dataProp.getProperty("Pharmacies_GetPharmacyDRXID_Data"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyDrx_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyDRXID_Query"));
        requestURL = tokens.get("host") + pharmacyDrx_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyDRXID_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Pharmacies DRX ID response body:- " + response.asPrettyString());
        String pharmacyId = response.jsonPath().getString("PharmacyID");
        assertEquals(pharmacyId,dataProp.getProperty("Pharmacies_GetPharmacyDRXID_Data"));
        assertNotNull(response.jsonPath().getString("PharmacyID"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testGetPharmacyDrxIdWithInvalidId(){
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("TOOLS-API");
        String pharmacyDrx_EndPoint = prop.getProperty("Pharmacies_GetPharmacyDRXID").replace("{DrxId}", GeneralErrorMessages.INVALID_TEXT.value);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyDrx_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyDRXID_Query"));
        requestURL = tokens.get("host") + pharmacyDrx_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyDRXID_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Pharmacies DRX ID with Invalid ID response body :- " + response.asPrettyString());
        errorMessage = response.jsonPath().getString("Message");
        assertEquals(errorMessage, ToolsApiErrorMessages.CANNOT_FIND_PHARMACY_BY_THE_GIVEN_ID.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 3)
    public void GetPharmacyByNpi(){
        String pharmacyByNpi_EndPoint = prop.getProperty("Pharmacies_GetPharmacyByNPI").replace("{PharmacyId}", dataProp.getProperty("Pharmacies_GetPharmacyByNPI_Data"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyByNpi_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyByNPI_Query"));
        requestURL = tokens.get("host") + pharmacyByNpi_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyByNPI_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Pharmacy bu Npi ID response body :- " + response.asPrettyString());
        String pharmacyNPI = response.jsonPath().getString("PharmacyNPI");
        assertEquals(pharmacyNPI,dataProp.getProperty("Pharmacies_GetPharmacyByNPI_Data"));
        assertNotNull(response.jsonPath().getString("PharmacyID"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void GetPharmacyByNpiWithInvalidId(){
        String pharmacyByNpi_EndPoint = prop.getProperty("Pharmacies_GetPharmacyByNPI").replace("{PharmacyId}", GeneralErrorMessages.INVALID_TEXT.value);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyByNpi_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyByNPI_Query"));
        requestURL = tokens.get("host") + pharmacyByNpi_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyByNPI_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Pharmacy Npi ID with Invalid ID response body :- " + response.asPrettyString());
        errorMessage = response.jsonPath().getString("Message");
        assertEquals(errorMessage,ToolsApiErrorMessages.CANNOT_FIND_PHARMACY_BY_THE_GIVEN_ID.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 5)
    public void GetPharmacyByNabp(){
        String pharmacyByNabp_EndPoint = prop.getProperty("Pharmacies_GetPharmacyByNABP").replace("{NabpId}", dataProp.getProperty("Pharmacies_GetPharmacyByNABP_Data"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyByNabp_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyByNABP_Query"));
        requestURL = tokens.get("host") + pharmacyByNabp_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyByNABP_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Pharmacy by Nabp ID response body :- " + response.asPrettyString());
        String pharmacyNabp = response.jsonPath().getString("PharmacyNABP");
        assertEquals(pharmacyNabp,dataProp.getProperty("Pharmacies_GetPharmacyByNABP_Data"));
        assertNotNull(response.jsonPath().getString("PharmacyID"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void GetPharmacyByNabpWithInvalidId(){
        String pharmacyByNabp_EndPoint = prop.getProperty("Pharmacies_GetPharmacyByNABP").replace("{NabpId}", GeneralErrorMessages.INVALID_TEXT.value);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyByNabp_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyByNABP_Query"));
        requestURL = tokens.get("host") + pharmacyByNabp_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacyByNABP_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Pharmacy Nabp with Invalid ID response body :- " + response.asPrettyString());
        errorMessage = response.jsonPath().getString("Message");
        assertEquals(errorMessage,ToolsApiErrorMessages.CANNOT_FIND_PHARMACY_BY_THE_GIVEN_ID.value);
        assertEquals(response.getStatusCode(), 400);
    }

    @Test(priority = 7)
    public void GetPharmacySearch(){
        String fetPharmacySearch_EndPoint = prop.getProperty("Pharmacies_GetPharmacySearch");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), fetPharmacySearch_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacySearch_Query"));
        requestURL = tokens.get("host") + fetPharmacySearch_EndPoint + dataProp.getProperty("Pharmacies_GetPharmacySearch_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Pharmacy search response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }
}