package com.eddy.base.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.eddy.base.swipeback.SwipeBackActivityBase;
import com.eddy.base.swipeback.SwipeBackActivityHelper;
import com.eddy.base.swipeback.SwipeBackLayout;
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

public abstract class BaseSwipeBackActivity extends BaseActivity implements SwipeBackActivityBase {

    public interface FragmentCallback {
        boolean onBackPressed();
    }

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mHelper.getSwipeBackLayout().setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        if (null != getSwipeBackLayout())
            getSwipeBackLayout().setEnableGesture(enable);
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity可以滑动退出, 并且总是优先; false: Activity不允许滑动退出
     */
    public boolean swipeBackPriority() {
        return getSupportFragmentManager().getBackStackEntryCount() <= 1;
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
}
