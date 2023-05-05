package com.connecture.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class PlanDetailsPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/PlanDetails";

    // Locators
        public ElementsCollection vNextContainer = $$("vnext-liveedit-container");
        public SelenideElement contractPlanSegmentArea = $(".Spa_Style_Smaller_Font.Spa_Style_Gray4_Color");
        public ElementsCollection tabLink = $$(".tab");



        public SelenideElement headerText = $(".Spa_Style_H1");
        public SelenideElement planCard = $(".plan-card");
        public SelenideElement planNameTitle = $(".plan-title");
        public SelenideElement planNameHeader = $(".plan-name-header");

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void validateMonthlyPlanNameHeader(String planName){
        headerText.shouldHave(text(planName));
    }

    public void validateMonthlyPlanNameCard(String planName){
        planCard.shouldHave(text(planName));
    }

    public void validatePlanNameTitle(String planName){
        planNameTitle.shouldHave(text(planName));
    }

    public void validatePlanNameHeader(String planName){
        planNameHeader.shouldHave(text(planName));
    }

    public String getContractPlanIDSegment() {
        return contractPlanSegmentArea.getText().replace("Plan:","");
    }

    public void selectTab(String tabName){
        tabLink.find(text(tabName)).click();
    }

    public void validateSelectedTab(String tabName){
        tabLink.find(text(tabName)).find("a[aria-selected='true']").isDisplayed();
    }
}
