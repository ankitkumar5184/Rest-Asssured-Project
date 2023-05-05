package com.connecture.utils;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static com.connecture.base.BaseUISpec.ENV;

public class TestDataProvider {

    private static JSONObject workflowTestDataJson;
    private static JSONObject userTestDataJson;
    private static JSONObject hostedSiteTestDataJson2022;
    private static JSONObject hostedSiteTestDataJson2023;
    private static JSONObject publicAPIServiceDataJson;
    private static JSONObject publicAPISiteTokenDataJson;

    private static String workflowTestDataJsonFilename = "regression_config.json";
    private static String userTestDataJsonFilename = "user_config.json";
    private static String hostedSiteTestDataJsonFilename2022 = "site_config2022.json";
    private static String hostedSiteTestDataJsonFilename2023 = "site_config2023.json";
    private static String publicAPIServiceDataJsonFilename = "api_urls.json";
    private static String publicAPISiteTokenDataJsonFilename = "api_sites.json";

    private static String pathToTestDataFolder = "./src/test/resources/";

    static{
        try {
            TestDataProvider.workflowTestDataJson = new JSONObject(Files.readString(Paths.get(pathToTestDataFolder + workflowTestDataJsonFilename)));
            TestDataProvider.userTestDataJson = new JSONObject(Files.readString(Paths.get(pathToTestDataFolder + userTestDataJsonFilename)));
            TestDataProvider.hostedSiteTestDataJson2022 = new JSONObject(Files.readString(Paths.get(pathToTestDataFolder + hostedSiteTestDataJsonFilename2022)));
            TestDataProvider.hostedSiteTestDataJson2023 = new JSONObject(Files.readString(Paths.get(pathToTestDataFolder + hostedSiteTestDataJsonFilename2023)));
            TestDataProvider.publicAPIServiceDataJson = new JSONObject(Files.readString(Paths.get(pathToTestDataFolder + publicAPIServiceDataJsonFilename)));
            TestDataProvider.publicAPISiteTokenDataJson = new JSONObject(Files.readString(Paths.get(pathToTestDataFolder + publicAPISiteTokenDataJsonFilename)));
            //System.out.println("userTestDataJson: " + userTestDataJson); // Debug
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Some test data (ie zip) won't need to be different for every ENV, so default back to QA
    public static String getData(String tag){
        try {
            return TestDataProvider.workflowTestDataJson.getJSONObject(ENV).getString(tag);
        } catch (Throwable t) {
            return TestDataProvider.workflowTestDataJson.getJSONObject("QA").getString(tag);
        }
    }
    // For getting a string within an object
    public static String getDrugData(String drugTag, String drugType){
        try {
            return TestDataProvider.workflowTestDataJson.getJSONObject(ENV).getJSONObject(drugTag).getString(drugType);
        } catch (Throwable t) {
            return TestDataProvider.workflowTestDataJson.getJSONObject("QA").getJSONObject(drugTag).getString(drugType);
        }
    }

    public static String getFlexcareCarrierData(String qaMap, String param){
        try {
            return TestDataProvider.workflowTestDataJson.getJSONObject(ENV).getJSONObject(qaMap).getString(param);
        } catch (Throwable t) {
            return TestDataProvider.workflowTestDataJson.getJSONObject("QA").getJSONObject(qaMap).getString(param);
        }
    }


    public static String getSiteBaseUrl(String siteTag, String siteYear){
        if( siteYear.equals("2023")){
            return TestDataProvider.hostedSiteTestDataJson2023.getJSONObject(ENV).getJSONObject(siteTag).getString("siteURL");
        }
        if( siteYear.equals("2022")){
            return TestDataProvider.hostedSiteTestDataJson2022.getJSONObject(ENV).getJSONObject(siteTag).getString("siteURL");
        }
        return null;
    }

    public static String getSitePlatformUrl(String siteTag, String siteYear){
        if( siteYear.equals("2023")){
            return TestDataProvider.hostedSiteTestDataJson2023.getJSONObject(ENV).getJSONObject(siteTag).getString("platformURL");
        }
        if( siteYear.equals("2022")){
            return TestDataProvider.hostedSiteTestDataJson2022.getJSONObject(ENV).getJSONObject(siteTag).getString("platformURL");
        }
        return null;
    }

    public static String getAPIAuthUrl(){
        return TestDataProvider.publicAPIServiceDataJson.getJSONObject(ENV).getString("auth");
    }

    public static String getAPIServiceUrl(){
        return TestDataProvider.publicAPIServiceDataJson.getJSONObject(ENV).getString("service");
    }

    public static String getAPISiteToken(String tag){
        return TestDataProvider.publicAPISiteTokenDataJson.getJSONObject(ENV).getJSONObject(tag).getString("token");
    }

    public static String getPlatformLoginUsername(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("platformuser").getString("username");
    }

    public static String getPlatformLoginPassword(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("platformuser").getString("password");
    }

   public static String getCommonBrokerUsername(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("atmWorkflowBrokerMap").getString("username");
    }

    public static String getCommonBrokerPassword(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("atmWorkflowBrokerMap ").getString("password");
    }

    public static String getMedigapBrokerUsername(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("medigapBroker").getString("username");
    }

    public static String getMedigapBrokerPassword(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("medigapBroker").getString("password");
    }

    public static String getStateMedicareBrokerUsername(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("stateMedicareBroker").getString("username");
    }

    public static String getStateMedicareBrokerPassword(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("stateMedicareBroker").getString("password");
    }

    public static String getATMWorkflowBrokerFirstName(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("atmWorkflowBrokerMap").getString("firstName");
    }

    public static String getATMWorkflowBrokerLastName(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("atmWorkflowBrokerMap").getString("lastName");
    }

    public static String getCallCenterBrokerUsername(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("callCenterBroker").getString("username");
    }

    public static String getCallCenterBrokerPassword(){
        return TestDataProvider.userTestDataJson.getJSONObject(ENV).getJSONObject("callCenterBroker").getString("password");
    }

}

