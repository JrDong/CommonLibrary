package com.djr.commonlibrary.annotation;

import java.lang.reflect.Method;

/**
 * Created by DongJr on 2016/12/30.
 */

public class Annotation {

    public static void getAnnotation() {

        try {
            Class<?> clazz = Class.forName("com.djr.commonlibrary.annotation.Person");
            //找到类上的注解
            boolean isExist = clazz.isAnnotationPresent(Bind.class);
            if (isExist) {
                Bind annotation = clazz.getAnnotation(Bind.class);
                System.out.println(annotation.value());
            }

            //找到方法上的注解
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                boolean isMethodExist = method.isAnnotationPresent(Bind.class);
                if (isMethodExist) {
                    Bind annotation = method.getAnnotation(Bind.class);
                    System.out.println(annotation.value());
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        getAnnotation();
    }

}
