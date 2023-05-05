package com.connecture.spec2023;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideWait;
import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import com.connecture.utils.exception.ConnectureCustomException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.connecture.utils.TestDataProvider.getData;
import static com.connecture.utils.TestDataProvider.getDrugData;

@Site(tag="Consumer_default", year="2023")
public class Consumer_PlanListSpec extends BaseUISpec {

    @Test(groups = { "regression" }, description = "Validate plan counts, plan card links")
    public void validatePlanCountsPlanCardLinks()  {


        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.guidedHelpArea.shouldHave(text("Enter your preferences"));
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        int totalPlanCount = planListPage.planCountHeaderCount(); // 21 plans
        // selection of MAPD tab
        planListPage.selectMedicalPlanTab(getData("mapdPlanListTabText"));
        planListPage.validatePlanCardshaveCompareDetailsAndCartButton();
        int mapdPlanCount = planListPage.getActiveTabPlanCount(); // 7 plans
        // selection of MAP tab
        planListPage.selectMedicalPlanTab(getData("maPlanListTabText"));
        planListPage.validatePlanCardshaveCompareDetailsAndCartButton();
        int maPlanCount = planListPage.getActiveTabPlanCount(); // 8 plans
        // selection of PDP tab
        planListPage.selectMedicalPlanTab(getData("pdpPlanListTabText"));
        planListPage.validatePlanCardshaveCompareDetailsAndCartButton();
        int pdpPlanCount = planListPage.getActiveTabPlanCount(); // 6 plans
        int sumOfPlanCount = mapdPlanCount+maPlanCount+pdpPlanCount; //sumOfPlanCount = 21
        Assert.assertEquals(sumOfPlanCount,totalPlanCount);
    }

