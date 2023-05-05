package com.connecture.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProfileSearchPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/Search";

    // Locators
    public SelenideElement exampleLocator = $("vnext-guided-help-block");
    public SelenideElement mbiTextBox  =  $("#mbi") ;
    public SelenideElement phoneTextBox =  $("#phoneNumber");
    public SelenideElement dobTextBox  =  $("#dateOfBirth") ;
    public SelenideElement agentTextBox  =  $("#agentUserName");
    public SelenideElement applicationStartDateTextBox  =  $("#applicationStartDate") ;
    public SelenideElement applicationEndDateTextBox   = $("#applicationEndDate") ;
    public SelenideElement emailTextBox =   $("#emailAddress") ;
    public SelenideElement confTextBox =  $("#confirmationNumber");
    public SelenideElement firstNameTextBox  = $("#firstName");
    public SelenideElement lastNameTextBox  = $("#lastName");
    public ElementsCollection allMemberCards = $$("vnext-member-result-card");
    public SelenideElement firstMemberCardAddressArea = $$("vnext-member-result-card .flex-column > div").get(0);
    public SelenideElement firstMemberCardNameAddressArea =  $$("vnext-member-result-card .flex-column > div").get(0);
    public SelenideElement firstMemberCardEmailPhoneArea =   $$("vnext-member-result-card .flex-column > div").get(1);
    public SelenideElement firstMemberCardTypeArea =  $$("vnext-member-result-card .flex-column > div").get(2);
    public SelenideElement firstMemberCardAgentArea =  $$("vnext-member-result-card .flex-column > div").get(3);
    public SelenideElement firstMemberCardDateArea = $$("vnext-member-result-card .flex-column > div").get(4);

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void validateFirstMemberCard(String name, String drxMemberAddressLineLA, String drxMemberAddressLine2LA,
                                   String drxMemberZip, String drxMemberCity, String drxMemberState, String emailPlaceHolder,
                                   String drxMemberPhone, String applicantType, String commonBrokerUsername, String updated, String dateFormatted) {
        firstMemberCardNameAddressArea.shouldHave(text(name));
        firstMemberCardNameAddressArea.shouldHave(text(drxMemberAddressLineLA));
        firstMemberCardNameAddressArea.shouldHave(text(drxMemberAddressLine2LA));
        firstMemberCardNameAddressArea.shouldHave(text(drxMemberZip));
        firstMemberCardNameAddressArea.shouldHave(text(drxMemberCity));
        firstMemberCardNameAddressArea.shouldHave(text(drxMemberState));
        firstMemberCardEmailPhoneArea.shouldHave(text(drxMemberPhone));
        firstMemberCardTypeArea.shouldHave(text(applicantType));
        firstMemberCardAgentArea.shouldHave(text(commonBrokerUsername));
        firstMemberCardDateArea.shouldHave(text(dateFormatted));
    }

    public void enterFirstName(String firstName){
        firstNameTextBox.sendKeys(firstName);
    }

    public void enterLastName(String lastName){
        lastNameTextBox.sendKeys(lastName);
    }

    public void validateMemberNameAndType(String memberName,String type){
        allMemberCards.find(text(type)).$$(".Spa_Style_Link").find(text(memberName)).isDisplayed();
    }
    public void clickMemberNameAndType(String memberName,String type){
        allMemberCards.find(text(type)).$$(".Spa_Style_Link").find(text(memberName)).isDisplayed();
        allMemberCards.find(text(type)).$$(".Spa_Style_Link").find(text(memberName)).click();
    }
}
