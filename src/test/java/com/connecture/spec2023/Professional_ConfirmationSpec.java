package com.connecture.spec2023;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.files.FileFilters;
import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.connecture.base.BaseUIPage.*;
import static com.connecture.page.ConfirmationPage.getDateWithSystemTimeZone;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="Broker_perfMC", year="2023")
public class Professional_ConfirmationSpec extends BaseUISpec {


    @Test( groups = { "deployment" }, description = "Enroll in SNP MAPD with combined rider, check confirmation page and member profile page")

    public void validateEnrollSNPMAPDWithCombinedRiderCheckConfirmationAndProfilePage() throws InterruptedException, IOException {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToSiteBaseUrl();
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
        editProfilePage.validateTemporaryTooltip("Saved");
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
        // Skipping the PDF validation
        // soaPage.validateDownloadedPDF(fileToUpload,);
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
        //OtherPreferences page
        OtherPreferencesPage preferencesPage = new OtherPreferencesPage();
        preferencesPage.validatePageLoaded();
        preferencesPage.selectBenefitOptions("Hearing");
        preferencesPage.selectBenefitOptions("Vision");
        preferencesPage.selectBenefitOptions("Dental");
        preferencesPage.clickPrimaryButton("continue");
        //PlanList page
        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        String getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        String healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Generally healthy\n65-69");
        String providerSectionText = planListPage.fetchProvidersPreferences("Providers");
        String prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugBrand","name"));
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));
        String pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(getData("pharmacyLA"));
        planListPage.clickPrimaryLink("Add all Special Needs Plans (SNP)");
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareSNPMAPDPlanToEnroll"));
        planListPage.clickPlanDetails(getData("flexcareSNPMAPDPlanToEnroll"));

        PlanDetailsPage planDetailsPage = new PlanDetailsPage();
        planDetailsPage.validateMonthlyPlanNameCard(getData("flexcareSNPMAPDPlanToEnroll"));
        planDetailsPage.validatePlanNameHeader(getData("flexcareSNPMAPDPlanToEnroll"));
        String contractPlanSegment = planDetailsPage.getContractPlanIDSegment();
        planDetailsPage.clickPrimaryLink("Previous");

        planListPage.clickAddToCart(getData("flexcareSNPMAPDPlanToEnroll"));
        CartPage cartPage = new CartPage();
        cartPage.clickAddButton(getData("riderMonthlyPlanNameCombined"));
        String riderMonthlyPremium = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameCombined")); // $25.00
        cartPage.validatePlanNameSelected(getData("flexcareSNPMAPDPlanToEnroll"));
        String totalMonthlyPremium = cartPage.getTotalMonthlyPremium(); // $46.10
        cartPage.clickSecondaryButton("Complete and submit form myself");
        cartPage.clickPrimaryButton("Continue to apply");

        EnrollmentFormPage contactInfoPage = new EnrollmentFormPage("/ContactInfo");
        contactInfoPage.validatePageLoaded();
        contactInfoPage.validateContactInfoSection();
        contactInfoPage.validateZipCityState(getData("drxMemberZip"),getData("drxMemberCity"),getData("drxMemberState"));
        contactInfoPage.fillOutPersonalInfo(getData("drxMemberFirstName"),getData("drxMemberLastName"),
                getData("drxMemberDOB"),getData("drxMemberPhone") ,getData("drxMemberAddressLineLA"),
                getData("drxMemberAddressLine2LA"));
        contactInfoPage.clickLabel(getData("drxMemberGender"));
        contactInfoPage.clickPrimaryButton("Next");

        EnrollmentFormPage benefitInfoPage = new EnrollmentFormPage("BenefitInfo");
        benefitInfoPage.validatePageLoaded();
        benefitInfoPage.enterMediCareNumber(getData("medicareNumber"));
        benefitInfoPage.selectEffectiveDate();
        benefitInfoPage.selectAndEnterReasonSEP("test reason");
        benefitInfoPage.selectOptionForQuestion("No");
        benefitInfoPage.clickPrimaryButton("Next");

        EnrollmentFormPage otherInfoPage = new EnrollmentFormPage("OtherInfo");
        otherInfoPage.validatePageLoaded();
        otherInfoPage.clickPrimaryButton("Next");

        EnrollmentFormPage agentInfoPage = new EnrollmentFormPage("AgentInfo");
        agentInfoPage.validatePageLoaded();
        agentInfoPage.clickeSigAgentProducerSigCheckBox();
        agentInfoPage.clickPrimaryButton("Next");

        SubmitPage submitPage = new SubmitPage();
        submitPage.validatePageLoaded();
        submitPage.clickEsigCustomQuestionCheckBox();
        submitPage.selectOptionDescribeRelation("I am the person listed on this enrollment form or I am simply helping to complete this enrollment form.");
        submitPage.clickDisabilityCheckBoxAndEnterName("drxtext");
        submitPage.beneficiarySignature(500);
        submitPage.clickPrimaryButton("Submit");

        ConfirmationPage confirmationPage = new ConfirmationPage();
        confirmationPage.validatePageLoaded();
        confirmationPage.validateH1Heading("Application submitted");
        confirmationPage.validateNoticeBanner("application has been submitted");
        confirmationPage.validatesApplicationData("Member name",getData("drxMemberFirstName")+ " " + getData("drxMemberLastName"));
        confirmationPage.validatesApplicationData("Address",getData("drxMemberAddressLineLA"));
        confirmationPage.validatesApplicationData("Address",getData("drxMemberAddressLine2LA"));
        confirmationPage.validatesApplicationData("Address",getData("drxMemberCity"));
        confirmationPage.validatesApplicationData("Address",getData("drxMemberZip"));
        confirmationPage.validatesApplicationData("Address",getData("drxMemberState"));
        confirmationPageDate = getDateWithSystemTimeZone(getData("serverTimeZone"));
        confirmationPageDateString = String.format(confirmationPageDate.toString(), "MMMM d, yyyy");
        confirmationPage.validatesApplicationData("Submitted",confirmationPageDateString);
        String confirmationNumber = confirmationPage.getApplicationData("Confirmation number");
        confirmationNumber.startsWith("A");
        confirmationNumber.endsWith("M");
        // For Sanity Check purpose
        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber);
        confirmationPage.validatesApplicationData("Confirmation number",confirmationNumber);
        confirmationPage.validateSubmissionBanner("your application has been submitted");
        confirmationPage.validatePlanCoverageCardForMonthlyPremium(planCardMonthlyPremium);
        confirmationPage.validatePlanCoverageCardforMAPDPlanTabText(getData("mapdPlanListTabText"));
        confirmationPage.validatePlanCoverageCardFlexCareSNP(getData("flexcareSNPMAPDPlanToEnroll"));
        confirmationPage.validateRiderCoverageCard(riderMonthlyPremium);
        confirmationPage.validateRiderCoverageCard(getData("riderMonthlyPlanNameCombined"));
        confirmationPage.validateTotalPremiumBanner(totalMonthlyPremium);
        confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","name"));
        confirmationPage.validateCarierDataForAddress(getFlexcareCarrierData("flexcareCarrierMap","address"));
        confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","url"));
        String fixedBrokerPhone = getData("drxMemberPhone");

        // PDF download and validate, use long 30s wait because QA is slowFile
        File enrollmentPDF = confirmationPage.primaryLinks.find(text("View application"))
                .download(30000, new FileFilters().withName("EnrollmentApplication#" + confirmationNumber + ".pdf") );
        confirmationPage.validateDownloadedPDF(enrollmentPDF, new String[] { totalMonthlyPremium, riderMonthlyPremium, planCardMonthlyPremium,
                getData("drxMemberFirstName"),contractPlanSegment.replace("-","_"),
                getData("drxMemberAddressLineLA"), getData("drxMemberAddressLine2LA"), getData("drxMemberCity"),
                getData("drxMemberState"), getData("drxMemberZip"),getData("drxMemberLastName"),
                getFlexcareCarrierData("flexcareCarrierMap","name"),
                getFlexcareCarrierData("flexcareCarrierMap","address"),
                getFlexcareCarrierData("flexcareCarrierMap","url"),
                getFlexcareCarrierData("flexcareCarrierMap","cityState"),
                "Broker Information",
                getBrokerFullNameForSiteLevel(), fixedBrokerPhone});
    }


File fileToUpload = new File("./src/test/resources/uploads/Scope of AppointmentForm - Generic.pdf");

}
