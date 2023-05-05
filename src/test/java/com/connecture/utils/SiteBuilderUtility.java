package com.connecture.utils;

import com.codeborne.selenide.Configuration;
import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.connecture.utils.TestDataProvider.getData;
import static com.connecture.utils.TestDataProvider.getSitePlatformUrl;

/*
      Builds specific sites for specific year. All children of site are invoked. In the future we
      can refactor to accept a specific year or hotfix build.

 */
public class SiteBuilderUtility extends BaseUISpec {

    @Test(groups = { "builder" }, description = "Site builder")
    public void consumerEnrollsAndValidatesConfirmationPage()  {

        String consumerPlatformBaseUrl = getSitePlatformUrl( "Consumer_base", "2023" );
        String brokerPlatformBaseUrl = getSitePlatformUrl( "Broker_base", "2023" );
        String ccPlatformBaseUrl = getSitePlatformUrl( "CallCenter_base", "2023" );
        String[] sitesToBuild = {consumerPlatformBaseUrl, brokerPlatformBaseUrl, ccPlatformBaseUrl};
        Boolean isFirstRun = true;

        for (String currentSite: sitesToBuild) {

            if(isFirstRun){
                System.out.println("Baseurl: " + currentSite);
                Configuration.baseUrl = currentSite;
                PlatformLoginPage platformLoginPage = new PlatformLoginPage();
                platformLoginPage.openWithBaseUrl();
                platformLoginPage.validatePageLoaded();
                platformLoginPage.login();
            } else {
                System.out.println("Baseurl: " + currentSite);
                Configuration.baseUrl = currentSite;
                PlatformSitePage platformSitePage = new PlatformSitePage();
                platformSitePage.openWithBaseUrl();

            }

            PlatformSitePage platformSitePage = new PlatformSitePage();
            platformSitePage.validatePageLoaded();
            platformSitePage.deployAll();
            isFirstRun = false;
            // platformSitePage.waitForDeploy(); - Validate and print build numbers next

        }

    }

}
