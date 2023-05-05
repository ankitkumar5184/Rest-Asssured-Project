package com.connecture.page;

import com.codeborne.selenide.*;
import com.connecture.base.BaseUIPage;
import com.connecture.utils.exception.ConnectureCustomException;
import org.openqa.selenium.By;
import org.testng.Assert;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;



public class PlanListPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/PlanList";

    // Locators
    public ElementsCollection planCards = $$(".plan-card");

    public SelenideElement firstPlanTab = $("vnext-filter-tab");
    public SelenideElement firstPlanCard = $(".plan-card");
    public ElementsCollection getStartedArea = $$("div.pb-2");
    public SelenideElement planCountHeader = $(".plan-list-title");
    public SelenideElement activeTab = $(".tab.d-flex.active");
    public ElementsCollection planTab = $$("vnext-filter-tab");
    public SelenideElement tooltipArea = $(".tooltip");
    public SelenideElement dentalRiderIcon = $(".Spa_Style_DentalRiderIcon");

    public SelenideElement filterCheckboxLabel=  $(".plan-filter-container");
    public SelenideElement dropDownWidget = $(".dropdown");
    public SelenideElement dropDownWidgetOption = $(".dropdown-item");

    public SelenideElement comparePlansCheckbox = $(".checkbox.float-right");

    public void validatePageLoaded() {
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
        firstPlanTab.shouldBe(Condition.visible);
        firstPlanCard.shouldBe(Condition.visible);
    }

    public void scrollPlanListToBottom(int times) {
        for (int i = 0; i <= times; i++) {
            //System.out.println("scrolling...");
            executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
        }
    }

    public void validatePlanCardsRemaining(String tag) {
        planCards.shouldHave(CollectionCondition.texts(tag));
    }

    public void clickPlanDetails(String planName) {
        planCards.find(text(planName)).$$(".Spa_Style_SecondaryButton").find(Condition.interactable).click();
    }

    public void clickAddToCart(String planName) {
        planCards.find(text(planName)).$$(".Spa_Style_PrimaryButton").find(Condition.interactable).click();
    }


    public String getPlanCardMonthlyPremium(String planName) {
        String planRate = planCards.find(text(planName)).$(".monthly-premium-container").$(" .Spa_Style_CostPremiumSmall").getText();
        validateIsDollarCost(planRate);
        return planRate;
    }

    public Integer planCountHeaderCount() {
        return Integer.parseInt(planCountHeader.getText().split(" plans available")[0]);
    }

    public Integer getActiveTabPlanCount(){
        return Integer.parseInt(activeTab.find(".plan-count").getText().split(" plan")[0].trim());
    }
    public void selectPlanTab(String planName){
        planTab.find(text(planName)).click();

    }

    public void selectMedicalPlanTab(String planType){
        planTab.filterBy(text(planType)).last().click();
    }

    public boolean validateDateOfBirthFieldDisplayed(String datebofbirthplaceholder,String expectedPlaceHolderValue){
        return $("vnext-med-sup-birth-date").$$("input.med-sup-number-input").find(attribute(datebofbirthplaceholder,expectedPlaceHolderValue)).isDisplayed();

    }


    public String fetchGetStartedPreferences(String section){
        return getStartedArea.find(text(section)).getText();
    }
    public String fetchHealthPreferences(String section) {
        return getStartedArea.find(text(section)).getText();
    }
    public String fetchProvidersPreferences(String section) {
           return getStartedArea.find(text(section)).getText();
       }

    public String fetchRXBrandPreferences(String section) {
       return getStartedArea.find(text(section)).getText();
    }

    public String fetchPharmacyPreferences(String section) {
        return  getStartedArea.find(text(section)).getText();
    }
    public void clickEditButton(String attributeName,String sectionLabel){
        $$("a").find(attribute(attributeName,sectionLabel)).click();
    }

    public String getPlanCardTotalEstimatedCost(String planName) throws ConnectureCustomException {
        String planCost = planCards.find(text(planName)).find(".preferences-section .Spa_Style_CostPremiumSmall").getText();
        //System.out.println("Cost: " + planCost);
        validateIsDollarCost(planCost);
        //validatePremiumIsGreater("0", planCost);
        return planCost;
    }

    public String getPreferencesAreaForPlan(String planName){
        return planCards.find(text(planName)).find(".preferences-section").getText();
    }

    public void validateIsDollarCost(String costString) {
        // fail if negative
        Assert.assertEquals(costString.contains("-"),false); //assert costString.contains("-") == false

        // Assert and remove dollar sign
        Assert.assertEquals(costString.contains("$"),true); //assert costString.contains("\$") == true
        costString = costString.replace("$", "");
        // Check if numeric
          Double parseDoubleAttempt = Double.parseDouble(costString.replace(",","")); //	2331

        // Check decimal places
        if (costString.contains(".")) //
        {
            String[] splitter = costString.split("\\.");
                int postDecimalLength = splitter[1].length();
                Assert.assertEquals(postDecimalLength, 2);
            }
        }


        //For Sanity
    public void validatePremiumIsGreater(String smallerPremium,String largePremium) throws ConnectureCustomException {
        BigDecimal smallerBigDecimal = convertStringRateToBigDecimal(smallerPremium);
        BigDecimal largeBigDecimal = convertStringRateToBigDecimal(largePremium);
         boolean flag = largeBigDecimal.compareTo(smallerBigDecimal) > 0;
         Assert.assertEquals(flag,true);
    }

    public void clickAddButton(String attributeName,String sectionLabel){
        $$("a").find(attribute(attributeName,sectionLabel)).click();
    }

    public void clickAddButtonForSection(String sectionName){
        $$("a").findBy(text(sectionName)).click(); // .find("a")
    }

    public void validatePlanCardshaveCompareDetailsAndCartButton() {

        for (SelenideElement planCard : planCards) {
            Assert.assertEquals((planCard.$$(".Spa_Style_PrimaryButton").find(text("add to cart")).isDisplayed()), true);
            Assert.assertEquals((planCard.$$(".Spa_Style_SecondaryButton").find(text("Plan Details")).isDisplayed()), true);
            Assert.assertEquals((planCard.$$("span").find(text("Add to compare")).isDisplayed()), true);

        }
    }
        public void validateStartRatingTooltip(String planName, String tooltipText){
            Assert.assertEquals(tooltipArea.isDisplayed(),false);
            planCards.find(text(planName)).find(".ml-2").hover();
            tooltipArea.getText().contains(tooltipText);

        }
        public void validateAllRiderTooltip(String planName) throws InterruptedException {
            planCards.find(text(planName)).find("div.Spa_Style_Paragraph").hover();
            Thread.sleep(300);
            Assert.assertEquals(tooltipArea.isDisplayed(),false);
            planCards.find(text(planName)).find(".Spa_Style_DentalRiderIcon").hover();
            tooltipArea.getText().contains("Dental");
            planCards.find(text(planName)).find(".Spa_Style_VisionRiderIcon").hover();
            tooltipArea.getText().contains("Vision");
            planCards.find(text(planName)).find(".Spa_Style_HearingRiderIcon").hover();
            tooltipArea.getText().contains("Hearing");
    }

    public void validatePlanBenefit(String planName, String benefit){
        String benefitAmount = planCards.find(text(planName)).$$(".plan-benefits-items").find(text(benefit)).find(".Spa_Style_H3").getText();

    }

    public List<String> getPlanCardsText(){
        return planCards.texts();
    }

    public void selectFilterCheckBox(String attributeName,String filterText){
        filterCheckboxLabel.$$("input").find(attribute(attributeName,filterText)).click();
    }

    public void selectDropDown(String before,String after) {
        // selection of dropdown
        $(".dropdown button.sort-dropdown-button>span").click();
        // Selection of dropdown on the basis of parameter
        $$(".dropdown button.dropdown-item").find(text(after)).click();
    }


    public void validatePlansSortedByCost() throws ConnectureCustomException {
        List costList = new ArrayList();
        int planCardList = planCards.size();
        if (planCardList >= 1) {
            for(SelenideElement planCard:planCards){
               costList.add(convertStringRateToBigDecimal(planCard.find(".monthly-premium-container .Spa_Style_CostPremiumSmall").text()));
            }
            Assert.assertEquals(isSorted(costList),true);
        }

    }

    public void validatePlansSortedByPremium() throws ConnectureCustomException {
        List premiumList = new ArrayList();
        int planCardList = planCards.size();
        if(planCardList>=1){
            for(SelenideElement planCard:planCards)
            premiumList.add(convertStringRateToBigDecimal(planCard.find(".monthly-premium-container .Spa_Style_CostPremiumSmall").text()));
        }

        Assert.assertEquals(isSorted(premiumList),true);
    }

    public void  validatesPlansSortedByName() throws ConnectureCustomException {
        List dataList = new ArrayList();
        int planCardList = planCards.size();
        if(planCardList>=1){
            for(SelenideElement planCard:planCards)
                dataList.add(planCard.find(".plan-name").text());
        }

        Assert.assertEquals(isSorted(dataList),true);
    }

    public void validatePlanNameInCard(String planName){
        planCards.contains(planName);
    }

    public void selectPlanToCompare(String planName){
            planCards.find(text(planName)).find(".checkbox.float-right").click();
    }



}

