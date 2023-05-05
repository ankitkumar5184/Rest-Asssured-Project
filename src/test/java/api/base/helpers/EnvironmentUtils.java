package api.base.helpers;

import org.testng.SkipException;
import static api.base.helpers.EnvInstanceHelper.prop;

public class EnvironmentUtils {
    public static String envType = prop.getProperty("RunTestsForEnvironment");

    public static Boolean isQaEnv(){
        if (envType.equals("QA")) {
            return true;
        } else {
            throw new SkipException("This test will run in QA environment only");
        }
    }

    public static Boolean isStagingEnv() {
        if (envType.equals("STAGING")) {
            return true;
        } else {
            throw new SkipException("This test will run in Staging environment only");
        }
    }

    public static Boolean isTestEnv() {
        if (envType.equals("TEST")) {
            return true;
        } else {
            throw new SkipException("This test will run in Test environment only");
        }
    }

    public static Boolean isProdEnv() {
        if (envType.equals("PROD")) {
            return true;
        } else {
            throw new SkipException("This test will run in Production environment only");
        }
    }

    public static Boolean skipOnQaEnv() {
        if (envType.equals("QA")) {
            throw new SkipException("This test will skip in QA environment only");
        }
        return null;
    }

    public static Boolean skipOnStagingEnv() {
        if (envType.equals("STAGING")) {
            throw new SkipException("This test will skip in Staging environment only");
        }
        return null;
    }

    public static Boolean skipOnTestEnv() {
        if (envType.equals("TEST")) {
            throw new SkipException("This test will skip in Test environment only");
        }
        return null;
    }

    public static Boolean skipOnProdEnv() {
        if (envType.equals("PROD")) {
            throw new SkipException("This test will skip in Production environment only");
        }
        return null;
    }

    /* Making this method here instead of creating a new class for a single skip method.*/
    public static Boolean skipBug() {
            throw new SkipException("This test will skip because it is affected by a bug");
    }
}
