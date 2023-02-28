package com.cloudofgoods.xenia.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String SUCCESS = "Success";
    public static String STATUS_2000 = "2000";
    public static String STATUS_5000 = "5000";
    public static String STATUS_OK = "OK";
    public static String FAIL = "Fail";
    public static String ORGANIZATION_NOT_FOUND= "Organization Not Found";
    public static String ORGANIZATION_NOT_EMPTY= "Organization cannot be empty";
    public static DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    public static Date today = new Date();

}
