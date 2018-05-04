package com.eddy.base.swipeback;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：16:35 on 2018/3/2.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */

public interface SwipeBackFragmentListener {

    void setLocking(boolean locking);

    List<Fragment> getFragments();
}
