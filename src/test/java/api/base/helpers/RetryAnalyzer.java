package api.base.helpers;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

// This class is used to retry the failed tests. e.g. usage below
// To use it, add the following in @Test annotation - @Test(retryAnalyzer = RetryAnalyzer.class)
public class RetryAnalyzer implements IRetryAnalyzer {

    int retryAttemptsCounter = 0;
    int maxRetryLimit = 2;

    @Override
    public boolean retry(ITestResult result) {
        if(!result.isSuccess()){
            if(retryAttemptsCounter <= maxRetryLimit){
                retryAttemptsCounter++;
                return true;
            }
        }
        return false;
    }
}
