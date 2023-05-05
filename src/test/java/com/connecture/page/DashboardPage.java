package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.CollectionElement;
import com.connecture.base.BaseUIPage;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class DashboardPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/Dashboard";

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        overlayLoader.shouldNotBe(visible, Duration.ofSeconds(30));
        waitForNgxOverlay();
    }

    // Locators
    public SelenideElement dashBoardPageHeading = $(".Spa_Style_H1");

}
