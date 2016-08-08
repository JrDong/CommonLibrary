package com.djr.commonlibrary.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 */
public class InputUtils {

    private static final String PATTERN_USER_NAME = "^\\w+$";
    private static final String PATTERN_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    private static final String PATTERN_MOBILE = "^1[0-9]{10}$";
    private static final String PATTERN_NAME="[\u4e00-\u9fa5]{2,10}$";
    private static final String PATTERN_CHINESE_STRING="[\u4e00-\u9fa5]*$";
    public static boolean isValidUserName(String username) {
        if (TextUtils.isEmpty(username)) return false;//为空
        if (!username.trim().equals(username)) return false;//前后有空格
        if (username.length() < 6 || username.length() > 15) return false;//长度不符合
        Pattern p = Pattern.compile(PATTERN_USER_NAME);
        return p.matcher(username).matches();
    }

    public static boolean isValidEmailAddress(String email) {
        if (TextUtils.isEmpty(email)) return false;
        Pattern p = Pattern.compile(PATTERN_EMAIL);//复杂匹配
        return p.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) return false;
        Pattern p = Pattern.compile(PATTERN_MOBILE);
        return p.matcher(phone).matches();
    }

    public static boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) return false;
        return !(password.length() < 6 || password.length() > 30);
    }
    public static boolean isValidName(String name) {
        if (TextUtils.isEmpty(name)) return false;
        Pattern p = Pattern.compile(PATTERN_NAME);
        return p.matcher(name).matches();
    }
    public static boolean onlyChineseString(String s) {
        Pattern p = Pattern.compile(PATTERN_CHINESE_STRING);
        return p.matcher(s).matches();
    }
}
