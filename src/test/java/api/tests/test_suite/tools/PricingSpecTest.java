package api.tests.test_suite.tools;

import api.base.core.PostRequest;
import api.base.helpers.CommonMethods;
import api.base.helpers.EnvInstanceHelper;
import api.base.helpers.FileReader;
import api.base.reporter.ExtentLogger;
import api.tests.utils.TokenGeneration;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;
import static api.base.helpers.CommonMethods.dataProp;
import static api.base.helpers.CommonMethods.prop;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PricingSpecTest {

    TokenGeneration tokenGeneration = new TokenGeneration();
    EnvInstanceHelper envInstanceHelper = new EnvInstanceHelper();
    Map<String, String> tokens = envInstanceHelper.getEnvironment();
    FileReader fileReader = new FileReader();
    PostRequest postRequest = new PostRequest();
    CommonMethods commonMethods = new CommonMethods();

    private String requestURL, requestBody;

    @Test(priority = 0)
    public void testPricingForSingleCase() {
        commonMethods.usePropertyFileData("TOOLS-API");
        String pricingForNameEndpoint = prop.getProperty("Pricing_Calculate");
        String pricingForNameData = commonMethods.getTestDataPath() + "singlePricing.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", pricingForNameEndpoint + dataProp.getProperty("PRICING_CALCULATE"), fileReader.getTestJsonFile(pricingForNameData));
        requestURL = tokens.get("host") + pricingForNameEndpoint + dataProp.getProperty("PRICING_CALCULATE");
        requestBody = fileReader.getTestJsonFile(pricingForNameData);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        ExtentLogger.pass("Test pricing for single case response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull("APTCValue");
    }

    @Test(priority = 1)
    public void testPricingForNameFamily() {
        String pricingForNameEndpoint = prop.getProperty("Pricing_Calculate");
        String pricingForFamily = commonMethods.getTestDataPath() + "famPricing.json";
        Response response = postRequest.postMethod(tokens.get("host"), tokenGeneration.getAccess_token(tokens.get("auth-host"), tokens.get("basic-auth")),
                "JSON", pricingForNameEndpoint + dataProp.getProperty("PRICING_CALCULATE"), fileReader.getTestJsonFile(pricingForFamily));
        requestURL = tokens.get("host") + pricingForNameEndpoint + dataProp.getProperty("PRICING_CALCULATE");
        requestBody = fileReader.getTestJsonFile(pricingForFamily);
        ExtentLogger.pass("Request URL is :- " + requestURL);
        ExtentLogger.pass("Request body is :- " + requestBody);
        ExtentLogger.pass("Test pricing for name family response body :- " + response.asPrettyString());
        assertEquals(response.getStatusCode(), 200);
        assertNotNull("APTCValue");
    }
}