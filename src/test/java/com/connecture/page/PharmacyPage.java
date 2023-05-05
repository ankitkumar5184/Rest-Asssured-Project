package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.files.DownloadActions.click;

public class PharmacyPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/GuidedHelp/Pharmacy";

    // Locators
    public ElementsCollection pharmacyList = $$(".pharmacy-list");
    public ElementsCollection pharmacyNames =  $$(".pharmacy-list .Spa_Style_HeaderTextLevel");
    public ElementsCollection pageLinkButton = $$("a.page-link");

    public SelenideElement selectedPharmacyArea = $(".selected-pharmacy");

    public ElementsCollection selectedPharmacyAreaSecond = $$(".selected-pharmacy");


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }


    public ElementsCollection getPharmacyNames() {
        pharmacyNames.texts();
        return pharmacyNames;
    }
    public void validatePharmacyPageCount(String pageCount){
        pageLinkButton.find(text(pageCount)).isDisplayed();
    }

    public void userSelectPharmacyPage(String pageNo){
        pageLinkButton.find(text(pageNo)).click();
    }

    public void validatePharmacy(String pharmacyName){
        pharmacyNames.find(text(pharmacyName)).isDisplayed();
    }

    public String getSelectedPharmacyName(){
        return selectedPharmacyArea.getText();
    }

    public void selectPharmacy(String pharmacyName,String partialText){
        pharmacyList.find(text(pharmacyName)).$$("button").find(text(partialText)).click();
    }

    public String getFirstPharmacyNameOnPage(){
       return pharmacyNames.first().getText();
    }

    public String getSecondPharmacyNameOnPage(){
        return selectedPharmacyAreaSecond.last().getText();

    }

}
