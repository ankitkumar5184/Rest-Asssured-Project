package api.tests.test_suite.plancompare;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.ConditionalSkipTestAnalyzer;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.helpers.env_annotations.SkipOnProd;
import api.base.helpers.env_annotations.SkipOnTest;
import api.base.reporter.ExtentLogger;
import api.tests.config.enums.GeneralErrorMessages;
import api.tests.config.enums.UserNamePasswords;
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
public class ExistingMemberSearchPDF2020SpecTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();
    private String existingMemberSearchPDF2020SessionId;
    private String memberFirstName;
    private String memberLastName, requestUrl;

    @Test(priority = 1)
    public void testVerifySearchByMemberName() {
        commonMethods.usePropertyFileData("PC-API");
        String searchByMemberNameEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchByMemberNameEndpoint, getExistingMemberSearchPDF2020SessionId()) + dataProp.getProperty("SearchByMemberNameData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint")+ String.format(searchByMemberNameEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchByMemberNameData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Request URL is :- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
        memberFirstName = response.jsonPath().get("[0].FirstName");
        assertEquals(memberFirstName, UserNamePasswords.BMAGENT1003_TEXT.value);
        memberLastName = response.jsonPath().get("[0].LastName");
        assertEquals(memberLastName, UserNamePasswords.BMDRXTEST1234_TEXT.value);
        assertTrue(response.jsonPath().get("[0].Phone") instanceof String);
    }

    @Test(priority = 2)
    public void testVerifySearchByMemberNameWithInvalidSessionId() {
        String searchByMemberNameEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchByMemberNameEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchByMemberNameData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint")+ String.format(searchByMemberNameEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchByMemberNameData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search by member name with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 3)
    public void testVerifySearchByMemberNameWithBlankSessionId() {
        String searchByMemberNameEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchByMemberNameEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("SearchByMemberNameData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchByMemberNameEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("SearchByMemberNameData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search by member name with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 4)
    public void testVerifySearchForEnrollmentWithConfirmationNumber() {
        String searchForEnrollmentWithConfirmationNumberEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, existingMemberSearchPDF2020SessionId)
                + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumberData").replace("{FirstName}", memberFirstName).replace("{LastName}", memberLastName));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, existingMemberSearchPDF2020SessionId)
                + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumberData").replace("{FirstName}", memberFirstName).replace("{LastName}", memberLastName);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment with confirmation number case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testVerifySearchForEnrollmentWithConfirmationNumberWithInvalidSessionId() {
        String searchForEnrollmentWithConfirmationNumberEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumberData").replace("{FirstName}", memberFirstName).replace("{LastName}", memberLastName));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumberData").replace("{FirstName}", memberFirstName).replace("{LastName}", memberLastName);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment with confirmation number with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 6)
    public void testVerifySearchForEnrollmentWithConfirmationNumberWithBlankSessionId() {
        String searchForEnrollmentWithConfirmationNumberEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumberData").replace("{FirstName}", memberFirstName).replace("{LastName}", memberLastName));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentWithConfirmationNumberEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentWithConfirmationNumberData").replace("{FirstName}", memberFirstName).replace("{LastName}", memberLastName);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment with confirmation number with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 7)
    public void testVerifySearchForEnrollmentJustPhone() {
        String searchForEnrollmentJustPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustPhoneEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentJustPhoneData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustPhoneEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentJustPhoneData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().get("[0].FirstName"));
        assertNotNull(response.jsonPath().get("[0].LastName"));
        assertTrue(response.jsonPath().get("[0].Phone") instanceof String);
    }

    @Test(priority = 8)
    public void testVerifySearchForEnrollmentJustPhoneWithInvalidSessionId() {
        String searchForEnrollmentJustPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustPhoneEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentJustPhoneData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustPhoneEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentJustPhoneData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment just phone with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 9)
    public void testVerifySearchForEnrollmentJustPhoneWithBlankSessionId() {
        String searchForEnrollmentJustPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustPhoneEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentJustPhoneData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustPhoneEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentJustPhoneData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment just phone with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 10)
    public void testVerifySearchForEnrollmentJustConf() {
        String SearchForEnrollmentJustConfEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(SearchForEnrollmentJustConfEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentJustConfData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(SearchForEnrollmentJustConfEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentJustConfData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment just conf case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 11)
    public void testVerifySearchForEnrollmentJustConfWithInvalidSessionId() {
        String SearchForEnrollmentJustConfEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(SearchForEnrollmentJustConfEndpoint, GeneralErrorMessages.INVALID_TEXT.value + dataProp.getProperty("SearchForEnrollmentJustConfData")));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(SearchForEnrollmentJustConfEndpoint, GeneralErrorMessages.INVALID_TEXT.value + dataProp.getProperty("SearchForEnrollmentJustConfData"));
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment just conf with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 12)
    public void testVerifySearchForEnrollmentJustConfWithBlankSessionId() {
        String SearchForEnrollmentJustConfEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(SearchForEnrollmentJustConfEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentJustConfData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(SearchForEnrollmentJustConfEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentJustConfData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment just conf with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 13)
    public void testVerifySearchForEnrollmentJustLast() {
        String searchForEnrollmentJustLastEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustLastEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentJustLastData").replace("{LastName}", memberLastName));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustLastEndpoint, existingMemberSearchPDF2020SessionId) +
                dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentJustLastData").replace("{LastName}", memberLastName);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().get("[0].FirstName"), UserNamePasswords.BMAGENT1003_TEXT.value);
        assertEquals(response.jsonPath().get("[0].LastName"), UserNamePasswords.BMDRXTEST1234_TEXT.value);
        assertTrue(response.jsonPath().get("[0].Phone") instanceof String);
    }

    @Test(priority = 14)
    public void testVerifySearchForEnrollmentJustLastWithInvalidSessionId() {
        String searchForEnrollmentJustLastEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustLastEndpoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentJustLastData").replace("{LastName}", memberLastName));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustLastEndpoint, GeneralErrorMessages.INVALID_TEXT.value) +
                dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentJustLastData").replace("{LastName}", memberLastName);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment just last name with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 15)
    public void testVerifySearchForEnrollmentJustLastWithBlankSessionId() {
        String searchForEnrollmentJustLastEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForEnrollmentJustLastEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentJustLastData").replace("{LastName}", memberLastName));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentJustLastEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) +
                dataProp.getProperty("ExistingMemberSearchPDF2020SpecTest_SearchForEnrollmentJustLastData").replace("{LastName}", memberLastName);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment just last name with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @SkipOnTest
    @SkipOnProd
    @Test(priority = 16)
    public void testVerifySearchForEnrollmentPartialPhone() {
        String searchForEnrollmentPartialPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentPartialPhoneData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneEndpoint, existingMemberSearchPDF2020SessionId) +
                dataProp.getProperty("SearchForEnrollmentPartialPhoneData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().get("[0].FirstName"));
        assertNotNull(response.jsonPath().get("[0].LastName"));
        assertTrue(response.jsonPath().get("[0].Phone") instanceof String);
    }

    @Test(priority = 17)
    public void testVerifySearchForEnrollmentPartialPhoneWithInvalidSessionId() {
        String searchForEnrollmentPartialPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentPartialPhoneData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentPartialPhoneData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment partial phone with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 18)
    public void testVerifySearchForEnrollmentPartialPhoneWithBlankSessionId() {
        String searchForEnrollmentPartialPhoneEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentPartialPhoneData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentPartialPhoneData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment partial phone with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @SkipOnTest
    @SkipOnProd
    @Test(priority = 19)
    public void testVerifySearchForEnrollmentPartialPhoneAndDob() {
        String searchForEnrollmentPartialPhoneAndDobEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
        }
        assertEquals(response.getStatusCode(), 200);
        assertNotNull(response.jsonPath().get("[0].FirstName"));
        assertNotNull(response.jsonPath().get("[0].LastName"));
        assertTrue(response.jsonPath().get("[0].Phone") instanceof String);
    }

    @Test(priority = 20)
    public void testVerifySearchForEnrollmentPartialPhoneAndDobWithInvalidSessionId() {
        String searchForEnrollmentPartialPhoneAndDobEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment partial phone and dob with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 21)
    public void testVerifySearchForEnrollmentPartialPhoneAndDobWithBlankSessionId() {
        String searchForEnrollmentPartialPhoneAndDobEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +
                String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForEnrollmentPartialPhoneAndDobEndpoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("SearchForEnrollmentPartialPhoneAndDobData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for enrollment partial phone and dob with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 22)
    public void testVerifySearchForViaHicn() {
        String searchForViaHicnEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") +String.format(searchForViaHicnEndpoint, existingMemberSearchPDF2020SessionId) +
                dataProp.getProperty("SearchForViaHicnData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForViaHicnEndpoint, existingMemberSearchPDF2020SessionId) + dataProp.getProperty("SearchForViaHicnData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for via hicn case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 23)
    public void testVerifySearchForViaHicnWithInvalidSessionId() {
        String searchForViaHicnEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForViaHicnEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForViaHicnData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForViaHicnEndpoint, GeneralErrorMessages.INVALID_TEXT.value) + dataProp.getProperty("SearchForViaHicnData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for via hicn with invalid SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 500);
        assertEquals(response.jsonPath().get("Message"), GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(response.jsonPath().get("ErrorCode"), GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
    }

    @Test(priority = 24)
    public void testVerifySearchForViaHicnWithBlankSessionId() {
        String searchForViaHicnEndpoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint")
                + String.format(searchForViaHicnEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForViaHicnData"));
        requestUrl = tokens.get("host") + prop.getProperty("sessionId-Endpoint") + String.format(searchForViaHicnEndpoint, dataProp.getProperty("BLANK_SESSION_ID")) + dataProp.getProperty("SearchForViaHicnData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + requestUrl);
            ExtentLogger.pass("Test ExistingMemberSearchPDF2020SpecTest search for via hicn with blank SessionID case response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    private String getExistingMemberSearchPDF2020SessionId() {
        String existingMemberSearchPDF2020SessionIdData = commonMethods.getTestDataPath() + "createSessionMemberSearch.json";
        existingMemberSearchPDF2020SessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(existingMemberSearchPDF2020SessionIdData));
        return existingMemberSearchPDF2020SessionId;
    }
}