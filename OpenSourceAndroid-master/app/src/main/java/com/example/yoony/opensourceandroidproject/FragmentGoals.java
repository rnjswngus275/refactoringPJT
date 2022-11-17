package com.example.yoony.opensourceandroidproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentGoals extends Fragment {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        View view1 = inflater.inflate(R.layout.content_main, container, false);
        super.onCreate(savedInstanceState);

        DBHelper dbHelper = new DBHelper(view.getContext(), "QuestApp.db", null, 1);
        String[] str = dbHelper.MainQuest().split("\n");
        String[] _id = dbHelper.addedByUser().split("\n");

        //TabLayout
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        for (int a = 0; a < str.length; a++) {
            Log.e("sangeun", "added: " + str[a]);
            dbHelper.setRate(str[a], 0);
            tabs.addTab(tabs.newTab().setText(str[a]));
        }
//        tabs.addTab(tabs.newTab().setText(str[0]));
//        tabs.addTab(tabs.newTab().setText(str[1]));
//        tabs.addTab(tabs.newTab().setText(str[2]));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어답터설정
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        Log.e("str.length", "" + str.length);
        final Goals_PagerAdapter myPagerAdapter = new Goals_PagerAdapter(getChildFragmentManager(), str.length, _id, str);
        viewPager.setAdapter(myPagerAdapter);

        //탭메뉴를 클릭하면 해당 프래그먼트로 변경-싱크화
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));


        if (dbHelper.MainQuest() == "") {
            return view1;
        } else {
            return view;
        }
    }
}
