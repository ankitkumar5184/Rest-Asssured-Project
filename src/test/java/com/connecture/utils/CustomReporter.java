package com.connecture.utils;

import org.testng.ITestResult;
import org.testng.reporters.JUnitReportReporter;

public class CustomReporter extends JUnitReportReporter {

    @Override
    protected String getTestName(ITestResult tr) {
        return tr.getMethod().getDescription();
    }

}
