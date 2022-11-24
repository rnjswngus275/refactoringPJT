package com.ssafy.finalpjt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.adapter.FragmentGoalsPagerAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.viewmodel.FragmentMyGoalsViewModel

class FragmentMyGoals : Fragment() {
    private val viewmodel : FragmentMyGoalsViewModel by viewModels()
    private lateinit var viewPagerAdapter:FragmentGoalsPagerAdapter
    var mainQuestList= mutableListOf<Goal>()
    val goalIdList= ArrayList<Long> ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager2: ViewPager2 = view.findViewById<View>(R.id.viewpager) as ViewPager2

        viewPagerAdapter= FragmentGoalsPagerAdapter(requireActivity().supportFragmentManager, lifecycle)

        val tabLayout: TabLayout = view.findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        viewmodel.getAllGoal().observe(viewLifecycleOwner){
            mainQuestList=it
            viewPagerAdapter.mainQuestList=it
            for(i in it){
                goalIdList.add(i.id)
            }
            viewPagerAdapter.id=goalIdList
            viewPager2.adapter=viewPagerAdapter
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->     //tablayout과 viewpager연결하는게  Mediator
                tab.text = it[position].GoalTitle
            }.attach()
        }
    }
}
