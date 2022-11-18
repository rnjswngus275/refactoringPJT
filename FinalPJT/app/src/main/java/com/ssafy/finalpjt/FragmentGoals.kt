package com.ssafy.finalpjt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

class FragmentGoals constructor() : Fragment() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_goals, container, false)
        val view1: View = inflater.inflate(R.layout.content_main, container, false)
        super.onCreate(savedInstanceState)
        val dbHelper: DBHelper = DBHelper(view.getContext(), "QuestApp.db", null, 1)
        val str: Array<String> = dbHelper.MainQuest().split("\n").toTypedArray()
        val _id: Array<String> = dbHelper.addedByUser().split("\n").toTypedArray()

        //TabLayout
        val tabs: TabLayout = view.findViewById<View>(R.id.tabs) as TabLayout
        for (a in str.indices) {
            Log.e("sangeun", "added: " + str.get(a))
            dbHelper.setRate(str.get(a), 0)
            tabs.addTab(tabs.newTab().setText(str.get(a)))
        }
        //        tabs.addTab(tabs.newTab().setText(str[0]));
//        tabs.addTab(tabs.newTab().setText(str[1]));
//        tabs.addTab(tabs.newTab().setText(str[2]));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL)

        //어답터설정
        val viewPager: ViewPager = view.findViewById<View>(R.id.viewpager) as ViewPager
        Log.e("str.length", "" + str.size)
        val myPagerAdapter: Goals_PagerAdapter =
            Goals_PagerAdapter(getChildFragmentManager(), str.size, _id, str)
        viewPager.setAdapter(myPagerAdapter)

        //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
        tabs.addOnTabSelectedListener(ViewPagerOnTabSelectedListener(viewPager))
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabs))
        if (dbHelper.MainQuest() === "") {
            return view1
        } else {
            return view
        }
    }
}