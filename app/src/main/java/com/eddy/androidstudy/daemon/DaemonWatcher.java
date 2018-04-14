package com.eddy.androidstudy.daemon;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：15:03 on 2018/4/14.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */

public class DaemonWatcher {

    static {
        System.loadLibrary("daemon");
    }

    /**
     * @param process_uId
     */
    public native void openDaemonWatcher(int process_uId);
}
