package com.connecture.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProvidersPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/GuidedHelp/Providers";

    // Locators
    public SelenideElement exampleLocator = $("vnext-guided-help-block");
    public SelenideElement providerTextBox = $(".provider-name-input");
    public ElementsCollection providerItems = $$(".provider-list");
    public SelenideElement providerItemSelected = $(".Spa_Style_SecondaryButtonSelected");
    public ElementsCollection providerNames = $$(".provider-name");


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }
    public String getSelectedProviderName(){
        return $(".selected-provider").$(".Spa_Style_H4").getText();
    }

}
