package com.connecture.utils;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.connecture.utils.exception.ConnectureCustomException;

//  Provides static helper methods to support correct money rounding and converting money from string to BigDecimal and vise versa

public class MoneyUtil {

    /**
     * @param valueToConvert accept any numeric string with or w/o dollar sign,
     *  for example: '$6,780.7045', '6,780.7045'
     * @return BigDecimal value
     */
        public static BigDecimal convertToBigDecimal(String valueToConvert) throws ConnectureCustomException  {
        String stringToBuildNewBdValue = new String();
        Pattern pattern1 = Pattern.compile("\\$\\d+((\\,|\\.)\\d+)*");
        Pattern pattern2 = Pattern.compile("\\d+((\\,|\\.)\\d+)*");
        Matcher matcher1 = pattern1.matcher(valueToConvert);
        Matcher matcher2 = pattern2.matcher(valueToConvert);
        if (matcher1.find()){
            stringToBuildNewBdValue = matcher1.group().replaceAll("\\$", "").replaceAll("\\,", "");
        } else if(matcher2.find()){
            stringToBuildNewBdValue = matcher2.group().replaceAll("\\,", "");
        } else {
            throw new ConnectureCustomException("Input string does not mach pattern. Input was '" + valueToConvert.toString() +
                    "'. Possible format is any numeric string with or w/o $ sign, for example: '$6,780.7045', '6,780.7045'");
        }
        return new BigDecimal(stringToBuildNewBdValue);
    }
}
