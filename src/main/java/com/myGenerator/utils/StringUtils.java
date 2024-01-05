package com.myGenerator.utils;

import java.util.Locale;

public class StringUtils {
    public static String uppercaseFirstLetter(String str){
        if(org.apache.commons.lang3.StringUtils.isEmpty(str)){
            return str;
        }
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }
    public static String lowercaseFirstLetter(String str){
        if(org.apache.commons.lang3.StringUtils.isEmpty(str)){
            return str;
        }
        return str.substring(0,1).toLowerCase()+str.substring(1);
    }

}
