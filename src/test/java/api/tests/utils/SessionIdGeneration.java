package api.tests.utils;

import api.base.reporter.ExtentLogger;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static api.base.helpers.CommonMethods.prop;

/*
This class is used for generating the sessionID used by PC API
 */
public class SessionIdGeneration extends TokenGeneration {
    protected static String sessionId;

    public String getSessionId(String host, String jsonData) {
        return generateSessionId(host, jsonData);
    }

    private String generateSessionId(String host, String jsonData) {
        RestAssured.baseURI = host;
        RequestSpecification request = RestAssured.given();
        try {
            request.header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + access_token);
            Response responseFormToken = request.body(jsonData).post(prop.getProperty("sessionId-Endpoint"));
            sessionId = responseFormToken.jsonPath().getString("[0].SessionID");
        } catch (IndexOutOfBoundsException e) {
            ExtentLogger.fail("IndexOutOfBoundsException: " + e.getMessage());
        } catch (Exception e) {
            ExtentLogger.fail("Exception: " + e.getMessage());
        }
        return sessionId;
    }
}