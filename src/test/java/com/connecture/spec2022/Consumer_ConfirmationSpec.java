/*
package com.connecture.spec2022;

import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.testng.annotations.Test;

import java.util.Date;

import static com.codeborne.selenide.Condition.text;


import static com.connecture.utils.TestDataProvider.*;

*/
/*@Site(tag="Consumer_default", year="2022")
public class Consumer_ConfirmationSpec extends BaseUISpec {

    @Test(groups = { "deployment" }, description = "Show a demo workflow")
    public void consumerEnrollsAndValidatesConfirmationPage()  {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.scrollPlanListToBottom(5); // TODO: Refactor so scrolls automatically to bottom
        planListPage.clickPlanDetails(getData("flexcareCommonMAPDPlanToEnroll"));

        PlanDetailsPage planDetailsPage = new PlanDetailsPage();
        planDetailsPage.validatePlanNameTitle(getData("flexcareCommonMAPDPlanToEnroll"));
        planDetailsPage.validatePlanNameHeader(getData("flexcareCommonMAPDPlanToEnroll"));
        String contractPlanSegmentText = planDetailsPage.getContractPlanIDSegment();
        planDetailsPage.clickPrimaryLink("previous");


        planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.scrollPlanListToBottom(5); // TODO: Refactor so scrolls automatically to bottom
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 145.00
        planListPage.clickAddToCart(getData("flexcareCommonMAPDPlanToEnroll"));

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.planCard.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        cartPage.clickAddButton(getData("riderMonthlyPlanNameCombined"));  // 25.00
        cartPage.validateRiderSelected();
        String riderMonthlyPremiumCombined = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameCombined")); // 25.00
        cartPage.validatePlanNameSelected(getData("flexcareCommonMAPDPlanToEnroll"));
        cartPage.validateSelectedPlanPremium(getData("flexcareCommonMAPDPlanToEnroll"),planCardMonthlyPremium);
        String totalMonthlyPremium = cartPage.getTotalMonthlyPremium(); // $170.00
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

        EnrollmentFormPage confirmationPage = new EnrollmentFormPage("Submit");
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
        confirmationNumber.startsWith("A");
        confirmationNumber.endsWith("M");
        // For Sanity Check purpose
        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber);
        confirmationPage.validatesApplicationData("Confirmation number",confirmationNumber);
        //Plan Coverage Card data validation
        confirmationPage.validatePlanCoverageCard(planCardMonthlyPremium);
        confirmationPage.validatePlanCoverageCard(getData("mapdPlanListTabText"));
        confirmationPage.validatePlanCoverageCard(getData("flexcareCommonMAPDPlanToEnroll"));
        //Rider Coverage Card data validation
        confirmationPage.validateRiderCoverageCard(riderMonthlyPremiumCombined);
        confirmationPage.validateRiderCoverageCard(getData("riderMonthlyPlanNameCombined"));
        // Banner data validation
        confirmationPage.validateTotalPremiumBanner(totalMonthlyPremium);
        //Carrier data validation
        confirmationPage.validateCarierData(getCarrierData("flexcareCarrierQAMap","name"));
        confirmationPage.validateCarierData(getCarrierData("flexcareCarrierQAMap","address"));
        confirmationPage.validateCarierData(getCarrierData("flexcareCarrierQAMap","cityState"));
        confirmationPage.validateCarierData(getCarrierData("flexcareCarrierQAMap","phone"));
        confirmationPage.clickPrimaryButton("Shop for someone else");

        homePage = new HomePage();
        homePage.validatePageLoaded();
        homePage.validateZipCode("");
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View Plans");

        planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareSecondCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 33.00
        planListPage.scrollPlanListToBottom(5); // TODO: Refactor so scrolls automatically to bottom
        planListPage.clickAddToCart(getData("flexcareSecondCommonMAPDPlanToEnroll"));

        cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.validatePlanNameSelected(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        totalMonthlyPremium = cartPage.getTotalMonthlyPremium(); // $33.00
        cartPage.clickPrimaryButton("apply");

        contactInfoPage = new EnrollmentFormPage("ContactInfo");
        contactInfoPage.validatePageLoaded();
        contactInfoPage.validateCityStateZip(getData("drxMemberCity"),getData("drxMemberState"),getData("drxMemberZip"));
        contactInfoPage.validateEmptyNameDOBPhone("","","");
        contactInfoPage.validateMiniCart(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        contactInfoPage.validateMiniCart(totalMonthlyPremium); //33.00
        contactInfoPage.validateMiniCartTotal(totalMonthlyPremium); //33.00
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
        submitPage.clickPrimaryButton("Submit");

        confirmationPage = new EnrollmentFormPage("Confirmation");
        confirmationPage.validatePageLoaded();
        confirmationPage.validateH1Heading("Application submitted");
        confirmationPage.validateNoticeBanner("application has been submitted");
        confirmationPage.validatesApplicationData("Member name","Shop Again" + " " + "Shop Again");
        confirmationNumber = confirmationPage.getApplicationData("Confirmation number");
        // For Sanity Check purpose
        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber);*//*


    //}


    @Test(enabled = false, groups = { "deployment" }, description = "User validates FOAM transaction data")
    public void validateTransactionData() {

    }

}
*/
