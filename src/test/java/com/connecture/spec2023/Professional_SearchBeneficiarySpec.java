package com.connecture.spec2023;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.files.FileFilters;
import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.codeborne.selenide.Condition.text;
import static com.connecture.base.BaseUIPage.getBrokerFullNameForSiteLevel;
import static com.connecture.base.BaseUIPage.getTwoDaysFutureDateWithSystemTimeZone;
import static com.connecture.page.ConfirmationPage.getDateWithSystemTimeZone;
import static com.connecture.utils.TestDataProvider.*;
import org.apache.commons.lang3.RandomStringUtils;

@Site(tag="Broker_default", year="2023")
public class Professional_SearchBeneficiarySpec extends BaseUISpec {

    @Test( groups = { "regression" }, description = "Enroll then search for member using all fields")
    public void enrollAndMemberSearchAllFields() throws InterruptedException, IOException {

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
        String emailPlaceHolder = null; // Pending support
        String newUserFirstName = RandomStringUtils.randomAlphabetic(8);
        String newUserLastName = RandomStringUtils.randomAlphabetic(8);
        editProfilePage.fillOutProfileInfo(newUserFirstName,newUserLastName,getData("drxMemberZip"));
        editProfilePage.enterHomeAddress(getData("drxMemberAddressLineLA"), getData("drxMemberAddressLine2LA"), getData("drxMemberCity"), getData("drxMemberState"));
        editProfilePage.setTextFormControl("city",getData("drxMemberCity"));
        editProfilePage.setTextFormControl("phone",getData("drxMemberPhone"));
        // editProfilePage.setTextFormControl("email", emailPlaceHolder));
        editProfilePage.setTextFormControl("dateOfBirth",getData("drxMemberDOB"));
        editProfilePage.validateCountyName(getData("drxMemberCountyName"));
        editProfilePage.clickPrimaryButton("Save");
        editProfilePage.validateTemporaryTooltip("Saved");
        editProfilePage.clickPrimaryButton("Continue to plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.clickPrimaryLink("Add all Special Needs Plans (SNP)");
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
        String confirmationNumber = confirmationPage.getApplicationData("Confirmation number");
        confirmationPage.selectBrokerMenuBarItem("Search");
        confirmationPage.selectBrokerMenuDropdown("Search beneficiary");

        // 0. Validate no search result and nav back
        ProfileSearchPage profileSearchPage = new ProfileSearchPage();
        profileSearchPage.validatePageLoaded();
        profileSearchPage.firstNameTextBox.sendKeys("NoPerson452342");
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.waitForPrimaryButton("Create a new profile");
        profileSearchPage.validateBodyText("There are no results");
        profileSearchPage.clickPrimaryLink("search again");

        // 1. Search by last name
        profileSearchPage.validatePageLoaded();
        profileSearchPage.lastNameTextBox.sendKeys(newUserLastName);
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.size(1));
        profileSearchPage.validateFirstMemberCard(newUserFirstName + " " + newUserLastName,getData("drxMemberAddressLineLA"),getData("drxMemberAddressLine2LA"),getData("drxMemberZip"),getData("drxMemberCity"), getData("drxMemberState"),
                emailPlaceHolder, getData("drxMemberPhone"),"Applicant", getCommonBrokerUsername(), "Updated", new SimpleDateFormat("MM/dd/yyyy").format(getDateWithSystemTimeZone()) );
        profileSearchPage.clickPrimaryLink("search again");

        // 2. Search by last name and first
        profileSearchPage.validatePageLoaded();
        profileSearchPage.firstNameTextBox.sendKeys(newUserFirstName);
        profileSearchPage.lastNameTextBox.sendKeys(newUserLastName);
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.size(1));
        profileSearchPage.validateFirstMemberCard(newUserFirstName + " " + newUserLastName,getData("drxMemberAddressLineLA"),getData("drxMemberAddressLine2LA"),getData("drxMemberZip"),getData("drxMemberCity"), getData("drxMemberState"),
                emailPlaceHolder, getData("drxMemberPhone"),"Applicant", getCommonBrokerUsername(), "Updated", new SimpleDateFormat("MM/dd/yyyy").format(getDateWithSystemTimeZone()) );
        profileSearchPage.clickPrimaryLink("search again");

