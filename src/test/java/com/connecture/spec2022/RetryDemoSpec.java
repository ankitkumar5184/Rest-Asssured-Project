package com.connecture.spec2022;

import com.connecture.base.BaseUISpec;
import com.connecture.utils.Site;
import com.connecture.page.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

/*
   Authur:  jkruger

 */
@Site(tag="Consumer_default", year="2022")
public class RetryDemoSpec extends BaseUISpec {

    private static int testCount = 0;

    @Test(groups = { "deployment" }, description = "Shows how retry works")
    public void retryDemoSpec()  {

        System.out.println("Single test run ...!!");
        HomePage homePage = new HomePage();
        homePage.navigateToSiteBaseUrl();
        testCount++;
        Assert.assertEquals( 3, testCount);

    }

}
