package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.ToolsApiErrorMessages;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class AgenciesTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();

    private String agenciesEndpoint, getAgencyID, requestURL, requestBody;

    @Test
    public void testVerifyGetAgencies() {
        commonMethods.usePropertyFileData("TOOLS-API");
        agenciesEndpoint = prop.getProperty("Agencies_GetAgencies");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), agenciesEndpoint);
        requestURL = tokens.get("host") + agenciesEndpoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        getAgencyID = response.jsonPath().getString("[0].AgencyID");
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("[0].AgencyID"));
        assertNotNull(response.jsonPath().getString("[0].AgencyName"));
        assertNotNull(response.jsonPath().getString("[1].AgencyID"));
        assertNotNull(response.jsonPath().getString("[1].AgencyName"));
    }

    @Test(priority = 1)
    public void testVerifyGetAgenciesByAgencyId(){
        agenciesEndpoint = prop.getProperty("Agencies_GetAgencies");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")),agenciesEndpoint + getAgencyID);
        requestURL = tokens.get("host") + agenciesEndpoint + getAgencyID;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().getString("AgencyID"));
        assertNotNull(response.jsonPath().getString("AgencyName"));
        assertNull(response.jsonPath().getString("ParentAgencyID"));
        assertNull(response.jsonPath().getString("ParentAgencyName"));
    }

    @Test(priority = 2)
    public void testVerifyGetAgenciesWithInvalidAgencyId(){
        agenciesEndpoint = prop.getProperty("Agencies_GetAgencies");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")),agenciesEndpoint + dataProp.getProperty("INVALID_TEXT"));
        requestURL = tokens.get("host") + agenciesEndpoint + dataProp.getProperty("INVALID_TEXT");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        assertEquals(response.getStatusCode(), 404);
        assertEquals(response.jsonPath().getString("Message"), ToolsApiErrorMessages.INVALID_AGENCY_ID_ERROR_MESSAGE.value);
    }

    @Test(priority = 3)
    public void testVerifyCreateAgencies() {
        agenciesEndpoint = prop.getProperty("Agencies_PostCreateAgencies");
        String agenciesEndpointData = commonMethods.getTestDataPath() + "createAgenciesData.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", agenciesEndpoint, fileReader.getTestJsonFile(agenciesEndpointData));
        requestURL = tokens.get("host") + agenciesEndpoint;
        requestBody = fileReader.getTestJsonFile(agenciesEndpointData);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getString("Status"), "SUCCESS");
    }

    @Test(priority = 4)
    public void testVerifyCreateAgenciesWithBlankBody() {
        agenciesEndpoint = prop.getProperty("Agencies_PostCreateAgencies");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", agenciesEndpoint, CommonValues.BLANK_BODY_VALID_REQUEST.value);
        requestURL = tokens.get("host") +  agenciesEndpoint + CommonValues.BLANK_BODY_VALID_REQUEST.value;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().getString("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().getString("ModelState['agenciesRequest[0].AgencyID'][0]"), ToolsApiErrorMessages.THE_AGENCY_FIELD_IS_REQUIRED.value);
        assertEquals(response.jsonPath().getString("ModelState['agenciesRequest[0].AgencyName'][0]"), ToolsApiErrorMessages.THE_AGENCY_NAME_IS_REQUIRED.value);
    }
}