package com.connecture.spec2023;

import com.connecture.base.BaseUISpec;
import com.connecture.page.HomePage;
import com.connecture.page.LoginPage;
import com.connecture.utils.Site;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import com.connecture.page.*;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="CallCenter_default", year="2023")
public class Professional_MemberSearchSpec extends BaseUISpec {


    @Test(groups = {"regression"}, description = "To verify Create member, search for member, validate member page")
    public void validateCreateMemberSearchMemberValidateMember() {
        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        LoginPage loginPage = new LoginPage();
        loginPage.validatePageLoaded();
        loginPage.brokerLogin("username", getCallCenterBrokerUsername(), "password", getCallCenterBrokerPassword());

        ProfileSearchPage profileSearchPage = new ProfileSearchPage();
        profileSearchPage.validatePageLoaded();
        profileSearchPage.selectNewProfile("New profile");
        profileSearchPage.clickPrimaryLink("New beneficiary");

        EditProfilePage editProfilePage = new EditProfilePage();
        editProfilePage.validatePageLoaded();
        String newUserFirstName = RandomStringUtils.randomAlphabetic(8);
        String newUserLastName = RandomStringUtils.randomAlphabetic(8);
        editProfilePage.fillOutProfileInfo(newUserFirstName, newUserLastName, getData("drxMemberZip"));
        editProfilePage.validateCountyName(getData("drxMemberCountyName"));
        editProfilePage.enterDOB(getData("drxMemberDOB"));
        editProfilePage.enterPhone(getData("drxMemberPhone"));
        String newUserEmail = getData("unusedEmailDrop");
        editProfilePage.enterEmail(newUserEmail);
        editProfilePage.enterHomeAddress(getData("drxMemberAddressLineLA"), getData("drxMemberAddressLine2LA")
                , getData("drxMemberCity"), getData("drxMemberState"));
        editProfilePage.clickPrimaryButton("Save");
        editProfilePage.validateInnerTooltip("Saved");
        profileSearchPage.selectNewProfile("Search");
        profileSearchPage.clickPrimaryLink("Search beneficiary");

        profileSearchPage = new ProfileSearchPage();
        profileSearchPage.validatePageLoaded();
        profileSearchPage.enterFirstName(newUserFirstName);
        profileSearchPage.enterLastName(newUserLastName);
        profileSearchPage.clickPrimaryButton("Search profiles");
        profileSearchPage.validatePrimaryLinks("search again");
        profileSearchPage.validateMemberNameAndType(newUserFirstName + " " + newUserLastName, "Registrant");
        profileSearchPage.clickMemberNameAndType(newUserFirstName + " " + newUserLastName, "Registrant");

        editProfilePage = new EditProfilePage();
        editProfilePage.validatePageLoaded();
        editProfilePage.validatesBeneficaryName(newUserFirstName, newUserLastName);
        editProfilePage.validateBeneficiaryDOB(getData("drxMemberDOB"));
        System.out.println("Test is done");

    }

}

