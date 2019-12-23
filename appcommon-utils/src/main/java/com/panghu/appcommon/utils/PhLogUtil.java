package com.panghu.appcommon.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PhLogUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhLogUtil {

    public static String customTagPrefix = "";
    public static String sTAG = "PhLogUtil";

    private PhLogUtil() {
    }

    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static CustomLogger customLogger;

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

    public static void setLogEnable(boolean flag){
        PhLogUtil.allowD =flag;
        PhLogUtil.allowI =flag;
        PhLogUtil.allowV =flag;
        PhLogUtil.allowW =flag;
        PhLogUtil.allowWtf =flag;
        Config.getInstance().logFileSwitch(flag);
        Config.getInstance().logSwitch(flag);
    }

    @Deprecated
    public static void d(String content) {
        if (!checkLogSwitch()) return;
        Log.d(sTAG, checkEmpty(content));
    }

    private static String checkEmpty(String content) {
        return PhStringUtil.isEmpty(content) ? "null!!" : content;
    }

    public static void d(String content, Throwable tr) {
        if (!checkLogSwitch()) return;
        StackTraceElement caller = PhExceptionUtil.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
            Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (!checkLogSwitch())
            return;
        Log.e(sTAG, checkEmpty(content));
    }

    public static void e(String content, Throwable tr) {
        if (!checkLogSwitch()) return;
        StackTraceElement caller = PhExceptionUtil.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
            Log.e(tag, content, tr);
        }
    }

    @Deprecated
    public static void i(String content) {
        if (!checkLogSwitch()) return;
        Log.i(sTAG, checkEmpty(content));
    }

    public static void i(String content, Throwable tr) {
        if (!checkLogSwitch()) return;
        StackTraceElement caller = PhExceptionUtil.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
            Log.i(tag, content, tr);
        }
    }

    @Deprecated
    public static void v(String content) {
        if (!checkLogSwitch()) return;
        Log.v(sTAG, checkEmpty(content));
    }

    public static void v(String content, Throwable tr) {
        if (!checkLogSwitch()) return;
        StackTraceElement caller = PhExceptionUtil.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
            Log.v(tag, content, tr);
        }
    }

    @Deprecated
    public static void w(String content) {
        if (!checkLogSwitch()) return;Log.w(sTAG, checkEmpty(content));
    }

    public static void w(String content, Throwable tr) {
        if (!checkLogSwitch())
            return;
        StackTraceElement caller = PhExceptionUtil.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
            Log.w(tag, content, tr);
        }
    }

    @Deprecated
    public static void w(Throwable tr) {
        if (!checkLogSwitch()) return;Log.w(sTAG, tr);
    }


    public static void wtf(String content) {
        if (!checkLogSwitch()) return;Log.wtf(sTAG, checkEmpty(content));
    }

    public static void wtf(String content, Throwable tr) {
        if (!checkLogSwitch())
            return;
        StackTraceElement caller = PhExceptionUtil.getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content, tr);
        } else {
            Log.wtf(tag, content, tr);
        }
    }

    @Deprecated
    public static void wtf(Throwable tr) {
        if (!checkLogSwitch()) return;
        Log.wtf(sTAG, tr);
    }


    public static class Config {

        private static final Config INSTANCE = new Config();

        private boolean isOpenSwitch = true;

        private boolean isLogFileExists = false;

        private boolean isOpenLogFileSwitch = true;

        private String logFilePath = null;

        private String includeTag = null;

        private int logLevel = Log.VERBOSE;

        private Config() {
        }

        public static Config getInstance() {
            return INSTANCE;
        }

        /**
         * 全局切换Log开关，是否打印log
         */
        public Config logSwitch(boolean isOpenSwitch) {
            this.isOpenSwitch = isOpenSwitch;
            return this;
        }

        /**
         * 设置默认标签，这种主要用于LogUtil.d() 这种默认一个大标签，通过子标签来区分内容的
         */
        public Config includeTag(String includeTag) {
            this.includeTag = includeTag;
            return this;
        }

        /**
         * 线上apk可以通过包路径或者sdcard路径下是否有某个文件来决定是否打开
         */
        public Config logFileSwitch(boolean isOpenLogFileSwitch) {
            this.isOpenLogFileSwitch = isOpenLogFileSwitch;
            return this;
        }

        /**
         * 控制log开关的file的path
         */
        public Config logFilePath(String logFilePath) {
            this.logFilePath = logFilePath;
            checkLogFilePath();
            return this;
        }

        public Config logLevel(int logLevel) {
            this.logLevel = logLevel;
            return this;
        }

    }


    public static Config config() {
        return Config.getInstance();
    }


    /**
     * 检查文件是否存在
     */
    public static void checkLogFilePath() {
        boolean flag = false;
        try {
            File f = new File(Config.getInstance().logFilePath);
            flag = f.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Config.getInstance().isLogFileExists = flag;
    }

    /**
     * 是否打开log的总方法
     */
    private static boolean checkLogSwitch() {
        if (Config.getInstance().isOpenSwitch) {
            return true;
        } else if (!Config.getInstance().isOpenLogFileSwitch) {
            return false;
        } else if (!Config.getInstance().isLogFileExists) {
            return false;
        }
        return true;
    }

    /**
     * 打印 DEBUG 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg  消息内容，支持{@link String#format(String, Object...)}的语法
     * @param args format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void d(String tag, String msg, Object... args) {
        log(Log.DEBUG, null, tag, msg, args);
    }

    /**
     * 打印 VERBOSE 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg  消息内容，支持{@link String#format(String, Object...)}的语法
     * @param args format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void v(String tag, String msg, Object... args) {
        log(Log.VERBOSE, null, tag, msg, args);
    }

    /**
     * 打印 INFO 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg  消息内容，支持{@link String#format(String, Object...)}的语法
     * @param args format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void i(String tag, String msg, Object... args) {
        log(Log.INFO, null, tag, msg, args);
    }

    /**
     * 打印 ERROR 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg  消息内容，支持{@link String#format(String, Object...)}的语法
     * @param args format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void e(String tag, String msg, Object... args) {
        log(Log.ERROR, null, tag, msg, args);
    }

    /**
     * 打印 WARN 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg  消息内容，支持{@link String#format(String, Object...)}的语法
     * @param args format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void w(String tag, String msg, Object... args) {
        log(Log.WARN, null, tag, msg, args);
    }

    /**
     * 打印带有调用栈的 DEBUG 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg       消息内容，支持{@link String#format(String, Object...)}的语法
     * @param throwable
     * @param args      format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void d(String tag, String msg, Throwable throwable, Object... args) {
        log(Log.DEBUG, throwable, tag, msg, args);
    }

    /**
     * 打印带有调用栈的 VERBOSE 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg       消息内容，支持{@link String#format(String, Object...)}的语法
     * @param throwable
     * @param args      format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void v(String tag, String msg, Throwable throwable, Object... args) {
        log(Log.VERBOSE, throwable, tag, msg, args);
    }

    /**
     * 打印带有调用栈的 INFO 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg       消息内容，支持{@link String#format(String, Object...)}的语法
     * @param throwable
     * @param args      format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void i(String tag, String msg, Throwable throwable, Object... args) {
        log(Log.INFO, throwable, tag, msg, args);
    }

    /**
     * 打印带有调用栈的 ERROR 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg       消息内容，支持{@link String#format(String, Object...)}的语法
     * @param throwable
     * @param args      format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void e(String tag, String msg, Throwable throwable, Object... args) {
        log(Log.ERROR, throwable, tag, msg, args);
    }

    /**
     * 打印带有调用栈的 WARN 级别的日志，
     * 如果开启了debug模式
     * 则不过滤任何级别的日志
     *
     * @param tag
     * @param msg       消息内容，支持{@link String#format(String, Object...)}的语法
     * @param throwable
     * @param args      format msg的参数，可以为null
     * @throws java.util.IllegalFormatException if the format is invalid.
     */
    public static void w(String tag, String msg, Throwable throwable, Object... args) {
        log(Log.WARN, throwable, tag, msg, args);
    }

    private static void log(int level, Throwable throwable, String tag, String msg, Object... args) {
        if (!checkLogSwitch()) {
            return;
        }

        if (PhStringUtil.isEmpty(Config.getInstance().includeTag)) {
            Config.getInstance().includeTag = "panghu";
        }

        String includeTag = Config.getInstance().includeTag;
        boolean hasThrowable = (throwable != null);
        //msg content
        StringBuilder msgBuilder = new StringBuilder(includeTag);
        msgBuilder.append(" : ");
        if (PhStringUtil.isNotEmpty(msg) && args != null && args.length > 0) {
            msgBuilder.append(String.format(msg, args));
        } else {
            msgBuilder.append(msg);
        }

        switch (level) {
            case Log.VERBOSE:
                if (hasThrowable) {
                    Log.v(tag, msgBuilder.toString(), throwable);
                } else {
                    Log.v(tag, msgBuilder.toString());
                }
                break;
            case Log.DEBUG:
                if (hasThrowable) {
                    Log.d(tag, msgBuilder.toString(), throwable);
                } else {
                    Log.d(tag, msgBuilder.toString());
                }
                break;
            case Log.INFO:
                if (hasThrowable) {
                    Log.i(tag, msgBuilder.toString(), throwable);
                } else {
                    Log.i(tag, msgBuilder.toString());
                }
                break;
            case Log.WARN:
                if (hasThrowable) {
                    Log.w(tag, msgBuilder.toString(), throwable);
                } else {
                    Log.w(tag, msgBuilder.toString());
                }
                break;
            case Log.ERROR:
                if (hasThrowable) {
                    Log.e(tag, msgBuilder.toString(), throwable);
                } else {
                    Log.e(tag, msgBuilder.toString());
                }
                break;
        }
    }


    // LogWrite
    private static String LOG_PATH_SDCARD_DIR;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
    private static Process process;

    public static void startLogcatLog(Context context) {
        init(context);
        createLog(context);
    }

    public static void stopLogcatLog() {
        if (process != null) {
            process.destroy();
        }
    }

    private static void init(Context context) {
        LOG_PATH_SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + PhAppUtil.getAppVersionName(context) + "_Log";
        createLogDir();
    }

    /**
     * create log
     */
    public static void createLog(Context context) {
        List<String> commandList = new ArrayList<String>();
        commandList.add("logcat");
        commandList.add("-f");
        commandList.add(getLogPath(context));
        commandList.add("-v");
        commandList.add("time");
        try {
            process = Runtime.getRuntime().exec(
                    commandList.toArray(new String[commandList.size()]));
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG,e.getMessage(), e);
        }
    }

    /**
     * the path of the log file
     *
     * @return
     */
    public static String getLogPath(Context context) {
        createLogDir();
        String logFileName = PhAppUtil.getAppName(context) + "_logcat_" + simpleDateFormat.format(new Date()) + ".log";// name
        return LOG_PATH_SDCARD_DIR + File.separator + logFileName;

    }

    /**
     * make the dir
     */
    private static void createLogDir() {
        File file;
        boolean mkOk;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            file = new File(LOG_PATH_SDCARD_DIR);
            if (!file.isDirectory()) {
                mkOk = file.mkdirs();
                if (!mkOk) {
                    return;
                }
            }
        }
    }
}
