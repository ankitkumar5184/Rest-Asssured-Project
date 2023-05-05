package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
public class CartPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/ShoppingCart";

    // Locators
    public ElementsCollection planCards = $$(".plan-card");
    public SelenideElement planCard = $(".plan-card");
    public SelenideElement emptyCartTitle = $("empty-cart-title");
    public SelenideElement totalMonthlyPremium = $(".Spa_Style_CostPremiumSmall");
    public ElementsCollection riderCards = $$(".rider-card");
    public SelenideElement cardBody = $(".card-body");
    public ElementsCollection totalPremiumBanner = $$(".total-premium-banner");
    public SelenideElement planNameHeading = $(".Spa_Style_H5");
    public SelenideElement removeButton = $(".remove-button");
    public SelenideElement changePlan = $(".Spa_Style_Link");


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }
    public String getRiderMonthlyPremium(String riderMonthlyPlanName){
        return riderCards.find(text(riderMonthlyPlanName)).find(".rider-amount").getText();
    }
    public void clickAddButton(String riderMonthlyPlanName ){
        riderCards.find(text(riderMonthlyPlanName)).$$(".Spa_Style_SecondaryButton").find(Condition.interactable).click();
    }
    public String getTotalMonthlyPremium(){
       return totalMonthlyPremium.getText();
    }
    public void clickRiderDetails(String riderName){
        riderCards.find(text(riderName)).$(".Spa_Style_Link").click();
    }
    public void validateRiderDetails(String riderName, String riderText){
        riderCards.find(text(riderName)).shouldHave(text(riderText));
    }
    public void validateRiderSelected(){
        removeButton.isDisplayed();
    }

    public void clickRemoveRider(){
        removeButton.click();
    }
    public void validatePlanNameSelected(String planName){
        cardBody.shouldHave(text(planName));
    }
    public void validateCartEmpty(){
        emptyCartTitle.isDisplayed();
    }

    public void validateSelectedPlanPremium(String planName,String planCardMonthlyPremium){
        String planCardText = planCards.find(text(planName)).getText();
        planCardText.contains(planCardMonthlyPremium);
    }


}
