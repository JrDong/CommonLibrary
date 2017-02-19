package com.djr.commonlibrary.algorithm.sort;

import android.app.ActivityManager;
import android.widget.EdgeEffect;

/**
 * Created by DongJr on 2017/2/19.
 */

public class QuickSort {


    private static void swap(int[] arr, int x, int y) {
        int temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }

    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end)
            return;

        int mid = arr[end];
        int left = start;
        int right = end - 1;

        while (left < right) {
            while (arr[left] <= mid && left < right) {
                left++;
            }
            while (arr[right] >= mid && left < right) {
                right--;
            }
            swap(arr, left, right);
        }

        if (arr[left] >= arr[end]) {
            swap(arr, left, end);
        } else {
            left++;
        }
        quickSort(arr, start, left - 1);
        quickSort(arr, left + 1, end);

        print(arr);
    }

    public static void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i + ",");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        int[] arr = {5, 3, 12, 4, 1, 2, 8, 6, 1, 11};

        quickSort(arr, 0, arr.length - 1);
    }

}
