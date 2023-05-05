package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import com.connecture.config.Locations;
import org.testng.Assert;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class EditProfilePage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/EditProfile";

    // Locators
    public SelenideElement firstNameTextBox = $("input[formControlname='firstName']");
    public SelenideElement lastNameTextBox = $("input[formControlname='lastName']");
    public SelenideElement zipTextBox = $(".demographic-input");
    public SelenideElement tooltipArea = $(".tooltip");
    public SelenideElement stateElement = $("vnext-state-selector-input");
    public ElementsCollection stateDropDowns = $$(".dropdown-item");

    public SelenideElement tooltipInner = $("div.tooltip-inner");
    public SelenideElement dob = $("input[formControlname='dateOfBirth']");
    public SelenideElement phoneTextBox = $("input[formControlname='phone']");
    public SelenideElement addressLine1TextBox = $("input[aria-labelledby='memberRegistrationAddressLabelId ' ]");
    public SelenideElement addressLine2TextBox = $("input[formControlname='address2']");
    public SelenideElement dobTextBox = $("input[formControlname='dateOfBirth']");
    public SelenideElement emailTextBox = $("input[formControlname='email']");
    public SelenideElement cityTextBox = $("input[formControlname='city']");
    public SelenideElement stateDropDownButton = stateElement.find("input.can-click");
    public SelenideElement saveButton = $("span.Spa_Style_Large_Font");
    public SelenideElement stateDropDownButtonItem = $(".dropdown-item");


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
        firstNameTextBox.shouldBe(Condition.interactable);
        firstNameTextBox.shouldBe(Condition.visible);
    }

    public void fillOutProfileInfo(String first, String last, String zip){
        firstNameTextBox.setValue(first); // sendKeys failing here at times
        lastNameTextBox.sendKeys(last);
        zipTextBox.sendKeys(zip);
    }

    public void validateInnerTooltip(String tooltipText){
        tooltipInner.shouldBe(visible);
        tooltipInner.getText().contains(tooltipText);
    }


    public void enterDOB(String dobValue){
        dob.sendKeys(dobValue);
    }

    public void enterPhone(String phoneNumber){
        phoneTextBox.sendKeys(phoneNumber);
    }

    public void enterEmail(String emailAddress){
        emailTextBox.sendKeys(emailAddress);
    }


    public void validateTemporaryTooltip(String tooltipText){
        tooltipArea.isDisplayed();
        tooltipArea.getText().contains(tooltipText);
    }


    public void enterHomeAddress(String drxMemberAddressLineLA, String drxMemberAddressLine2LA, String drxMemberCity, String drxMemberState) {
        formControls.find(attribute("formcontrolname","address2")).setValue(drxMemberAddressLine2LA);
        inputs.find(attribute("aria-labelledby","memberRegistrationAddressLabelId ")).setValue(drxMemberAddressLineLA);
        formControls.find(attribute("formcontrolname","city")).setValue(drxMemberCity);
        stateElement.click();
        String longStateName = Locations.STATE_MAP.get(drxMemberState);
        stateDropDowns.find(Condition.text(longStateName)).click();
    }

    public void validatesBeneficaryName(String first,String last) {
        String firstNameText = firstNameTextBox.getValue();
        String lastNameText = lastNameTextBox.getValue();
        Assert.assertEquals(firstNameText,first);
        Assert.assertEquals(lastNameText,last);
    }
    public void validateBeneficiaryDOB(String dobGiven){
        String dobText = dobTextBox.getValue();
        Assert.assertEquals(dobText,dobGiven);
    }
}

