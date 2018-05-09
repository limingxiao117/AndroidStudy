package com.eddy.base.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.eddy.base.R;
import com.eddy.base.event.EventMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

/**
 * 1）EventBus封装：
 * -        isBindEventBusHere()
 * -        onEventComming(EventMsg eventMsg)
 * -        postEventMsg(EventMsg eventMsg)
 * -
 * 2）Activity进入与退出动画封装
 * -        toggleOverridePendingTransition()
 * -        TransitionMode getOverridePendingTransitionMode()
 * -
 * 3）Activity Extra封装
 * -        getStringFromExtra(String key)
 * -        getIntFromExtra(String key, int defaultValue)
 * -        getLongFromExtra(String key, long defaultValue)
 * -        getBooleanFromExtra(String key, boolean defaultValue)
 * -        getSerializeFromExtra(String key)
 * -        getParcelableFromExtra(String key)
 * -
 * 4）Activity Toast提示封装
 * -        showToast(String msg)
 * -
 * 5）Activity
 * -        readyGo(Class<?> clazz)
 * -        readyGo(Class<?> clazz, Bundle bundle)
 * -        readyGoThenKill(Class<?> clazz) {
 * -        readyGoThenKill(Class<?> clazz, Bundle bundle)
 * -        readyGoForResult(Class<?> clazz, int requestCode)
 * -        readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle)
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {

    /**
     * context
     */
    protected Context mContext = null;

    /**
     * overridePendingTransition mode
     */
    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
        super.onCreate(savedInstanceState);
        // base setup
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            getBundleExtras(extras);
        }

        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }

        mContext = this;
        BaseAppManager.getInstance().addActivity(this);

        initViewsAndEvents(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @CallSuper
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = (Toolbar) findViewById(R.id.common_toolbar);
    }

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().finishActivity(this);
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * get bundle data
     *
     * @param extras
     */
    protected void getBundleExtras(Bundle extras) {

    }

    /**
     * init all views and add events
     */
    protected abstract void initViewsAndEvents(Bundle savedInstanceState);

    /**
     * toggle overridePendingTransition
     *
     * @return
     */
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    /**
     * get the overridePendingTransition mode
     */
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * show toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        //防止遮盖虚拟按键
//        if (null != msg && !TextUtils.isEmpty(msg)) {
//            Snackbar.make(getLoadingTargetView(), msg, Snackbar.LENGTH_SHORT).show();
//        }
    }

    protected String getStringFromExtra(String key) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(key))
            return intent.getStringExtra(key);
        return null;
    }

    protected int getIntFromExtra(String key, int defaultValue) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(key))
            return intent.getIntExtra(key, defaultValue);
        return defaultValue;
    }

    protected long getLongFromExtra(String key, long defaultValue) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(key))
            return intent.getLongExtra(key, defaultValue);
        return defaultValue;
    }

    protected boolean getBooleanFromExtra(String key, boolean defaultValue) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(key))
            return intent.getBooleanExtra(key, defaultValue);
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Serializable> T getSerializeFromExtra(String key) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(key))
            return (T) intent.getSerializableExtra(key);
        return null;
    }


    @SuppressWarnings("unchecked")
    protected <T extends Parcelable> T getParcelableFromExtra(String key) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(key))
            return (T) intent.getParcelableExtra(key);
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEventBus(EventMsg eventMsg) {
        if (null != eventMsg) {
            onEventComming(eventMsg);
        }
    }

    /**
     * 要支持EventBus,请返回true，并覆写onEventComming()
     *
     * @return
     */
    protected boolean isBindEventBusHere() {
        return false;
    }

    protected void onEventComming(EventMsg eventMsg) {

    }

    public void postEventMsg(EventMsg eventMsg) {
        EventBus.getDefault().post(eventMsg);
    }
}
