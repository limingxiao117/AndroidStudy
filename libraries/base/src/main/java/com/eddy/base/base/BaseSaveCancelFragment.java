package com.eddy.base.base;

import android.view.View;
import android.widget.TextView;

import com.eddy.base.R;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：13:35 on 2018/3/7.
 * 描述 ：//TODO 描述本文件的功能
 * 修改 ：//TODO 每次修改的时候描述修改的地方或才要点
 */

public abstract class BaseSaveCancelFragment extends BaseFragment {

    private TextView mItemLeft;
    private TextView mItemRight;
    private TextView mTitle;
    private View     mCustomContainer;

    @Override
    protected void initToolbar() {
        super.initToolbar();
        if (null != mLayoutView) {
            if (null != mToolbar) {
                mCustomContainer = findViewById(R.id.common_toolbar_custom_container);
                mItemLeft = findViewById(R.id.text_left);
                mItemRight = findViewById(R.id.text_right);
                mTitle = findViewById(R.id.text_title);
                mItemLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickLeftItem();
                    }
                });
                mItemRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickRightItem();
                    }
                });
            }
        }
    }

    @Override
    protected final boolean isDisplayHomeAdUp() {
        return false;
    }

    protected abstract void onClickLeftItem();

    protected abstract void onClickRightItem();


    protected void setLeftItemEnable(boolean enable) {
        mItemLeft.setEnabled(enable);
    }

    protected void setRightItemEnable(boolean enable) {
        mItemRight.setEnabled(enable);
    }

    protected void startActionMode() {
        mCustomContainer.setVisibility(View.VISIBLE);
    }

    protected void exitActionMode() {
        mCustomContainer.setVisibility(View.GONE);
    }

    protected boolean isInActionMode() {
        return mCustomContainer.getVisibility() == View.VISIBLE;
    }

    @Override
    public void setTitle(int titleId) {
        if (null != mTitle)
            mTitle.setText(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (null != mTitle)
            mTitle.setText(title);
    }

    protected void setRightItemText(CharSequence string) {
        if (null != mItemRight)
            mItemRight.setText(string);
    }

    protected void setLeftItemText(CharSequence string) {
        if (null != mItemLeft)
            mItemLeft.setText(string);
    }

    protected void setRightItemText(int resId) {
        if (null != mItemRight)
            mItemRight.setText(resId);
    }

    protected void setLeftItemText(int resId) {
        if (null != mItemLeft)
            mItemLeft.setText(resId);
    }

    protected View getTitleView() {
        if (null != mTitle) {
            return mTitle;
        }
        return null;
    }
}
