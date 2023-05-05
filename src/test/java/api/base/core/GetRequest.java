package api.base.core;

import api.base.reporter.ExtentLogger;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetRequest {

    Response response;

    // Generic API Method for calling GET request
    public Response getMethod(String host, String accessToken, String apiUrl) {
        try {
            RestAssured.baseURI = host;
            RequestSpecification httpRequest = RestAssured.given().
                    header("Authorization", "Bearer " + accessToken);
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.get(apiUrl);
        } catch (Exception e) {
            ExtentLogger.fail("Exception: " + e.getMessage());
        }
        return response;
    }
}