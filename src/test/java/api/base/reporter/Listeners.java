package api.base.reporter;

import api.tests.utils.TokenGeneration;
import org.testng.*;
import java.io.IOException;
import java.util.Arrays;
import static api.base.reporter.ExtentReport.test;


    /*Listener class is used to be monitored the corresponding interface that must be implemented.
    When a listener method is invoked,it is passed an event that contains information appropriate to the event. */

public class Listeners extends TokenGeneration implements ITestListener, ISuiteListener {
    @Override
    public void onStart(ISuite suite) {
        try {
            ExtentReport.initReports();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        try {
            ExtentReport.flushReports();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            ExtentReport.createTest(result.getMethod().getMethodName());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            ExtentLogger.pass(result.getMethod().getMethodName() + " is passed");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            ExtentLogger.fail("Class name - " + result.getTestClass().getRealClass().getName());
            ExtentLogger.fail(String.valueOf(result.getThrowable().getMessage()));
            ExtentLogger.fail("Stacktrace for error - " + Arrays.toString(result.getThrowable().getCause().getStackTrace()));
        } catch (Exception e) {
            test.fail(new RuntimeException());
            ExtentLogger.fail("Exception: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentLogger.skip(result.getMethod().getMethodName() + " is skipped");
        ExtentLogger.skip("TEST CASE SKIPPED IS " + result.getThrowable());
        test.skip(new RuntimeException());
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}