package com.example.yoony.opensourceandroidproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class Goals_PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs; //tab의 갯수
    String[] str;
    String[] main_name;

    public Goals_PagerAdapter(FragmentManager fm, int numOfTabs, String[] str, String[] main_name) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
        this.str = str;
        this.main_name = main_name;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("position", "" + position);
        Goals_Fragment1 tab1 = new Goals_Fragment1();
        tab1.getInstance(str[position], main_name[position]);
        return tab1;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}