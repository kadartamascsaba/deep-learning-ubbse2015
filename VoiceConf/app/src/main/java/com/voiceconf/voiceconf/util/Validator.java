package com.voiceconf.voiceconf.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tamas-Csaba Kadar on 1/2/2016.
 */
public class Validator {

    public static boolean isValidIpAddress(String text) {
        Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = p.matcher(text);
        return m.find();
    }

    public static boolean isValidPort(String text) {
        int port = 0;
        try {
            port = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }
        if (port < 0 || port > 65535) {
            return  false;
        }
        return true;
    }

    public static boolean isValidEmail(String text) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(ePattern);
        Matcher m = p.matcher(text);
        return m.matches();
    }


}
