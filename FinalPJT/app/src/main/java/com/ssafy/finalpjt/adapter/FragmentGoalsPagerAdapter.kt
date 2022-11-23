package com.ssafy.finalpjt.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.fragment.FragmentMyGoalsItem

private const val TAG = "FragmentGoalsPagerAdapt"
class FragmentGoalsPagerAdapter(
    fm: FragmentManager, //tab의 갯수
    lifecycle: Lifecycle,
    var id: ArrayList<Long>,
    var mainQuestList: MutableList<Goal>
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return id.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d(TAG, "createFragment: ")
        val fragmentMyGoalsItem = FragmentMyGoalsItem()
        fragmentMyGoalsItem.getInstance(id[position], mainQuestList[position].GoalTitle)
        return fragmentMyGoalsItem
    }
}