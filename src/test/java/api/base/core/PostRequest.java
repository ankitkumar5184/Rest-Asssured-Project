package api.base.core;

import api.base.reporter.ExtentLogger;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PostRequest {
   Response response;

    // Generic API Method for calling POST request, works for both JSON and XML requests
    public Response postMethod(String host, String accessToken, String jsonOrXml, String apiUrl, String testData) {
        try {
            RestAssured.baseURI = host;
            RequestSpecification httpRequest = RestAssured.given().
                    header("Authorization", "Bearer " + accessToken);
            switch (jsonOrXml) {
                case "JSON":
                    httpRequest.contentType(ContentType.JSON);
                    break;
                case "XML":
                    httpRequest.contentType(ContentType.XML);
                    break;
                default:
                    ExtentLogger.fail("Unknown contentType for post request. Valid values are JSON or XML");
            }
            httpRequest.body(testData);
            response = httpRequest.post(apiUrl);
        } catch (Exception e) {
            ExtentLogger.fail("Exception: " + e.getMessage());
        }
        return response;
    }
}