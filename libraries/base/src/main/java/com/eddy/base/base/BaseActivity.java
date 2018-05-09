package com.eddy.base.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.eddy.immersionbar.ImmersionBar;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：14:15 on 2018/3/2.
 * 描述 ：
 * -    1）初始化沉浸式模块：ImmersionBar
 * -        isImmersionBarEnabled()：是否可以使用沉浸式：默认 true
 * -
 * -    2）软键盘处理
 * -    3）滑动返回
 * -        setSwipeBackEnable()
 */

public abstract class BaseActivity extends BaseAppCompatActivity {

    private   InputMethodManager mInputMethodManager;
    protected ImmersionBar       mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mInputMethodManager = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 在BaseActivity里初始化
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mImmersionBar.titleBar(mToolbar)
                    .init();
        } else {
            mImmersionBar.init();
        }
    }

    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.mInputMethodManager == null) {
            this.mInputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.mInputMethodManager != null)) {
            this.mInputMethodManager.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }
}
