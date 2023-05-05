package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static api.base.helpers.CommonMethods.dataProp;

import java.util.Map;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class PharmacyCostsValidationSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    private String pharmacyCostsValidationSessionId;
    private String idForEnbrel;
    private String idForHumira;
    private String idForBupropion;
    protected String requestURL;

    @Test(priority = 1)
    public void testVerifyLoadDrugDataForEnbrel() {
        commonMethods.usePropertyFileData("PC-API");
        String loadDrugDataForEnbrelEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForEnbrelEndpoint, getPharmacyCostsValidationSessionId("createSessionJsonEnbrelData.json")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForEnbrelEndpoint, pharmacyCostsValidationSessionId)
                + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
        }
        assertEquals(response.getStatusCode(), 200);
        idForEnbrel = response.jsonPath().get("MedicarePlans[0].ID");
    }

    @Test(priority = 2)
    public void testVerifyLoadDrugDataForEnbrelWithInvalidSessionId() {
        String loadDrugDataForEnbrelEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForEnbrelEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForEnbrelEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest load drug data for enbrel with invalid SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifyLoadDrugDataForEnbrelWithBlankSessionId() {
        String loadDrugDataForEnbrelEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForEnbrelEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForEnbrelEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest load drug data for enbrel with blank SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 4)
    public void testVerifyValidateEnbrelCostPhasesForPlanIdToCheck() {
        String validateEnbrelCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForEnbrel);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateEnbrelCostPhasesForPlanIdToCheckEndpoint, pharmacyCostsValidationSessionId) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateEnbrelCostPhasesForPlanIdToCheckEndpoint, pharmacyCostsValidationSessionId) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate enbrel cost phases for #PlanIdToCheck case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testVerifyValidateEnbrelCostPhasesForPlanIdToCheckWithInvalidSessionId() {
        String validateEnbrelCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForEnbrel);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateEnbrelCostPhasesForPlanIdToCheckEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateEnbrelCostPhasesForPlanIdToCheckEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate enbrel cost phases for #PlanIdToCheck with invalid SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifyValidateEnbrelCostPhasesForPlanIdToCheckWithBlankSessionId() {
        String validateEnbrelCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForEnbrel);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateEnbrelCostPhasesForPlanIdToCheckEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateEnbrelCostPhasesForPlanIdToCheckEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate enbrel cost phases for #PlanIdToCheck with blank SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 7)
    public void testVerifyLoadDrugDataForHumira() {
        String loadDrugDataForHumiraEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForHumiraEndpoint, getPharmacyCostsValidationSessionId("createSessionJsonHumiraData.json")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForHumiraEndpoint, pharmacyCostsValidationSessionId) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
        }
        assertEquals(response.getStatusCode(), 200);
        idForHumira = response.jsonPath().get("MedicarePlans[0].ID");
    }

    @Test(priority = 8)
    public void testVerifyLoadDrugDataForHumiraWithInvalidSessionId() {
        String loadDrugDataForHumiraEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForHumiraEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForHumiraEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest load drug data from humira with invalid SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifyLoadDrugDataForHumiraWithBlankSessionId() {
        String loadDrugDataForHumiraEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForHumiraEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForHumiraEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest load drug data from humira with blank SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 10)
    public void testVerifyValidateHumiraCostPhasesForPlanIdToCheck() {
        String validateHumiraCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForHumira);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateHumiraCostPhasesForPlanIdToCheckEndpoint, pharmacyCostsValidationSessionId) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateHumiraCostPhasesForPlanIdToCheckEndpoint, pharmacyCostsValidationSessionId) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate humira cost phases for #PlanIdToCheck case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        Integer monthId = response.jsonPath().get("PharmacyCosts[0].MonthlyCosts[0].MonthID");
        assertTrue(monthId instanceof Integer);
    }

    @Test(priority = 11)
    public void testVerifyValidateHumiraCostPhasesForPlanIdToCheckWithInvalidSessionId() {
        String validateHumiraCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForHumira);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateHumiraCostPhasesForPlanIdToCheckEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateHumiraCostPhasesForPlanIdToCheckEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate humira cost phases for #PlanIdToCheck with invalid SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 12)
    public void testVerifyValidateHumiraCostPhasesForPlanIdToCheckWithBlankSessionId() {
        String validateHumiraCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForHumira);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateHumiraCostPhasesForPlanIdToCheckEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateHumiraCostPhasesForPlanIdToCheckEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate humira cost phases for #PlanIdToCheck with blank SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 13)
    public void testVerifyLoadDrugDataForBupropion() {
        String loadDrugDataForBupropionEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForBupropionEndpoint, getPharmacyCostsValidationSessionId("createSessionJsonBupropionData.json")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForBupropionEndpoint, pharmacyCostsValidationSessionId) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
        }
        assertEquals(response.getStatusCode(), 200);
        idForBupropion = response.jsonPath().get("MedicarePlans[0].ID");
    }

    @Test(priority = 14)
    public void testVerifyLoadDrugDataForBupropionWithInvalidSessionId() {
        String loadDrugDataForBupropionEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForBupropionEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForBupropionEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest load drug data from Bupropion with invalid SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 15)
    public void testVerifyLoadDrugDataForBupropionWithBlankSessionId() {
        String loadDrugDataForBupropionEndpoint = prop.getProperty("PharmacyCostsValidation_LoadDrugData").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(loadDrugDataForBupropionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(loadDrugDataForBupropionEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_LoadDrugDataTestData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest load drug data from Bupropion with blank SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 16)
    public void testVerifyValidateBupropionCostPhasesForPlanIdToCheck() {
        String validateBupropionCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForBupropion);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateBupropionCostPhasesForPlanIdToCheckEndpoint, pharmacyCostsValidationSessionId) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateBupropionCostPhasesForPlanIdToCheckEndpoint, pharmacyCostsValidationSessionId) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate Bupropion cost phases for #PlanIdToCheck case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        Integer monthId = response.jsonPath().get("PharmacyCosts[0].MonthlyCosts[0].MonthID");
        assertTrue(monthId instanceof Integer);
    }

    @Test(priority = 17)
    public void testVerifyValidateBupropionCostPhasesForPlanIdToCheckWithInvalidSessionId() {
        String validateBupropionCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForBupropion);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateBupropionCostPhasesForPlanIdToCheckEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateBupropionCostPhasesForPlanIdToCheckEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate Bupropion cost phases for #PlanIdToCheck with invalid SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 18)
    public void testVerifyValidateBupropionCostPhasesForPlanIdToCheckWithBlankSessionId() {
        String validateBupropionCostPhasesForPlanIdToCheckEndpoint = prop.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheck").replace("{SessionID}", "%s").replace("{PlanIdToCheck}", idForBupropion);
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(validateBupropionCostPhasesForPlanIdToCheckEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(validateBupropionCostPhasesForPlanIdToCheckEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PharmacyCostsValidation_ValidateCostPhasesForPlanIdToCheckData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Test PharmacyCostsValidationSpecTest validate Bupropion cost phases for #PlanIdToCheck with blank SessionID case response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    private String getPharmacyCostsValidationSessionId(String fileName) {
        String pharmacyCostsValidationSessionIdData = commonMethods.getTestDataPath() + fileName;
        pharmacyCostsValidationSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(pharmacyCostsValidationSessionIdData));
        return pharmacyCostsValidationSessionId;
    }
}