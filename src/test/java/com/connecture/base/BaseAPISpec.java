package com.connecture.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseAPISpec {

    // Local configs:  QA or STAGE or TEST or PROD
    public static String ENV = "QA";

    @BeforeClass(alwaysRun=true)
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeMethod
    public void setUp() {
        // TODO
    }


}
