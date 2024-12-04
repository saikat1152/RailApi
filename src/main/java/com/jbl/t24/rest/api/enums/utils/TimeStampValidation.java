package com.jbl.t24.rest.api.enums.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeStampValidation {
    
    public static boolean isWithinTimestampRange(Timestamp issueTime, Timestamp currentTime){

        LocalDate issueDate, currentDate;

        issueDate =  issueTime.toLocalDateTime().toLocalDate();
        currentDate = currentTime.toLocalDateTime().toLocalDate();
    
        if (issueDate.isEqual(currentDate)) {
            return false;
        }
            return true;
    }

}
