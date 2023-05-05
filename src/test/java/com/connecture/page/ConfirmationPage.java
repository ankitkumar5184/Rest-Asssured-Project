package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import org.testng.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ConfirmationPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/Confirmation";

    // Locators
    public ElementsCollection noticeArea = $$(".notice");
    public ElementsCollection applicationDetailsSections = $$(".application-details-body > div");
    public SelenideElement planCoverageCard = $(".coverage-card");
    public SelenideElement riderCoverageCard = $(".rider-card");
    public SelenideElement totalPremiumBanner= $(".total-premium-banner");
    public SelenideElement carrierArea = $(".contact-section");
    public ElementsCollection spaHeadingH1 = $$(".Spa_Style_H1");
    public ElementsCollection viewApplicationLink = $$(".Spa_Style_Link");

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void validateH1Heading(String h1Text){
        String spaHeadingH1Text = spaHeadingH1.find(text(h1Text)).getText();
        spaHeadingH1Text.contains(h1Text);
    }
    public void validateNoticeBanner(String noticeText){
        String noticeAreaText = noticeArea.find(text(noticeText)).getText();
        noticeAreaText.contains(noticeAreaText);
    }

    public void validatesApplicationData(String dataLabel, String expected){
        String labelData = applicationDetailsSections.find(text(dataLabel)).find(".application-data").getText();
        labelData.contains(expected);
    }

    public String getApplicationData(String dataLabel){
        applicationDetailsSections.find(text(dataLabel)).find(".application-data").shouldHave(matchText(".+"));
        return applicationDetailsSections.find(text(dataLabel)).find(".application-data").getText();
    }

    public void validateConfirmationNumber(String dataLabel,String confirmationNo){
        applicationDetailsSections.find(text(dataLabel)).find(".application-data").shouldNot(Condition.empty);
        boolean errorsFlag = confirmationNo.toLowerCase().contains("errors");
        Assert.assertEquals(errorsFlag,false);
        boolean nullFlag = confirmationNo.toLowerCase().contains("null");
        Assert.assertEquals(errorsFlag,false);
    }

    public void validatePlanCoverageCardFlexCareSNP(String data){
        String planCoverageCardText = planCoverageCard.getText().toLowerCase();
            Assert.assertTrue(planCoverageCardText.contains(data.toLowerCase()));
    }

    public void validatePlanCoverageCardForMonthlyPremium(String data){
        String planCoverageCardText = planCoverageCard.getText().toLowerCase();
        Assert.assertTrue(planCoverageCardText.contains(data.toLowerCase()));
    }

    public void validatePlanCoverageCardforMAPDPlanTabText(String data){
        String planCoverageCardText = planCoverageCard.getText().toLowerCase();
        planCoverageCardText.contains(data.toLowerCase());
    }

    public void validateRiderCoverageCard(String data){
        String riderCoverageCardText = riderCoverageCard.getText().toLowerCase();
        Assert.assertTrue(riderCoverageCardText.contains(data.toLowerCase()));
    }
    public void validateTotalPremiumBanner(String premium){
        String totalPremiumBannerText = totalPremiumBanner.getText().toLowerCase();
        Assert.assertTrue(totalPremiumBannerText.contains(premium.toLowerCase()));
    }

    public void validateCarierData(String data){
       String  carrierAreaText= carrierArea.getText().toLowerCase();
        Assert.assertTrue(carrierAreaText.contains(data.toLowerCase()));
    }

    public void validateCarierDataForAddress(String data){
        String  carrierAreaText= carrierArea.getText().toLowerCase();
        Assert.assertTrue(carrierAreaText.contains(data.toLowerCase()));
    }

    public void validateCarierDataForURL(String data){
        String  carrierAreaText= carrierArea.getText().toLowerCase();
        Assert.assertTrue(carrierAreaText.contains(data.toLowerCase()));
    }

    public void validateSubmissionBanner(String noticeText){
        String noticeAreaText = noticeArea.find(text(noticeText)).getText().toLowerCase();
        Assert.assertTrue(noticeAreaText.contains(noticeText.toLowerCase()));
    }

    public void UserClearsDownloadsViewApplicationPDF(String applicationText){
        viewApplicationLink.find(text(applicationText)).click();
    }




}
