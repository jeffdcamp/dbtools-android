package org.dbtools.android.domain.log;

public interface DBToolsLogger {
    void v(String tag, String message);
    void v(String tag, String message, Throwable t);
    void d(String tag, String message);
    void d(String tag, String message, Throwable t);
    void i(String tag, String message);
    void i(String tag, String message, Throwable t);
    void w(String tag, String message);
    void w(String tag, String message, Throwable t);
    void e(String tag, String message);
    void e(String tag, String message, Throwable t);
    void wtf(String tag, String message);
    void wtf(String tag, String message, Throwable t);
}
