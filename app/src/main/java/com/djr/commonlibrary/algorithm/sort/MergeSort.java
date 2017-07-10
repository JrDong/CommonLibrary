package com.djr.commonlibrary.algorithm.sort;

/**
 * Created by DongJr on 2017/2/19.
 */

public class MergeSort {


    private static void merge(int[] arr, int[] result, int start, int end) {

        if (start >= end) return;

        int center = (start + end) / 2;
        int start1 = start, end1 = center;
        int start2 = center + 1, end2 = end;
        merge(arr, result, start1, end1);
        merge(arr, result, start2, end2);
        int k = start1;
        while (start1 <= end1 && start2 <= end2) {
            if (arr[start1] < arr[start2]) {
                result[k++] = arr[start1++];
            } else {
                result[k++] = arr[start2++];
            }
        }
        while (start1 <= end1) {
            result[k++] = arr[start1++];
        }

        while (start2 <= end2) {
            result[k++] = arr[start2++];
        }
        for (k = start; k <= end; k++) {
            arr[k] = result[k];
        }
        print(arr);

    }

    /**
     *
     * @param a 有序数组a
     * @param b 有序数组b
     * @param result 结果数组
     */
    public static void merge2(int[] a,int [] b, int[] result){

        int i = 0 , j = 0 , k = 0 ;
        while (i < a.length && j < b.length){
            if (a[i] < b[j]){
                result[k++] = a[i++];
            }else {
                result[k++] = b[j++];
            }
        }

        while (i < a.length){
            result[k++] = a[i++];
        }

        while (j < b.length){
            result[k++] = b[j++];
        }
        print(result);
    }


    public static void print(int[] arr) {
        for (int i : arr) {
            System.out.print(i + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = {5, 3, 12, 4, 1, 2, 8, 1};
        int[] result = new int[arr.length];
        int[] a = {3,6};
        int[] b = {2,7};
        merge(arr, result, 0, arr.length - 1);
        merge2(a,b,new int[4]);
    }
}
