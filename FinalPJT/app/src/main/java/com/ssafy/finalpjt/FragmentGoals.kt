package com.ssafy.finalpjt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.finalpjt.adapter.FragmentGoalsPagerAdapter

class FragmentGoals : Fragment() {

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

        val dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
        val mainQuestList: Array<String> = dbHelper.MainQuest().split("\n").toTypedArray()
        val _id: Array<String> = dbHelper.addedByUser().split("\n").toTypedArray()

        val tabLayout: TabLayout = view.findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager2: ViewPager2 = view.findViewById<View>(R.id.viewpager) as ViewPager2
        viewPager2.apply {
            adapter = FragmentGoalsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, _id, mainQuestList)
        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = mainQuestList[position]
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