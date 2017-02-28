package com.djr.commonlibrary.algorithm.leetcode;

/**
 * Created by DongJr on 2017/2/28.
 * <p>
 * Given a string s consists of upper/lower-case alphabets and empty space characters' '
 * , return the length of last word in the string.
 * If the last word does not exist, return 0.
 * Note: A word is defined as a character sequence consists of non-space characters only.
 * <p>
 * For example,
 * Given s ="Hello World",
 * return5.
 */

public class LengthOfLastWord {

    public int solution(String s) {
        String[] split = s.split(" ");
        if (split.length == 0) {
            return split[0].length();
        }
        return split[split.length - 1].length();
    }

    public int solution2(String s) {
        int index = s.length() - 1;

        while (index >= 0) {
            if (s.charAt(index) != ' ')
                break;
            index--;
        }
        int end = index;

        if (index < 0) return 0;

        while (index >= 0) {
            if (s.charAt(index) == ' ') break;
            index--;
        }
        return end - index;
    }

    public static void main(String[] args) {
        LengthOfLastWord lastWord = new LengthOfLastWord();
        System.out.println(lastWord.solution("Hello World"));
        System.out.println(lastWord.solution2("Hello World"));

    }

}
