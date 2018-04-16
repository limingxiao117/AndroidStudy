package com.eddy.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：10:10 on 2018/4/16.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */
public class Logger {

    public static       boolean DEBUG       = BuildConfig.DEBUG;
    public static final int     LEVEL_V     = 1;
    public static final int     LEVEL_D     = 2;
    public static final int     LEVEL_I     = 3;
    public static final int     LEVEL_W     = 4;
    public static final int     LEVEL_E     = 5;
    public static final int     LEVEL_WTF   = 6;
    public static final int     LEVEL_ALL   = 7;
    public static final int     DEBUG_LEVEL = LEVEL_ALL;

    public static String TAG = "eddy";

    private Logger() {

    }

    /**
     * 打印堆栈信息
     */
    public static void printStackInfo() {
        Exception e = new Exception("print Stack info");
        e.printStackTrace();
    }

    //用法与系统Log一样
    public static void v(String tag, String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_V) {
            printLog(LEVEL_V, tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_D) {
            printLog(LEVEL_D, tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_I) {
            printLog(LEVEL_I, tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_W) {
            printLog(LEVEL_W, tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_E) {
            printLog(LEVEL_E, tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_WTF) {
            printLog(LEVEL_WTF, tag, msg);
        }
    }

    //省略TAG，默认采用TAG = "Meshare"
    public static void v(String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_V) {
            printLog(LEVEL_V, TAG, msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_D) {
            printLog(LEVEL_D, TAG, msg);
        }
    }

    public static void i(String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_I) {
            printLog(LEVEL_I, TAG, msg);
        }
    }

    public static void w(String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_W) {
            printLog(LEVEL_W, TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_E) {
            printLog(LEVEL_E, TAG, msg);
        }
    }

    public static void wtf(String msg) {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_WTF) {
            printLog(LEVEL_WTF, TAG, msg);
        }
    }

    public static void v() {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_V) {
            printLog(LEVEL_V, TAG, null);
        }
    }

    public static void d() {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_D) {
            printLog(LEVEL_D, TAG, null);
        }
    }

    public static void i() {
        if (DEBUG && DEBUG_LEVEL >= LEVEL_I) {
            printLog(LEVEL_I, TAG, null);
        }
    }

    private static void printLog(int log_level, String tag, String msg) {
        Thread              thread = Thread.currentThread();
        StackTraceElement[] ste    = thread.getStackTrace();
        msg = formatMessage(ste, msg);
        switch (log_level) {
            case LEVEL_V:
                Log.v(tag, msg);
                break;
            case LEVEL_D:
                // 部分手机不打印Log.d
//                Log.d(tag, msg);
                Log.i(tag, msg);
                break;
            case LEVEL_I:
                Log.i(tag, msg);
                break;
            case LEVEL_W:
                Log.w(tag, msg);
                break;
            case LEVEL_E:
                Log.e(tag, msg);
                break;
            case LEVEL_WTF:
                Log.wtf(tag, msg);
                break;
        }
    }

    private static String formatMessage(StackTraceElement[] ste, String msg) {
        StringBuilder builder = new StringBuilder();
        String        file    = ste[4].getFileName();
        if (!TextUtils.isEmpty(file)) {
            file = file.replace(".java", "");
            builder.append(file);
        }
        String method = ste[4].getMethodName();
        if (!TextUtils.isEmpty(method) && (!"<unknown method>".equals(method))) {
            builder.append(" -- ");
            builder.append(method);
            builder.append("()");
        }
        int line = ste[4].getLineNumber();
        if (line > 0) {
            builder.append(" -- line = ");
            builder.append(line);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.append(" -- ");
            builder.append(msg);
        }
        return builder.toString();
    }

    public static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            Field    field = clazz.getField(fieldName);
            return field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void init(boolean isDebug, String actionName) {
        DEBUG = isDebug;
        TAG = actionName;
    }

    public static boolean getDebug() {
        return DEBUG;
    }
}
