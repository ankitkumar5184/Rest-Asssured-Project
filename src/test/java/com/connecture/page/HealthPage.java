package com.connecture.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class HealthPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/GuidedHelp/Health";

    // Locators
    public ElementsCollection toggleOptions = $$(".toggle-option");

    public SelenideElement sliderKnob = $("div.slider-knob");

     Actions action = new Actions(this.sliderKnob.getWrappedDriver());


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void selectToggleOption(String ageRange){
        toggleOptions.find(text(ageRange)).click();
    }

    public void validateSelectedToggleOption(String ageRangeOption){
        toggleOptions.find(text(ageRangeOption)).isDisplayed();
    }

    public void moveSliderHealth(int px){
       action.dragAndDropBy(sliderKnob,px,0).build().perform();
       action.release();
    }

}
