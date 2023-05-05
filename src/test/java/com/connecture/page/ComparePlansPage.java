package com.connecture.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ComparePlansPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/ComparePlans";

    // Locators
    public ElementsCollection planCardDetails = $$(".plan-card-column");

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void clickPlanDetails(String planName,String buttonText){
        planCardDetails.find(text(planName)).$$(".Spa_Style_SecondaryButton").find(text(buttonText)).click();
    }



}
