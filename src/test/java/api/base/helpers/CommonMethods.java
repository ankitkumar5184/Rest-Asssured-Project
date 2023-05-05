package api.base.helpers;

import api.base.reporter.ExtentLogger;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonMethods {
    public static Properties prop;
    public static Properties dataProp;
    public String env;
    public String apiType;

    public CommonMethods(){
        usePropertyFileForEndpoints();
    }

    // this method loads the API endpoints from the env.properties file
    public void usePropertyFileForEndpoints() {
        try {
            InputStream ip = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/api/tests/config/env.properties");
            prop = new Properties();
            prop.load(ip);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // this method loads the data from property files for PC API or Tools API based on the specific environment
    public void usePropertyFileData(String apiEndPtType) {
        env = prop.getProperty("RunTestsForEnvironment");
        apiType = apiEndPtType;
        InputStream ip = null;
        try {
            switch (apiEndPtType) {
                case "TOOLS-API" ->
                        ip = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/api/tests/config/data_properties/data-TOOLS-API_" + env + ".properties");
                case "PC-API" ->
                        ip = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/api/tests/config/data_properties/data-PC-API_" + env + ".properties");
                default -> ExtentLogger.fail("Unknown API End point type. Valid values are PC-API or TOOLS-API");
            }
            dataProp = new Properties();
            dataProp.load(ip);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // this method loads the test data (JSON/XML) from different folder based on the specific environment
    public String getTestDataPath() {
        try {
            return System.getProperty("user.dir") + "/src/test/resources/api.testdata/" + apiType + "_" + env + "/";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
