package com.connecture.spec2022;

import com.connecture.base.*;
import com.connecture.page.*;
import com.connecture.base.BaseUISpec;
import com.connecture.page.HomePage;
import com.connecture.utils.Site;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.SQLOutput;

import static com.codeborne.selenide.Condition.text;
import static com.connecture.utils.TestDataProvider.*;

@Site(tag="Professional_perfMC", year="2022")
public class Professional_AgentASPSpec extends BaseUISpec {


    @Test( groups = { "deployment" }, description = "Show permissioned plans for certain medicare states for medicare agent")
    public void ShowPermPlansToMedicareStatsForMedicareAgent() throws InterruptedException {
    LoginPage loginPage = new LoginPage();
    loginPage.navigateToSiteBaseUrl();
    loginPage.validatePageLoaded();
    boolean userNameDisplayedFlag = loginPage.validateUserNameDisplayed("username");
    Assert.assertEquals(true,userNameDisplayedFlag);
    loginPage.brokerLogin("username",getStateMedicareBrokerUsername(),"password",getStateMedicareBrokerPassword());

    DashboardPage dashboardPage = new DashboardPage();
    dashboardPage.validatePageLoaded();
    dashboardPage.dashBoardPageHeading.isDisplayed();
    dashboardPage.selectPlans("Plans");

    PlanListPage planListPage = new PlanListPage();
    planListPage.enterZipCode(getData("drxMemberZip"));
    planListPage.validateCountyName(getData("drxMemberCountyName"));
    planListPage.clickModalPrimaryButton("Shop for plans");
    planListPage.waitForOverlay();
    planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
    planListPage.planCountHeader.shouldHave(text("plans available in 90058"));
    Thread.sleep(10000);
    Integer planHeaderCount = planListPage.planCountHeaderCount();
    Assert.assertNotEquals(planHeaderCount,0);
    planListPage.activeTab.shouldHave(text(getData("mapdPlanListTabText"))); //Medicare Advantage Prescription Drug Plans
    Integer activeTabPlanCount = planListPage.getActiveTabPlanCount(); // count = 6
    Assert.assertNotEquals(activeTabPlanCount,0);
    planListPage.selectPlanTab(getData("maPlanListTabText"));
    planListPage.activeTab.shouldHave(text(getData("maPlanListTabText"))); //Medicare Advantage Plans
    activeTabPlanCount = planListPage.getActiveTabPlanCount(); // count = 5
    Assert.assertNotEquals(activeTabPlanCount,0);
    planListPage.selectPlanTab(getData("pdpPlanListTabText"));
    planListPage.activeTab.shouldHave(text(getData("pdpPlanListTabText"))); //Prescription Drug Plans
    activeTabPlanCount = planListPage.getActiveTabPlanCount(); // count = 2
    Assert.assertNotEquals(activeTabPlanCount,0);
    }

