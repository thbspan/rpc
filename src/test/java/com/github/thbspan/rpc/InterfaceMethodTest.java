package com.github.thbspan.rpc;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.thbspan.rpc.service.EchoImpl;
import com.github.thbspan.rpc.service.SubIEcho;

public class InterfaceMethodTest {

    @Test
    public void test() {
        try {
            Method method;
            method = Object.class.getMethod("toString", (Class<?>[]) null);
            System.out.println(method);

            method= SubIEcho.class.getMethod("echo", (Class<?>[]) null);
            System.out.println(method);

            try {
                method= SubIEcho.class.getMethod("toString", (Class<?>[]) null);
                System.out.println(method);
            } catch (Exception e) {
                e.printStackTrace();
            }

            method= EchoImpl.class.getMethod("toString", (Class<?>[]) null);
            System.out.println(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
