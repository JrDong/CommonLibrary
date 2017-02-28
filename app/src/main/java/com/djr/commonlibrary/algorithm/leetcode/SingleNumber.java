package com.djr.commonlibrary.algorithm.leetcode;

/**
 * Created by DongJr on 2017/2/28.
 * <p>
 * Given an array of integers, every element appears twice except for one. Find that single one.
 * Note:
 * Your algorithm should have a linear runtime complexity. Could you implement it without using
 * extra memory?
 */

public class SingleNumber {

    /**
     * ^ 相同为0,不同为1
     */
    private static int solution(int[] A) {
        int x = 0;
        for (int a : A) {
            x = x ^ a;
        }
        return x;
    }

    public static void main(String[] args) {
        int[] A = {2, 2, 4, 5, 4, 5, 3};
        System.out.println(solution(A) + "");
    }

}
