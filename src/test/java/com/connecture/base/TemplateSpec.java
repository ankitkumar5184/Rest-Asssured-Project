package com.connecture.base;

import com.connecture.base.BaseUISpec;
import com.connecture.page.HomePage;
import com.connecture.utils.Site;
import org.testng.annotations.Test;
import com.connecture.base.*;
import com.connecture.page.*;
import com.connecture.utils.Site;
import org.testng.annotations.Test;
import static com.codeborne.selenide.Condition.*;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="Consumer_default", year="2022")
public class TemplateSpec extends BaseUISpec {


    @Test( groups = { "deployment" }, description = "User friendly description")
    public void testCaseToRun()  {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();

    }

}
