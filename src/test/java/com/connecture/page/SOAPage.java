package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class SOAPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/SOA";

    // Locators
    public SelenideElement vNextLiveEditContainer = $("vnext-liveedit-container");
    public SelenideElement fileUpload = $("vnext-soa input[type=file]");
    public SelenideElement uploadItem = $(".d-lg-flex");

    public ElementsCollection soaActivityRows = $$("vnext-soa-activity-item");

    public int soaActivityRow = soaActivityRows.indexOf(1);


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForNgxOverlay();
    }

    public void uploadSOADocument(File fileToUpload){
        Assert.assertEquals(fileToUpload.exists(),true);
        String uploadPath = fileToUpload.getAbsolutePath();
        String  uploadName = fileToUpload.getName();
       fileUpload.sendKeys(uploadPath);
    }

    public void validateSOAActivityRow(int expectedSOAActivitySize){
        int soaActivityRowSize = soaActivityRows.size();
        Assert.assertEquals(soaActivityRowSize,expectedSOAActivitySize);
    }

    public void waitForRowToDisplay(){
        uploadItem.shouldBe(visible,Duration.ofSeconds(10));
    }

    public void validateSOAROwActivityText(String expectedText){
        List<String> soaActivityRowsText =soaActivityRows.texts();
        soaActivityRowsText.contains(expectedText);
    }



}
