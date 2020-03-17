package com.gp.webdriverapi.common.utils;

import java.text.DateFormat;
import java.util.Date;

public class DateUtils {


    public static String getDate(Date date) {
        DateFormat format = DateFormat.getDateInstance();
        return format.format(date);
    }


}
