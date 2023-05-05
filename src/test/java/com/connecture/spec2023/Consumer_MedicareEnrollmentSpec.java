package com.connecture.spec2023;

import com.codeborne.selenide.ElementsCollection;
import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Condition.text;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="Consumer_default", year="2023")
public class Consumer_MedicareEnrollmentSpec extends BaseUISpec {

    @Test( groups = { "regression" }, description = "Update zip on enrollment form, re-shop, validate enrollment")
    public void validateUpdatedZipReshopValidateEnrollment()  {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");
        //GetStarted tab
        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        String zipCodeValue = getStartedPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
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
        zipCodeValue = pharmacyPage.validateZipCode();
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
        String getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        String healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Generally healthy\n65-69");
        String prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugBrand","name"));
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));
        String pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(getData("pharmacyLA"));
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));
        planListPage.clickAddToCart(getData("flexcareCommonMAPDPlanToEnroll"));

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.validatePlanNameSelected(getData("flexcareCommonMAPDPlanToEnroll"));
        String totalMonthlyPremium = cartPage.getTotalMonthlyPremium();
        cartPage.clickPrimaryButton("apply");

        EnrollmentFormPage contactInfoPage = new EnrollmentFormPage("ContactInfo");
        contactInfoPage.validatePageLoaded();
        contactInfoPage.miniCartArea.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        contactInfoPage.validateMiniCart(planCardMonthlyPremium);
        contactInfoPage.validateMiniCartTotal(planCardMonthlyPremium);
        contactInfoPage.validateEmptyNameDOBPhone("","","");
        contactInfoPage.validateCityStateZip(getData("drxMemberCity"),getData("drxMemberState"),getData("drxMemberZip"));
        contactInfoPage.clickPrimaryLink("Edit");
        contactInfoPage.clickModalPrimaryButton("Back to plans");

        planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Generally healthy\n65-69");
        prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugBrand","name"));
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));
        pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(getData("pharmacyLA"));

        planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        planListPage.clickLargeZipCode();
        planListPage.clearModalZipCodeInput();
        planListPage.enterZipCodeInModalWindow(getData("editZipNewZip"));
        planListPage.validateCountyNameInModalWindow(getData("editZipNewCounty"));
        planListPage.clickModalPrimaryButton("Shop for plans");
        planListPage.clickAddToCart(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        planListPage.clickModalPrimaryButton("Add");

        cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.validatePlanNameSelected(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        totalMonthlyPremium = cartPage.getTotalMonthlyPremium();
        cartPage.clickPrimaryButton("Continue to apply");

        contactInfoPage = new EnrollmentFormPage("/ContactInfo");
        contactInfoPage.validatePageLoaded();
        contactInfoPage.miniCartArea.shouldHave(text(getData("flexcareSecondCommonMAPDPlanToEnroll")));
        contactInfoPage.validateMiniCart(planCardMonthlyPremium);
        contactInfoPage.validateMiniCartTotal(planCardMonthlyPremium);
        contactInfoPage.validateEmptyNameDOBPhone("","","");
        contactInfoPage.validateCityStateZip(getData("editZipNewCity"),getData("drxMemberState"),getData("editZipNewZip"));
        contactInfoPage.fillOutContactInfo( getData("drxMemberFirstName"),getData("drxMemberLastName"),
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

        SubmitPage submitPage = new SubmitPage();
        submitPage.validatePageLoaded();
        submitPage.clickPrimaryButton("Submit");

        ConfirmationPage confirmationPage = new ConfirmationPage();
        confirmationPage.validatePageLoaded();
        confirmationPage.validatesApplicationData("Member name",getData("drxMemberFirstName")+ " " + getData("drxMemberLastName"));
        confirmationPage.validatesApplicationData("Address",getData("drxMemberAddressLineLA"));
        confirmationPage.validatesApplicationData("Address",getData("editZipNewCity"));
        confirmationPage.validatesApplicationData("Address",getData("editZipNewZip"));
        String confirmationNumber = confirmationPage.getApplicationData("Confirmation number");
        confirmationNumber.startsWith("A");
        confirmationNumber.endsWith("M");
        // For Sanity Check purpose
        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber);
        confirmationPage.validatesApplicationData("Confirmation number",confirmationNumber);
        confirmationPage.validateSubmissionBanner("your application has been submitted");
        confirmationPage.validatePlanCoverageCardForMonthlyPremium(planCardMonthlyPremium);
        confirmationPage.validatePlanCoverageCardforMAPDPlanTabText("MAPD");
        confirmationPage.validatePlanCoverageCardforMAPDPlanTabText(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        confirmationPage.validateTotalPremiumBanner(totalMonthlyPremium);
        confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","name"));
        confirmationPage.validateCarierDataForAddress(getFlexcareCarrierData("flexcareCarrierMap","address"));
        confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","url"));

    }


    @Test( groups = { "regression" }, description = "Enroll in flexcare #planLabel without riders, validate confirmation data")
    public void validateEnrollFlexcareWithoutRidersAndConfirmationData()  {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");
        //GetStarted tab
        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        String zipCodeValue = getStartedPage.validateZipCode();

        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
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
        zipCodeValue = pharmacyPage.validateZipCode();
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
        String getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        String healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Generally healthy\n65-69");
        String prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugBrand","name"));
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));
        String pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(getData("pharmacyLA"));
        planListPage.selectMedicalPlanTab(getData("maPlanListTabText"));
        planListPage.selectMedicalPlanTab(getData("pdpPlanListTabText"));
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonPDPlanToEnroll"));
        planListPage.clickAddToCart(getData("flexcareCommonPDPlanToEnroll"));

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.validatePlanNameSelected(getData("flexcareCommonPDPlanToEnroll"));
        String totalMonthlyPremium = cartPage.getTotalMonthlyPremium();
        cartPage.clickPrimaryButton("Continue to apply");

        EnrollmentFormPage contactInfoPage = new EnrollmentFormPage("/ContactInfo");
        contactInfoPage.validatePageLoaded();
        contactInfoPage.miniCartArea.shouldHave(text(getData("flexcareCommonPDPlanToEnroll")));
        contactInfoPage.validateMiniCart(planCardMonthlyPremium);
        contactInfoPage.validateMiniCartTotal(planCardMonthlyPremium);
        contactInfoPage.validateEmptyNameDOBPhone("","","");
        contactInfoPage.validateCityStateZip(getData("drxMemberCity"),getData("drxMemberState"),getData("drxMemberZip"));
        contactInfoPage.fillOutContactInfo( getData("drxMemberFirstName"),getData("drxMemberLastName"),
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

        SubmitPage submitPage = new SubmitPage();
        submitPage.validatePageLoaded();
        submitPage.clickPrimaryButton("Submit");

        ConfirmationPage confirmationPage = new ConfirmationPage();
        confirmationPage.validatePageLoaded();
        confirmationPage.validatesApplicationData("Member name",getData("drxMemberFirstName")+ " " + getData("drxMemberLastName"));
        confirmationPage.validatesApplicationData("Address",getData("drxMemberAddressLineLA"));
        confirmationPage.validatesApplicationData("Address",getData("editZipNewCity"));
        confirmationPage.validatesApplicationData("Address",getData("editZipNewZip"));
        String confirmationNumber = confirmationPage.getApplicationData("Confirmation number");
        confirmationNumber.startsWith("A");
        confirmationNumber.endsWith("M");
        // For Sanity Check purpose
        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber);
        confirmationPage.validatesApplicationData("Confirmation number",confirmationNumber);
        confirmationPage.validateSubmissionBanner("your application has been submitted");
        confirmationPage.validatePlanCoverageCardForMonthlyPremium(planCardMonthlyPremium);
        confirmationPage.validatePlanCoverageCardforMAPDPlanTabText(getData("maPlanListTabText"));
        confirmationPage.validatePlanCoverageCardforMAPDPlanTabText(getData("flexcareCommonPDPlanToEnroll"));
        confirmationPage.validateTotalPremiumBanner(totalMonthlyPremium);
        confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","name"));
        confirmationPage.validateCarierDataForAddress(getFlexcareCarrierData("flexcareCarrierMap","address"));
        confirmationPage.validateCarierDataForURL(getFlexcareCarrierData("flexcareCarrierMap","url"));

    }

}
