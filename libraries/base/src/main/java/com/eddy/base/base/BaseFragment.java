package com.eddy.base.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.eddy.base.BuildConfig;
import com.eddy.base.R;
import com.eddy.base.event.EventMsg;
import com.eddy.base.swipeback.SwipeBackLayout;
import com.eddy.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 创建 ：eddyli@zmodo.com
 * 时间 ：16:09 on 2018/3/2.
 * 描述 ：
 * -    1）Toast提示封装：
 * -        showToast(String msg)
 * -
 * -    2）EventBus：
 * -        isBindEventBusHere()
 * -        onEventComming(EventMsg eventMsg)
 * -        postEventMsg(EventMsg eventMsg)
 * -
 * -    3）Extra数据封装
 * -        stringFromArguments(String key)
 * -        booleanFromArguments(String key, boolean defaultValue)
 * -        intFromArguments(String key, int defaultValue)
 * -        longFromArguments(String key, long defaultValue)
 * -        serializeFromArguments(String key)
 * -        parcelableFromArguments(String key)
 * -        parcelableArrayListFromArguments(String key)
 * -    4）ActionBar管理：
 * -    5）Fragment跳转管理：
 * -
 */

public abstract class BaseFragment extends Fragment implements IComStatus, BaseSwipeBackActivity.FragmentCallback {

