package com.djr.commonlibrary.algorithm;

/**
 * Created by jerry on 2017/7/10.
 */

public class TryCatchTest {

    public int finallyReturn(){
        int i = 1;
        try {
            i = 1;
            return returnWithSout("try",i);
        } catch (Exception e){
            return returnWithSout("catch",2);
        } finally {
            return returnWithSout("finally",3);
        }
    }

    private int returnWithSout(String str,int value){
        System.out.println(str);
        return value;
    }

    public static void main(String[] args){
        TryCatchTest test = new TryCatchTest();
        System.out.println(test.finallyReturn());
    }
}
