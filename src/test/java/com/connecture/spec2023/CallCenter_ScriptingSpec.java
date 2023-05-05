package com.connecture.spec2023;

import com.connecture.base.BaseUISpec;
import com.connecture.config.Locations;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.checkerframework.checker.units.qual.C;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.connecture.base.BaseUIPage.getBeneficiaryText;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="CallCenter_default", year="2023")
public class CallCenter_ScriptingSpec extends BaseUISpec {


    @Test( groups = { "regression" }, description = "To verify user validates page scripting")
    public void verifyUserValidatesPageScripting() throws InterruptedException {
        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        LoginPage loginPage = new LoginPage();
        loginPage.validatePageLoaded();
        loginPage.brokerLogin("username",getCallCenterBrokerUsername(),"password",getCallCenterBrokerPassword());

        ProfileSearchPage profileSearchPage = new ProfileSearchPage();
        profileSearchPage.validatePageLoaded();
        String scriptTextFromPage = getBeneficiaryText();
        String expectedSearchBeneficiaryPageText = "\"I would be happy to help you find a plan that meets your needs. To get us started, " +
                "can I have your first and last name please?"+"\"\n\n"+
        "If you find one or more matching profiles, ask: " +"\"May I have your telephone number?"+"\"";
        Assert.assertEquals(scriptTextFromPage,expectedSearchBeneficiaryPageText);
        profileSearchPage.selectNewProfile("New profile");
        profileSearchPage.clickPrimaryLink("New beneficiary");

        EditProfilePage editProfilePage= new EditProfilePage();
        editProfilePage.validatePageLoaded();
        scriptTextFromPage = getBeneficiaryText();

        String expectedEditProfilePageText = "If you did not find a matching profile: \"What is the first and last name as shown on your Medicare card?\"\n" +
                "\n" +
                "\"To identify available plans in your area, what is the ZIP code of your primary residence?\"\n" +
                "\n" +
                "In all cases, collect or verify required information (e.g., ZIP code, phone number).";
        Assert.assertEquals(scriptTextFromPage,expectedEditProfilePageText);
        editProfilePage.fillOutProfileInfo(getData("drxMemberFirstName"),getData("drxMemberLastName"),
                getData("drxMemberZip"));
        editProfilePage.validateCountyName(getData("drxMemberCountyName"));
        editProfilePage.enterDOB(getData("drxMemberDOB"));
        editProfilePage.enterPhone(getData("drxMemberPhone"));
        editProfilePage.enterHomeAddress(getData("drxMemberAddressLineLA"),getData("drxMemberAddressLine2LA")
                ,getData("drxMemberCity"),getData("drxMemberState"));
        editProfilePage.clickPrimaryButton("Save");
        editProfilePage.validateInnerTooltip("Saved");
        editProfilePage.clickPrimaryLink("Add preferences");

        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.validateZipCodeValue(getData("drxMemberZip"));
        scriptTextFromPage = getBeneficiaryText();
        String expectedMultiLineStringGuidedHelpPageGetStarted = "\"Do you receive Extra Help from Medicare to pay your prescription drug costs?\"\n" +
                "\n" +
         "If the answer is yes, ask the question below.\n"+
         "If the answer is no, select another tab.\n"+
         "If the answer is uncertain, select 'I don't know' below.";
        Assert.assertEquals(scriptTextFromPage,expectedMultiLineStringGuidedHelpPageGetStarted);
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        getStartedPage.validateCountyName(getData("drxMemberCountyName"));
        getStartedPage.clickSecondaryButton("Medical and prescription drug");
        getStartedPage.waitExtraHelpLoad("No, I am not eligible for special assistance");
        getStartedPage.clickSecondaryButton("No, I am not eligible for special assistance");
        getStartedPage.clickPrimaryButton("continue");

        HealthPage healthPage = new HealthPage();
        healthPage.validatePageLoaded();
        healthPage.selectToggleOption("80-84");
        scriptTextFromPage= getBeneficiaryText();
        String expectedMultiLineStringGuidedHelpHealth= "\"Shopping for a health plan is not just about finding a low premium. There are costs such as copays and deductibles. I can help you " +
                "estimate what your total cost will be with each plan if you answer a few questions. Your answers will not affect your premium.\"\n" +
                "\n" +
                "Ask the age and health questions below.";
        Assert.assertEquals(scriptTextFromPage,expectedMultiLineStringGuidedHelpHealth);
        healthPage.clickPrimaryButton("continue");

        PrescriptionsPage prescriptionsPage = new PrescriptionsPage();
        prescriptionsPage.validatePageLoaded();
        scriptTextFromPage= getBeneficiaryText();
        String multiLineStringGuidedHelpPagePrescriptions ="\"Mr./Ms. "+getData("drxMemberLastName")+", I can calculate what your drug costs would be for each plan." +
                " What prescribed medication, if any, are you currently taking?\"";
        Assert.assertEquals(scriptTextFromPage,multiLineStringGuidedHelpPagePrescriptions);
        healthPage.clickPrimaryButton("continue");

        PharmacyPage pharmacyPage = new PharmacyPage();
        pharmacyPage.validateZipCode();
        pharmacyPage.validateZipCode(getData("drxMemberZip"));
        scriptTextFromPage= getBeneficiaryText();
        String expectedMultiLineStringGuidedHelpPagePharmacy = "\"If you have a favorite pharmacy, I can use that to provide you with more accurate drug pricing.\"";
        Assert.assertEquals(scriptTextFromPage,expectedMultiLineStringGuidedHelpPagePharmacy);
        pharmacyPage.clickPrimaryButton("Continue");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        scriptTextFromPage= getBeneficiaryText();
        String multiLineStringPlanListPage = "There are several plans to choose from, with premiums ranging from " +
                "[say lowest premium] to [say highest premium]. I recommend [say the first plan on the list] because " +
                "you'll likely have the lowest total costs with it. This may be the best value for you, based on your " +
                "health and prescriptions. Would you like to learn more about that plan, or would you like to compare a " +
                "few options?";
        Assert.assertEquals(scriptTextFromPage,multiLineStringPlanListPage);
        planListPage.validatePlanNameInCard(getData("flexcareCommonMAPDPlanToEnroll"));
        planListPage.selectPlanToCompare(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        planListPage.selectPlanToCompare(getData("flexcareCommonMAPDPlanToEnroll"));
        String mapdPlanToEnrollPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));
        planListPage.clickPrimaryLink("Compare now");

