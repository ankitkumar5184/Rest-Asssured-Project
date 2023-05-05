package com.connecture.spec2022;

import com.connecture.base.BaseUISpec;
import com.connecture.page.*;
import com.connecture.utils.Site;
import com.connecture.utils.exception.ConnectureCustomException;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static com.codeborne.selenide.Condition.text;
import static com.connecture.utils.TestDataProvider.getData;

@Site(tag="Consumer_default", year="2022")
public class Consumer_CartSpec extends BaseUISpec {

    @Test(groups = {"deployment"}, description = "Validate premiums by adding multiple riders and minicart")
    public void validatePremiumByAddingRiders() throws ConnectureCustomException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        planListPage.scrollPlanListToBottom(15); // TODO: Refactor so scrolls automatically to bottom
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 145.00
        planListPage.clickAddToCart(getData("flexcareCommonMAPDPlanToEnroll"));

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.planCard.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        String totalMonthlyPremium = cartPage.getTotalMonthlyPremium();  //totalMonthlyPremium= 145.00
        Assert.assertEquals(planCardMonthlyPremium, totalMonthlyPremium);
        String riderMonthlyPremiumDental = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameDental")); // 15.00
        String riderMonthlyPremiumHearing = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameHearing")); // 5.00
        String riderMonthlyPremiumVision = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameVision"));   //10.00
        cartPage.clickAddButton(getData("riderMonthlyPlanNameDental"));  // 15.00
        cartPage.clickAddButton(getData("riderMonthlyPlanNameHearing")); // 5.00
        cartPage.clickAddButton(getData("riderMonthlyPlanNameVision")); // 10.00
        totalMonthlyPremium = cartPage.getTotalMonthlyPremium(); // totalMonthlyPremium = 175
        BigDecimal totalMonthlyPremiumDecimal = cartPage.convertStringRateToBigDecimal(totalMonthlyPremium);  //175.00
        BigDecimal totalPlanCardMonthlyPremiumDecimal = cartPage.convertStringRateToBigDecimal(planCardMonthlyPremium);
        BigDecimal totalRiderMonthlyPremiumDentalDecimal = cartPage.convertStringRateToBigDecimal(riderMonthlyPremiumDental);
        BigDecimal totalRiderMonthlyPremiumHearingDecimal = cartPage.convertStringRateToBigDecimal(riderMonthlyPremiumHearing);
        BigDecimal totalRiderMonthlyPremiumVisionDecimal = cartPage.convertStringRateToBigDecimal(riderMonthlyPremiumVision);
        List<BigDecimal> totals = new ArrayList<>();
        totals.add(totalPlanCardMonthlyPremiumDecimal); // 145.00
        totals.add(totalRiderMonthlyPremiumDentalDecimal); //15.00
        totals.add(totalRiderMonthlyPremiumHearingDecimal); // 05.00
        totals.add(totalRiderMonthlyPremiumVisionDecimal); //10.00
        BigDecimal sumPlanCardMontlyPremiumDecimal = BigDecimal.ZERO;
        for (BigDecimal total : totals) {
            sumPlanCardMontlyPremiumDecimal = sumPlanCardMontlyPremiumDecimal.add(total);
        }
        Assert.assertEquals(totalMonthlyPremiumDecimal, sumPlanCardMontlyPremiumDecimal);
        cartPage.clickPrimaryButton("Continue to apply");

        EnrollmentFormPage contactInfoPage = new EnrollmentFormPage("ContactInfo");
        contactInfoPage.validatePageLoaded();

        //Verification of Plan and Rider names in mini cart
        contactInfoPage.miniCartArea.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        contactInfoPage.miniCartArea.shouldHave(text(getData("riderMonthlyPlanNameDental")));
        contactInfoPage.miniCartArea.shouldHave(text(getData("riderMonthlyPlanNameHearing")));
        contactInfoPage.miniCartArea.shouldHave(text(getData("riderMonthlyPlanNameVision")));

        //Verification of Plan and Rider amounts in mini cart
        contactInfoPage.validateMonthlyPremium(planCardMonthlyPremium); // $145.00
        contactInfoPage.validateMonthlyPremium(riderMonthlyPremiumDental); //$15.00
        contactInfoPage.validateMonthlyPremium(riderMonthlyPremiumHearing); //$5.00
        contactInfoPage.validateMonthlyPremium(riderMonthlyPremiumVision); //$5.00

        // Verification of the Sum of monthly and rider monthly premium i.e. $ 175.00
        contactInfoPage.validateMonthlyPremium(totalMonthlyPremium);

        // Navigate to Cart Page
        contactInfoPage.clickPrimaryHeadingLink("Cart");

        cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.planCard.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        cartPage.planCard.shouldHave(text(getData("riderMonthlyPlanNameDental")));
        cartPage.planCard.shouldHave(text(getData("riderMonthlyPlanNameHearing")));
        cartPage.planCard.shouldHave(text(getData("riderMonthlyPlanNameVision")));
    }

    @Test(groups = {"deployment"}, description = "Validate rider combinations, add riders, replace and remove, also validate with show/hide")
    public void validateRiderCombinations() throws ConnectureCustomException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        planListPage.scrollPlanListToBottom(15); // TODO: Refactor so scrolls automatically to bottom
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 145.00
        planListPage.clickAddToCart(getData("flexcareCommonMAPDPlanToEnroll"));

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.planCard.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        cartPage.planCard.shouldHave((text(getData("riderMonthlyPlanNameDental"))));
        String riderMonthlyPremiumDental = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameDental")); // 15.00
        String riderMonthlyPremiumHearing = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameHearing")); // 5.00
        String riderMonthlyPremiumVision = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameVision"));   //10.00
        String totalMonthlyPremium = cartPage.getTotalMonthlyPremium();  //totalMonthlyPremium= 145.00
        Assert.assertEquals(planCardMonthlyPremium, totalMonthlyPremium);
        cartPage.clickAddButton(getData("riderMonthlyPlanNameDental"));  // 15.00
        totalMonthlyPremium = cartPage.getTotalMonthlyPremium(); // totalMonthlyPremium = 160.00
        BigDecimal totalMonthlyPremiumDecimal = cartPage.convertStringRateToBigDecimal(totalMonthlyPremium);  //160.000
        BigDecimal totalPlanCardMonthlyPremiumDecimal = cartPage.convertStringRateToBigDecimal(planCardMonthlyPremium); //145.00
        BigDecimal totalRiderMonthlyPremiumDentalDecimal = cartPage.convertStringRateToBigDecimal(riderMonthlyPremiumDental); //15.00
        List<BigDecimal> totals = new ArrayList<>();
        totals.add(totalPlanCardMonthlyPremiumDecimal); // 145.00
        totals.add(totalRiderMonthlyPremiumDentalDecimal); //15.00
        BigDecimal sumPlanCardMontlyPremiumDecimal = BigDecimal.ZERO;
        for (BigDecimal total : totals) {
            sumPlanCardMontlyPremiumDecimal = sumPlanCardMontlyPremiumDecimal.add(total);
        }
        Assert.assertEquals(totalMonthlyPremiumDecimal, sumPlanCardMontlyPremiumDecimal);
        cartPage.clickSecondaryButton("Replace");
        totalMonthlyPremium = cartPage.getTotalMonthlyPremium(); // totalMonthlyPremium = 160.00
        cartPage.clickPrimaryButton("Continue to apply");

        EnrollmentFormPage contactInfoPage = new EnrollmentFormPage("ContactInfo");
        contactInfoPage.validatePageLoaded();

        //Verification of Plan and Rider names in mini cart
        contactInfoPage.miniCartArea.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        contactInfoPage.miniCartArea.shouldHave(text(getData("riderMonthlyPlanNameCombined")));

        // Verification of the Sum of monthly and rider combined premium i.e. $ 170.00
        contactInfoPage.validateMonthlyPremium(totalMonthlyPremium);

    }

    @Test(groups = {"deployment"}, description = "Validate view rider details, change plan, remove plan")
    public void validateRiderDetailsChangeRemovePlan() throws ConnectureCustomException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.validateCountyName(getData("drxMemberCountyName"));
        homePage.clickPrimaryButton("View plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        planListPage.scrollPlanListToBottom(15); // TODO: Refactor so scrolls automatically to bottom
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 145.00
        planListPage.clickAddToCart(getData("flexcareCommonMAPDPlanToEnroll"));

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.planCard.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        cartPage.clickRiderDetails(getData("riderMonthlyPlanNameDental"));
        cartPage.validateRiderDetails((getData("riderMonthlyPlanNameDental")),"Deductible");
        cartPage.validateRiderDetails((getData("riderMonthlyPlanNameDental")),"Major Services");
        cartPage.validateRiderDetails((getData("riderMonthlyPlanNameDental")),"Annual Maximum Benefit");
        cartPage.clickAddButton(getData("riderMonthlyPlanNameDental"));  // 15.00
        cartPage.validateRiderSelected();

        String riderMonthlyPremiumDental = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameDental")); // 15.00
        String riderMonthlyPremiumHearing = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameHearing")); // 5.00
        String riderMonthlyPremiumVision = cartPage.getRiderMonthlyPremium(getData("riderMonthlyPlanNameVision"));   //10.00
        String totalMonthlyPremium = cartPage.getTotalMonthlyPremium();  //totalMonthlyPremium= 160.00
        BigDecimal totalMonthlyPremiumDecimal = cartPage.convertStringRateToBigDecimal(totalMonthlyPremium);  //160.00
        BigDecimal totalPlanCardMonthlyPremiumDecimal = cartPage.convertStringRateToBigDecimal(planCardMonthlyPremium); //145.00
        BigDecimal totalRiderMonthlyPremiumDentalDecimal = cartPage.convertStringRateToBigDecimal(riderMonthlyPremiumDental); //15.00
        List<BigDecimal> totals = new ArrayList<>();
        totals.add(totalPlanCardMonthlyPremiumDecimal); // 145.00
        totals.add(totalRiderMonthlyPremiumDentalDecimal); //15.00
        BigDecimal sumPlanCardMontlyPremiumDecimal = BigDecimal.ZERO;
        for (BigDecimal total : totals) {
            sumPlanCardMontlyPremiumDecimal = sumPlanCardMontlyPremiumDecimal.add(total);
        }
        Assert.assertEquals(totalMonthlyPremiumDecimal, sumPlanCardMontlyPremiumDecimal);

        // Validation again after removal of Rider
        cartPage.clickRemoveRider();
        totalMonthlyPremium = cartPage.getTotalMonthlyPremium();  //totalMonthlyPremium= 145.00
        totalMonthlyPremiumDecimal = cartPage.convertStringRateToBigDecimal(totalMonthlyPremium);  //145.00
        totalPlanCardMonthlyPremiumDecimal = cartPage.convertStringRateToBigDecimal(planCardMonthlyPremium); //145.00
        List<BigDecimal> totalss = new ArrayList<>();
        totalss.add(totalPlanCardMonthlyPremiumDecimal); // 145.00
         sumPlanCardMontlyPremiumDecimal = BigDecimal.ZERO;
        for (BigDecimal total : totalss) {
            sumPlanCardMontlyPremiumDecimal = sumPlanCardMontlyPremiumDecimal.add(total);
        }
        Assert.assertEquals(totalMonthlyPremiumDecimal, sumPlanCardMontlyPremiumDecimal);
        cartPage.clickPrimaryLink("View details");

        PlanDetailsPage planDetailsPage = new PlanDetailsPage();
        planDetailsPage.validatePageLoaded();
        planDetailsPage.validateMonthlyPlanNameHeader(getData("flexcareCommonMAPDPlanToEnroll"));
        planDetailsPage.validateMonthlyPlanNameCard(getData("flexcareCommonMAPDPlanToEnroll"));
        planDetailsPage.clickPrimaryButton("Add to cart");

        cartPage.validatePageLoaded();
        cartPage.validatePlanNameSelected(getData("flexcareCommonMAPDPlanToEnroll"));
        cartPage.clickPrimaryLink("Change plan");

        planListPage.validatePageLoaded();
        planListPage.clickAddToCart(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        planListPage.clickModalPrimaryButton("Add");

        cartPage.validatePageLoaded();
        cartPage.validatePlanNameSelected(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        cartPage.clickPrimaryLink("Remove plan");
        planListPage.clickModalPrimaryButton("Remove");

        planListPage.validatePageLoaded();
        planListPage.clickPrimaryHeadingLink("Cart");

        cartPage.validatePageLoaded();
        cartPage.validateCartEmpty();
    }

    @Test(groups = {"deployment"}, description = "Validate empty cart, zip change, add, replace plan")
    public void validateEmptyCartZipChangeAddReplacePlan() throws ConnectureCustomException {

        HomePage homePage = new HomePage();
        homePage.openWithBaseUrlAndAuthenticate();
        homePage.validatePageLoaded();
        homePage.enterZipCode(getData("drxMemberZip"));
        homePage.clickPrimaryHeadingLink("Cart");

        CartPage cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.validateCartEmpty();
        cartPage.clickPrimaryButton("Shop for plans");

        PlanListPage planListPage = new PlanListPage();
        planListPage.validatePageLoaded();
        planListPage.largeZipLink.shouldHave(text(getData("drxMemberZip")));
        planListPage.scrollPlanListToBottom(15); // TODO: Refactor so scrolls automatically to bottom
        String planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 145.00
        planListPage.clickAddToCart(getData("flexcareCommonMAPDPlanToEnroll"));

        cartPage = new CartPage();
        cartPage.validatePageLoaded();
        cartPage.planCard.shouldHave(text(getData("flexcareCommonMAPDPlanToEnroll")));
        cartPage.clickPrimaryLink("Previous");

        planListPage.validatePageLoaded();
        planCardMonthlyPremium = planListPage.getPlanCardMonthlyPremium(getData("flexcareSecondCommonMAPDPlanToEnroll"));  //planCardMonthlyPremium = 145.00
        planListPage.clickAddToCart(getData("flexcareSecondCommonMAPDPlanToEnroll"));
        planListPage.validateModalTitle("Replace Plan");
        planListPage.validateModalBody("replace the plan you currently ");
        planListPage.clickModalPrimaryButton("Add");

        cartPage.validatePageLoaded();
        cartPage.validatePlanNameSelected(getData("flexcareSecondCommonMAPDPlanToEnroll"));
    }
}