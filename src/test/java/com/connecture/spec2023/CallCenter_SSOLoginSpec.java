package com.connecture.spec2023;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
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
import static com.codeborne.selenide.Selenide.switchTo;
import static com.connecture.base.BaseUIPage.getBrokerFullNameForSiteLevel;
import static com.connecture.page.ConfirmationPage.getDateWithSystemTimeZone;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="CallCenter_default", year="2023")
public class CallCenter_SSOLoginSpec extends BaseUISpec {

    @Test( groups = { "regression" }, description = "User does SSO login with agent")
    public void callCenterLoginAndValidateViaSSSO()  {

        String samleBaseURL = Configuration.baseUrl;
        Configuration.baseUrl = "https://admin.qa.destinationrx.com/PlatformManager2/TestTools/SAML"; // F
        System.out.println("Baseurl: " + Configuration.baseUrl);
        PlatformLoginPage platformLoginPage = new PlatformLoginPage();
        platformLoginPage.openWithBaseUrl();
        platformLoginPage.validatePageLoaded();
        platformLoginPage.loginReturn();

        PlatformSAMLPage samlPage = new PlatformSAMLPage();
        samlPage.validatePageLoaded();
        samlPage.enterURL(samleBaseURL + getData("ssoUrlSuffixForExternalId"));
        samlPage.enterNameID(getData("ssoAgentExternalUserUd"));
        samlPage.addAssertion.click();
        samlPage.assertionFieldTextBox.get(1).sendKeys("ClientID");
        samlPage.assertionFieldTextBox.get(2).sendKeys("DRX");
        samlPage.assertionFieldTextBox.get(4).sendKeys("SSOValue");
        samlPage.assertionFieldTextBox.get(5).sendKeys("NewT55654");
        samlPage.sendSAMLButton.click();
        switchTo().window(1);

        EditProfilePage editProfilePage = new EditProfilePage();
        editProfilePage.validatePageLoaded();
        editProfilePage.firstNameTextBox.shouldHave(Condition.value("Automated"));

    }


}
