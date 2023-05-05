package com.connecture.base;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.Selenide.*;

public class TemplatePage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/Template";

    // Locators
    public SelenideElement exampleLocator = $("vnext-guided-help-block");

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

}