        // 3. Search by last name and first and current Agent username
        profileSearchPage.validatePageLoaded();
        profileSearchPage.firstNameTextBox.sendKeys(newUserFirstName);
        profileSearchPage.lastNameTextBox.sendKeys(newUserLastName);
        profileSearchPage.agentTextBox.sendKeys(getCommonBrokerUsername() );
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.size(1));
        profileSearchPage.validateFirstMemberCard(newUserFirstName + " " + newUserLastName,getData("drxMemberAddressLineLA"),getData("drxMemberAddressLine2LA"),getData("drxMemberZip"),getData("drxMemberCity"), getData("drxMemberState"),
                emailPlaceHolder, getData("drxMemberPhone"),"Applicant", getCommonBrokerUsername(), "Updated", new SimpleDateFormat("MM/dd/yyyy").format(getDateWithSystemTimeZone()) );
        profileSearchPage.clickPrimaryLink("search again");

        // 4. Search by last name and first, phone, and dob
        profileSearchPage.validatePageLoaded();
        profileSearchPage.firstNameTextBox.sendKeys(newUserFirstName);
        profileSearchPage.phoneTextBox.sendKeys( getData("drxMemberPhone"));
        profileSearchPage.dobTextBox.sendKeys( getData("drxMemberDOB"));
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.size(1));
        profileSearchPage.validateFirstMemberCard(newUserFirstName + " " + newUserLastName,getData("drxMemberAddressLineLA"),getData("drxMemberAddressLine2LA"),getData("drxMemberZip"),getData("drxMemberCity"), getData("drxMemberState"),
                emailPlaceHolder, getData("drxMemberPhone"),"Applicant", getCommonBrokerUsername(), "Updated", new SimpleDateFormat("MM/dd/yyyy").format(getDateWithSystemTimeZone()) );
        profileSearchPage.clickPrimaryLink("search again");

        // 5. Search by last name and first, mbi
        profileSearchPage.validatePageLoaded();
        profileSearchPage.firstNameTextBox.sendKeys(newUserFirstName);
        profileSearchPage.mbiTextBox.sendKeys( getData("medicareNumber"));
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.size(1));
        profileSearchPage.validateFirstMemberCard(newUserFirstName + " " + newUserLastName,getData("drxMemberAddressLineLA"),getData("drxMemberAddressLine2LA"),getData("drxMemberZip"),getData("drxMemberCity"), getData("drxMemberState"),
                emailPlaceHolder, getData("drxMemberPhone"),"Applicant", getCommonBrokerUsername(), "Updated", new SimpleDateFormat("MM/dd/yyyy").format(getDateWithSystemTimeZone()) );
        profileSearchPage.clickPrimaryLink("search again");

        // 6. Search by conf
        profileSearchPage.validatePageLoaded();
        profileSearchPage.confTextBox.sendKeys(confirmationNumber);
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.size(1));
        profileSearchPage.validateFirstMemberCard(newUserFirstName + " " + newUserLastName,getData("drxMemberAddressLineLA"),getData("drxMemberAddressLine2LA"),getData("drxMemberZip"),getData("drxMemberCity"), getData("drxMemberState"),
                emailPlaceHolder, getData("drxMemberPhone"),"Applicant", getCommonBrokerUsername(), "Updated", new SimpleDateFormat("MM/dd/yyyy").format(getDateWithSystemTimeZone()) );
        profileSearchPage.clickPrimaryLink("search again");

        // 7. Search by conf number and dates
        profileSearchPage.validatePageLoaded();
        profileSearchPage.confTextBox.sendKeys(confirmationNumber);
        profileSearchPage.applicationStartDateTextBox.sendKeys( new SimpleDateFormat("MM/dd/yyyy").format(getDateWithSystemTimeZone()) );
        profileSearchPage.applicationEndDateTextBox.sendKeys( new SimpleDateFormat("MM/dd/yyyy").format(getTwoDaysFutureDateWithSystemTimeZone()) );
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.size(1));
        profileSearchPage.validateFirstMemberCard(newUserFirstName + " " + newUserLastName,getData("drxMemberAddressLineLA"),getData("drxMemberAddressLine2LA"),getData("drxMemberZip"),getData("drxMemberCity"), getData("drxMemberState"),
                emailPlaceHolder, getData("drxMemberPhone"),"Applicant", getCommonBrokerUsername(), "Updated", new SimpleDateFormat("MM/dd/yyyy").format(getDateWithSystemTimeZone()) );
        profileSearchPage.clickPrimaryLink("search again");

        // 8. Search by email - Out of scope currently due to lack of email support methods for new utility
        /* TODO */

        // 9. Search by Agent username and see many (use dates to limit or else will be slow)
        profileSearchPage.validatePageLoaded();
        profileSearchPage.agentTextBox.sendKeys(getCommonBrokerUsername());
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.sizeGreaterThanOrEqual(10));
         profileSearchPage.clickPrimaryLink("search again");

        // 10. Search by MBI username and see many (use dates to limit or else will be slow)
        profileSearchPage.validatePageLoaded();
        profileSearchPage.mbiTextBox.sendKeys(getData("medicareNumber"));
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.allMemberCards.shouldBe(CollectionCondition.sizeGreaterThanOrEqual(10));
        profileSearchPage.clickPrimaryLink("search again");

        // Finally validate the new profile button and new auto-fill name
        profileSearchPage.validatePageLoaded();
        String createNewUserFirstName = RandomStringUtils.randomAlphabetic(8);
        profileSearchPage.firstNameTextBox.sendKeys(createNewUserFirstName);
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.waitForPrimaryButton("Create a new profile");
        profileSearchPage.clickPrimaryButton("Create a new profile");

        EditProfilePage newEditProfilePage = new EditProfilePage();
        newEditProfilePage.validatePageLoaded();
        newEditProfilePage.firstNameTextBox.shouldHave(Condition.value(createNewUserFirstName));

    }


File fileToUpload = new File("./src/test/resources/uploads/Scope of AppointmentForm - Generic.pdf");

}
