package com.connecture.page;

import com.connecture.base.BaseUIPage;
import com.codeborne.selenide.SelenideElement;
import org.testng.Assert;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.*;

public class HomePage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/Home";

    // Locators
    public SelenideElement guidedHelpArea = $("vnext-guided-help-block");

    public SelenideElement loginPasswordError = $("#passwordFieldValidationError");

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void validateCountyNonDuplicates(String countyName){
        countyNameListItems.filter(text(countyName)).shouldHave(size(1));
    }

    public void validateCountySize(int expectedSize){
        countyNameListItems.shouldHave(size(expectedSize));
    }

    public void clickPartialHeadingLink(String partialHeadingLink){
        $$(".Spa_Style_H3").find(text(partialHeadingLink)).click();
    }

    public void validateCreateAccountButtonDisplayed(String buttonText){
        secondaryButtons.find(text(buttonText)).isDisplayed();
    }
    public void clickCreateAccountButton(String partialText){
        secondaryButtons.find(text(partialText)).click();
    }

    public void validateLoginPasswordErrorDisplayed(){
        boolean flag = loginPasswordError.isDisplayed();
        Assert.assertEquals(flag,true);
    }

    public void validateLoginPasswordErrorNotDisplayed(){
        boolean flag = loginPasswordError.isDisplayed();
        Assert.assertEquals(flag,false);
    }

}
