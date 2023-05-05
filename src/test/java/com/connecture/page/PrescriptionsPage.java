package com.connecture.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.connecture.utils.TestDataProvider.*;
import com.connecture.utils.Site;

public class PrescriptionsPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/GuidedHelp/Prescriptions";

    // Locators
    public SelenideElement widgetDrugDosage = $("vnext-drug-dosage");
    public SelenideElement widgetDrugSearch = $("vnext-widget-drug-search");
    public SelenideElement drugTextBox= widgetDrugSearch.$(".drug-search-input");

    public SelenideElement drugSearchResults = widgetDrugSearch.$(".dropdown-item");

    public SelenideElement toggleOptionSelected = $(".toggle-option.Spa_Style_SecondaryButtonSelected");
    public ElementsCollection toggleOptions = $$(".toggle-option");

    public SelenideElement overlaySmoker = $(".overlay");

    public ElementsCollection drugCardList = $$(".drug-card-panel");


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
        waitForNgxOverlay();
    }

    public void searchDrug(String drug){
    drugTextBox.shouldBe(visible);
    drugTextBox.setValue(drug);
    drugSearchResults.shouldHave(text(drug));
    drugSearchResults.click();
    }

    public void userSelectFirstSearchResult(String drugDosage){
        toggleOptions.find(text(drugDosage)).click();
    }

    public void validateSelectedOption(String selectionOption){
        toggleOptionSelected.shouldHave(text(selectionOption));
    }

    public void validateOptionalMinDosage(String minDosage){
        widgetDrugDosage.findAll(".toggle-option").shouldBe(CollectionCondition.sizeGreaterThan(Integer.parseInt(minDosage)));
    }
    public boolean validateOverlaySmoker(){
    return overlaySmoker.isDisplayed();
    }

    public void validateDrugTextBox(){
        drugTextBox.isDisplayed();
    }

    public void validateDrugCardList() {
        drugCardList.shouldBe(CollectionCondition.sizeGreaterThanOrEqual(1));
    }
    public void removeDrugFromList(String drugName){
            drugCardList.find(text(drugName)).find("i").click();
        }

    public void validateDrugNameInDrugCardList(String drugName){
        drugCardList.find(text(drugName)).isDisplayed();
    }


    public void standardDrugList(){
        addDrugBrand();
        addDrugGeneric();
        addDrugSwitchGenericSelectionYes();
        addDrugCommonSelection();
        addDrugPackageSelection();
    }


    // Case for selection of Drugs

    Boolean selectedButtonYes = validateSecondaryButtonSelectedDisplayed("yes");
    Boolean selectedButtonNo = validateSecondaryButtonSelectedDisplayed("no");

    //Case#1: Drug Brand selection have min dosage
    public void addDrugBrand() {
        searchDrug(getDrugData("drugBrand", "searchName"));
        userSelectFirstSearchResult(getDrugData("drugBrand", "searchName"));
        validateSelectedOption(getDrugData("drugBrand", "name"));
        validateOptionalMinDosage(getDrugData("drugBrand", "optionalMinDosages"));


        //Case#1: default to No
        if (!getDrugData("drugBrand", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    clickSecondaryButton("yes");
                }
            }

        } else if (getDrugData("drugBrand", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    clickSecondaryButton("no");
                }
            }

        }
        clickPrimaryButton("Add");
    }

    //Case#2: Case#2 Drug Generic selection
    public void addDrugGeneric() {

        searchDrug(getDrugData("drugGeneric", "searchName"));
        userSelectFirstSearchResult(getDrugData("drugGeneric", "dosageName"));
        validateSelectedOption(getDrugData("drugGeneric", "dosageName"));

        //Case#2: Case#2 Drug Generic selection default to No
        if (!getDrugData("drugGeneric", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonYes == false) {
                    clickSecondaryButton("yes");
                }
            }

        } else if (getDrugData("drugGeneric", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    clickSecondaryButton("no");
                }
            }

        }

        clickPrimaryButton("Add");
    }

    //Case#3: Drug Switch Generic selection set to Yes

    public void addDrugSwitchGenericSelectionYes(){

        searchDrug(getDrugData("drugSwitchGeneric", "searchName"));
        userSelectFirstSearchResult(getDrugData("drugSwitchGeneric", "dosageName"));
        validateSelectedOption(getDrugData("drugSwitchGeneric", "dosageName"));

        //If Drug have generic alternative then user needs to select Yes option
        if (!getDrugData("drugSwitchGeneric", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonYes == false) {
                    clickSecondaryButton("yes");
                }
            }
        } else if (getDrugData("drugSwitchGeneric", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    clickSecondaryButton("no");
                }

            }
        }

        clickPrimaryButton("Add");
    }

    //Case#4 Drug Common selection

    public void addDrugCommonSelection() {

        searchDrug(getDrugData("drugCommon", "searchName"));
        userSelectFirstSearchResult(getDrugData("drugCommon", "dosageName"));
        validateSelectedOption(getDrugData("drugCommon", "dosageName"));

        //Default to yes
        if (!getDrugData("drugCommon", "genericAlternative").equals("null")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonYes == false) {
                    clickSecondaryButton("yes");
                }
            }
        } else if (getDrugData("drugCommon", "type").equals("Brand")) {
            if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                if (selectedButtonNo == false) {
                    clickSecondaryButton("no");
                }
            }
        }
        clickPrimaryButton("Add");
    }
        //Case#5 Drug package selection
        public void addDrugPackageSelection(){

            searchDrug(getDrugData("drugPackage", "searchName"));
            userSelectFirstSearchResult(getDrugData("drugPackage", "dosageName"));
            validateSelectedOption(getDrugData("drugPackage", "dosageName"));

            //Default to yes
            if (!getDrugData("drugPackage", "genericAlternative").equals("null")) {
                if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                    if (selectedButtonYes == false) {
                        clickSecondaryButton("yes");
                    }
                }
            } else if (getDrugData("drugPackage", "type").equals("Brand")) {
                if ((selectedButtonYes == true) || (selectedButtonNo == true)) {
                    if (selectedButtonNo == false) {
                        clickSecondaryButton("no");
                    }
                }
            }
            clickPrimaryButton("Add");
        }

    }
