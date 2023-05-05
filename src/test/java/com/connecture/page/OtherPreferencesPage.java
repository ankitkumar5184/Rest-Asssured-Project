package com.connecture.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class OtherPreferencesPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/GuidedHelp/OtherPreferences";

    // Locators
    public SelenideElement exampleLocator = $("vnext-guided-help-block");
    public ElementsCollection secondaryButtonWithText = $$(".Spa_Style_SecondaryButton");


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void selectBenefitOptions(String secButonTxt){
        secondaryButtonWithText.find(text(secButonTxt)).click();
    }

}
