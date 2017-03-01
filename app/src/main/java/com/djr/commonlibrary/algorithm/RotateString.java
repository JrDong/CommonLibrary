package com.djr.commonlibrary.algorithm;

/**
 * Created by jerry on 2017/2/13.
 */

public class RotateString {

    private static final String str = "abcdef";


    private String leftShiftOne(char[] chars) {

        char first = chars[0];

        for (int i = 1; i < chars.length; i++) {
            chars[i - 1] = chars[i];
        }
        chars[chars.length - 1] = first;

        return String.valueOf(chars);
    }


    private void leftShiftString(String str) {
        int length = str.length();
        while (length-- > 0) {
            str = leftShiftOne(str.toCharArray());
            System.out.println(str);
        }
    }

    private String reverseString(char[] chars, int start, int end) {

        while (start < end) {
            char temp = chars[start];
            chars[start++] = chars[end];
            chars[end--] = temp;
        }
        System.out.println(String.valueOf(chars));
        return String.valueOf(chars);
    }


    public static void main(String[] args) {
        //方法1
        RotateString rotateString = new RotateString();
        rotateString.leftShiftString(str);
        //方法2
        char[] chars = str.toCharArray();
        String s = rotateString.reverseString(chars, 0, 1);
        String s1 = rotateString.reverseString(s.toCharArray(), 2, s.length() - 1);
        rotateString.reverseString(s1.toCharArray(),0,s1.length()-1);

    }

}
