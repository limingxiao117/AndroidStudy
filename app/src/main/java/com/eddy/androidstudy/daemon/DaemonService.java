package com.eddy.androidstudy.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：15:02 on 2018/4/14.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */

public class DaemonService extends Service {

    public DaemonService() {
        Log.i("eddy", "开启 DaemonService: 进程ID = " + Process.myUid());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaemonWatcher watcher = new DaemonWatcher();
        Log.i("eddy", "Process.myUid() = " + Process.myUid());
        watcher.openDaemonWatcher(Process.myUid());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
