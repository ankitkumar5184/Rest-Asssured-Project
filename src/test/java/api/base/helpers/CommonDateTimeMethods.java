package api.base.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Class for calling Date/Time methods and return a Unique number
public class CommonDateTimeMethods {
    private int year;

    public int currentYear() {
        LocalDate now = LocalDate.now();
        year = now.getYear();
        return year;
    }

    public String getUniqueNumber()
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyhhmmss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }
}