package com.connecture.base;

import com.codeborne.selenide.*;
import com.codeborne.selenide.testng.ScreenShooter;
import com.connecture.utils.CustomReporter;
import com.connecture.utils.CustomRetryAnalyzer;
import com.connecture.utils.Site;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.*;
import java.util.HashMap;
import static com.codeborne.selenide.FileDownloadMode.*;
import static com.connecture.utils.TestDataProvider.getSiteBaseUrl;

@Listeners({ ScreenShooter.class, CustomReporter.class})
public class BaseUISpec {

    // Local debug modes for testing and development
    public static String ENV = "QA";   // DO NOT COMMIT !!!
    public static Boolean enableRetry = false; // DO NOT COMMIT !!!
    public static Boolean enableHoldBrowserOpen = false; // DO NOT COMMIT !!!
    public static Boolean isLocalHotfixOverride = false; // DO NOT COMMIT !!!
    public static AssertionMode assertionModeType = AssertionMode.STRICT; // DO NOT COMMIT !!!

    // Be careful with @BeforeClass/AftreClass - Use alwaysRun OR assign a gruop via (groups={"UAP"})
    // TODO: Fix Static configuration methods can cause unexpected behavior
    @BeforeClass(alwaysRun=true)
    public static void setUpAll() {
        System.out.println("ENV: " + ENV);
        Configuration.holdBrowserOpen = enableHoldBrowserOpen;
        Configuration.assertionMode = assertionModeType;
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080"; // TODO: Deploy server adjust resolution to 1080
        Configuration.reportsFolder =  "./test-output/junitreports/";
        Configuration.savePageSource = true;
        Configuration.screenshots = true;
        Configuration.textCheck = TextCheck.PARTIAL_TEXT; // Use exactText() when fully desired
        Configuration.pollingInterval = 250;
        ScreenShooter.captureSuccessfulTests = true;
        Configuration.timeout = 12000;
        Configuration.proxyEnabled = false;
        Configuration.fileDownload = FOLDER;
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, new ChromeOptions().addArguments("--remote-allow-origins=*"));
        //Configuration.downloadsFolder =  "./build/downloads/";
        // WebDriverManager.chromedriver().setup(); // TODO: Not needed because of Selenide?
        // Configuration.browser = browser;
        // SelenideLogger.addListener("allure", new AllureSelenide());
        // Insure pipelines always operate outside of debug mode
        if( System.getProperty("enableRetry") != null && System.getProperty("enableRetry").equals("true")) {
            Configuration.holdBrowserOpen = false;
        }
    }

    @BeforeSuite(alwaysRun = true)
    public void setupSuite(ITestContext context) {
        Boolean enableRetryFromRemotry =  System.getProperty("enableRetry") != null && System.getProperty("enableRetry").equals("true");
        if(enableRetry == true || enableRetryFromRemotry ){
            System.out.println("Enabling retry ...");
            for (ITestNGMethod method : context.getAllTestMethods()) {
                method.setRetryAnalyzerClass(CustomRetryAnalyzer.class);
            }
        }
    }

    @AfterClass(alwaysRun=true)
    public static void tearDownAll() {
        // TODO
    }

    @BeforeMethod(alwaysRun=true)
    public void setUpMethodAll() throws ClassNotFoundException {
        if(getClass().getAnnotation(Site.class) != null ) {
            // Handle annotations
            Configuration.baseUrl = getSiteBaseUrl(getClass().getAnnotation(Site.class).tag(), getClass().getAnnotation(Site.class).year());
            // Handle branch
            if( System.getProperty("isHotfix") != null && System.getProperty("isHotfix").equals("true") || isLocalHotfixOverride == true ) {
                Configuration.baseUrl += "H"; System.out.println("Running hotfix site!");
            }
            // Readout the site url
            System.out.println("BaseUrl loaded: " + Configuration.baseUrl);
        }
    }

    @AfterMethod(alwaysRun=true)
    public void tearDownMethodAll() {
        // TODO: Skip closing the browser if the flow continues (same user, etc)
        WebDriverRunner.closeWebDriver();
    }


    
}
