package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
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
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class DrugToolsPricingSpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    GetRequest getRequest = new GetRequest();
    PostRequest postRequest = new PostRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();

    private String createSessionData;
    private String memberSearchSessionId;
    private String invalidSessionIdResponse;
    private String invalidSessionIdErrorCode;
    private String getSession_Phar2418209EndPoint;
    private String add_ToolsPhar2418209;
    private String getSession_Phar0581808EndPoint;
    private String add_ToolsPricingForMissingPharmacyId, requestUrl, requestBody;

    @Test(priority = 1)
    public void testFullCostForCertainDrugAndPlan() {
        commonMethods.usePropertyFileData("PC-API");
        String add_tools7679Data = commonMethods.getTestDataPath() + "tools_7679.json";
        requestBody = fileReader.getTestJsonFile(add_tools7679Data);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("DrugPricingTool_PlanDetail").replace("{PlanId_Tools_7679}", dataProp.getProperty("PlanId_Tools_7679")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("DrugPricingTool_PlanDetail").replace("{PlanId_Tools_7679}", dataProp.getProperty("PlanId_Tools_7679"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session For Full Cost For Certain Drug And Plan:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testDrugThatDrugFootnotesArePresent() {
        String getSessionDrugFootnotesEndPoint = prop.getProperty("DrugFootNotes_EndPoint").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSessionDrugFootnotesEndPoint, getSessionDrugFootnotesSessionId(), dataProp.getProperty("DrugFootNotes_ID")) + dataProp.getProperty("DrugFootNotes_EndPointData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSessionDrugFootnotesEndPoint, memberSearchSessionId, dataProp.getProperty("DrugFootNotes_ID")) +
                dataProp.getProperty("DrugFootNotes_EndPointData") ;
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session For Drug That Drug Footnotes Are Present:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    //  Test PC Pricing for SME-14805
    @Test(priority = 3)
    public void testPcPricing_1_forSme_14805() {
        getSession_Phar0581808EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_Phar0581808EndPoint, getSessionIdToolsPricing_PC_Phar0581808(), dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSession_Phar0581808EndPoint, memberSearchSessionId, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")) +
                dataProp.getProperty("PC_Pricing_SME-14805_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session For Pc Pricing 1 For Sme 14805:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void testPcPricing_2_forSme_14805() {
        getSession_Phar2418209EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_Phar2418209EndPoint, getSessionIdToolsPricing_PC_Phar2418209(), dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_2")) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSession_Phar2418209EndPoint, memberSearchSessionId, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_2")) + dataProp.getProperty("PC_Pricing_SME-14805_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session For Pc Pricing 2 For Sme 14805:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    //  Test Tools Pricing for SME-14805
    @Test(priority = 5)
    public void testToolsPricing_1_forSme_14805() {
        add_ToolsPhar2418209 = commonMethods.getTestDataPath() + "tools_pharm2418209.json";
        requestBody = fileReader.getTestJsonFile(add_ToolsPhar2418209);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session to Tools Pricing 1 For SME 14805:- " + response.asPrettyString());
        }
        String idValue = response.jsonPath().getString("PlanID");
        assertNotNull(idValue);
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testToolsPricing_2_forSme_14805() {
        add_ToolsPhar2418209 = commonMethods.getTestDataPath() + "tools_pharm2418209.json";
        requestBody = fileReader.getTestJsonFile(add_ToolsPhar2418209);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_2")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_2"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session to Tools Pricing 2 For SME 14805:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String idValue = response.jsonPath().getString("PlanID");
        assertNotNull(idValue);
    }

    @Test(priority = 7)
    public void testToolsPricing_3_forSme_14805() {
        String add_ToolsPhar0581808 = commonMethods.getTestDataPath() + "tools_pharm0581808.json";
        requestBody = fileReader.getTestJsonFile(add_ToolsPhar0581808);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session to Tools Pricing 3 For SME 14805:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String idValue = response.jsonPath().getString("PlanID");
        assertNotNull(idValue);
    }

    //  Test Tools Pricing For Missing Pharmacy ID Issue
    @Test(priority = 8)
    public void testToolsPricingForMissingPharmacyId_1_Issue() {
        add_ToolsPricingForMissingPharmacyId = commonMethods.getTestDataPath() + "tools_pharm2418209.json";
        requestBody = fileReader.getTestJsonFile(add_ToolsPricingForMissingPharmacyId);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session To Tools Pricing For Missing Pharmacy Id 1 Issue:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String idValue = response.jsonPath().getString("PlanID");
        assertNotNull(idValue);
    }

    @Test(priority = 9)
    public void testToolsPricingForMissingPharmacyId_2_Issue() {
        add_ToolsPricingForMissingPharmacyId = commonMethods.getTestDataPath() + "tools_pharm0581808_NewZip.json";
        requestBody = fileReader.getTestJsonFile(add_ToolsPricingForMissingPharmacyId);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session To Tools Pricing For Missing Pharmacy Id 2 Issue:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String idValue = response.jsonPath().getString("PlanID");
        assertNotNull(idValue);
    }

    //  Test PC Pricing Against New Tools EndPoint
    @Test(priority = 10)
    public void testTestPcPricingAgainstNewToolsEndPoint() {
        String getSession_PcPricing_Ca_10_EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_PcPricing_Ca_10_EndPoint, getSessionIdCa_10_PcPricingAgainstNewTools(), dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSession_PcPricing_Ca_10_EndPoint, memberSearchSessionId, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")) +
                dataProp.getProperty("PC_Pricing_SME-14805_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session For Test Pc Pricing Against New Tools:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    //  Test Tools Pricing Against New Tools EndPoint
    @Test(priority = 11)
    public void testToolsPricingAgainstNewToolsEndPoint() {
        String add_ToolsPricing = commonMethods.getTestDataPath() + "toolsCA_10drugs.json";
        requestBody = fileReader.getTestJsonFile(add_ToolsPricing);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session To Tools Pricing Against New Tools:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String idValue = response.jsonPath().getString("PlanID");
        assertNotNull(idValue);
    }

    //  Test Pc Vs Tools For Case With Drug Footnotes
    @Test(priority = 12)
    public void testPcVsToolsForCaseWithDrugFootnotes() {
        String getSession_PcVsToolsForFootnotesEndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_PcVsToolsForFootnotesEndPoint, getSessionId_Test_Ca_Oxy(), dataProp.getProperty("PLANID_DRUG_FOOT_NOTE_1")) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSession_PcVsToolsForFootnotesEndPoint, memberSearchSessionId, dataProp.getProperty("PLANID_DRUG_FOOT_NOTE_1")) + dataProp.getProperty("PC_Pricing_SME-14805_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session Pc Vs Tools For Case With Drug Footnotes:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    //  Test Tools Vs Pc For Case With Drug Footnotes
    @Test(priority = 13)
    public void testToolsVsPcForCaseWithDrugFootnotes() {
        String add_ToolsPricing = commonMethods.getTestDataPath() + "toolsCA_Oxy.json";
        requestBody = fileReader.getTestJsonFile(add_ToolsPricing);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session To Tools Vs Pc For Case With Drug Footnotes:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String idValue = response.jsonPath().getString("PlanID");
        assertNotNull(idValue);
    }

    //  Test Small PC Pricing Difference With Mail Order
    @Test(priority = 14)
    public void testSmall_Pc_PricingDifferenceWithMailOrder() {
        String getSession_PcSmallPricingDifferenceEndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_PcSmallPricingDifferenceEndPoint, getSessionId_Test_Ca_Pricing(), dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSession_PcSmallPricingDifferenceEndPoint, memberSearchSessionId, dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")) +
                dataProp.getProperty("PC_Pricing_SME-14805_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session Test Small PC Pricing Difference With Mail Order:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    //  Test Small Tools Pricing Difference With Mail Order
    @Test(priority = 15)
    public void testSmall_Tools_PricingDifferenceWithMailOrder() {
        String add_ToolsSmallPricing = commonMethods.getTestDataPath() + "toolsCA_Pricing.json";
        requestBody = fileReader.getTestJsonFile(add_ToolsSmallPricing);
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3")),
                requestBody);
        requestUrl = prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_3"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session To Small Tools Pricing Difference With Mail Order:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        String idValue = response.jsonPath().getString("PlanID");
        assertNotNull(idValue);
    }

    @Test(priority = 16)
    public void testPcPricingForSme_14805_WithInvalidSessions() {
        String getSession_Phar0581808EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_Phar0581808EndPoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + prop.getProperty("sessionId-Endpoint") + String.format(getSession_Phar0581808EndPoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("PC_Pricing_SME-14805_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session Pc Pricing For Sme-14805 With Invalid Sessions:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse, GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode, GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 17)
    public void testPcPricingForSme_14805_WithBlankSessions() {
        String getSession_Phar0581808EndPoint = prop.getProperty("PC_Pricing_SME-14805").replace("{SessionID}", "%s").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(getSession_Phar0581808EndPoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PC_Pricing_SME-14805_Data"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(getSession_Phar0581808EndPoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("PC_Pricing_SME-14805_Data");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Get Session Pc Pricing For Sme-14805 With Blank Sessions:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 18)
    public void testToolsPostAPIsWithBlankBody() {
        requestBody = CommonValues.BLANK_BODY_VALID_REQUEST.value;
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), "JSON",
                prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1")),
                requestBody);
        requestUrl = tokens.get("host") + prop.getProperty("Tools_Pricing_SME-14805").replace("{PlanId}", dataProp.getProperty("PLANID_PC_TOOLS_SME-14805_1"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Add Session To Tools Pricing Against New Tools:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 400);
    }

    private String getSessionDrugFootnotesSessionId() {
        createSessionData = commonMethods.getTestDataPath() + "createsession_footnoes.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionIdToolsPricing_PC_Phar2418209() {
        createSessionData = commonMethods.getTestDataPath() + "createsession_pharm2418209.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionIdToolsPricing_PC_Phar0581808() {
        createSessionData = commonMethods.getTestDataPath() + "createsession_pharm0581808.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionIdCa_10_PcPricingAgainstNewTools() {
        createSessionData = commonMethods.getTestDataPath() + "createSessionCA_10drugs.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionId_Test_Ca_Oxy() {
        createSessionData = commonMethods.getTestDataPath() + "createSessionCA_Oxy.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }

    private String getSessionId_Test_Ca_Pricing() {
        createSessionData = commonMethods.getTestDataPath() + "createSessionCA_Pricing.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }
}