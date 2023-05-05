package com.connecture.spec2023;

import com.codeborne.selenide.files.FileFilters;
import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.connecture.page.ConfirmationPage.getDateWithSystemTimeZone;
import static com.connecture.utils.TestDataProvider.getData;
import static com.connecture.utils.TestDataProvider.getFlexcareCarrierData;

@Site(tag="Consumer_customA", year="2023")
public class ConsumerCustom_PlanDetailsOnConfirmationOnSpec extends BaseUISpec {

    @Test(groups = { "regression" }, description = "Validate plan details link from confirmation")
    public void validatePlanDetailsFromConfirmation() throws IOException {

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
        Date confirmationPageDate = getDateWithSystemTimeZone(getData("serverTimeZone"));
        String confirmationNumber = confirmationPage.getApplicationData("Confirmation number");
        confirmationPage.validateConfirmationNumber("Confirmation number",confirmationNumber);
        confirmationPage.clickPrimaryLink("view plan details");

        PlanDetailsPage planDetailsPageAfterConfirmation = new PlanDetailsPage();
        planDetailsPageAfterConfirmation.validatePlanNameTitle(getData("flexcareCommonMAPDPlanToEnroll"));
        planDetailsPageAfterConfirmation.validatePlanNameHeader(getData("flexcareCommonMAPDPlanToEnroll"));

    }

}
