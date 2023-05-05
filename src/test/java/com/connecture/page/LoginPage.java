package com.connecture.page;

import com.codeborne.selenide.ElementsCollection;
import com.connecture.base.BaseUIPage;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.connecture.utils.TestDataProvider.*;

public class LoginPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/Login";

    // Locators
    public ElementsCollection loginFormInputField = $$("input").filterBy(attribute("formcontrolname"));


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }
    public boolean validateUserNameDisplayed(String formControlAttributeUserName){
        return loginFormInputField.find(attribute("formcontrolname",formControlAttributeUserName)).isDisplayed();
    }

    public void brokerLogin(String formControlAttributeUserName,String userName,String formControlAttributePassword,String password){
        loginFormInputField.find(attribute("formcontrolname",formControlAttributeUserName)).setValue(userName);
        loginFormInputField.find(attribute("formcontrolname",formControlAttributePassword)).setValue(password);
        primaryButtons.find(text("Log in")).click();
    }



}
