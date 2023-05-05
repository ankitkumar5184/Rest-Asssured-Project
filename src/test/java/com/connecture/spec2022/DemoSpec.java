package com.connecture.spec2022;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.connecture.base.*;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="Consumer_default", year="2022")
public class DemoSpec extends BaseUISpec {

    @Test(groups = { "deployment" }, description = "Show a demo workflow")
    public void consumerEnrollsAndValidatesConfirmationPage()  {

        /* Paint times demo for testing remote machines
        Selenide.open("https://qaautomation1a.testing.destinationrx.com/PC/2023/Shopping/PlanList?ZipCode=90058&CountyFIPS=06037");
        PlanListPage paintTimesPageDemo = new PlanListPage();
        paintTimesPageDemo.validatePageLoaded();
        System.out.println("PlanList FP: " + Selenide.executeJavaScript("return performance.getEntriesByType('paint')[0].startTime"));
        System.out.println("PlanList FCP: " + Selenide.executeJavaScript("return performance.getEntriesByType('paint')[1].startTime"));
        WebDriverRunner.closeWebDriver();
        Selenide.open("https://qaautomation1a.testing.destinationrx.com/PC/2023/Resources/HelpfulTools?ZipCode=90058&CountyFIPS=06037");
        webdriver().shouldHave(urlContaining("Helpful"));
        paintTimesPageDemo.largeZipLink.shouldBe(Condition.visible);
        paintTimesPageDemo.waitForOverlay();
        System.out.println("Helpful FP: " + Selenide.executeJavaScript("return performance.getEntriesByType('paint')[0].startTime"));
        System.out.println("Helpful FCP: " + Selenide.executeJavaScript("return performance.getEntriesByType('paint')[1].startTime"));
        WebDriverRunner.closeWebDriver();
         */

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.guidedHelpArea.shouldHave(text("Enter your preferences"));
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.scrollPlanListToBottom(5); // TODO: Refactor so scrolls automatically to bottom
        planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        planListPage.clickPlanDetails(getData("flexcareCommonMAPDPlanToEnroll"));

        PlanDetailsPage planDetailsPage = new PlanDetailsPage();
        planDetailsPage.clickPrimaryLink("2021 Summary of Benefits");
        //planDetailsPage.validateFileDownload("pdf-sample.pdf");
        planDetailsPage.clickPrimaryLink("Previous");

        planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.clickPrimaryButton("Add pref");

        GetStartedPage getStarted = new GetStartedPage();
        getStarted.validatePageLoaded();
        getStarted.zipCodeInput.shouldHave(value(getData("drxMemberZip")));
        getStarted.clickSecondaryButton("I don't know");
        getStarted.clickSecondaryButton("I receive help");
        getStarted.validateSecondaryButtonSelected("I don't know");
        getStarted.clickPrimaryLink("Go to plans");

        planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.clickLabel("DRX Demo");
        //planListPage.validatePlanCardsRemaining("flexcare");  // Demo for CollectionCondition
        planListPage.scrollPlanListToBottom(5); // TODO: Refactor so scrolls until bottom
        planListPage.clickAddToCart(getData("flexcareCommonMAPDPlanToEnroll"));

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.planCard.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        cartPage.clickPrimaryButton("Continue to apply");

        EnrollmentFormPage contactInfoPage = new EnrollmentFormPage("ContactInfo");
        contactInfoPage.validatePageLoaded();
        contactInfoPage.miniCartArea.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        contactInfoPage.fillOutContactInfo( getData("drxMemberFirstName"),getData("drxMemberLastName"),
                getData("drxMemberDOB"),getData("drxMemberPhone") ,getData("drxMemberAddressLineLA"),
                getData("drxMemberAddressLine2LA"));
        contactInfoPage.clickLabel(getData("drxMemberGender"));
        contactInfoPage.clickPrimaryButton("Next");

        EnrollmentFormPage benefitInfoPage = new EnrollmentFormPage("BenefitInfo");
        benefitInfoPage.validatePageLoaded();

    }

    @Test(enabled = false, groups = { "deployment" }, description = "User validates FOAM transaction data")
    public void validateTransactionData() {

    }

}
