package com.eddy.base.base;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class BaseAppManager {
    private static BaseAppManager instance = null;

    private Stack<Activity> activityStack = null;

    private BaseAppManager() {
    }

    /**
     * 单一实例
     */
    protected static BaseAppManager getInstance() {
        if (instance == null) {
            instance = new BaseAppManager();
        }
        return instance;
    }

    public static void add(Activity activity) {
        getInstance().addActivity(activity);
    }

    /**
     * 添加Activity到堆栈
     */
    protected void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    public static Activity current() {
        return getInstance().currentActivity();
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    protected Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public static void finish() {
        getInstance().finishActivity();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    protected void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    public static void finish(Activity activity) {
        getInstance().finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    protected void finishActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing())
                activity.finish();
        }
    }

    public static void finish(Class<?> cls) {
        getInstance().finishActivity(cls);
    }

    /**
     * 结束指定类名的Activity
     */
    protected void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public static void finishAllExcept(Class<?> cls) {
        getInstance().finishAllActivitiesExcept(cls);
    }

    /**
     * 除了指定类名的Activity，结束其他所有Activity
     */
    protected void finishAllActivitiesExcept(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (!activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public static int getStackActivityCount() {
        if (null != getInstance().activityStack)
            return getInstance().activityStack.size();
        return 0;
    }

    public static void finishAll() {
        getInstance().finishAllActivity();
    }

    /**
     * 结束所有Activity
     */
    protected synchronized void finishAllActivity() {
        if (activityStack != null) {
            try {
                for (int i = 0, size = activityStack.size(); i < size; i++) {
                    Activity activity = activityStack.get(i);
                    if (null != activity && !activity.isFinishing()) {
                        activity.finish();
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                activityStack.clear();
            }
            activityStack.clear();
        }
    }

    public static void exit(Context context) {
        getInstance().exitApp(context);
    }

    /**
     * 退出应用程序
     */
    protected void exitApp(Context context) {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}