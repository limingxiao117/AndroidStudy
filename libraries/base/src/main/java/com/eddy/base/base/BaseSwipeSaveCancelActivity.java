package com.eddy.base.base;

import android.view.View;
import android.widget.TextView;

import com.eddy.base.R;

public abstract class BaseSwipeSaveCancelActivity extends BaseSwipeBackActivity {

    private TextView mItemLeft;
    private TextView mItemRight;
    private TextView mTitle;
    private View     mCustomContainer;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (null != mToolbar) {
            mCustomContainer = findViewById(R.id.common_toolbar_custom_container);
            mItemLeft = (TextView) findViewById(R.id.text_left);
            mItemRight = (TextView) findViewById(R.id.text_right);
            mTitle = (TextView) findViewById(R.id.text_title);
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
            // 默认处于正常状态
            exitActionMode();
        }
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
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    protected void exitActionMode() {
        mCustomContainer.setVisibility(View.GONE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected boolean isInActionMode() {
        return mCustomContainer.getVisibility() == View.VISIBLE;
    }

    @Override
    public void setTitle(int titleId) {
        mTitle.setText(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    protected void setRightItemText(CharSequence string) {
        mItemRight.setText(string);
    }

    protected void setLeftItemText(CharSequence string) {
        mItemLeft.setText(string);
    }

    protected void setRightItemText(int resId) {
        mItemRight.setText(resId);
    }

    protected void setLeftItemText(int resId) {
        mItemLeft.setText(resId);
    }

    protected View getTitleView() {
        if (null != mTitle) {
            return mTitle;
        }
        return null;
    }
}
