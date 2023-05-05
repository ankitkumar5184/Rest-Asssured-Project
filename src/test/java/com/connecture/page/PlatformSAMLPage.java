package com.connecture.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class PlatformSAMLPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/SAML";

    // Locators
    public SelenideElement sendSAMLButton = $$("button").find(attribute("value","Send SAML"));
    public SelenideElement addAssertion = $$("button").find(text("Add"));
    public SelenideElement endpointTextBox = $$("input").find(attribute("name","Url"));
    public SelenideElement usernameTextBox = $$("input").find(attribute("name","NameId"));
    public ElementsCollection assertionFieldTextBox = $$("td > input");

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
    }

    public void enterURL(String url){
        endpointTextBox.sendKeys(url);
    }

    public void enterNameID(String id){
        usernameTextBox.sendKeys(id);
    }

}