        ComparePlansPage comparePlansPage = new ComparePlansPage();
        comparePlansPage.validatePageLoaded();
        scriptTextFromPage= getBeneficiaryText();
        String expectedMultiLineStringPlanComparePage = "\"As I go over the highlights of the plans, I encourage you to stop me and ask any questions.\"\n"+
                "\n"+
        "\"Both/all of the plans offer... Plan A offers... whereas Plan B offers...\"";
        Assert.assertEquals(scriptTextFromPage,expectedMultiLineStringPlanComparePage);
        comparePlansPage.clickPlanDetails(getData("flexcareCommonMAPDPlanToEnroll"),"Plan Details");

        PlanDetailsPage planDetailsPage = new PlanDetailsPage();
        planDetailsPage.validatePageLoaded();
        scriptTextFromPage= getBeneficiaryText();
        String expectedMultiLineStringPlanDetailsPagePlanDetailTab = "\"I will discuss with you the benefits of "+ getData("flexcareCommonMAPDPlanToEnroll") +
                " at a detailed level so that we can make sure this will fit your needs.\"";
        Assert.assertEquals(scriptTextFromPage,expectedMultiLineStringPlanDetailsPagePlanDetailTab);
        planDetailsPage.selectTab("Prescriptions");
        planDetailsPage.validateSelectedTab("Prescriptions");
        scriptTextFromPage= getBeneficiaryText();
        String  exptectedMultiLineStringPlanDetailsPagePrescriptionsTab= "\"I can review with you information about your estimated drug costs based on the prescriptions you mentioned.\"";
        Assert.assertEquals(scriptTextFromPage,exptectedMultiLineStringPlanDetailsPagePrescriptionsTab);
        planDetailsPage.selectTab("Total estimated costs");
        planDetailsPage.validateSelectedTab("Total estimated costs");
        planDetailsPage.validateMonthlyPlanNameCard(getData("flexcareCommonMAPDPlanToEnroll"));
        planDetailsPage.validatePlanNameHeader(getData("flexcareCommonMAPDPlanToEnroll"));
        planDetailsPage.clickPrimaryButton("Add to cart");

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.clickAddButton(getData("riderMonthlyPlanNameCombined"));
        String riderMonthlyPremium = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameCombined"));
        cartPage.validatePlanNameSelected(getData("flexcareCommonMAPDPlanToEnroll"));
        String localTotalMonthlyPremium = cartPage.getTotalMonthlyPremium();
        cartPage.clickPrimaryButton("Continue to apply");

        EnrollmentFormPage contactInfoPage = new EnrollmentFormPage("ContactInfo");
        contactInfoPage.validatePageLoaded();
        scriptTextFromPage= getBeneficiaryText();
        String expectedMultiLineStringEnrollmentPage = "\"To confirm, you have decided to enroll in " +
                getData("flexcareCommonMAPDPlanToEnroll")+" which has a monthly price of " +mapdPlanToEnrollPremium
                +". We'll just need to answer a series of questions...\"";
        Assert.assertEquals(scriptTextFromPage,expectedMultiLineStringEnrollmentPage);
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

        EnrollmentFormPage agentInfo = new EnrollmentFormPage("AgentInfo");
        agentInfo.validatePageLoaded();
        agentInfo.clickeSigAgentProducerSigCheckBox();
        agentInfo.clickPrimaryButton("Next");

        SubmitPage submitPage = new SubmitPage();
        submitPage.validatePageLoaded();
        submitPage.clickEsigCustomQuestionCheckBox();
        submitPage.selectOptionDescribeRelation("I am the person listed on this enrollment form or I am simply helping to complete this enrollment form.");
        submitPage.clickDisabilityCheckBoxAndEnterName("drxtext");
        submitPage.beneficiarySignature(500);
        submitPage.clickPrimaryButton("Submit");

        ConfirmationPage confirmationPage = new ConfirmationPage();
        confirmationPage.validatePageLoaded();
        scriptTextFromPage= getBeneficiaryText();
        String expectedMultiLineStringConfirmationPage = "\"Congratulations, Mr./Ms. "+
                getData("drxMemberLastName")+". You are now enrolled with "+
                getFlexcareCarrierData("flexcareCarrierMap","name")+ " in "+ getData("flexcareCommonMAPDPlanToEnroll")+
                ". Do you have a pen and paper ready? It will take a few days for the enrollment to process. " +
                "In the meantime, I will provide you with the confirmation number for reference as well as my name" +
                " and phone number in case you have any additional questions. " +
                "Is there anything else I can assist you with today? Thank you for choosing [[Spa_ClientName]]. It was a pleasure helping you find the best plan.\"";
        Assert.assertEquals(scriptTextFromPage,expectedMultiLineStringConfirmationPage);
        confirmationPage.validateH1Heading("Application submitted");
        confirmationPage.validateNoticeBanner("application has been submitted");
        confirmationPage.validatesApplicationData("Member name",getData("drxMemberFirstName")+ " " + getData("drxMemberLastName"));

    }

}
