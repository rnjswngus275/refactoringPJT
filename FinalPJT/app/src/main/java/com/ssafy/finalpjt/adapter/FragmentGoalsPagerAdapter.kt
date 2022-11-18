package com.ssafy.finalpjt.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssafy.finalpjt.FragmentGoalsItem

class FragmentGoalsPagerAdapter(
    fm: FragmentManager, //tab의 갯수
    lifecycle: Lifecycle,
    var id: Array<String>,
    var mainQuestList: Array<String>
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return id.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragmentGoalsItem = FragmentGoalsItem()
        fragmentGoalsItem.getInstance(id[position], mainQuestList[position])
        return fragmentGoalsItem
    }
}