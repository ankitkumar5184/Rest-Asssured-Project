package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.core.PutRequest;
import api.base.helpers.*;
import api.base.helpers.env_annotations.SkipOnTest;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.CommonValues;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.ToolsApiErrorMessages;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Listeners(value = ConditionalSkipTestAnalyzer.class)
public class AgentsTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    PutRequest putRequest = new PutRequest();
    CommonMethods commonMethods = new CommonMethods();
    CommonDateTimeMethods commonDateTimeMethods = new CommonDateTimeMethods();

    private String requestURL, requestBody;

    @Test(priority = 1)
    public void testVerifyGetCarriers() {
        commonMethods.usePropertyFileData("TOOLS-API");
        String getCarriersEndpoint = prop.getProperty("Agents_GetCarriers");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getCarriersEndpoint);
        requestURL = tokens.get("host") + getCarriersEndpoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test AgentsTest get carriers case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().get("[0].ID"), "Id");
        assertNotNull(response.jsonPath().get("[0].Name"), "Name");
        assertNotNull(response.jsonPath().get("[0].LongName"), "Carrier name");
    }

    @Test(priority = 2)
    public void testVerifySearchAgents() {
        String searchAgentsEndpoint = prop.getProperty("Agents_GetSearchAgents");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), searchAgentsEndpoint + dataProp.getProperty("Agents_GetSearchAgentsData"));
        requestURL = tokens.get("host") + searchAgentsEndpoint + dataProp.getProperty("Agents_GetSearchAgentsData");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test AgentsTest search agents case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        int totalCount = Integer.parseInt(dataProp.getProperty("TotalCount"));
        assertEquals(response.jsonPath().getInt("TotalCount"), totalCount);
    }

    @SkipOnTest
    @Test(priority = 3)
    public void testVerifyGetAgent() {
        String getAgentEndpoint = prop.getProperty("Agents_GetAgent").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getAgentEndpoint);
        requestURL = tokens.get("host") + getAgentEndpoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test AgentsTest get agent case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("AgentID"), dataProp.getProperty("AgentId"));
        assertEquals(response.jsonPath().get("ExternalUserId"), dataProp.getProperty("AgentId"));
    }

    @SkipOnTest
    @Test(priority = 4)
    public void testVerifyCreateAgent() {
        String unique = commonDateTimeMethods.getUniqueNumber();
        String createAgentEndpoint = prop.getProperty("Agents_PostCreateAgent");
        String createAgentData = commonMethods.getTestDataPath() + "createAgentData.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", createAgentEndpoint, fileReader.getTestJsonFile(createAgentData).replace("{uniqueNumber}", unique));
        requestURL = tokens.get("host") + createAgentEndpoint;
        requestBody = fileReader.getTestJsonFile(createAgentData).replace("{uniqueNumber}", unique);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is:- " + requestBody);
        ExtentLogger.pass("Test AgentsTest create agent case response body:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 201);
        assertEquals(response.jsonPath().get("AgentID"), dataProp.getProperty("AgentId"));
        assertEquals(response.jsonPath().get("UserName"), dataProp.getProperty("CreateAgent_UserName").replace("{uniqueNumber}", unique));
        assertEquals(response.jsonPath().get("FirstName"), dataProp.getProperty("FirstName"));
        assertEquals(response.jsonPath().get("LastName"), dataProp.getProperty("LastName"));
    }

    @Test(priority = 5)
    public void testVerifyCreateAgentWithBlankBodyData() {
        String createAgentEndpoint = prop.getProperty("Agents_PostCreateAgent");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", createAgentEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + createAgentEndpoint;
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is:- " + requestBody);
        ExtentLogger.pass("Test AgentsTest create agent with blank body data case response body:- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.AgentID\"][0]"), ToolsApiErrorMessages.AGENT_ID_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.UserName\"][0]"), ToolsApiErrorMessages.USERNAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.FirstName\"][0]"), ToolsApiErrorMessages.FIRST_NAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.LastName\"][0]"), ToolsApiErrorMessages.LAST_NAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.AgentRole\"][0]"), ToolsApiErrorMessages.AGENT_ROLE_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.IsActive\"][0]"), ToolsApiErrorMessages.IS_ACTIVE_MISSING.value);
    }

    @SkipOnTest
    @Test(priority = 6)
    public void testVerifyCreateAgentCopy() {
        String createAgentCopyEndpoint = prop.getProperty("Agents_PutCreateAgentCopy").replace("{AgentID}", dataProp.getProperty("AgentId"));
        String createAgentCopyData = commonMethods.getTestDataPath() + "createAgentCopyData.json";
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), createAgentCopyEndpoint, fileReader.getTestJsonFile(createAgentCopyData));
        requestURL = tokens.get("host") + createAgentCopyEndpoint;
        requestBody = fileReader.getTestJsonFile(createAgentCopyData);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is:- " + requestBody);
        ExtentLogger.pass("Test AgentsTest create agent copy case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("AgentID"), dataProp.getProperty("AgentId"));
        assertEquals(response.jsonPath().get("FirstName"), dataProp.getProperty("FirstName"));
        assertEquals(response.jsonPath().get("LastName"), dataProp.getProperty("LastName"));
    }

    @Test(priority = 7)
    public void testVerifyCreateAgentCopyWithBlankBodyData() {
        String createAgentCopyEndpoint = prop.getProperty("Agents_PutCreateAgentCopy").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = putRequest.putMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), createAgentCopyEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + createAgentCopyEndpoint;
        requestBody =  CommonValues.BLANK_BODY_REQUEST.value;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is:- " + requestBody);
        ExtentLogger.pass("Test AgentsTest create agent copy with blank body data case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.AgentID\"][0]"), ToolsApiErrorMessages.AGENT_ID_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.FirstName\"][0]"), ToolsApiErrorMessages.FIRST_NAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.LastName\"][0]"), ToolsApiErrorMessages.LAST_NAME_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.AgentRole\"][0]"), ToolsApiErrorMessages.AGENT_ROLE_MISSING.value);
        assertEquals(response.jsonPath().get("ModelState[\"agentRequest.IsActive\"][0]"), ToolsApiErrorMessages.IS_ACTIVE_MISSING.value);
    }

    @SkipOnTest
    @Test(priority = 8)
    public void testVerifyGetSellingPermissions() {
        String getSellingPermissionsEndpoint = prop.getProperty("Agents_GetSellingPermissions").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), getSellingPermissionsEndpoint);
        requestURL = tokens.get("host") + getSellingPermissionsEndpoint;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test AgentsTest get selling permissions case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        int carrierId = Integer.parseInt(dataProp.getProperty("GetSellingPermissions_CarrierId"));
        assertEquals(response.jsonPath().getInt("[0].CarrierID"), carrierId);
        assertEquals(response.jsonPath().get("[0].AgentProducerID"), dataProp.getProperty("GetSellingPermissions_AgentProducerID"));
    }

    @SkipOnTest
    @Test(priority = 9)
    public void testVerifyPostSellingPermissions() {
        String postSellingPermissionsData = commonMethods.getTestDataPath() + "postSellingPermissionsData.json";
        String postSellingPermissionsEndpoint = prop.getProperty("Agents_PostSellingPermissions").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", postSellingPermissionsEndpoint, fileReader.getTestJsonFile(postSellingPermissionsData));
        requestURL = tokens.get("host") + postSellingPermissionsEndpoint;
        requestBody = fileReader.getTestJsonFile(postSellingPermissionsData);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is:- " + requestBody);
        ExtentLogger.pass("Test AgentsTest post selling permissions case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 204);
    }

    @Test(priority = 10)
    public void testVerifyPostSellingPermissionsWithBlankBodyData() {
        String postSellingPermissionsEndpoint = prop.getProperty("Agents_PostSellingPermissions").replace("{AgentID}", dataProp.getProperty("AgentId"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), "JSON", postSellingPermissionsEndpoint, CommonValues.BLANK_BODY_REQUEST.value);
        requestURL = tokens.get("host") + postSellingPermissionsEndpoint;
        requestBody = CommonValues.BLANK_BODY_REQUEST.value;
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is:- " + requestBody);
        ExtentLogger.pass("Test AgentsTest post selling permissions with blank body data case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 400);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.THE_REQUEST_IS_INVALID.value);
        assertEquals(response.jsonPath().get("ModelState.sellingPermissions[0]"), GeneralErrorMessages.AN_ERROR_HAS_OCCURRED.value);
    }
}