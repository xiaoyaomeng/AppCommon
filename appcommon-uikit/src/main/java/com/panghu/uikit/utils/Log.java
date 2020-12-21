package com.panghu.uikit.utils;

/**
 * Log implementation for the application.
 * <p/>
 *
 * Important notes:<p />
 * 1. The methods defined in this class must contain at least two args, first is log tag, second
 * is log message. <p />
 * 2. The client-side codes for the methods of this class will be transformed by log-plugin
 * (https://git.ringcentral.com/android/logplugin).<p /><p />
 *
 * Created by panghu on 5/20/16.
 */
public class Log {

    private static String APP = "APP";
    private static ILogPrinter sLogPrinter = null;

    private Log() {
    }

    public static void setLogPrinter(ILogPrinter logPrinter) {
        sLogPrinter = logPrinter;
    }

    public static ILogPrinter getLogPrinter() {
        return sLogPrinter;
    }

    /**
     * Print a verbose log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     */
    public static int v(String tag, String msg) {
        if (sLogPrinter == null) {
            return android.util.Log.v(APP, msg);
        } else {
            sLogPrinter.print(ILogPrinter.VERBOSE, APP, msg);
            return 0;
        }
    }

    /**
     * Print a verbose log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     * @param tr An exception to log.
     */
    public static int v(String tag, String msg, Throwable tr) {
        if (sLogPrinter == null) {
            return android.util.Log.v(APP, msg, tr);
        } else {
            sLogPrinter.print(ILogPrinter.VERBOSE, APP,
                    msg + '\n' + android.util.Log.getStackTraceString(tr));
            return 0;
        }
    }

    /**
     * Print a debug log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     */
    public static int d(String tag, String msg) {
        if (sLogPrinter == null) {
            return android.util.Log.d(APP, msg);
        } else {
            sLogPrinter.print(ILogPrinter.DEBUG, APP, msg);
            return 0;
        }
    }

    /**
     * Print a debug log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     * @param tr An exception to log.
     */
    public static int d(String tag, String msg, Throwable tr) {
        if (sLogPrinter == null) {
            return android.util.Log.d(APP, msg, tr);
        } else {
            sLogPrinter.print(ILogPrinter.DEBUG, APP,
                    msg + '\n' + android.util.Log.getStackTraceString(tr));
            return 0;
        }
    }

    /**
     * Print a info log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     */
    public static int i(String tag, String msg) {
        if (sLogPrinter == null) {
            return android.util.Log.i(APP, msg);
        } else {
            sLogPrinter.print(ILogPrinter.INFO, APP, msg);
            return 0;
        }
    }

    /**
     * Print a info log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     * @param tr An exception to log.
     */
    public static int i(String tag, String msg, Throwable tr) {
        if (sLogPrinter == null) {
            return android.util.Log.i(APP, msg, tr);
        } else {
            sLogPrinter.print(ILogPrinter.INFO, APP,
                    msg + '\n' + android.util.Log.getStackTraceString(tr));
            return 0;
        }
    }

    /**
     * Print a warn log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     */
    public static int w(String tag, String msg) {
        if (sLogPrinter == null) {
            return android.util.Log.w(APP, msg);
        } else {
            sLogPrinter.print(ILogPrinter.WARN, APP, msg);
            return 0;
        }
    }

    /**
     * Print a warn log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     * @param tr An exception to log.
     */
    public static int w(String tag, String msg, Throwable tr) {
        if (sLogPrinter == null) {
            return android.util.Log.w(APP, msg, tr);
        } else {
            sLogPrinter.print(ILogPrinter.WARN, APP,
                    msg + '\n' + android.util.Log.getStackTraceString(tr));
            return 0;
        }
    }

    /**
     * Print a error log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     */
    public static int e(String tag, String msg) {
        if (sLogPrinter == null) {
            return android.util.Log.e(APP, msg);
        } else {
            sLogPrinter.print(ILogPrinter.ERROR, APP, msg);
            return 0;
        }
    }

    /**
     * Print a error log message.
     *
     * @param tag The log tag.
     * @param msg The log message.
     * @param tr An exception to log.
     */
    public static int e(String tag, String msg, Throwable tr) {
        if (sLogPrinter == null) {
            return android.util.Log.e(APP, msg, tr);
        } else {
            sLogPrinter.print(ILogPrinter.ERROR, APP,
                    msg + '\n' + android.util.Log.getStackTraceString(tr));
            return 0;
        }
    }
}
