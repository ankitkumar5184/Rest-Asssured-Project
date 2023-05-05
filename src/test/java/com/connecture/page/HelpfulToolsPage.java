package com.connecture.page;

import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class HelpfulToolsPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/HelpfulTools";

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

}
