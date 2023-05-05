package com.connecture.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.CollectionElement;
import com.connecture.base.BaseUIPage;
import org.testng.Assert;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.openqa.selenium.remote.http.FormEncodedData.getData;

public class GetStartedPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/GuidedHelp/GetStarted";

    // Locators

    public SelenideElement zipCodeError = $(".zip-error");
    public SelenideElement toggleOptionSelected=$(".toggle-option.Spa_Style_SecondaryButtonSelected");
    public SelenideElement accordianOptionSelected = $(".outer-accordion-button.Spa_Style_SecondaryButtonSelected");
    public ElementsCollection accordianOptions = $$(".outer-accordion-button");

    public SelenideElement exampleLocator = $("vnext-guided-help-block");
    public ElementsCollection secondaryButtonWithText = $$(".Spa_Style_SecondaryButton");

    public SelenideElement accordianOptionList = $(".outer-accordion-button");


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
        waitForNgxOverlay();
    }


    public void validateZipCodeError(){
        zipCodeError.isDisplayed();
    }

    public void validateToggleOptionSelected(String toggleText){
        toggleOptionSelected.find(toggleText).isDisplayed();
    }

    public void validateAccordianOptionSelected(String accordianOptionText){
        accordianOptionSelected.find(accordianOptionText).isDisplayed();
    }

    public void selectAccordianOption(String accordianText){
        accordianOptions.find(text(accordianText)).click();
    }

    public void validateZipCodeValue(String zipCode){
        Assert.assertEquals(zipCodeInput.getValue(),zipCode);

    }
    public boolean validateSecondaryButtonMissing(String partialText){
       return secondaryButtonWithText.find(text(partialText)).isDisplayed();
    }

    public void validateAccordianOptionNotDisplayed(){
        Assert.assertEquals(accordianOptionList.isDisplayed(),false);
    }

    public void waitExtraHelpLoad(String partialText){
     secondaryButtons.find(text(partialText)).shouldBe(visible, Duration.ofSeconds(10)).isDisplayed();
        }
    }

