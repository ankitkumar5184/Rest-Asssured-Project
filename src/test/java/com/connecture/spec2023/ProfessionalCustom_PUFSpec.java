package com.connecture.spec2023;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.files.FileFilters;
import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.connecture.base.BaseUIPage.getBrokerFullNameForSiteLevel;
import static com.connecture.page.ConfirmationPage.getDateWithSystemTimeZone;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="Broker_customB", year="2023")
public class ProfessionalCustom_PUFSpec extends BaseUISpec {

    @Test( groups = { "regression" }, description = "Show PUF plans after guided help with prefs and TEC")
     public void validatePUFAfterGuidedHelpandTEC() throws InterruptedException, IOException {

        LoginPage loginPage = new LoginPage();
        loginPage.openWithBaseUrlAndAuthenticate();
        loginPage.validatePageLoaded();
        boolean userNameDisplayedFlag = loginPage.validateUserNameDisplayed("username");
        Assert.assertEquals(true,userNameDisplayedFlag);
        loginPage.brokerLogin("username",getCommonBrokerUsername(),"password",getStateMedicareBrokerPassword());

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.validatePageLoaded();
        dashboardPage.dashBoardPageHeading.isDisplayed();

        dashboardPage.selectNewProfile("New Profile");
        dashboardPage.clickPrimaryLink("New beneficiary");

        EditProfilePage editProfilePage = new EditProfilePage();
        editProfilePage.validatePageLoaded();
        editProfilePage.fillOutProfileInfo(getData("drxMemberFirstName"),getData("drxMemberLastName"),
                getData("drxMemberZip"));
        editProfilePage.validateCountyName(getData("drxMemberCountyName"));
        editProfilePage.clickPrimaryButton("Save");
        //editProfilePage.validateTemporaryTooltip("Saved"); // TODO: Fix me
        editProfilePage.clickSecondaryButton("Continue to SOA");

        SOAPage soaPage = new SOAPage();
        soaPage.validatePageLoaded();
        soaPage.uploadSOADocument(fileToUpload);
        soaPage.waitForRowToDisplay();
        soaPage.validateSOAActivityRow(1);
        soaPage.validateSOAROwActivityText("View");
        soaPage.validateSOAROwActivityText("Uploaded");
        Date confirmationPageDate = getDateWithSystemTimeZone(getData("serverTimeZone"));
        String confirmationPageDateString = String.format(confirmationPageDate.toString(), "MM/dd/yyyy");
        soaPage.validateSOAROwActivityText(confirmationPageDateString);
        String  uploadFileName = fileToUpload.getName();
        soaPage.validateSOAROwActivityText(uploadFileName);
        soaPage.clickPrimaryLink("View");
        soaPage.clickSecondaryButton("Add preferences");

        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.validateZipCodeValue(getData("drxMemberZip"));
        getStartedPage.clickSecondaryButton("Medical and prescription drug");
        getStartedPage.waitExtraHelpLoad("No, I am not eligible for special assistance");
        getStartedPage.clickSecondaryButton("No, I am not eligible for special assistance");
        getStartedPage.clickPrimaryButton("continue");
        //Health tab
        HealthPage healthPage = new HealthPage();
        healthPage.validatePageLoaded();
        healthPage.selectToggleOption("65-69");
        getStartedPage.clickPrimaryButton("continue");
        //Providers tab
        ProvidersPage providersPage = new ProvidersPage();
        providersPage.validatePageLoaded();
        providersPage.clickPrimaryButton("continue");
        //Prescription tab
        PrescriptionsPage prescriptionsPage = new PrescriptionsPage();
        prescriptionsPage.validatePageLoaded();
        // Add drugs
        prescriptionsPage.standardDrugList();
        prescriptionsPage.clickPrimaryButton("continue");
        //Pharmacy tab
        PharmacyPage pharmacyPage = new PharmacyPage();
        pharmacyPage.validatePageLoaded();
        String zipCodeValue = pharmacyPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        String firstPharmacyName = pharmacyPage.getFirstPharmacyNameOnPage();
        ElementsCollection pharmacyNames = pharmacyPage.getPharmacyNames();
        String pharmacyNamesText = pharmacyNames.toString();
        pharmacyNamesText.contains(firstPharmacyName);
        pharmacyPage.validatePharmacyPageCount("2");
        pharmacyPage.userSelectPharmacyPage("2");
        pharmacyPage.clickPrimaryButton("continue");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        int expectedMinimumMAPDPlanCount = 2;
        int currentPlanCount = planListPage.getActiveTabPlanCount();
        System.out.println("Before PUF count: " +  currentPlanCount);
        Assert.assertTrue( currentPlanCount > expectedMinimumMAPDPlanCount);
        planListPage.clickPrimaryLink("Add non-licensed");
        String samplePufPlanName = "SCAN";
        planListPage.ngxOverlayLoader.shouldBe(visible, Duration.ofSeconds(5));
        planListPage.ngxOverlayLoader.shouldNotBe(visible, Duration.ofSeconds(90));  // Long wait for PUF plans in QA
        planListPage.validatePageLoaded(); // Sanity
        int finalPlanCount = planListPage.getActiveTabPlanCount();
        System.out.println("After PUF count: " +  finalPlanCount);
        Assert.assertTrue( finalPlanCount > currentPlanCount);
        Assert.assertTrue( finalPlanCount > currentPlanCount);
        // TODO: Optionally validate samplePufPlanName variable (fragile due to date changes -- evaluate)
    }

    @Test(enabled = false, groups = { "regression" }, description = "Validate no PUF plans in Helful Tools")
    public void validatePUFHelpfulTools() {

    }

    @Test(enabled = false, groups = { "regression" }, description = "Validate PUF plan details and plan compare")
    public void validatePUFCompare() {

    }

    @Test(enabled = false, groups = { "regression" }, description = "Validate PUF plan cards and quote filtering")
    public void validatePUFCardsQuote() {

    }

    File fileToUpload = new File("./src/test/resources/uploads/Scope of AppointmentForm - Generic.pdf");

}
