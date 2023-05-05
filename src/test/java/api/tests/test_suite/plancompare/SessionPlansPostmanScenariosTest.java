package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.ConditionalSkipTestAnalyzer;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.helpers.env_annotations.ExecuteOnProd;
import api.base.helpers.env_annotations.ExecuteOnTest;
import api.base.helpers.env_annotations.SkipOnProd;
import api.base.helpers.env_annotations.SkipOnTest;
import api.base.reporter.ExtentLogger;
import api.tests.utils.SessionIdGeneration;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

@Listeners(value = ConditionalSkipTestAnalyzer.class)
public class SessionPlansPostmanScenariosTest {
    PostRequest postRequest = new PostRequest();
    GetRequest getRequest = new GetRequest();
    TokenGeneration tokenGeneration = new TokenGeneration();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    private final Map<String, String> tokens = envInstanceHelper.getEnvironment();

    private String planSessionId;
    private String getPlanType;
    private String medicarePlansID;
    private String getMethod;
    private String getPlanSubType;
    private String getUrl;
    private String getPlanYear;
    private String getMedicarePlansId;
    private String getID;
    private String requestURL, requestBody;
    private String sessionIdWithMedGapQuestions, sessionIdWithDentalVisionQuestions, sessionIdHip;

    @Test
    public void testVerifyGetPlansSCS_43234() {
        commonMethods.usePropertyFileData("PC-API");
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, getSessionPlansSessionId()) +
                dataProp.getProperty("Plans_GetPlans_SCS_43234"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlans_SCS_43234");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plans PlansSCS_43234 response body :- " + response.asPrettyString());
        }
        medicarePlansID = response.jsonPath().getString("MedicarePlans[0].ID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 1)
    public void testVerifyGetPlansWTWIssue() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansWTWIssue"));
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansWTWIssue");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plans WTW Issue response body :- " + response.asPrettyString());
        }
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @SkipOnTest
    @Test(priority = 2)
    public void testVerifyGetMedigapPlans() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, testCreateSessionWithMedGapQuestions()) +
                dataProp.getProperty("Plans_GetMedigapPlans"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, sessionIdWithMedGapQuestions) +
                dataProp.getProperty("Plans_GetMedigapPlans");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Medigap Plans response body :- " + response.asPrettyString());
        }
        getID = response.jsonPath().getString("MedigapPlans[0].ID");
        assertFalse(getID.isEmpty());
        getPlanType = response.jsonPath().getString("MedigapPlans[0].PlanType");
        assertEquals(getPlanType, dataProp.getProperty("MEDIGAP_PLAN_TYPE"));
        assertEquals(response.getStatusCode(), 200);
    }

    @ExecuteOnTest
    @Test(priority = 3)
    public void testVerifyGetMedigapPlansForTestEnv() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, testCreateSessionWithMedGapQuestions()) +
                dataProp.getProperty("Plans_GetMedigapPlans"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, sessionIdWithMedGapQuestions) +
                dataProp.getProperty("Plans_GetMedigapPlans");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Medigap Plans response body :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 4)
    public void testVerifyGetDentalPlans() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, testCreateSessionWithDentalVisionQuestions()) +
                dataProp.getProperty("Plans_GetDentalPlans"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, sessionIdWithDentalVisionQuestions) +
                dataProp.getProperty("Plans_GetDentalPlans");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Dental Plans response body :- " + response.asPrettyString());
        }
        getPlanType = response.jsonPath().getString("AncillaryPlans[0].PlanType");
        getPlanSubType = response.jsonPath().getString("AncillaryPlans[0].PlanSubType");
        getID = response.jsonPath().getString("AncillaryPlans[0].ID");
        assertFalse(getID.isEmpty());
        assertEquals(getPlanType, dataProp.getProperty("DENTAL_PLAN_TYPE"));
        assertTrue(getPlanSubType.contains(dataProp.getProperty("DENTAL_PLAN_SUB_TYPE")));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testVerifyGetVisionPlans() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, testCreateSessionWithDentalVisionQuestions()) +
                dataProp.getProperty("Plans_GetVisionPlans"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, sessionIdWithDentalVisionQuestions) +
                dataProp.getProperty("Plans_GetVisionPlans");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Vision Plans response body :- " + response.asPrettyString());
        }
        getPlanType = response.jsonPath().getString("AncillaryPlans[0].PlanType");
        getPlanSubType = response.jsonPath().getString("AncillaryPlans[0].PlanSubType");
        getID = response.jsonPath().getString("AncillaryPlans[0].ID");
        assertFalse(getID.isEmpty());
        assertEquals(getPlanType, dataProp.getProperty("VISIONS_PLAN_TYPE"));
        assertTrue(getPlanSubType.contains(dataProp.getProperty("VISIONS_PLAN_SUB_TYPE")));
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 6)
    public void testVerifyGetHIPPlans() {
        String plansEndpoint = prop.getProperty("Plans_GetHipPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, createHipSession()) +
                dataProp.getProperty("Plans_GetHIPPlans"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, sessionIdHip) +
                dataProp.getProperty("Plans_GetHIPPlans");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get HIP Plans response body :- " + response.asPrettyString());
        }
        getID = response.jsonPath().getString("AncillaryPlans[0].ID");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 7)
    public void testVerifyGetPlanDetails() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsEndPoint").replace("{PlansID}", "%s"), medicarePlansID));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsEndPoint").replace("{PlansID}", "%s"), medicarePlansID);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details response body :- " + response.asPrettyString());
        }
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 8)
    public void testVerifyGetPlanDetailsMedSup() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsMedSup").replace("{PlansID}", "%s"), medicarePlansID));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsMedSup").replace("{PlansID}", "%s"), medicarePlansID);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details MedSup response body :- " + response.asPrettyString());
        }
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 9)
    public void testVerifyGetPlanDetailsDental() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsDental").replace("{PlansID}", "%s"), medicarePlansID));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsDental").replace("{PlansID}", "%s"), medicarePlansID);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details Dental response body :- " + response.asPrettyString());
        }
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 10)
    public void testVerifyGetPlanDetailsVision() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsVision").replace("{PlansID}", "%s"), medicarePlansID));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsVision").replace("{PlansID}", "%s"), medicarePlansID);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details Vision response body :- " + response.asPrettyString());
        }
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 11)
    public void testVerifyGetPlanDetailsHardcoded() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsHardcoded").replace("{PlansID}", "%s"), medicarePlansID));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                String.format(dataProp.getProperty("Plans_GetPlanDetailsHardcoded").replace("{PlansID}", "%s"), medicarePlansID);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plan Details Hardcoded response body :- " + response.asPrettyString());
        }
        getMedicarePlansId = response.jsonPath().getString("ID");
        getPlanYear = response.jsonPath().getString("PlanYear");
        assertEquals(getMedicarePlansId, medicarePlansID);
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 12)
    public void testVerifyGetPlansPlanSmart() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansPlanSmartPostman"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansPlanSmartPostman");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Get Plans PlanSmart response body :- " + response.asPrettyString());
        }
        getID = response.jsonPath().getString("MedicarePlans[0].ID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertFalse(getID.isEmpty());
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 13)
    public void testVerifyGetPlansManyParams() {
        String plansEndpoint = prop.getProperty("Plans_GetPlans").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansManyParams"));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEndpoint, planSessionId) +
                dataProp.getProperty("Plans_GetPlansManyParams");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
        }
        getID = response.jsonPath().getString("MedicarePlans[0].ID");
        getPlanYear = response.jsonPath().getString("MedicarePlans[0].PlanYear");
        assertFalse(getID.isEmpty());
        assertTrue(Integer.parseInt(getPlanYear) >= 2010 && Integer.parseInt(getPlanYear) <= 2023, "Plan year should be between 2010 & 2023");
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 14)
    public void testVerifyPostEnroll() {
        String postPlansEnrollmentBodyData = commonMethods.getTestDataPath() + "enrollData.json";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId, medicarePlansID),
                fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId, medicarePlansID);
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Post Enroll response body :- " + response.asPrettyString());
        }
        getMethod = response.jsonPath().getString("Method");
        getUrl = response.jsonPath().getString("Url");
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertFalse(getUrl.isEmpty());
        assertEquals(response.getStatusCode(), 200);
    }

    @SkipOnProd
    @Test(priority = 15)
    public void testVerifyPostPlanEnrollJson() {
        String postPlansEnrollmentBodyData = commonMethods.getTestDataPath() + "planEnrollJson.json";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId, medicarePlansID),
                fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId, medicarePlansID);
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Plan Enroll Json response body :- " + response.asPrettyString());
        }
        getMethod = response.jsonPath().getString("Method");
        getUrl = response.jsonPath().getString("Url");
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertFalse(getUrl.isEmpty());
        assertEquals(response.getStatusCode(), 200);
    }

    @ExecuteOnProd
    @Test(priority = 16)
    public void testVerifyPostPlanEnrollJsonForProdEnv() {
        String postPlansEnrollmentBodyData = commonMethods.getTestDataPath() + "planEnrollJson.json";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s").replace("{PlansID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId, dataProp.getProperty("ENROLL_PLAN_ID_WITH_JSON")),
                fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId, dataProp.getProperty("ENROLL_PLAN_ID_WITH_JSON"));
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Plan Enroll Json response body :- " + response.asPrettyString());
        }
        getMethod = response.jsonPath().getString("Method");
        getUrl = response.jsonPath().getString("Url");
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertFalse(getUrl.isEmpty());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 17)
    public void testVerifyPostPlanEnrollJsonMinimum() {
        String postPlansEnrollmentBodyData = commonMethods.getTestDataPath() + "planEnrollmentJsonMinimum.json";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnroll").replace("{SessionID}", "%s")
                .replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID"));
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "JSON", prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId) +
                        dataProp.getProperty("PlanEnrollJSONMinimum").replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId) +
                dataProp.getProperty("PlanEnrollJSONMinimum").replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Plan Enroll Json With Minimum data response body :- " + response.asPrettyString());
        }
        getMethod = response.jsonPath().getString("Method");
        getUrl = response.jsonPath().getString("Url");
        assertEquals(getMethod, dataProp.getProperty("ENROLL_METHOD"));
        assertFalse(getUrl.isEmpty());
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 18)
    public void testVerifyPlanEnrollWithRiders() {
        String postPlansEnrollmentBodyData = commonMethods.getTestDataPath() + "planEnrollWithRiders.xml";
        String plansEnrollEndpoint = prop.getProperty("Plans_PlanEnrollment").replace("{SessionID}", "%s");
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                        tokens.get("basic-auth")), "XML", prop.getProperty("sessionId-Endpoint") +
                        String.format(plansEnrollEndpoint, planSessionId) + dataProp.getProperty("PlanEnrollWithRiders").
                        replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID")),
                fileReader.getTestJsonFile(postPlansEnrollmentBodyData));
        requestURL = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(plansEnrollEndpoint, planSessionId) + dataProp.getProperty("PlanEnrollWithRiders").
                replace("{PlansID}", dataProp.getProperty("ENROLL_PLAN_ID")).replace("{RiderID}", dataProp.getProperty("ENROLL_RIDER_ID"));
        requestBody = fileReader.getTestJsonFile(postPlansEnrollmentBodyData);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestURL);
            ExtentLogger.pass("Request body is :- " + requestBody);
            ExtentLogger.pass("Plan Enroll Json With Riders data response body :- " + response.asPrettyString());
        }
        getMethod = response.jsonPath().getString("Method");
        assertEquals(response.getStatusCode(), 200);
    }

    private String testCreateSessionWithMedGapQuestions() {
        String createSessionWithMedGapQuestions = commonMethods.getTestDataPath() + "createSessionWithMedGapQuestions.json";
        sessionIdWithMedGapQuestions = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionWithMedGapQuestions));
        return sessionIdWithMedGapQuestions;
    }

    private String testCreateSessionWithDentalVisionQuestions() {
        String createSessionWithDentalVisionQuestions = commonMethods.getTestDataPath() + "createSessionWithDentalVisionQuestions.json";
        sessionIdWithDentalVisionQuestions = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionWithDentalVisionQuestions));
        return sessionIdWithDentalVisionQuestions;
    }

    private String createHipSession() {
        String createSessionHip = commonMethods.getTestDataPath() + "createSessionHipData.json";
        sessionIdHip = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionHip));
        return sessionIdHip;
    }

    private String getSessionPlansSessionId() {
        String postCreateSessionData = commonMethods.getTestDataPath() + "createSessionApiData.json";
        planSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(postCreateSessionData));
        return planSessionId;
    }
}