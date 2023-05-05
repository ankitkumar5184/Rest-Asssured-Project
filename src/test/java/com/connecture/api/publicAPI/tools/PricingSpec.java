package com.connecture.api.publicAPI.tools;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.connecture.base.BaseAPISpec;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static com.connecture.utils.TestDataProvider.*;

public class PricingSpec extends BaseAPISpec {

    @BeforeClass(alwaysRun=true)
    public static void setUpAll() {
        // TODO: Utilities that use QA urls must fetch the right URL
        String siteTokenTag = "QA_testing";
        System.out.println("Utils can get auth URL like this: " + getAPIAuthUrl() );
        System.out.println("Utils can get service URL like this: " + getAPIServiceUrl() );
        System.out.println("Utils can get token URL like this: "+ getAPISiteToken(siteTokenTag) );
    }

    @Test(groups = { "tools" })
    public void validateToolsPricingCA() {
        System.out.println("Demo validateToolsPricingCA!");
        Assert.assertTrue(true);
    }

}
