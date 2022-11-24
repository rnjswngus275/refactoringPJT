package com.ssafy.finalpjt.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.fragment.FragmentMyGoalsItem

private const val TAG = "FragmentGoalsPagerAdapt"
class FragmentGoalsPagerAdapter(
     fm: FragmentManager, //tab의 갯수
     lifecycle: Lifecycle,
) : FragmentStateAdapter(fm, lifecycle) {

    var id= arrayListOf<Long>()
    var mainQuestList= mutableListOf<Goal>()
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }
    
    override fun getItemCount(): Int {
        return id.size
    }

    override fun createFragment(position: Int): Fragment {

        for(i in id){
                return FragmentMyGoalsItem(id[position])
        }

        return FragmentMyGoalsItem(1)
    }
}