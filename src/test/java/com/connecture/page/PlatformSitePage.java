package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

import java.time.Duration;

public class PlatformSitePage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/PlatformManager2/Site";

    // Locators
    public SelenideElement deployTab = $$("a").find(exactText("Deploy"));
    public SelenideElement deployToQAButton = $$("button").find(exactText("Deploy site to QA"));
    public SelenideElement deployToQAHotfixButton = $$("button").find(exactText("Deploy site to QA Hotfix"));
    public SelenideElement allCheckBox = $("#SelectAllSitesStg");
    public SelenideElement deployTable = $("table.content");

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
    }

    public void deployAll() {
        deployTab.click();
        deployToQAButton.shouldBe(Condition.visible);
        allCheckBox.shouldBe(Condition.visible);
        if ( allCheckBox.isSelected() == false ) {
            allCheckBox.click();
        }
        allCheckBox.shouldBe(Condition.checked);
        if( deployTable.text().contains("Building")){
            return; // Already started, no-op, needed for script debugging
        }
        if( System.getProperty("isHotfix").equals("true")){
            deployToQAHotfixButton.click();
        } else {
            deployToQAButton.click();
        }
        try {
            deployTable.shouldHave(text("Requested"), Duration.ofSeconds(120));
            return; // Done if made it all the way to building
        } catch (Throwable t){
            // Okay to have a non-building status
        }
        try {
            deployTable.shouldHave(text("Starting"));
            return; // Done if made it all the way to Starting
        } catch (Throwable t){
            // Okay to have a non-building status
        }
        deployTable.shouldHave(text("Building"));
    }
}
