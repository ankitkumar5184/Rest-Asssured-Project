package api.base.helpers;

import api.base.helpers.env_annotations.*;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import java.lang.reflect.Method;

/* This class is used to skip any test based on different environments conditions.
*  Use the following below line on class level to use this class and env_annotations.
* @Listeners(value = ConditionalSkipTestAnalyzer.class) */

public class ConditionalSkipTestAnalyzer implements IInvokedMethodListener {
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult result) {
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        if (method == null) {
            return;
        }
        if (method.isAnnotationPresent(ExecuteOnQa.class)) {
            EnvironmentUtils.isQaEnv();
        }
        if (method.isAnnotationPresent(ExecuteOnStaging.class)) {
            EnvironmentUtils.isStagingEnv();
        }
        if (method.isAnnotationPresent(ExecuteOnTest.class)) {
            EnvironmentUtils.isTestEnv();
        }
        if (method.isAnnotationPresent(ExecuteOnProd.class)) {
            EnvironmentUtils.isProdEnv();
        }
        if (method.isAnnotationPresent(SkipOnQa.class)){
            EnvironmentUtils.skipOnQaEnv();
        }
        if (method.isAnnotationPresent(SkipOnStaging.class)){
            EnvironmentUtils.skipOnStagingEnv();
        }
        if (method.isAnnotationPresent(SkipOnTest.class)){
            EnvironmentUtils.skipOnTestEnv();
        }
        if (method.isAnnotationPresent(SkipOnProd.class)){
            EnvironmentUtils.skipOnProdEnv();
        }
        if (method.isAnnotationPresent(SkipBug.class)){
            EnvironmentUtils.skipBug();
        }
    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    }
}
