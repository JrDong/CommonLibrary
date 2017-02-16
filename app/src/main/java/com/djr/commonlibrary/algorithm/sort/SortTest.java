package com.djr.commonlibrary.algorithm.sort;

/**
 * Created by DongJr on 2017/2/14.
 */

public class SortTest {


    /**
     * 插入排序
     *
     * 时间复杂度 : O()
     */
    public static void insertSort(int[] numbers) {
        int k;
        int n;
        for (int i = 1; i < numbers.length; i++) {
            k = numbers[i];
            n = i - 1;
            while (n >= 0 && k < numbers[n]) {
                numbers[n + 1] = numbers[n];
                n--;
            }
            numbers[n + 1] = k;
        }
        printNumbers(numbers);
    }

    public static void printNumbers(int[] numbers) {
        for (int number : numbers) {
            System.out.print(number + "");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] numbers = {8, 2, 4, 9, 3, 6};
        SortTest.insertSort(numbers);
    }


}
