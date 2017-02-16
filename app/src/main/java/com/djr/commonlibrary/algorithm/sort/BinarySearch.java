package com.djr.commonlibrary.algorithm.sort;

/**
 * Created by DongJr on 2017/2/16.
 */

public class BinarySearch {

    public static int bSearch(int[] arrays, int start, int end, int target) {

        if (start > end) {
            return -1;
        }
        int mid = (start + end) / 2;

        if (arrays[mid] > target) {
            return bSearch(arrays, start, mid - 1, target);
        }
        if (arrays[mid] < target) {
            return bSearch(arrays, mid + 1, end, target);
        }
        return mid;
    }


    public static void main(String[] args) {
        int[] arrays = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        int i = BinarySearch.bSearch(arrays, 0, arrays.length - 1, 9);
        System.out.println("" + i);

    }

}
