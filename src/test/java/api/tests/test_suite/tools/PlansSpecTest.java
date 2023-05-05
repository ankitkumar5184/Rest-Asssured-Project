package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PlansSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();

    String planId, requestURL;

    @Test(priority = 0)
    public void testThreePlanSearch() {
        commonMethods.usePropertyFileData("TOOLS-API");
        String threePlanSearchEndpoint = prop.getProperty("Plans_Groovy_Three_Plans_Search");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), threePlanSearchEndpoint + dataProp.getProperty("THREE_PLANS_SEARCH"));
        requestURL = tokens.get("host") + threePlanSearchEndpoint + dataProp.getProperty("THREE_PLANS_SEARCH");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test three plan search response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
    }

    @Test(priority = 1)
    public void testPlanSearch() {
        String planSearchEndpoint = prop.getProperty("Plans_Search");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), planSearchEndpoint + dataProp.getProperty("PLAN_SEARCH"));
        requestURL = tokens.get("host") + planSearchEndpoint + dataProp.getProperty("PLAN_SEARCH");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
        planId = response.jsonPath().getString("MedicarePlans[0].ID");
    }

    @Test(priority = 2)
    public void testPlanDetailRequest(){
        String planDetailRequestEndpoint = prop.getProperty("Plan_Detail").replace("{planId}", planId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), planDetailRequestEndpoint);
        requestURL = tokens.get("host") + planDetailRequestEndpoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test plan detail request response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("ID"), planId);
        assertNotNull(response.jsonPath().getString("PlanID"));
    }

    @Test(priority = 3)
    public void testPlanPharmacyRequestWithManyResponses(){
        String pharmacyWithManyParamEndpoint = prop.getProperty("Plan_Pharmacy_With_Many_Param").replace("{planId}", planId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyWithManyParamEndpoint + dataProp.getProperty("PHARMACY_WITH_MANY_PARAMS"));
        requestURL = tokens.get("host") + pharmacyWithManyParamEndpoint + dataProp.getProperty("PHARMACY_WITH_MANY_PARAMS");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test plan pharmacy request with many responses response body:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("PharmacyList[0].PharmacyID"));
        assertNotNull(response.jsonPath().getString("PharmacyList[0].Name"));
    }

    @Test(priority = 4)
    public void testPlanPharmacyRequestWithLargeRadius(){
        String pharmacyWithLargeRadiusEndpoint = prop.getProperty("Plan_Pharmacy_With_Many_Param").replace("{planId}", planId);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), pharmacyWithLargeRadiusEndpoint + dataProp.getProperty("PHARMACY_WITH_LARGE_RADIUS"));
        requestURL = tokens.get("host") + pharmacyWithLargeRadiusEndpoint + dataProp.getProperty("PHARMACY_WITH_LARGE_RADIUS");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test plan pharmacy request with large radius response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("PharmacyList[0].PharmacyID"));
        assertNotNull(response.jsonPath().getString("PharmacyList[0].Name"));
    }

    @Test(priority = 5)
    public void testConvertIdsForPlans() {
        String convertPlanIDsEndpoint = prop.getProperty("Plan_ConvertId");
        String convertPlanIDsData = commonMethods.getTestDataPath() + "convertIds.xml";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", convertPlanIDsEndpoint, fileReader.getTestJsonFile(convertPlanIDsData));
        requestURL = tokens.get("host") + convertPlanIDsEndpoint + fileReader.getTestJsonFile(convertPlanIDsData);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test convert Ids for plans response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].ContractID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].PlanID"));
        assertNotNull(response.jsonPath().getString("MedicarePlans[0].SegmentID"));
    }
}