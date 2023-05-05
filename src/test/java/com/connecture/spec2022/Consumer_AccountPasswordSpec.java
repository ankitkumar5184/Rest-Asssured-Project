package com.connecture.spec2022;

import com.connecture.base.BaseUISpec;
import com.connecture.page.HomePage;
import com.connecture.utils.Site;
import org.testng.annotations.Test;
import static com.connecture.utils.TestDataProvider.getData;

@Site(tag="Consumer_default", year="2022")
public class Consumer_AccountPasswordSpec extends BaseUISpec {

    @Test( groups = { "deployment" }, description = "Validate password requirements")
    public void validateInvalidPasswordSceOne ()  {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        String invalidPassword1 = "QAmissingnumbers";
        homePage.clickPartialHeadingLink("Login");
        homePage.validateCreateAccountButtonDisplayed("Create account");
        homePage.clickCreateAccountButton("Create account");
        homePage.validateLoginPasswordErrorNotDisplayed();
        homePage.setTextFormControl("firstName",getData("drxMemberFirstName"));
        homePage.setTextFormControl("lastName",getData("drxMemberLastName"));
        homePage.setTextFormControl("email",getData("unusedEmailDrop"));
        homePage.setTextFormControl("password",invalidPassword1);
        homePage.setTextFormControl("passwordConfirm",invalidPassword1);
        homePage.setTextFormControl("userName",getData("newUserName"));
        homePage.zipCodeInput.setValue(getData("multiCountyZip"));
        homePage.selectCountyFromList(getData("multiCountyZipCountyName1"));
        homePage.validateCountyName(getData("multiCountyZipCountyName1"));
        homePage.clickPrimaryButton("Create account");
        homePage.validateLoginPasswordErrorDisplayed();
    }
    @Test( groups = { "deployment" }, description = "Validate password requirements")
    public void validateInvalidPasswordSceTwo ()  {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        String invalidPassword2 = "Short1";
        homePage.clickPartialHeadingLink("Login");
        homePage.validateCreateAccountButtonDisplayed("Create account");
        homePage.clickCreateAccountButton("Create account");
        homePage.validateLoginPasswordErrorNotDisplayed();
        homePage.setTextFormControl("firstName",getData("drxMemberFirstName"));
        homePage.setTextFormControl("lastName",getData("drxMemberLastName"));
        homePage.setTextFormControl("email",getData("unusedEmailDrop"));
        homePage.setTextFormControl("password",invalidPassword2);
        homePage.setTextFormControl("passwordConfirm",invalidPassword2);
        homePage.setTextFormControl("userName",getData("newUserName"));
        homePage.zipCodeInput.setValue(getData("multiCountyZip"));
        homePage.selectCountyFromList(getData("multiCountyZipCountyName1"));
        homePage.validateCountyName(getData("multiCountyZipCountyName1"));
        homePage.clickPrimaryButton("Create account");
        homePage.validateLoginPasswordErrorDisplayed();
    }

    @Test( groups = { "deployment" }, description = "Validate password requirements")
    public void validateInvalidPasswordSceThree ()  {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        String invalidPassword3 = "nouppercase123312312312312nouppercase123312312312312";
        homePage.clickPartialHeadingLink("Login");
        homePage.validateCreateAccountButtonDisplayed("Create account");
        homePage.clickCreateAccountButton("Create account");
        homePage.validateLoginPasswordErrorNotDisplayed();
        homePage.setTextFormControl("firstName",getData("drxMemberFirstName"));
        homePage.setTextFormControl("lastName",getData("drxMemberLastName"));
        homePage.setTextFormControl("email",getData("unusedEmailDrop"));
        homePage.setTextFormControl("password",invalidPassword3);
        homePage.setTextFormControl("passwordConfirm",invalidPassword3);
        homePage.setTextFormControl("userName",getData("newUserName"));
        homePage.zipCodeInput.setValue(getData("multiCountyZip"));
        homePage.selectCountyFromList(getData("multiCountyZipCountyName2"));
        homePage.validateCountyName(getData("multiCountyZipCountyName2"));
        homePage.clickPrimaryButton("Create account");
        homePage.validateLoginPasswordErrorDisplayed();
    }

}
