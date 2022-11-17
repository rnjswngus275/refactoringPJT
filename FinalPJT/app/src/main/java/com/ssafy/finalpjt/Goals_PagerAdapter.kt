package com.ssafy.finalpjt

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Goals_PagerAdapter constructor(
    fm: FragmentManager?, //tab의 갯수
    var mNumOfTabs: Int, var str: Array<String>, var main_name: Array<String>
) : FragmentPagerAdapter(fm!!) {
    public override fun getItem(position: Int): Fragment {
        Log.e("position", "" + position)
        val tab1: FragmentGoalsItem = FragmentGoalsItem()
        tab1.getInstance(str.get(position), main_name.get(position))
        return tab1
    }

    public override fun getCount(): Int {
        return mNumOfTabs
    }
}