package com.ssafy.finalpjt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.adapter.FragmentGoalsPagerAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.repository.GoalRepository

class FragmentGoals : Fragment() {
    private lateinit var goalRepository: GoalRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        goalRepository=GoalRepository.get()

        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mainQuestList= mutableListOf<Goal>()
        goalRepository.getAllGoals().observe(viewLifecycleOwner){
            mainQuestList=it
        }
            val _id= ArrayList<Long> ()
        for(i in mainQuestList){
            _id.add(i.id)
        }

        val tabLayout: TabLayout = view.findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager2: ViewPager2 = view.findViewById<View>(R.id.viewpager) as ViewPager2
        viewPager2.apply {
            adapter = FragmentGoalsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, _id, mainQuestList)
        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = mainQuestList[position].GoalTitle
        }
    }
}

//        val myPagerAdapter: FragmentGoalsPagerAdapter =
//            FragmentGoalsPagerAdapter(getChildFragmentManager(), str.size, _id, str)
//        viewPager.setAdapter(myPagerAdapter)
//
//        //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
//        tabs.addOnTabSelectedListener(ViewPagerOnTabSelectedListener(viewPager))
//        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabs))
//        if (dbHelper.MainQuest() === "") {
//            return contentView
//        } else {
//            return view
//        }