package com.eddy.base.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：16:25 on 2018/3/2.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */

public interface OnAddFragmentListener {

    void startFragment(Fragment from, Class<? extends Fragment> target);

    void startFragment(Fragment from, Class<? extends Fragment> target, Bundle bundleArgs);

    void startFragment(Fragment from, Class<? extends Fragment> target, int requestCode);

    void startFragment(Fragment from, Class<? extends Fragment> target, Bundle bundleArgs, int requestCode);

    void startFragment(Fragment from, Fragment target);

    void startFragment(Fragment from, Fragment target, Bundle bundleArgs);

    void startFragment(Fragment from, Fragment target, int requestCode);

    void startFragment(Fragment from, Fragment target, Bundle bundleArgs, int requestCode);

    void replaceFragment(Fragment fragment, boolean isAddToBackStack);
}