    //
    private static final String   KEY_BUNDLE_INSTANCE                    = "key_bundle_instance";
    public static final  int      RESULT_CANCELED                        = 0;
    public static final  int      RESULT_OK                              = -1;
    private              int      mResultCode                            = RESULT_CANCELED;
    private              int      mRequestCode                           = -1;
    private              boolean  mIsAttached                            = false;
    private              int      mStatus                                = INITIAL;
    protected            Activity mContext                               = null;
    private static final String   SWIPEBACKFRAGMENT_STATE_SAVE_IS_HIDDEN = "SWIPEBACKFRAGMENT_STATE_SAVE_IS_HIDDEN";
    private              boolean  mLocking                               = false;
    //
    protected View                  mLayoutView;
    protected Bundle                savedState;
    protected Toolbar               mToolbar;
    private   Intent                mResultData;
    private   Fragment              mRequestFragment;
    private   SwipeBackLayout       mSwipeBackLayout;
    private   Animation             mNoAnim;
    protected OnAddFragmentListener mAddFragmentListener;
    protected ImmersionBar          mImmersionBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddFragmentListener) {
            mAddFragmentListener = (OnAddFragmentListener) context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mIsAttached = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
        mStatus = CREATED;

        if (savedInstanceState != null) {
            boolean             isSupportHidden = savedInstanceState.getBoolean(SWIPEBACKFRAGMENT_STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft              = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }

        mNoAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.no_anim);
        onFragmentCreate();
    }

    private void onFragmentCreate() {
        if (isNeedSwipeBack()) {
            mSwipeBackLayout = new SwipeBackLayout(getActivity());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mSwipeBackLayout.setLayoutParams(params);
            mSwipeBackLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutView == null) {
            mLayoutView = onCreateView(inflater, container);
            initToolbar();
        }
        if (isNeedSwipeBack()) {
            return attachToSwipeBack(mLayoutView);
        } else {
            return mLayoutView;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewsAndEvents();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStatus = CREATED;

        View view = getView();
        initFragmentLayoutBg(view);
        if (view != null) {
            view.setClickable(true);
        }
        // 当栈中只有当前一个Fragment时，禁用Fragment的Swipe特性，使用Activity中的Swipe，防止产生抖动
        if (isNeedSwipeBack() && getSupportFragmentManager().getBackStackEntryCount() == 0
                && mSwipeBackLayout.getEnableValue()) {
            setSwipeBackEnable(false);
        }

        initFragment(savedInstanceState);

        if (!restoreStateFromArguments()) {
            onFirstLaunchFragment(savedInstanceState);
        }
    }

    private void initFragmentLayoutBg(View view) {
        if (view != null && !(view instanceof SwipeBackLayout) && view.getBackground() == null) {
            int background = getWindowBackground();
            view.setBackgroundResource(background);
        } else {
            if (view instanceof SwipeBackLayout) {
                View childView = ((SwipeBackLayout) view).getChildAt(0);
                if (childView != null && childView.getBackground() == null) {
                    int background = getWindowBackground();
                    childView.setBackgroundResource(background);
                }
            }
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (mLocking) {
            return mNoAnim;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    protected int getWindowBackground() {
        TypedArray a          = getActivity().getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int        background = a.getResourceId(0, 0);
        a.recycle();
        return background;
    }

    public boolean isNeedSwipeBack() {
        return true;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public void setSwipeBackEnable(boolean enable) {
        if (null != mSwipeBackLayout)
            mSwipeBackLayout.setEnableGesture(enable);
    }

    public void setLocking(boolean locking) {
        this.mLocking = locking;
    }

    protected abstract View onCreateView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initViewsAndEvents();

    protected abstract void initFragment(Bundle savedInstanceState);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SWIPEBACKFRAGMENT_STATE_SAVE_IS_HIDDEN, isHidden());
        saveStateToArguments();
    }

    private void saveStateToArguments() {
        if (getView() != null) {
            savedState = new Bundle();
            onSaveState(savedState);
        }
        if (savedState != null) {
            Bundle b = getArguments();
            if (null != b)
                b.putBundle(KEY_BUNDLE_INSTANCE, savedState);
        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (null != b) {
            savedState = b.getBundle(KEY_BUNDLE_INSTANCE);
            if (savedState != null) {
                onRestoreState(savedState);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        mStatus = STARTED;
    }

    @Override
    public void onResume() {
        super.onResume();
        mStatus = RUNNING;
    }

    @Override
    public void onPause() {
        super.onPause();
        mStatus = STARTED;
    }

    @Override
    public void onStop() {
        super.onStop();
        mStatus = CREATED;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStatus = DESTROYED;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStatus = DESTROYED;
        if (isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }

        if (mImmersionBar != null)
            mImmersionBar.destroy();

        mContext = null;
        mLayoutView = null;

        super.onDestroyView();
        saveStateToArguments();
    }

    protected View attachToSwipeBack(View view) {
        mSwipeBackLayout.attachToFragment(this, view);
        return mSwipeBackLayout;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && mSwipeBackLayout != null) {
            mSwipeBackLayout.hiddenFragment();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIsAttached = false;
        mAddFragmentListener = null;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    public boolean isFragmentValid() {
        return mIsAttached && mStatus != INITIAL && mStatus != DESTROYED;
    }

    /**
     * get the support fragment manager
     *
     * @return
     */
    protected FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
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
        Intent intent = new Intent(getActivity(), clazz);
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
        if (null != msg && !TextUtils.isEmpty(msg)) {
            Snackbar.make(mContext.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
        }
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


    public void runOnUiThread(Runnable runnable) {
        getActivity().runOnUiThread(runnable);
    }

    public int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public float getDimension(int resId) {
        return getResources().getDimension(resId);
    }

    protected String stringFromArguments(String key) {
        Bundle args = getArguments();
        if (args != null)
            return args.getString(key, null);
        return null;
    }

    protected boolean booleanFromArguments(String key, boolean defaultValue) {
        Bundle args = getArguments();
        if (args != null)
            return args.getBoolean(key, defaultValue);
        return defaultValue;
    }

    protected int intFromArguments(String key, int defaultValue) {
        Bundle args = getArguments();
        if (args != null)
            return args.getInt(key, defaultValue);
        return defaultValue;
    }

    protected long longFromArguments(String key, long defaultValue) {
        Bundle args = getArguments();
        if (args != null)
            return args.getLong(key, defaultValue);
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Serializable> T serializeFromArguments(String key) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(key))
            return (T) args.getSerializable(key);
        return null;
    }

    protected <T extends Parcelable> T parcelableFromArguments(String key) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(key))
            return (T) args.getParcelable(key);
        return null;
    }

    public <T extends Parcelable> ArrayList<T> parcelableArrayListFromArguments(String key) {
        Bundle args = getArguments();
        if (args != null && args.containsKey(key))
            return args.getParcelableArrayList(key);
        return null;
    }

    @CallSuper
    protected void initToolbar() {
        if (null != mLayoutView) {
            mToolbar = findViewById(R.id.common_toolbar);
            if (null != mToolbar && mContext instanceof AppCompatActivity) {
                ((AppCompatActivity) mContext).setSupportActionBar(mToolbar);
                if (isDisplayHomeAdUp()) {
                    ((AppCompatActivity) mContext).getSupportActionBar().setHomeButtonEnabled(true);
                    ((AppCompatActivity) mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }

                if (isInitImmensionBar()) {
                    mImmersionBar = ImmersionBar.with(this);
                    mImmersionBar.titleBar(mToolbar).init();
                }

                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                });
            }
        }
    }

    protected boolean isInitImmensionBar() {
        return true;
    }

    protected boolean isDisplayHomeAdUp() {
        return true;
    }

    protected void finishFragment() {
        if (null != mRequestFragment) {
            mRequestFragment.onActivityResult(mRequestCode, mResultCode, mResultData);
        }
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() <= 1) {
            finishActivity();
        } else {
            fm.popBackStackImmediate();
        }
    }

    protected void setRequestFragmentResult() {
        if (null != mRequestFragment) {
            mRequestFragment.onActivityResult(mRequestCode, mResultCode, mResultData);
        }
    }

    protected void finishActivity() {
        if (null != getActivity()) {
            getActivity().finish();
        }
    }

    protected void onFirstLaunchFragment(Bundle savedInstanceState) {

    }

    protected void onRestoreState(Bundle savedInstanceState) {
        // 此处直接去除保存的值，比如：
        // savedInstanceState.getString("test_name", "test_value");
    }

    protected void onSaveState(Bundle outState) {
        // 此处直接保存需要保存的数据，比如：
        // outState.putString("test_name", "test_default");
    }

    @SuppressWarnings("unchecked")
    public final <T extends View> T findViewById(int id) {
        if (mLayoutView == null)
            return null;
        return (T) mLayoutView.findViewById(id);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }


    public void replaceSelf(Fragment targetFragment, boolean isAddToBackStack) {
        mAddFragmentListener.replaceFragment(targetFragment, isAddToBackStack);
    }

    public void finish() {
        Activity activity = getActivity();
        if (null != activity) {
            activity.finish();
        }
    }

    public void finishWithResult(int resultCode) {
        setResult(resultCode, null);
        finish();
    }

    public void startFragmentForResult(Fragment targetFragment, int requestCode) {
        mAddFragmentListener.startFragment(this, targetFragment, requestCode);
    }

    public void startFragment(Fragment targetFragment) {
        mAddFragmentListener.startFragment(this, targetFragment);
    }

    public void startFragment(Class<? extends Fragment> target) {
        mAddFragmentListener.startFragment(this, target);
    }

    public void startFragment(Class<? extends Fragment> target, Bundle args) {
        mAddFragmentListener.startFragment(this, target, args);
    }

    public void startFragmentForResult(Class<? extends Fragment> target, int requestCode) {
        mAddFragmentListener.startFragment(this, target, requestCode);
    }

    public void startFragmentForResult(Class<? extends Fragment> target, Bundle args, int requestCode) {
        mAddFragmentListener.startFragment(this, target, args, requestCode);
    }

    public final void setResult(int resultCode) {
        setResult(resultCode, null);
    }

    public final void setResult(int resultCode, Intent data) {
        synchronized (this) {
            if (mRequestCode != -1 && mRequestFragment != null) {
                mResultCode = resultCode;
                mResultData = data;
            } else {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.setResult(resultCode, data);
                }
            }
        }
    }


    public void setActbarVisibility(int visibility) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        if (null != appCompatActivity.getSupportActionBar()) {
            if (visibility == View.VISIBLE) {
                appCompatActivity.getSupportActionBar().show();
            } else {
                appCompatActivity.getSupportActionBar().hide();
            }
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setTitle(int resId) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        ActionBar         actionBar         = appCompatActivity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(resId);
        }
    }

    public void setTitle(CharSequence text) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        ActionBar         actionBar         = appCompatActivity.getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(Html.fromHtml(text.toString()).toString());
        }
    }

    public void setRequestFragment(Fragment requestFragment) {
        this.mRequestFragment = requestFragment;
    }

    public void setRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
    }

    public void disableFragmentToolbar() {
        if (null != mToolbar) {
            mToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        logIntent(intent);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        logIntent(intent);
        super.startActivityForResult(intent, requestCode, options);
    }

    private void logIntent(Intent intent) {
        if (BuildConfig.DEBUG) {
            ComponentName componentName = intent.getComponent();
            if (null != componentName) {
                String clazzName = componentName.getClassName();
                if (!TextUtils.isEmpty(clazzName))
                    android.util.Log.d("zmodo", "clazz name = " + clazzName);
            }
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action))
                android.util.Log.d("zmodo", "ACTION = " + action);
        }
    }

    private View mTargetView  = null;
    private View mLoadingView = null;
    private View mEmptyView   = null;

    protected void setLoadingTargetView(View targetView) {
        this.mTargetView = targetView;
        mLoadingView = ((ViewGroup) mTargetView.getParent()).findViewById(R.id.ll_loading);
        mEmptyView = ((ViewGroup) mTargetView.getParent()).findViewById(R.id.tv_empty);
    }

    protected void showEmptyContent(boolean isEmpty) {
        if (isEmpty) {
            mTargetView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mTargetView.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    protected void hideLoading() {
        mTargetView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    protected void showLoading() {
        mTargetView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }
}