    @Test( groups = { "deployment" }, description = "Show no medicare plans in certain states for medicare agent")
    public void ShowNoMedicarePlansToCertainStatsForMedicareAgent() {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToSiteBaseUrl();
        loginPage.validatePageLoaded();
        boolean userNameDisplayedFlag = loginPage.validateUserNameDisplayed("username");
        Assert.assertEquals(true, userNameDisplayedFlag);
        loginPage.brokerLogin("username",getStateMedicareBrokerUsername(),"password",getStateMedicareBrokerPassword());

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.validatePageLoaded();
        dashboardPage.dashBoardPageHeading.isDisplayed();
        dashboardPage.selectPlans("Plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.enterZipCode(getData("medicareUnpermissionedZip"));
        planListPage.validateCountyName(getData("medicareUnpermissionedCountyName"));
        planListPage.clickModalPrimaryButton("Shop for plans");
        planListPage.waitForOverlay();
        planListPage.largeZipLink.shouldHave(text(getData("medicareUnpermissionedZip")));
        planListPage.planCountHeader.shouldHave(text("0 plans available in 14201"));
        Integer planHeaderCount = planListPage.planCountHeaderCount();
        Assert.assertEquals(planHeaderCount,0);
    }

    @Test( groups = { "deployment" }, description = "Show no medigap plans for medicare agent")
    public void ShowNoMedicarePlansToMedicareAgent() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToSiteBaseUrl();
        loginPage.validatePageLoaded();
        boolean userNameDisplayedFlag = loginPage.validateUserNameDisplayed("username");
        Assert.assertEquals(true, userNameDisplayedFlag);
        loginPage.brokerLogin("username",getStateMedicareBrokerUsername(),"password",getStateMedicareBrokerPassword());

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.validatePageLoaded();
        dashboardPage.dashBoardPageHeading.isDisplayed();
        dashboardPage.selectPlans("Plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.enterZipCode(getData("drxMemberZip"));
        planListPage.validateCountyName(getData("drxMemberCountyName"));
        planListPage.clickModalPrimaryButton("Shop for plans");
        planListPage.waitForOverlay();
        planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        planListPage.planCountHeader.shouldHave(text("plans available in 90058"));
        Thread.sleep(10000);
        Integer planHeaderCount = planListPage.planCountHeaderCount();
        Assert.assertNotEquals(planHeaderCount, 0);

        planListPage.selectPlanTab(getData("medsupPlanListTabText"));
        planListPage.activeTab.shouldHave(text(getData("medsupPlanListTabText"))); //Medicare Supplement Plans
        Integer activeTabPlanCount = planListPage.getActiveTabPlanCount(); // count = 0
        Assert.assertEquals(activeTabPlanCount,0);
        boolean dateOfBirth = planListPage.validateDateOfBirthFieldDisplayed("placeholder","mm/dd/yyyy");
        Assert.assertEquals(dateOfBirth,false);
    }

    @Test( groups = { "deployment" }, description = "Show medigap plans for medigap agent")
    public void ShowMedigapPlansForMedigapAgent() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToSiteBaseUrl();
        loginPage.validatePageLoaded();
        boolean userNameDisplayedFlag = loginPage.validateUserNameDisplayed("username");
        Assert.assertEquals(true, userNameDisplayedFlag);
        loginPage.brokerLogin("username",getMedigapBrokerUsername(),"password",getMedigapBrokerPassword());

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.validatePageLoaded();
        dashboardPage.dashBoardPageHeading.isDisplayed();
        dashboardPage.selectPlans("Plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.enterZipCode(getData("drxMemberZip"));
        planListPage.validateCountyName(getData("drxMemberCountyName"));
        planListPage.clickModalPrimaryButton("Shop for plans");
        planListPage.waitForOverlay();
        planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        planListPage.planCountHeader.shouldHave(text("plans available in 90058"));
        Thread.sleep(10000);
        Integer planHeaderCount = planListPage.planCountHeaderCount();
        Assert.assertNotEquals(planHeaderCount, 0);

        planListPage.selectPlanTab(getData("medsupPlanListTabText")); //Medicare Supplement Plans
        planListPage.activeTab.shouldHave(text(getData("medsupPlanListTabText"))); //Medicare Supplement Plans
        Integer activeTabPlanCount = planListPage.getActiveTabPlanCount(); // count = 16
        Assert.assertNotEquals(activeTabPlanCount, 0);
        boolean dateOfBirth = planListPage.validateDateOfBirthFieldDisplayed("placeholder","mm/dd/yyyy");
        Assert.assertEquals(dateOfBirth,true);

    }

    @Test( groups = { "deployment" }, description = "Show no medicare plans for medigap agent")
    public void ShowNoMedicarePlansForMedigapAgent() throws InterruptedException {
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToSiteBaseUrl();
        loginPage.validatePageLoaded();
        boolean userNameDisplayedFlag = loginPage.validateUserNameDisplayed("username");
        Assert.assertEquals(true, userNameDisplayedFlag);
        loginPage.brokerLogin("username",getMedigapBrokerUsername(),"password",getMedigapBrokerPassword());

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.validatePageLoaded();
        dashboardPage.dashBoardPageHeading.isDisplayed();
        dashboardPage.selectPlans("Plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.enterZipCode(getData("drxMemberZip"));
        planListPage.validateCountyName(getData("drxMemberCountyName"));
        planListPage.clickModalPrimaryButton("Shop for plans");
        planListPage.waitForOverlay();
        planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        planListPage.planCountHeader.shouldHave(text("plans available in 90058"));
        Thread.sleep(10000);
        Integer planHeaderCount = planListPage.planCountHeaderCount();
        Assert.assertNotEquals(planHeaderCount, 0);


        planListPage.activeTab.shouldHave(text(getData("mapdPlanListTabText"))); //Medicare Advantage Prescription Drug Plans
        Integer activeTabPlanCount = planListPage.getActiveTabPlanCount(); // count = 0
        Assert.assertEquals(activeTabPlanCount, 0);
        planListPage.selectPlanTab(getData("maPlanListTabText"));
        planListPage.activeTab.shouldHave(text(getData("maPlanListTabText"))); //Medicare Advantage Plans
        activeTabPlanCount = planListPage.getActiveTabPlanCount(); // count = 0
        Assert.assertEquals(activeTabPlanCount,0);
        planListPage.selectPlanTab(getData("pdpPlanListTabText"));
        planListPage.activeTab.shouldHave(text(getData("pdpPlanListTabText"))); //Prescription Drug Plans
        activeTabPlanCount = planListPage.getActiveTabPlanCount(); // count = 0
        Assert.assertEquals(activeTabPlanCount,0);
    }

}
