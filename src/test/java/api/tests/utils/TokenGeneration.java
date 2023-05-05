package api.tests.utils;

import api.base.helpers.CommonMethods;
import api.base.reporter.ExtentLogger;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static api.base.helpers.CommonMethods.prop;

/*
This class is used for generating the token for PC API and Tools API
 */
public class TokenGeneration {
    protected static String access_token;
    CommonMethods commonMethods = new CommonMethods();

    public String getAccess_token(String authHost, String basicAuth) {
        return generateBearerToken(authHost, basicAuth);
    }

    private String generateBearerToken(String authHost, String basicAuth) {
        commonMethods.usePropertyFileForEndpoints();
        RestAssured.baseURI = authHost;
        try {
            RequestSpecification httpRequest = RestAssured.given()
                    .header("Authorization", "Basic " + basicAuth)
                    .formParam("grant_type", "client_credentials");
            Response response = httpRequest.post(prop.getProperty("auth_token-Endpoint"));
            JsonPath jsonPathEvaluator = response.jsonPath();
            access_token = jsonPathEvaluator.get("access_token");
        } catch (IndexOutOfBoundsException e) {
            ExtentLogger.fail("IndexOutOfBoundsException: " + e.getMessage());
        } catch (Exception e) {
            ExtentLogger.fail("Exception: " + e.getMessage());
        }
        return access_token;
    }
}