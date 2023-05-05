package com.connecture.spec2022;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import com.connecture.utils.exception.ConnectureCustomException;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import static com.connecture.utils.TestDataProvider.getData;
import static com.connecture.utils.TestDataProvider.getDrugData;
import static io.netty.util.internal.SystemPropertyUtil.contains;

@Site(tag="Consumer_default", year="2022")
public class Consumer_GuidedHelpSpec extends BaseUISpec {


    @Test(groups = {"deployment"}, description = "Validate pharmacy pagination, map pin, tooltip")
    public void validatePharmacyPaginationMapPinTooltip() throws InterruptedException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");
        //GetStarted tab
        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        String zipCodeValue = getStartedPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
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
        providersPage.clickSecondaryButton("Add provider");
        String selectedProvider = providersPage.getSelectedProviderName();
        providersPage.clickPrimaryButton("continue");
        //Prescription tab
        PrescriptionsPage prescriptionsPage = new PrescriptionsPage();
        prescriptionsPage.validatePageLoaded();

        //Case#1: Drug Brand selection have min dosage
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
        zipCodeValue = pharmacyPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        String firstPharmacyName = pharmacyPage.getFirstPharmacyNameOnPage();
        ElementsCollection pharmacyNames = pharmacyPage.getPharmacyNames();
        String pharmacyNamesText = pharmacyNames.toString();
        pharmacyNamesText.contains(firstPharmacyName);
        pharmacyPage.validatePharmacyPageCount("2");
        pharmacyPage.userSelectPharmacyPage("2");
        pharmacyPage.clickPrimaryButton("continue");
        //OtherPreferences page
        OtherPreferencesPage preferencesPage = new OtherPreferencesPage();
        preferencesPage.validatePageLoaded();
        preferencesPage.selectBenefitOptions("Hearing");
        preferencesPage.selectBenefitOptions("Vision");
        preferencesPage.selectBenefitOptions("Dental");
        preferencesPage.clickPrimaryButton("continue");
        //PlanList page
        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        String getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        String healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Generally healthy\n65-69");
        String providerSectionText = planListPage.fetchProvidersPreferences("Providers");
        providerSectionText.contains(selectedProvider);
        String prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugBrand","name"));
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));
        String pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(getData("pharmacyLA"));
    }

    @Test(groups = {"deployment"}, description = "Go to plans requires zip code")
    public void validateGotoPlansRequiresZipCode() {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");
        //GetStarted tab
        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.clickSecondaryButton("Medical only");
        getStartedPage.clickPrimaryLink("Go to plans");
        getStartedPage.clickPrimaryButton("Shop for plans");
        getStartedPage.validateZipCodeError();
        getStartedPage.enterZipCodeInModalWindow(getData("drxMemberZip"));
        getStartedPage.validateCountyNameInModalWindow(getData("drxMemberCountyName"));
        getStartedPage.clickModalPrimaryButton("Shop for plans");
        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.scrollPlanListToBottom(15); // TODO: Refactor so scrolls automatically to bottom
        planListPage.clickAddToCart("flexcare MA plan 1");
    }
    @Test(groups = {"deployment"}, description = "Validate coverage type selection and correct Plan List tab")
    public void validateCoverageTypeSelectionAndCorrectPlanListTab() {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");
        //GetStarted tab
        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        String zipCodeValue = getStartedPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        getStartedPage.validateCountyName(getData("drxMemberCountyName"));
        getStartedPage.clickSecondaryButton("Medical only");
        getStartedPage.clickPrimaryLink("Go to plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.scrollPlanListToBottom(15); // TODO: Refactor so scrolls automatically to bottom
        planListPage.clickAddToCart("flexcare MA plan 1");

    }

    @Test(groups = {"deployment"}, description = "Validate Add Preferences button, then complete all of guided help")
    public void validateAddPreferencesButtonCompleteGuidedHelp() throws InterruptedException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.clickPrimaryButton("Add preferences");

        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.validateCountyName(getData("drxMemberCountyName"));
        String zipCode = getStartedPage.validateZipCode();
        Assert.assertEquals((getData("drxMemberZip")),zipCode);
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
        providersPage.clickSecondaryButton("Add provider");
        String selectedProvider = providersPage.getSelectedProviderName();
        providersPage.clickPrimaryButton("continue");
        //Prescription tab
        PrescriptionsPage prescriptionsPage = new PrescriptionsPage();
        prescriptionsPage.validatePageLoaded();

        //Case#1: Drug Brand selection have min dosage
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
        //OtherPreferences page
        OtherPreferencesPage preferencesPage = new OtherPreferencesPage();
        preferencesPage.validatePageLoaded();
        preferencesPage.selectBenefitOptions("Hearing");
        preferencesPage.selectBenefitOptions("Vision");
        preferencesPage.selectBenefitOptions("Dental");
        preferencesPage.clickPrimaryButton("continue");
        //PlanList page
        planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        String getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        String healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Generally healthy\n65-69");
        String providerSectionText = planListPage.fetchProvidersPreferences("Providers");
        providerSectionText.contains(selectedProvider);
        String prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugBrand","name"));
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));
        String pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(getData("pharmacyLA"));
    }

    @Test(groups = {"deployment"}, description = "User changes each section of guided help via edit button from PlanList")
    public void validateUserEditGuidedHelpFromPlanListPage() throws InterruptedException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");
        //GetStarted tab
        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        String zipCodeValue = getStartedPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        getStartedPage.clickSecondaryButton("Medical and prescription drug");
        // we need to see the solution for wait
        Thread.sleep(1000);
        getStartedPage.clickSecondaryButton("No, I am not eligible for special assistance");
        getStartedPage.clickPrimaryButton("continue");

        //Health tab
        HealthPage healthPage = new HealthPage();
        healthPage.validatePageLoaded();
        healthPage.selectToggleOption("65-69");
        healthPage.clickPrimaryButton("continue");

        //Providers tab
        ProvidersPage providersPage = new ProvidersPage();
        providersPage.validatePageLoaded();
        providersPage.clickSecondaryButton("Add provider");
        String selectedProvider = providersPage.getSelectedProviderName();
        providersPage.clickPrimaryButton("continue");

        //Prescription tab
        PrescriptionsPage prescriptionsPage = new PrescriptionsPage();
        prescriptionsPage.validatePageLoaded();

        //Case#1: Drug Brand selection have min dosage
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
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        pharmacyPage.validatePharmacy(getData("pharmacyLA"));
        pharmacyPage.selectPharmacy(getData("pharmacyLA"),"add pharmacy");
        pharmacyPage.clickPrimaryButton("continue");

        //OtherPreferences page
        OtherPreferencesPage preferencesPage = new OtherPreferencesPage();
        preferencesPage.validatePageLoaded();
        preferencesPage.selectBenefitOptions("Hearing");
        preferencesPage.selectBenefitOptions("Vision");
        preferencesPage.selectBenefitOptions("Dental");
        preferencesPage.clickPrimaryButton("continue");

        //PlanList page
        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        String getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        String healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Generally healthy\n65-69");
        String providerSectionText = planListPage.fetchProvidersPreferences("Providers");
        providerSectionText.contains(selectedProvider);
        String prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugBrand","name"));
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));
        String pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(getData("pharmacyLA"));

        planListPage.clickEditButton("aria-label","Edit Get Started");
        getStartedPage.validatePageLoaded();
        zipCodeValue = getStartedPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        getStartedPage.validateToggleOptionSelected("Medical and prescription drug");
        getStartedPage.validateAccordianOptionSelected("No, I am not eligible for special assistance");
        getStartedPage.selectAccordianOption("I receive help from Medicaid");
        getStartedPage.clickPrimaryLink("Go to plans");
        //Validate Get Started updated value
        planListPage.validatePageLoaded();
        getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("I receive help from Medicaid");

        planListPage.clickEditButton("aria-label","Edit Health");
        healthPage = new HealthPage();
        healthPage.validatePageLoaded();
        healthPage.validateSelectedToggleOption("65-69");
        healthPage.selectToggleOption("70-74");
        healthPage.moveSliderHealth(500);//Significant health needs
        healthPage.clickPrimaryLink("Go to plans");
        //Validate Health updated value
        planListPage.validatePageLoaded();
        healthSectionText = planListPage.fetchHealthPreferences("Health"); //
        healthSectionText.contains("Significant health needs\n70-74");

        planListPage.clickEditButton("aria-label","Edit Prescriptions");
        prescriptionsPage.validatePageLoaded();
        boolean flag = prescriptionsPage.validateOverlaySmoker();
        Assert.assertEquals(flag,false);
        prescriptionsPage.validateDrugTextBox();
        prescriptionsPage.validateDrugCardList();
        prescriptionsPage.removeDrugFromList(getDrugData("drugBrand","name"));
        prescriptionsPage.validateDrugNameInDrugCardList(getDrugData("drugGeneric","name"));
        prescriptionsPage.clickPrimaryLink("go to plans");
        //Validate Prescription updated value
        planListPage.validatePageLoaded();
        prescriptionSectionText = planListPage.fetchRXBrandPreferences("Prescriptions");
        prescriptionSectionText.contains(getDrugData("drugGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugSwitchGeneric","name"));
        prescriptionSectionText.contains(getDrugData("drugCommon","name"));
        prescriptionSectionText.contains(getDrugData("drugPackage","name"));

        planListPage.clickEditButton("aria-label","Edit Pharmacy");
        pharmacyPage.validatePageLoaded();
        zipCodeValue = pharmacyPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        //Validates First Pharmacy Name
        String firstSelectedPharmacyName = pharmacyPage.getSelectedPharmacyName();
        firstSelectedPharmacyName.contains(getData("pharmacyLA"));
        String newPharmacyName = pharmacyPage.getFirstPharmacyNameOnPage();
        pharmacyPage.selectPharmacy(newPharmacyName,"add pharmacy");
        String secondSelectedPharmacyName = pharmacyPage.getSecondPharmacyNameOnPage();
        secondSelectedPharmacyName.contains(newPharmacyName);
        pharmacyPage.clickPrimaryLink("Go to plans");
        //Validate Pharmacy updated value
        planListPage.validatePageLoaded();
        pharmacySectionText= planListPage.fetchPharmacyPreferences("Pharmacy");
        pharmacySectionText.contains(firstSelectedPharmacyName);
        pharmacySectionText.contains(secondSelectedPharmacyName);
        System.out.println("Edit Preferences Testing is done");
    }

    @Test(groups = {"deployment"}, description = "Valiate cost and correct plan preferences present after only GetStarted")
    public void validateCostAndCorrectPlanPreferecesAfterOnlyGetStarted() throws InterruptedException, ConnectureCustomException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.clickPrimaryLink("Enter your preferences");

        //GetStarted tab
        GetStartedPage getStartedPage = new GetStartedPage();
        getStartedPage.validatePageLoaded();
        getStartedPage.enterZipCode(getData("drxMemberZip"));
        String zipCodeValue = getStartedPage.validateZipCode();
        //Validate Zipcode
        Assert.assertEquals(zipCodeValue, getData("drxMemberZip"));
        getStartedPage.clickSecondaryButton("Medical and prescription drug");
        // we need to see the solution for wait
        Thread.sleep(1000);
        getStartedPage.clickSecondaryButton("No, I am not eligible for special assistance");
        getStartedPage.clickPrimaryLink("Go to plans");

        //PlanList page
        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        String getStartedSectionText = planListPage.fetchGetStartedPreferences("Get Started");
        getStartedSectionText.contains("No, I am not eligible for special assistance");
        planListPage.scrollPlanListToBottom(15); // TODO: Refactor so scrolls automatically to bottom
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = $145.00
        String planCardTotalEstimatedCost = planListPage.getPlanCardTotalEstimatedCost(getData("flexcareCommonMAPDPlanToEnroll"));  //Total Est Annual Cost = $2,331
        String prefsText = planListPage.getPreferencesAreaForPlan(getData("flexcareCommonMAPDPlanToEnroll"));
        Assert.assertEquals(prefsText.contains("Prescriptions"),false);
        Assert.assertEquals(prefsText.contains("Pharmacy"),false);
        Assert.assertEquals(prefsText.contains("covered"),false);
        Assert.assertEquals(prefsText.contains("Health cost"),false);
        // selection of MAPD tab
        planListPage.selectPlanTab(getData("maPlanListTabText"));
        planListPage.scrollPlanListToBottom(15); // TODO: Refactor so scrolls automatically to bottom
        planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium("flexcare MA plan 1");  //planCardMonthlyPremium = $145.00
        planCardTotalEstimatedCost = planListPage.getPlanCardTotalEstimatedCost("flexcare MA plan 1");  //Total Est Annual Cost = $2,331
        prefsText = planListPage.getPreferencesAreaForPlan("flexcare MA plan 1");
        Assert.assertEquals(prefsText.contains("Prescriptions"),false);
        Assert.assertEquals(prefsText.contains("Pharmacy"),false);
        Assert.assertEquals(prefsText.contains("covered"),false);


    }

}