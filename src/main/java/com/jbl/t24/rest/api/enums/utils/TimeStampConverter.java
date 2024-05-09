package com.jbl.t24.rest.api.enums.utils;

import java.sql.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStampConverter {

    public static Timestamp getCbsHittingTimeStamp(String cbsHittingTimeStr) throws ParseException{
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyMMddHHmm");
        Date date = inputFormat.parse(cbsHittingTimeStr);
        Timestamp timestamp = new Timestamp(date.getTime());  
        return timestamp;
    }
    
}
