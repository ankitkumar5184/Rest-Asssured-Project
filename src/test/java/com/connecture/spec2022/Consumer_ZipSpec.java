package com.connecture.spec2022;

import com.codeborne.selenide.Selenide;
import com.connecture.base.BaseUISpec;
import com.connecture.page.GetStartedPage;
import com.connecture.page.HelpfulToolsPage;
import com.connecture.page.HomePage;
import com.connecture.page.PlanListPage;
import com.connecture.utils.Site;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.Selenide.switchTo;
import static com.connecture.utils.TestDataProvider.getData;

@Site(tag="Consumer_default", year="2022")
public class Consumer_ZipSpec extends BaseUISpec {


    @Test( groups = { "deployment" }, description = "User validates zip code with multi state and multi county")
    public void zipCodeValidation() {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        //Enter Multi County Zip Code
        homePage.enterZipCode(getData("multiCountyZip"));
        homePage.validateCountyListItemName(getData("multiCountyZipCountyName1"));
        homePage.validateCountyListItemName(getData("multiCountyZipCountyName2"));
        homePage.validateCountyListItemName(getData("multiCountyZipCountyName3"));
        homePage.validateCountyListItemName(getData("multiCountyZipCountyName4"));
        homePage.selectCountyFromList(getData("multiCountyZipCountyName4"));
        homePage.validateCountyName(getData("multiCountyZipCountyName4"));
        homePage.clearZipCodeField();
        //Enter Multi State Zip Code
        homePage.enterZipCode(getData("multiStateZip"));
        homePage.validateCountyListItemName(getData("multiStateZipCountyName1"));
        homePage.validateCountyListItemName(getData("multiStateZipCountyName2"));
        homePage.selectCountyFromList(getData("multiStateZipCountyName1"));
        homePage.validateCountyName(getData("multiStateZipCountyName1"));
        // Change county
        homePage.clickPrimaryLink("edit");
        homePage.selectCountyFromList(getData("multiStateZipCountyName2"));
        homePage.validateCountyName(getData("multiStateZipCountyName2"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        // Plan list zip link validation
        planListPage.largeZipLink.shouldHave(text(getData("multiStateZip")));
    }

        @Test( groups = { "deployment" }, description = "User validates zip code at  Home, Guided Help, Plan List, Helpful Tools")
        public void zipCodeValidationAtHomeGuidedHelpPlanListHelpfulTools() {

            HomePage homePage = new HomePage();
            homePage.openWithBaseUrlAndAuthenticate();
            homePage.validatePageLoaded();
            homePage.enterZipCode(getData("multiCountyZip"));
            homePage.selectCountyFromList(getData("multiCountyZipCountyName4"));
            homePage.validateCountyName(getData("multiCountyZipCountyName4"));
            homePage.clickPrimaryButton("View plans");

            PlanListPage planListPage = new PlanListPage();
            planListPage.validatePageLoaded();
            // Plan list zip link validation
            planListPage.largeZipLink.shouldHave(text(getData("multiCountyZip")));
            planListPage.clickLargeZipCode();
            planListPage.clearZipCodeField();
            planListPage.enterZipCode(getData("multiStateZip"));

            planListPage.selectCountyFromList(getData("multiStateZipCountyName1"));
            planListPage.clickPrimaryLink("edit");
            planListPage.selectCountyFromList(getData("multiStateZipCountyName2"));
            planListPage.validateCountyName(getData("multiStateZipCountyName2"));
            planListPage.clickPrimaryButton("Shop for plans");
            planListPage.largeZipLink.shouldHave(text(getData("multiStateZip")));
            planListPage.clickPrimaryButton("Add preferences");

            GetStartedPage getStarted = new GetStartedPage();
            getStarted.validatePageLoaded();
            getStarted.zipCodeInput.shouldHave(value(getData("multiStateZip")));
            getStarted.clearZipCodeField();
            getStarted.enterZipCode(getData("multiStateZip"));
            getStarted.selectCountyFromList(getData("multiStateZipCountyName1"));
            //planListPage.validateSelectedCounty(getData("multiStateZipCountyName2")); // Pending verification
            getStarted.clickPrimaryLink("Go to plans");

            planListPage = new PlanListPage();
            planListPage.validatePageLoaded();
            planListPage.largeZipLink.shouldHave(text(getData("multiStateZip")));
            planListPage.clickPrimaryLink("Drug and pharmacy finder");

            // Browser tab needs to move from Plan List page to Helpful Tools page
            switchTo().window(1);

            HelpfulToolsPage helpfulToolsPage = new HelpfulToolsPage();
            helpfulToolsPage.validatePageLoaded();
            helpfulToolsPage.largeZipLink.shouldHave(text(getData("multiStateZip")));
            helpfulToolsPage.clickLargeZipCode();
            helpfulToolsPage.clearZipCodeField();
            helpfulToolsPage.enterZipCode(getData("drxMemberZip"));
            helpfulToolsPage.zipCodeInput.shouldHave(value(getData("drxMemberZip")));
            helpfulToolsPage.validateCountyName(getData("drxMemberCountyName"));
            helpfulToolsPage.waitForPrimaryButton("Shop for plans");
            helpfulToolsPage.clickPrimaryButton("Shop for plans");
            helpfulToolsPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        }

    @Test( groups = { "deployment" }, description = "Validate non-duplicate county names")
    public void zipCodeValidationForNonDuplicate() {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("multiStateZip"));
        homePage.validateCountyNonDuplicates(getData("multiStateZipCountyName1"));
        homePage.validateCountySize(2);
    }

}


