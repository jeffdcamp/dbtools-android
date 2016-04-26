package org.dbtools.android.domain.log;

import android.util.Log;

@SuppressWarnings("unused")
public class DBToolsAndroidLogger implements DBToolsLogger {
    public void v(String tag, String message) { // NOPMD - Name should match Android Log methods
        Log.v(tag, message);
    }

    public void v(String tag, String message, Throwable t) { // NOPMD - Name should match Android Log methods
        Log.v(tag, message, t);
    }

    public void d(String tag, String message) { // NOPMD - Name should match Android Log methods
        Log.d(tag, message);
    }

    public void d(String tag, String message, Throwable t) { // NOPMD - Name should match Android Log methods
        Log.d(tag, message, t);
    }

    public void i(String tag, String message) { // NOPMD - Name should match Android Log methods
        Log.i(tag, message);
    }

    public void i(String tag, String message, Throwable t) { // NOPMD - Name should match Android Log methods
        Log.i(tag, message, t);
    }

    public void w(String tag, String message) { // NOPMD - Name should match Android Log methods
        Log.w(tag, message);
    }

    public void w(String tag, String message, Throwable t) { // NOPMD - Name should match Android Log methods
        Log.w(tag, message, t);
    }

    public void e(String tag, String message) { // NOPMD - Name should match Android Log methods
        Log.e(tag, message);
    }

    public void e(String tag, String message, Throwable t) { // NOPMD - Name should match Android Log methods
        Log.e(tag, message, t);
    }

    public void wtf(String tag, String message) { // NOPMD - Name should match Android Log methods
        Log.wtf(tag, message);
    }

    public void wtf(String tag, String message, Throwable t) { // NOPMD - Name should match Android Log methods
        Log.wtf(tag, message, t);
    }
}
