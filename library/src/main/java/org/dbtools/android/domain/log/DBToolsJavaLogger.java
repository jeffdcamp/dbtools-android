package org.dbtools.android.domain.log;

public class DBToolsJavaLogger implements DBToolsLogger {

    @Override
    public void v(String tag, String message) {
        System.out.println(tag + " / " + message); // NOSONAR
    }

    @Override
    public void v(String tag, String message, Throwable t) {
        System.out.println(tag + " / " + message + " / " + t.getMessage()); // NOSONAR
        t.printStackTrace(); // NOSONAR
    }

    @Override
    public void d(String tag, String message) {
        System.out.println(tag + " / " + message); // NOSONAR
    }

    @Override
    public void d(String tag, String message, Throwable t) {
        System.out.println(tag + " / " + message + " / " + t.getMessage()); // NOSONAR
        t.printStackTrace(); // NOSONAR
    }

    @Override
    public void i(String tag, String message) {
        System.out.println(tag + " / " + message); // NOSONAR
    }

    @Override
    public void i(String tag, String message, Throwable t) {
        System.out.println(tag + " / " + message + " / " + t.getMessage()); // NOSONAR
        t.printStackTrace(); // NOSONAR
    }

    @Override
    public void w(String tag, String message) {
        System.out.println(tag + " / " + message); // NOSONAR
    }

    @Override
    public void w(String tag, String message, Throwable t) {
        System.out.println(tag + " / " + message + " / " + t.getMessage()); // NOSONAR
        t.printStackTrace(); // NOSONAR
    }

    @Override
    public void e(String tag, String message) {
        System.out.println(tag + " / " + message); // NOSONAR
    }

    @Override
    public void e(String tag, String message, Throwable t) {
        System.out.println(tag + " / " + message + " / " + t.getMessage()); // NOSONAR
        t.printStackTrace(); // NOSONAR
    }

    @Override
    public void wtf(String tag, String message) {
        System.out.println(tag + " / " + message); // NOSONAR
    }

    @Override
    public void wtf(String tag, String message, Throwable t) {
        System.out.println(tag + " / " + message + " / " + t.getMessage()); // NOSONAR
        t.printStackTrace(); // NOSONAR
    }
}

