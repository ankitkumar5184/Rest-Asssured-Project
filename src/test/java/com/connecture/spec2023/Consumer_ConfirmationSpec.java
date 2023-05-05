package com.connecture.spec2023;

import com.codeborne.selenide.SelenideElement;
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
import static com.connecture.page.ConfirmationPage.getDateWithSystemTimeZone;
import static com.connecture.utils.TestDataProvider.*;
import com.codeborne.selenide.files.FileFilters;

@Site(tag="Consumer_default", year="2023")
public class Consumer_ConfirmationSpec extends BaseUISpec {

    @Test(groups = { "deployment" }, description = "Validate MAPD with combined rider, PDF, email, rates, links, shop again")
    public void validateMAPDWithCombinedRiderRatesLinksAndShopAgain() throws IOException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.clickPlanDetails(getData("flexcareCommonMAPDPlanToEnroll"));

        PlanDetailsPage planDetailsPage = new PlanDetailsPage();
        planDetailsPage.validatePlanNameTitle(getData("flexcareCommonMAPDPlanToEnroll"));
        planDetailsPage.validatePlanNameHeader(getData("flexcareCommonMAPDPlanToEnroll"));
        String contractPlanSegmentText = planDetailsPage.getContractPlanIDSegment();
        planDetailsPage.clickPrimaryLink("previous");


        planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 0.00
        planListPage.clickAddToCart(getData("flexcareCommonMAPDPlanToEnroll"));

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.planCard.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));

        cartPage.clickPrimaryButton("continue to apply");

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
        benefitInfoPage.enterMediCareNumber(getData("medicareNumber"));
        benefitInfoPage.selectEffectiveDate();
        benefitInfoPage.selectAndEnterReasonSEP("test reason");
        benefitInfoPage.selectOptionForQuestion("No");
        benefitInfoPage.clickPrimaryButton("Next");

        EnrollmentFormPage otherInfoPage = new EnrollmentFormPage("OtherInfo");
        otherInfoPage.validatePageLoaded();
        otherInfoPage.clickPrimaryButton("Next");

        EnrollmentFormPage submitPage = new EnrollmentFormPage("Submit");
        submitPage.validatePageLoaded();
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
        Date confirmationPageDate = getDateWithSystemTimeZone(getData("serverTimeZone"));
        String confirmationPageDateString = String.format(confirmationPageDate.toString(), "MMMM d, yyyy");
        confirmationPage.validatesApplicationData("Submitted",confirmationPageDateString);
        String confirmationNumber = confirmationPage.getApplicationData("Confirmation number");
        Assert.assertTrue(confirmationNumber.startsWith("A"));
        Assert.assertTrue(confirmationNumber.endsWith("M"));
        // For Sanity Check purpose
        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber);
        confirmationPage.validatesApplicationData("Confirmation number",confirmationNumber);
        //Plan Coverage Card data validation
        //confirmationPage.validatePlanCoverageCard(planCardMonthlyPremium);
        //confirmationPage.validatePlanCoverageCard(getData("mapdPlanListTabText"));
        //confirmationPage.validatePlanCoverageCard(getData("flexcareCommonMAPDPlanToEnroll"));
        confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","name"));
        confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","address"));
        confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","cityState"));
        //confirmationPage.validateCarierData(getFlexcareCarrierData("flexcareCarrierMap","phone")); // TODO Fix phone validation
        // PDF download and validate, use long 30s wait because QA is slow
        File enrollmentPDF = confirmationPage.primaryLinks.find(text("View application"))
                .download(30000, new FileFilters().withName("EnrollmentApplication#" + confirmationNumber + ".pdf") );
        confirmationPage.validateDownloadedPDF(enrollmentPDF, new String[] { confirmationNumber }); // TODO: Extend confirmationNumber, complete all PDF validations
        // Validations done
        confirmationPage.clickPrimaryButton("Shop for someone else");

        homePage = new HomePage();
        homePage.validatePageLoaded();
        homePage.validateZipCode("");
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View Plans");

        planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareSecondCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 0.00
        planListPage.clickAddToCart(getData("flexcareSecondCommonMAPDPlanToEnroll"));

        cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.validatePlanNameSelected(getData("flexcareSecondCommonMAPDPlanToEnroll"));

        cartPage.clickPrimaryButton("apply");

        contactInfoPage = new EnrollmentFormPage("ContactInfo");
        contactInfoPage.validatePageLoaded();
        contactInfoPage.validateCityStateZip(getData("drxMemberCity"),getData("drxMemberState"),getData("drxMemberZip"));
        contactInfoPage.validateEmptyNameDOBPhone("","","");
        contactInfoPage.validateMiniCart(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        contactInfoPage.fillOutContactInfo( "Shop Again", "Shop Again",
                getData("drxMemberDOB"),getData("drxMemberPhone") ,getData("drxMemberAddressLineLA"),
                getData("drxMemberAddressLine2LA"));
        contactInfoPage.clickLabel(getData("drxMemberGender"));
        contactInfoPage.clickPrimaryButton("Next");

        benefitInfoPage = new EnrollmentFormPage("BenefitInfo");
        benefitInfoPage.validatePageLoaded();
        benefitInfoPage.enterMediCareNumber(getData("medicareNumber"));
        benefitInfoPage.selectEffectiveDate();
        benefitInfoPage.selectAndEnterReasonSEP("test reason");
        benefitInfoPage.selectOptionForQuestion("No");
        benefitInfoPage.clickPrimaryButton("Next");

        otherInfoPage = new EnrollmentFormPage("OtherInfo");
        otherInfoPage.validatePageLoaded();
        otherInfoPage.clickPrimaryButton("Next");

        submitPage = new EnrollmentFormPage("Submit");
        submitPage.validatePageLoaded();
        //Pending signature 
        submitPage.clickPrimaryButton("Submit");

        /*confirmationPage = new ConfirmationPage();
        confirmationPage.validatePageLoaded();
        confirmationPage.validateH1Heading("Application submitted");
        confirmationPage.validateNoticeBanner("application has been submitted");
        confirmationPage.validatesApplicationData("Member name","Shop Again" + " " + "Shop Again");
        confirmationNumber = confirmationPage.getApplicationData("Confirmation number");

        // For Sanity Check purpose
        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber);*/

        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber); // For Sanity Check purpose


    }


    @Test(enabled = false, groups = { "deployment" }, description = "User validates FOAM transaction data")
    public void validateTransactionData() {

    }

}
