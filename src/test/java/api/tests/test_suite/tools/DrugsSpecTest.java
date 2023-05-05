package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.reporter.ExtentLogger;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class DrugsSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();

    private String drugDetails_EndPoint;
    private String dosagesDetails_EndPoint;
    private String drugName;
    private String labelName;
    private String dosagesName;
    private String chemicalName;
    private String requestURL;

    @Test(priority = 1)
    public void testDrugFormOnTheInfo_1() {
        commonMethods.usePropertyFileData("TOOLS-API");
        drugDetails_EndPoint = prop.getProperty("DrugDetail_SME-14781").replace("{DrugId}", dataProp.getProperty("DRUGID_DRUGDETAILS_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), drugDetails_EndPoint);
        requestURL = tokens.get("host") + drugDetails_EndPoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Drug Details :- " + response.asPrettyString());
        drugName = response.jsonPath().getString("DrugName");
        dosagesName = response.jsonPath().getString("Dosages[0].DosageID");
        chemicalName = response.jsonPath().getString("GenericChemicalName");
        labelName = response.jsonPath().getString("Dosages[0].LabelName");
        assertEquals(drugName,dataProp.getProperty("DRUG_NAME_1"));
        assertEquals(chemicalName,dataProp.getProperty("CHEMICAL_NAME_1"));
        assertNotNull(response.jsonPath().getString("DrugTypeID"));
        List<Integer> dosagesList = new ArrayList<Integer>();
        dosagesList = response.jsonPath().getList("Dosages");
        assertTrue(dosagesList.size() > 2);
        assert drugName.length() > 2;
        assert dosagesName.length() > 2;
        assertTrue(dosagesName instanceof String);
        assertTrue(labelName instanceof String);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testDrugFormOnTheInfo_2(){
        drugDetails_EndPoint = prop.getProperty("DrugDetail_SME-14781").replace("{DrugId}", dataProp.getProperty("DRUGID_DRUGDETAILS_2"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), drugDetails_EndPoint);
        requestURL = tokens.get("host") + drugDetails_EndPoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Drug Details response body :- " + response.asPrettyString());
        drugName = response.jsonPath().getString("DrugName");
        assertNotNull(drugName,"Drug name");
        assertNotNull(response.jsonPath().getString("DrugTypeID"), "DrugType Id");
        assert drugName.length() > 2;
        assert dosagesName.length() > 2;
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 3)
    public void testDosageCalls_1(){
        dosagesDetails_EndPoint = prop.getProperty("DrugDosageDetails_SME_14781").replace("{DrugId}", dataProp.getProperty("DRUGID_DRUGDETAILS_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), dosagesDetails_EndPoint);
        requestURL = tokens.get("host") + dosagesDetails_EndPoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Dosage Details response body :- " + response.asPrettyString());
        labelName = response.jsonPath().getString("[0].LabelName");
        assertEquals(labelName,dataProp.getProperty("LABEL_NAME_1"));
        assertNotNull(response.jsonPath().getString("DosageID"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void testDosageCalls_2(){
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("TOOLS-API");
        dosagesDetails_EndPoint = prop.getProperty("DrugDosageDetails_SME_14781").replace("{DrugId}", dataProp.getProperty("DRUGID_DRUGDETAILS_2"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), dosagesDetails_EndPoint);
        requestURL = tokens.get("host") + dosagesDetails_EndPoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Dosage Details response body :- " + response.asPrettyString());
        labelName = response.jsonPath().getString("[0].LabelName");
        assertNotNull(labelName,"Label name");
        assertNotNull(response.jsonPath().getString("DosageID"));
        assert labelName.length() > 2;
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testDrugDetailsRequestForLipitorGeneric(){
        drugDetails_EndPoint = prop.getProperty("DrugDetail_SME-14781").replace("{DrugId}", dataProp.getProperty("DRUGID_DRUGDETAILS_3"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), drugDetails_EndPoint);
        requestURL = tokens.get("host") + drugDetails_EndPoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Drug Details response body :- " + response.asPrettyString());
        drugName = response.jsonPath().getString("DrugName");
        dosagesName = response.jsonPath().getString("Dosages[0].DosageID");
        chemicalName = response.jsonPath().getString("ChemicalName");
        assertEquals(drugName,dataProp.getProperty("DRUG_NAME_3"));
        assertEquals(chemicalName,dataProp.getProperty("CHEMICAL_NAME_1"));
        assertNotNull(response.jsonPath().getString("DrugType"));
        List<Integer> dosagesList = new ArrayList<Integer>();
        dosagesList = response.jsonPath().getList("Dosages");
        assertTrue(dosagesList.size() > 2);
        assert drugName.length() > 2;
        assert dosagesName.length() > 2;
        labelName = response.jsonPath().getString("Dosages[0].LabelName");
        assertEquals(labelName,dataProp.getProperty("LABEL_NAME_3"));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testDrugSearchAutocomplete(){
        dosagesDetails_EndPoint = prop.getProperty("DrugSearch_Autocomplete");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), dosagesDetails_EndPoint + dataProp.getProperty("DrugSearch_Autocomplete_Query"));
        requestURL = tokens.get("host") + dosagesDetails_EndPoint + dataProp.getProperty("DrugSearch_Autocomplete_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Drug Details response body :- " + response.asPrettyString());
        assertNotNull(response.jsonPath().getString("[0].DrugName"));
        drugName = response.jsonPath().getString("DrugName");
        assert drugName.contains("Lipitor");
        assert drugName.contains("Lipofen");
        assertEquals(response.getStatusCode(), 200);
    }
    @Test(priority = 7)
    public void testDrugSearchRequest(){
        dosagesDetails_EndPoint = prop.getProperty("DrugSearch_Request");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), dosagesDetails_EndPoint + dataProp.getProperty("DrugSearch_Request_Query"));
        requestURL = tokens.get("host") + dosagesDetails_EndPoint + dataProp.getProperty("DrugSearch_Request_Query");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Drug Details response body :- " + response.asPrettyString());
        assertNotNull(response.jsonPath().getString("[0].DrugName"));
        drugName = response.jsonPath().getString("DrugName");
        String drugType = response.jsonPath().getString("DrugType");
        String referenceNDC = response.jsonPath().getString("ReferenceNDC");
        assert drugType.length()>2;
        assert referenceNDC.length()>2;
//      Drug Type List
        List<Integer> drugTypeList = new ArrayList<Integer>();
        drugTypeList = response.jsonPath().getList("DrugType");
        assertTrue(drugTypeList.size() > 2);
//      Reference NDC List
        List<Integer> referenceNDCList = new ArrayList<Integer>();
        referenceNDCList = response.jsonPath().getList("ReferenceNDC");
        assertTrue(referenceNDCList.size() > 2);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 8)
    public void testDrugDetailsRequestProAirPackagedGeneric(){
        drugDetails_EndPoint = prop.getProperty("DrugDetail_SME-14781").replace("{DrugId}", dataProp.getProperty("DRUGID_DRUGDETAILS_4"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), drugDetails_EndPoint);
        requestURL = tokens.get("host") + drugDetails_EndPoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("GET Drug Details response body :- " + response.asPrettyString());
        drugName = response.jsonPath().getString("DrugName");
        dosagesName = response.jsonPath().getString("Dosages[0].DosageID");
        chemicalName = response.jsonPath().getString("ChemicalName");
        assertEquals(drugName,dataProp.getProperty("DRUG_NAME_4"));
        assertEquals(chemicalName,dataProp.getProperty("CHEMICAL_NAME_2"));
        assertNotNull(response.jsonPath().getString("DrugType"));
        List<Integer> dosagesList = new ArrayList<Integer>();
        dosagesList = response.jsonPath().getList("Dosages");
        assertTrue(dosagesList.size() > 2);
        assert drugName.length() > 2;
        assert dosagesName.length() > 2;
        assertEquals(response.getStatusCode(), 200);
    }
}