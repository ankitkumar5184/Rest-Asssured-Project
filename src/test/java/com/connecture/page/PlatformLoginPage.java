package com.connecture.page;

import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import static com.connecture.utils.TestDataProvider.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.Selectors.withText;

public class PlatformLoginPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/PlatformManager2";

    // Locators
    public SelenideElement loginStart = $(withText("Click here to Login"));
    public SelenideElement usernameInput = $("#UserName");
    public SelenideElement passwordInput = $("#Password");
    public SelenideElement loginSubmit = $("#btnLogin");

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
    }

    public void login() {
        loginStart.click();
        usernameInput.setValue(getPlatformLoginUsername());
        passwordInput.setValue(getPlatformLoginPassword());
        loginSubmit.click();
    }

    public void loginReturn() {
        usernameInput.setValue(getPlatformLoginUsername());
        passwordInput.setValue(getPlatformLoginPassword());
        loginSubmit.click();
    }
}
