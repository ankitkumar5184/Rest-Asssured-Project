package com.connecture.spec2023;

import com.connecture.base.BaseUISpec;
import com.connecture.page.GetStartedPage;
import com.connecture.page.HomePage;
import com.connecture.page.PlanListPage;
import com.connecture.utils.Site;
import org.junit.Assert;
import org.testng.annotations.Test;

import static com.connecture.utils.TestDataProvider.getData;

@Site(tag="Consumer_customA", year="2023")
public class ConsumerCustom_SubsidyOffSpec extends BaseUISpec {

    @Test(groups = { "regression" }, description = "Validate missing subsidy question")
    public void consumerEnrollsAndValidatesConfirmationPage() throws InterruptedException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");

        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        getStartedPage.clickSecondaryButton("Medical and prescription drug");
        boolean secondaryButtonDisplayed = getStartedPage.validateSecondaryButtonMissing("I am not eligible");
        Assert.assertEquals(secondaryButtonDisplayed,false);
        secondaryButtonDisplayed = getStartedPage.validateSecondaryButtonMissing("I receive help from Medicaid");
        Assert.assertEquals(secondaryButtonDisplayed,false);
        getStartedPage.validateAccordianOptionNotDisplayed();
        Thread.sleep(1000);
        getStartedPage.clickPrimaryLink("go to plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.clickAddButton("aria-label","Add Get Started");

        getStartedPage.validatePageLoaded();
        getStartedPage.validateZipCodeValue(getData("drxMemberZip"));
        secondaryButtonDisplayed = getStartedPage.validateSecondaryButtonMissing("I am not eligible");
        Assert.assertEquals(secondaryButtonDisplayed,false);
        getStartedPage.validateAccordianOptionNotDisplayed();
    }

}
