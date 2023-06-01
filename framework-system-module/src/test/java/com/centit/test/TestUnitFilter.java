package com.centit.test;

import com.centit.framework.components.SysUnitFilterEngine;

import java.util.Set;

public class TestUnitFilter {
    public static void main(String arg[]){

        Set<String> ss = SysUnitFilterEngine.calcSystemUnitsByExp("u0001||u0002||u0003",
            "system", null,null);

        for(String s:ss)
            System.out.println(s);
    }
}
