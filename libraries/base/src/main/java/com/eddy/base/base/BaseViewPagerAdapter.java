package com.eddy.base.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by eddyli on 2016/7/21.
 */
public abstract class BaseViewPagerAdapter extends FragmentPagerAdapter {

    public BaseViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return getFragments()[position];
    }

    @Override
    public int getCount() {
        return getFragments().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTitles()[position];
    }

    public abstract Fragment[] getFragments();

    public abstract CharSequence[] getTitles();
}
