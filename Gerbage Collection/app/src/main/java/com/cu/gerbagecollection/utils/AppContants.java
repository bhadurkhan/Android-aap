package com.cu.gerbagecollection.utils;

import android.service.autofill.RegexValidator;

import java.util.regex.Pattern;

public class AppContants {
    public static String baseUrl = "https://quranapp.pakcharity.org/Garbage/";

    public static boolean isPhoneNumberValid(String phoneNumber) {
        if (!Pattern.matches("[a-zA-Z]+", phoneNumber)) {
            return phoneNumber.length() > 6 && phoneNumber.length() <= 13;
        }
        return false;
    }

    public static boolean isValidMail(String email) {
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    public static boolean isValidCNIC(String cnic) {
        return cnic.matches("^[0-9]{5}-[0-9]{7}-[0-9]{1}$");
    }
}
