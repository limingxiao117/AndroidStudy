package com.eddy.androidstudy;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.eddy.base.Logger;
import com.eddy.jobqueue.JobManager;
import com.eddy.jobqueue.config.Configuration;
import com.eddy.jobqueue.log.CustomLogger;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：11:35 on 2018/4/14.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */

public class App extends MultiDexApplication {

    private static App        mInstance;
    private        JobManager mJobManager;

    private App() {
        mInstance = this;
    }

    public static synchronized App getInstance() {
        if (null == mInstance) {
            mInstance = new App();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // config Log
        Logger.init(BuildConfig.DEBUG, "eddy");

        // config JobManager
        configureJobManager();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void configureJobManager() {
        //3. JobManager的配置器，利用Builder模式
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {

                    @Override
                    public boolean isDebugEnabled() {
                        return Logger.getDebug();
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Logger.d(String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Logger.d(String.format(text, args));
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Logger.d(String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {
                        Logger.v(String.format(text, args));
                    }
                })
                // always keep at least one consumer alive
                .minConsumerCount(1)

                // up to 3 consumers at a time
                .maxConsumerCount(3)

                // 3 jobs per consumer
                .loadFactor(3)

                //wait 2 minute
                .consumerKeepAlive(120)
                .build();
        mJobManager = new JobManager(configuration);
    }

    public JobManager getJobManager() {
        return mJobManager;
    }
}
