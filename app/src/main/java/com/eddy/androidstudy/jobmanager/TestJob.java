package com.eddy.androidstudy.jobmanager;

import android.support.annotation.Nullable;

import com.eddy.base.Logger;
import com.eddy.jobqueue.Job;
import com.eddy.jobqueue.Params;
import com.eddy.jobqueue.RetryConstraint;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：10:21 on 2018/4/16.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */
public class TestJob extends Job {

    public static final int PRIORITY = 1;
    private String text;
    private int    sleepTime;

    public TestJob(String text) {
        // requireNetwork，需要网络连接
        // persist，需要持久化
        super(new Params(PRIORITY).requireNetwork().persist());
        this.text = text;
        sleepTime = 5;
        Logger.d(text + " goin");
    }

    @Override
    public void onAdded() {
        // Job已经被保存到磁盘里，可以用来更新UI
        Logger.d(text + " Onadded");
    }

    @Override
    public void onRun() throws Throwable {
        // 在这里处理Job逻辑，例如网络请求等，所有的工作就是异步完成
        Thread.sleep(sleepTime * 1000);
        Logger.d(text + "  onRun");
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Logger.d("cancelReason = " + cancelReason);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount,
                                                     int maxRunCount) {
        // An error occurred in onRun.
        // Return value determines whether this job should retry or cancel. You can further
        // specify a backoff strategy or change the job's priority. You can also apply the
        // delay to the whole group to preserve jobs' running order.
        return RetryConstraint.createExponentialBackoff(runCount, 10);
    }
}