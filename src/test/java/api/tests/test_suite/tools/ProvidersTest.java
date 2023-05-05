package api.tests.test_suite.tools;

import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.ToolsApiErrorMessages;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ProvidersTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();

    private String providerEndpoint, providerEndPointData, requestURL, requestBody;

    @Test
    public void testVerifyProviderSearch() {
        commonMethods.usePropertyFileData("TOOLS-API");
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        providerEndPointData = commonMethods.getTestDataPath() + "providerSearch.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, fileReader.getTestJsonFile(providerEndPointData));
        requestURL = tokens.get("host") + providerEndpoint;
        requestBody = fileReader.getTestJsonFile(providerEndPointData);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        ExtentLogger.pass("Test provider search response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("Total"));
        assertNotNull(response.jsonPath().getString("Providers[0].Id"));
        assertEquals(response.jsonPath().getString("Providers[0].Gender"), dataProp.getProperty("GENDER_FEMALE"));
        assertEquals(response.jsonPath().getString("Providers[0].FirstName"), dataProp.getProperty("FIRST_NAME"));
        assertEquals(response.jsonPath().getString("Providers[0].LastName"), dataProp.getProperty("LAST_NAME"));
    }

    @Test(priority = 1)
    public void testVerifyProviderSearchWithBlankBody() {
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + providerEndpoint;
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        ExtentLogger.pass("Test provider search when blank body response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().getString("Message"), ToolsApiErrorMessages.MISSING_SEARCH_TERM_PARAM_VALUE.value);
    }

    @Test(priority = 2)
    public void testVerifyProviderSearchByLatitudeLongitude(){
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        providerEndPointData = commonMethods.getTestDataPath() + "providerSearchByLatitudeLongitude.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, fileReader.getTestJsonFile(providerEndPointData));
        requestURL = tokens.get("host") + providerEndpoint;
        requestBody = fileReader.getTestJsonFile(providerEndPointData);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        ExtentLogger.pass("Test provider search by latitude longitude response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("Total"));
        assertEquals(response.jsonPath().getString("Providers[0].Gender"), dataProp.getProperty("GENDER_MALE"));
        assertEquals(response.jsonPath().getString("Providers[0].FirstName"), dataProp.getProperty("ProviderSearchByLatitudeLongitude_FIRST_NAME"));
        assertNotNull(response.jsonPath().getString("Providers[0].Addresses[0].ZipCode"));
    }

    @Test(priority = 3)
    public void testVerifyProviderSearchByLatitudeLongitudeWithBlankBody() {
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + providerEndpoint;
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        ExtentLogger.pass("Test provider search by latitude longitude when blank body response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().getString("Message"), ToolsApiErrorMessages.MISSING_SEARCH_TERM_PARAM_VALUE.value);
    }

    @Test(priority = 4)
    public void testVerifyProviderSearchByProviderSearchByID(){
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        providerEndPointData = commonMethods.getTestDataPath() + "providerSearchByID.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, fileReader.getTestJsonFile(providerEndPointData));
        requestURL = tokens.get("host") + providerEndpoint;
        requestBody = fileReader.getTestJsonFile(providerEndPointData);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        ExtentLogger.pass("Test provider search by provider search by ID response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("Total"));
        assertEquals(response.jsonPath().getString("Providers[0].Id"), dataProp.getProperty("NPIs_ID"));
        assertNotNull(response.jsonPath().getString("Providers[0].FirstName"));
        assertNotNull(response.jsonPath().getString("Providers[0].LastName"));
    }
    
    @Test(priority = 5)
    public void testVerifyProviderSearchByIDWithBlankBody() {
        providerEndpoint = prop.getProperty("Providers_PostProviderSearch");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", providerEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + providerEndpoint;
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        ExtentLogger.pass("Test provider search by ID when blank body response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().getString("Message"), ToolsApiErrorMessages.MISSING_SEARCH_TERM_PARAM_VALUE.value);
    }
}