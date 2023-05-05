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
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.*;

public class MemberSearchTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    FileReader fileReader = new FileReader();
    CommonMethods commonMethods = new CommonMethods();
    SessionIdGeneration sessionIdGeneration = new SessionIdGeneration();

    private String getSessionMemberSearchEndPoint;
    private String getSessionMemberSearchPdfEndPoint;
    protected String memberSearchSessionId;
    private String invalidSessionIdResponse;
    private String invalidSessionIdErrorCode;
    private String requestURL;

    @Test(priority = 1)
    public void testGetSessionMemberSearch(){
        commonMethods.usePropertyFileData("PC-API");
        getSessionMemberSearchEndPoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), prop.getProperty("sessionId-Endpoint") + String.format(getSessionMemberSearchEndPoint, getSessionMemberSessionId())
                + dataProp.getProperty("MemberSearch_GetMemberEnrollmentsData"));
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionMemberSearchEndPoint, getSessionMemberSessionId())
                + dataProp.getProperty("MemberSearch_GetMemberEnrollmentsData");
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void testGetSessionMemberSearchWithInvalidSessionId(){
        getSessionMemberSearchEndPoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionMemberSearchEndPoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("MemberSearch_GetMemberEnrollmentsData");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session Member Search With Invalid SessionId Response:- " + response.asPrettyString());
        }
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse,GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode,GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 3)
    public void testGetSessionMemberSearchWithBlankSessionId(){
        getSessionMemberSearchEndPoint = prop.getProperty("MemberSearch_GetMemberEnrollments").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionMemberSearchEndPoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MemberSearch_GetMemberEnrollmentsData");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session Member Search With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    @Test(priority = 4)
    public void testGetSessionMemberSearchPdf(){
        commonMethods.usePropertyFileForEndpoints();
        commonMethods.usePropertyFileData("PC-API");
        getSessionMemberSearchPdfEndPoint = prop.getProperty("MemberSearch_GetBrokerMemberEnrollmentPDF").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionMemberSearchPdfEndPoint, getSessionMemberSessionId())
                + dataProp.getProperty("MemberSearch_GetBrokerMemberEnrollmentPDFData");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session Member Search Response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 5)
    public void testGetSessionMemberSearchPdfWithInvalidSessionId(){
        getSessionMemberSearchPdfEndPoint = prop.getProperty("MemberSearch_GetBrokerMemberEnrollmentPDF").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionMemberSearchPdfEndPoint, GeneralErrorMessages.INVALID_TEXT.value)
                + dataProp.getProperty("MemberSearch_GetBrokerMemberEnrollmentPDFData");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session Member Search-PDF With Invalid SessionId Response:- " + response.asPrettyString());
        }
        invalidSessionIdResponse = response.jsonPath().get("Message");
        invalidSessionIdErrorCode = response.jsonPath().get("ErrorCode");
        assertEquals(invalidSessionIdResponse,GeneralErrorMessages.RESUME_SESSION_FAILED.value);
        assertEquals(invalidSessionIdErrorCode,GeneralErrorMessages.INTERNAL_SERVER_ERROR.value);
        assertEquals(response.getStatusCode(), 500);
    }

    @Test(priority = 6)
    public void testGetSessionMemberSearchPdfWithBlankSessionId(){
        getSessionMemberSearchPdfEndPoint = prop.getProperty("MemberSearch_GetBrokerMemberEnrollmentPDF").replace("{SessionID}", "%s");
        requestURL = prop.getProperty("sessionId-Endpoint") + String.format(getSessionMemberSearchPdfEndPoint, dataProp.getProperty("BLANK_SESSION_ID"))
                + dataProp.getProperty("MemberSearch_GetBrokerMemberEnrollmentPDFData");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), requestURL);
        if (Boolean.parseBoolean(prop.getProperty("Extent_Extra_Logs_Enable"))) {
            ExtentLogger.pass("Request URL is :- " + tokens.get("host") + requestURL);
            ExtentLogger.pass("Get Session Member Search-PDF With Blank SessionId response:- " + response.asPrettyString());
        }
        assertEquals(response.getStatusCode(), 404);
    }

    private String getSessionMemberSessionId(){
        String createSessionData = commonMethods.getTestDataPath() + "createSessionMemberSearch.json";
        memberSearchSessionId = sessionIdGeneration.getSessionId(tokens.get("host"), fileReader.getTestJsonFile(createSessionData));
        return memberSearchSessionId;
    }
}