package com.rkapps.ieeemyeventapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by robin on 6/9/2016.
 *
 */
public class Authentication {

    static boolean injection(String id){
        boolean a = id.contains("'");
        //boolean b = id.contains("-");
        boolean c = id.contains(";");
        return a||c;
    }

    static boolean hyperInjection(String id){
        Pattern pattern = Pattern.compile("[~#*+%{}<>\\[\\]|\"\\^]");//removed @_ For Email char exclusion
        Matcher matcher = pattern.matcher(id);
        return matcher.find();
    }



}
