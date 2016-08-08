package com.djr.commonlibrary;

/**
 * Created by DongJr on 2016/8/8.
 * <p/>
 * 几种常用的排序算法
 */
public class Sort {


    /**
     * 冒泡排序
     */
    private void BubbleSort() {
        int[] arr = {95, 45, 15, 78, 84, 51, 24, 12};
        int i, j, temp, len = arr.length;
        for (i = 0; i < len - 1; i++) {
            for (j = 0; j < len - 1 - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        print(arr);
    }

    /**
     * 选择排序
     */
    private void SelectSort() {
        int arr[] = {13, 15, 37, 89, 60, 39, 12, 109, 56, 72};
        int temp;
        for (int i = 0; i < arr.length - 1; i++) {
            int min = arr[i];
            for (int j = i; j < arr.length - 1; j++) {
                if (min > arr[j + 1]) {
                    temp = min;
                    min = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            arr[i] = min;
        }
        print(arr);
    }



    private void print(int arr[]) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Sort sort = new Sort();
        sort.BubbleSort();
        sort.SelectSort();
    }

}
