package api.base.helpers;

import api.base.reporter.ExtentLogger;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

public class EnvInstanceHelper {
    public static Properties prop;
    InputStream ip;

    public EnvInstanceHelper() {
        getEnvironment();
    }

    public Map<String, String> getEnvironment() {
        Map<String, String> tokens = new LinkedHashMap<>();
        try {
            usePropertyFile();
            String env = prop.getProperty("RunTestsForEnvironment");
            tokens.put("auth-host", prop.getProperty(env + "_auth-host"));
            tokens.put("host", prop.getProperty(env + "_host"));
            tokens.put("basic-auth", prop.getProperty(env + "_basic-auth"));
        } catch (Exception e) {
            ExtentLogger.fail(Arrays.toString(e.getStackTrace()));
        }
        return tokens;
    }

    private void usePropertyFile() {
        try {
            ip = new FileInputStream(System.getProperty("user.dir") + "/src/test/java/api/tests/config/env.properties");
            prop = new Properties();
            prop.load(ip);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}