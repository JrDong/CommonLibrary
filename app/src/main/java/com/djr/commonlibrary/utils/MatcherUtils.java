package com.djr.commonlibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MatcherUtils {
    public static boolean strSplitR(String str) {
        if (str != null) {
            String[] strs = str.split("\\.");
            if (strs.length > 0 && "R".equals(strs[0])) {
                return true;
            } else {
                return false;

            }
        }
        return false;
    }

    public static boolean isUrl(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "[a-zA-z]+://[^\\s]*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);

        return m.matches();
    }

}
