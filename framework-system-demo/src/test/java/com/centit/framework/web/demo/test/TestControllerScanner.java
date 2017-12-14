package com.centit.framework.web.demo.test;

import com.centit.support.algorithm.ClassScannerOpt;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by codefan on 17-9-11.
 */
public class TestControllerScanner {

    public static void main(String[] args) {
        List<Class<?>> classes = ClassScannerOpt.getClassList("com.centit" ,
            true, Controller.class);
        for(Class clazz : classes){
            System.out.println(clazz.getTypeName());
        }

        System.out.println("done!");
    }
}
