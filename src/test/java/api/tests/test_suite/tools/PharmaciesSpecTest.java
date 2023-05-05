package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.ToolsApiErrorMessages;
import api.tests.config.enums.ZipCodeValues;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class PharmaciesSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();

    private String requestURL;

    @Test(priority = 1)
    public void testVerifyGetPharmacyInfoWithWrongId() {
        commonMethods.usePropertyFileData("TOOLS-API");
        String getPharmacyInfoWithWrongIdEndpoint = prop.getProperty("PharmaciesSpecTest_GetPharmacyInfoWithWrongId").replace("{InvalidPharmacyId}", dataProp.getProperty("Invalid_Pharmacy_Id"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getPharmacyInfoWithWrongIdEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_0")));
        requestURL = tokens.get("host") + getPharmacyInfoWithWrongIdEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_0"));
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest get pharmacy info with wrong ID case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), ToolsApiErrorMessages.CANNOT_FIND_PHARMACY_BY_THE_GIVEN_ID.value);
    }

    @Test(priority = 2)
    public void testVerifyGetCvsPharmacyInfoUsingDrxPharmacyId() {
        String getCvsPharmacyInfoUsingDrxPharmacyIdEndpoint = prop.getProperty("PharmaciesSpecTest_GetCvsPharmacyInfoUsingDrxPharmacyId").replace("{DrxPharmacyId}", dataProp.getProperty("DrxPharmacyId"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getCvsPharmacyInfoUsingDrxPharmacyIdEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_0")));
        requestURL = tokens.get("host") + getCvsPharmacyInfoUsingDrxPharmacyIdEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_0"));
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest get CVS pharmacy info using DRXPharmacyID case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("PharmacyID"), dataProp.getProperty("DrxPharmacyId"));
        assertEquals(response.jsonPath().get("Name"), dataProp.getProperty("Pharmacy_Name"));
        assertEquals(response.jsonPath().get("City"), dataProp.getProperty("City"));
    }

    @Test(priority = 3)
    public void testVerifyGetCvsPharmacyInfoUsingNabp() {
        String getCvsPharmacyInfoUsingNabpEndpoint = prop.getProperty("PharmaciesSpecTest_GetCvsPharmacyInfoUsingNabp").replace("{Pharmacy_NABP}", dataProp.getProperty("Pharmacy_NABP"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getCvsPharmacyInfoUsingNabpEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_1")));
        requestURL = tokens.get("host") + getCvsPharmacyInfoUsingNabpEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_1"));
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest get CVS pharmacy info using NABP case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("PharmacyNABP"), dataProp.getProperty("Pharmacy_NABP"));
        assertEquals(response.jsonPath().get("PharmacyID"), dataProp.getProperty("DrxPharmacyId"));
        assertEquals(response.jsonPath().get("Name"), dataProp.getProperty("Pharmacy_Name"));
        assertEquals(response.jsonPath().get("City"), dataProp.getProperty("City"));
    }

    @Test(priority = 4)
    public void testVerifyGetCvsPharmacyInfoUsingNpi() {
        String getCvsPharmacyInfoUsingNpiEndpoint = prop.getProperty("PharmaciesSpecTest_GetCvsPharmacyInfoUsingNpi").replace("{Pharmacy_NPI}", dataProp.getProperty("Pharmacy_NPI"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getCvsPharmacyInfoUsingNpiEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_2")));
        requestURL = tokens.get("host") + getCvsPharmacyInfoUsingNpiEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_2"));
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest get CVS pharmacy info using NPI case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("PharmacyNPI"), dataProp.getProperty("Pharmacy_NPI"));
        assertEquals(response.jsonPath().get("PharmacyID"), dataProp.getProperty("DrxPharmacyId"));
    }

    @Test(priority = 5)
    public void testVerifyGetVonsPharmacyInfoUsingNpi() {
        String getVonsPharmacyInfoUsingNpiEndpoint = prop.getProperty("PharmaciesSpecTest_GetVonsPharmacyInfoUsingNpi").replace("{Vons_PharmacyNpi}", dataProp.getProperty("Vons_PharmacyNpi"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getVonsPharmacyInfoUsingNpiEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_2")));
        requestURL = tokens.get("host") + getVonsPharmacyInfoUsingNpiEndpoint + dataProp.getProperty("GetPharmacyInfo").replace("{pharmacyIdType}", dataProp.getProperty("PharmacyIdType_2"));
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest get VONS pharmacy info using NPI case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("PharmacyNPI"), dataProp.getProperty("Vons_PharmacyNpi"));
        assertEquals(response.jsonPath().get("PharmacyID"), dataProp.getProperty("Vons_PharmacyId"));
        assertEquals(response.jsonPath().get("Name"), dataProp.getProperty("Vons_Name"));
        assertEquals(response.jsonPath().get("City"), dataProp.getProperty("Vons_City"));
    }

    @Test(priority = 6)
    public void testVerifyPharmacySearchWithZipAndRadius() {
        String pharmacySearchWithZipAndRadiusEndpoint = prop.getProperty("PharmaciesSpecTest_PharmacySearch");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacySearchWithZipAndRadiusEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchByZipAndRadiusData"));
        requestURL = tokens.get("host") + pharmacySearchWithZipAndRadiusEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchByZipAndRadiusData");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest pharmacy search with Zip and Radius case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        String pharmacyId = response.jsonPath().get("PharmacyList[0].PharmacyID");
        assertTrue(pharmacyId instanceof String);
        assertTrue(pharmacyId.length() > 2);
        String name = response.jsonPath().get("PharmacyList[0].Name");
        assertTrue(name instanceof String);
        assertTrue(name.length() > 2);
    }

    @Test(priority = 7)
    public void testVerifyPharmacySearchWithZipAlone() {
        String pharmacySearchWithZipAloneEndpoint = prop.getProperty("PharmaciesSpecTest_PharmacySearch");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacySearchWithZipAloneEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchWithZipAlone"));
        requestURL = tokens.get("host") + pharmacySearchWithZipAloneEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchWithZipAlone");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest pharmacy search with Zip alone case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        String pharmacyId = response.jsonPath().get("PharmacyList[0].PharmacyID");
        assertTrue(pharmacyId instanceof String);
        assertTrue(pharmacyId.length() > 2);
        String name = response.jsonPath().get("PharmacyList[0].Name");
        assertTrue(name instanceof String);
        assertTrue(name.length() > 2);
    }

    @Test(priority = 8)
    public void testVerifyPharmacySearchWithZipAndNameAndRadius() {
        String pharmacySearchWithZipAndNameAndRadiusEndpoint = prop.getProperty("PharmaciesSpecTest_PharmacySearch");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacySearchWithZipAndNameAndRadiusEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchWithZipAndNameAndRadius"));
        requestURL = tokens.get("host") + pharmacySearchWithZipAndNameAndRadiusEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchWithZipAndNameAndRadius");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest pharmacy search with Zip and Name and Radius case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        String pharmacyId = response.jsonPath().get("PharmacyList[0].PharmacyID");
        assertTrue(pharmacyId instanceof String);
        assertTrue(pharmacyId.length() > 2);
        String name = response.jsonPath().get("PharmacyList[0].Name");
        assertTrue(name instanceof String);
        assertTrue(name.length() > 2);
        assertEquals(response.jsonPath().get("PharmacyList[0].Zip"), ZipCodeValues.ZIP_90404.value);
    }

    @Test(priority = 9)
    public void testVerifyPharmacySearchForOneCvsLocation() {
        String pharmacySearchForOneCvsLocationEndpoint = prop.getProperty("PharmaciesSpecTest_PharmacySearch");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacySearchForOneCvsLocationEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchForOneCvsLocation"));
        requestURL = tokens.get("host") + pharmacySearchForOneCvsLocationEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchForOneCvsLocation");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest pharmacy search for one CVS location case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("PharmacyList[0].PharmacyID"), dataProp.getProperty("DrxPharmacyId"));
        assertEquals(response.jsonPath().get("PharmacyList[0].Name"), dataProp.getProperty("Pharmacy_Name"));
    }

    @Test(priority = 10)
    public void testVerifyPharmacySearchForTwoCvsLocations() {
        String pharmacySearchForTwoCvsLocationsEndpoint = prop.getProperty("PharmaciesSpecTest_PharmacySearch");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacySearchForTwoCvsLocationsEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchForTwoCvsLocation"));
        requestURL = tokens.get("host") + pharmacySearchForTwoCvsLocationsEndpoint + dataProp.getProperty("PharmaciesSpecTest_PharmacySearchForTwoCvsLocation");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test PharmaciesSpecTest pharmacy search for two CVS location case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().get("PharmacyList[0].Name"));
        List<Integer> pharmacyList = new ArrayList<Integer>();
        pharmacyList = response.jsonPath().getList("PharmacyList");
        assertTrue(pharmacyList.size() >= 2);
    }
}