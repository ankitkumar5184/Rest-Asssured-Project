package api.base.reporter;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

/*
This class will log messages and create the label in the specific defined color for all the specific events in the Extent Report
 */
public final class ExtentLogger {
    private ExtentLogger() {
    }

    public static void pass(String message) {
        ExtentReport.test.pass(message);
    }

    public static void fail(String message) {
        ExtentReport.test.fail(MarkupHelper.createLabel(message, ExtentColor.RED));
    }

    public static void skip(String message) {
        ExtentReport.test.skip(MarkupHelper.createLabel(message, ExtentColor.ORANGE));
    }
}