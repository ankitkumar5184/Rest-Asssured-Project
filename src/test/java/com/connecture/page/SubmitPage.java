package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class SubmitPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/Submit";

    // Locators

    public SelenideElement XYZ = $("div#Signature_655_Canvas.DFLeft.DFSignatureCanvas.SignatureInitComplete");
    public SelenideElement jSignature = XYZ.find("canvas.jSignature");

    public ElementsCollection eSigCustomQuestion = $$("input[name='EsigCustomQuestionCheckbox10']");
    public ElementsCollection optionDescribeRelation = $$("label.DFSpaLabelTop");
    public ElementsCollection disabilityCheckBox = $$("input.DFStateChangeEvent");

    public SelenideElement disabilityEnterName = $("input[name='DisabilityName']");


    // check the Agent Info I agree checkbox


    public void selectOptionDescribeRelation(String optionText){
        optionDescribeRelation.find(text(optionText)).click();
    }
    Actions action = new Actions(this.jSignature.getWrappedDriver());


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void clickEsigCustomQuestionCheckBox(){
        eSigCustomQuestion.find(Condition.interactable).click();
    }

    public void clickDisabilityCheckBoxAndEnterName(String disabilityName){
        disabilityCheckBox.find(Condition.interactable).click();
        disabilityEnterName.setValue(disabilityName);

    }

    public void beneficiarySignature(int px){
        action.dragAndDropBy(jSignature,px,0).build().perform();
    }

}
