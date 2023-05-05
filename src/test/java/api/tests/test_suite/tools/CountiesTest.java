package api.tests.test_suite.tools;

import api.base.core.GetRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.reporter.ExtentLogger;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;

public class CountiesTest {
    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    GetRequest getRequest = new GetRequest();
    CommonMethods commonMethods = new CommonMethods();

    private String requestURL;

    @Test
    public void testCounties() {
        commonMethods.usePropertyFileData("TOOLS-API");
        String countiesEndPoint = prop.getProperty("Counties_GetCounties");
        Response response = getRequest.getMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"),
                tokens.get("basic-auth")), countiesEndPoint + dataProp.getProperty("COUNTIES_VALUES"));
        requestURL = tokens.get("host") + countiesEndPoint + dataProp.getProperty("COUNTIES_VALUES");
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Test counties response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
    }
}