    @Test(groups = { "regression" }, description = "Validate medicare plan card, star rating, rider icons, tooltips, " +
            "                                       default benefits")
    public void validatePlanCardRatingIconsTooltips() throws InterruptedException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.validateStartRatingTooltip("Flexcare MAPD Plan 2","Medicare evaluates plans");
        // Needs to enable PlanLogo
        planListPage.validateAllRiderTooltip("Flexcare MAPD Plan 2");
        planListPage.validatePlanBenefit("Flexcare MAPD Plan 2","Deductible");

    }

    @Test(groups = { "regression" }, description = "Validate filtering by carrier, subtype for MAPD")
    public void validateFilteringByCarrierSubTypeForMAPD()  {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        List planCardsText = planListPage.getPlanCardsText();
        planCardsText.toString().contains("Flexcare MAPD Plan 2");
        planListPage.selectFilterCheckBox("aria-label","DRX Demo");
        planCardsText.toString().contains("Flexcare MAPD Plan 2");
        planCardsText.toString().contains("Aetna");
        planListPage.selectFilterCheckBox("aria-label","Aetna Medicare");
        planListPage.selectFilterCheckBox("aria-label","HMO");
        planCardsText.toString().contains("Aetna");
        planCardsText.toString().contains("HMO");
    }

    @Test(groups = { "regression" }, description = "Validate #planLabel plan sorting by premium, cost, star, name")
    public void validatePanelLabelPlanSortingByPremiumCostStarName() throws InterruptedException, ConnectureCustomException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");

        //GetStarted tab
        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        getStartedPage.clickSecondaryButton("Medical and prescription drug");
        // we need to see the solution for wait
        Thread.sleep(1000);
        getStartedPage.clickSecondaryButton("No, I am not eligible for special assistance");
        getStartedPage.clickPrimaryButton("continue");
        //Health tab
        HealthPage healthPage = new HealthPage();
        healthPage.validatePageLoaded();
        healthPage.selectToggleOption("65-69");
        getStartedPage.clickPrimaryButton("continue");
        //Providers tab
        ProvidersPage providersPage = new ProvidersPage();
        providersPage.validatePageLoaded();
      //  providersPage.clickSecondaryButton("Add provider");
      //  String selectedProvider = providersPage.getSelectedProviderName();
        providersPage.clickPrimaryButton("continue");
        //Prescription tab
        PrescriptionsPage prescriptionsPage = new PrescriptionsPage();
        prescriptionsPage.validatePageLoaded();

        prescriptionsPage.searchDrug(getDrugData("drugBrand", "searchName"));
        prescriptionsPage.userSelectFirstSearchResult(getDrugData("drugBrand", "searchName"));
        prescriptionsPage.validateSelectedOption(getDrugData("drugBrand", "name"));
        prescriptionsPage.validateOptionalMinDosage(getDrugData("drugBrand", "optionalMinDosages"));
        Boolean selectedButtonYes = prescriptionsPage.validateSecondaryButtonSelectedDisplayed("yes");
        Boolean selectedButtonNo = prescriptionsPage.validateSecondaryButtonSelectedDisplayed("no");

        //Case#1: default to No
        if (!getDrugData("drugBrand", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    prescriptionsPage.clickSecondaryButton("yes");
                }
            }

        } else if (getDrugData("drugBrand", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    prescriptionsPage.clickSecondaryButton("no");
                }
            }

        }
        prescriptionsPage.clickPrimaryButton("Add");


        //Case#2: Case#2 Drug Generic selection
        prescriptionsPage.searchDrug(getDrugData("drugGeneric", "searchName"));
        prescriptionsPage.userSelectFirstSearchResult(getDrugData("drugGeneric", "dosageName"));
        prescriptionsPage.validateSelectedOption(getDrugData("drugGeneric", "dosageName"));

        //Case#2: Case#2 Drug Generic selection default to No
        if (!getDrugData("drugGeneric", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonYes == false) {
                    prescriptionsPage.clickSecondaryButton("yes");
                }
            }

        } else if (getDrugData("drugGeneric", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    prescriptionsPage.clickSecondaryButton("no");
                }
            }

        }
        prescriptionsPage.clickPrimaryButton("Add");


        //Case#3: Drug Switch Generic selection set to Yes
        prescriptionsPage.searchDrug(getDrugData("drugSwitchGeneric", "searchName"));
        prescriptionsPage.userSelectFirstSearchResult(getDrugData("drugSwitchGeneric", "dosageName"));
        prescriptionsPage.validateSelectedOption(getDrugData("drugSwitchGeneric", "dosageName"));

        //If Drug have generic alternative then user needs to select Yes option
        if (!getDrugData("drugSwitchGeneric", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonYes == false) {
                    prescriptionsPage.clickSecondaryButton("yes");
                }
            }
        } else if (getDrugData("drugSwitchGeneric", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    prescriptionsPage.clickSecondaryButton("no");
                }

            }
        }
        prescriptionsPage.clickPrimaryButton("Add");


        //Case#4 Drug Common selection
        prescriptionsPage.searchDrug(getDrugData("drugCommon", "searchName"));
        prescriptionsPage.userSelectFirstSearchResult(getDrugData("drugCommon", "dosageName"));
        prescriptionsPage.validateSelectedOption(getDrugData("drugCommon", "dosageName"));

        //Default to yes
        if (!getDrugData("drugCommon", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonYes == false) {
                    prescriptionsPage.clickSecondaryButton("yes");
                }
            }
        } else if (getDrugData("drugCommon", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    prescriptionsPage.clickSecondaryButton("no");
                }
            }
        }
        prescriptionsPage.clickPrimaryButton("Add");

        //Case#5 Drug package selection
        prescriptionsPage.searchDrug(getDrugData("drugPackage", "searchName"));
        prescriptionsPage.userSelectFirstSearchResult(getDrugData("drugPackage", "dosageName"));
        prescriptionsPage.validateSelectedOption(getDrugData("drugPackage", "dosageName"));

        //Default to yes
        if (!getDrugData("drugPackage", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonYes == false) {
                    prescriptionsPage.clickSecondaryButton("yes");
                }
            }
        } else if (getDrugData("drugPackage", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    prescriptionsPage.clickSecondaryButton("no");
                }
            }
        }
        prescriptionsPage.clickPrimaryButton("Add");

        prescriptionsPage.clickPrimaryButton("continue");
        //Pharmacy tab
        PharmacyPage pharmacyPage = new PharmacyPage();
        pharmacyPage.validatePageLoaded();
        String zipCodeValue = pharmacyPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        String firstPharmacyName = pharmacyPage.getFirstPharmacyNameOnPage();
        ElementsCollection pharmacyNames = pharmacyPage.getPharmacyNames();
        String pharmacyNamesText = pharmacyNames.toString();
        pharmacyNamesText.contains(firstPharmacyName);
        pharmacyPage.validatePharmacyPageCount("2");
        pharmacyPage.userSelectPharmacyPage("2");
        pharmacyPage.clickPrimaryButton("continue");

        //PlanList page
        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        String getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        String healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Generally healthy\n65-69");

        String prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugBrand","name"));
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));
        String pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(getData("pharmacyLA"));
        planListPage.selectMedicalPlanTab(getData("pdpPlanListTabText"));
        planListPage.selectDropDown(getData("premiumSortOption"),getData("costSortOption"));
        // It is commented due to bug in sorting based on Cost
 //     planListPage.validatePlansSortedByCost();
        planListPage.selectDropDown(getData("costSortOption"),getData("premiumSortOption"));
        planListPage.validatePlansSortedByPremium();
        planListPage.selectDropDown(getData("premiumSortOption"),getData("nameSortOption"));
        planListPage.validatesPlansSortedByName();
        planListPage.selectDropDown(getData("nameSortOption"),getData("costSortOption"));
        // It is commented due to bug in sorting based on Cost
     //   planListPage.validatePlansSortedByCost();
    }

}
