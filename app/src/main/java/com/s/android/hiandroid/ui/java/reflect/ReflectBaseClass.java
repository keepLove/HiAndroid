package com.s.android.hiandroid.ui.java.reflect;

public class ReflectBaseClass {

    public String testBasePublicField = "base class field";
    protected String testBaseProtectedField = "base class field";
    String testBaseField = "base class field";
    private String testBasePrivateField = "base class field";

    public ReflectBaseClass() {
        System.out.println("base class constructor");
    }

    public ReflectBaseClass(String message) {
        System.out.println("base class constructor = " + message);
    }

    public void testBasePublicMethod() {
        System.out.println("base class public method");
    }

    protected void testBaseProtectedMethod() {
        System.out.println("base class protected method");
    }

    private void testBasePrivateMethod() {
        System.out.println("base class private method");
    }

    void testBaseMethod() {
        System.out.println("base class method");
    }
}
