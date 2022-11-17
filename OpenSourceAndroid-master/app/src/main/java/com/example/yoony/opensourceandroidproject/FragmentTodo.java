package com.example.yoony.opensourceandroidproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Calendar;

public class FragmentTodo extends Fragment {
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private static final String KEY_NUMBER = "KEY_NUMBER";
    Calendar cal = Calendar.getInstance();
    int thisDay = cal.get(Calendar.DAY_OF_MONTH);
    int thisMonth = cal.get(Calendar.MONTH) + 1;
    int date = (thisMonth * 100) + thisDay;
    int fragmentPageNow = 0;
    String[] Months = {"1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"};
    LinearLayout tabLayout, btnLayout;
    Spinner monthSpinner;
    Button addBtn;
    ImageButton addListBtn;
    HorizontalScrollView btnScroll;
    private int mNumber = 0;
    private FragmentManager.OnBackStackChangedListener mListner = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            FragmentManager fragmentManager = getFragmentManager();
            int count = 0;
            for (Fragment f : fragmentManager.getFragments()) {
                if (f != null) {
                    count++;
                }
            }
            mNumber = count;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_todo, null);
        tabLayout = (LinearLayout) view.findViewById(R.id.tabLayout);
        btnLayout = (LinearLayout) view.findViewById(R.id.btnLayout);
        monthSpinner = (Spinner) view.findViewById(R.id.monthSpinner);


        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(mListner);
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        Log.d("MainActivity", "onCreate fragment =" + fragment);
        if (savedInstanceState == null) {//초기 프레그먼트 생성
            Log.d("enterIf", "savedInstanceState=null");
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, MyFragment.getInstace(date), FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
            fragmentPageNow = thisDay;
        }

        final LinearLayout tabWidgetLayout = (LinearLayout) view.findViewById(R.id.tabWidget);
        btnScroll = view.findViewById(R.id.btnScroll);

        createBtn(view, tabWidgetLayout);

        ArrayAdapter adapter = new ArrayAdapter(view.getContext(), R.layout.spin, Months);
        adapter.setDropDownViewResource(R.layout.spin_dropdown);

        monthSpinner.setAdapter(adapter);
        monthSpinner.setSelection(thisMonth - 1);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("month", "" + thisMonth);
                thisMonth = position + 1;
                date = (thisMonth * 100) + thisDay;
                Log.e("newMonth", "" + thisMonth);
                Log.e("dayofmonth", "" + cal.getActualMaximum(cal.DAY_OF_MONTH));
                cal.set(Calendar.YEAR, thisMonth - 1, thisDay);
                Log.e("dayofmonth", "" + cal.getActualMaximum(cal.DAY_OF_MONTH));
                tabWidgetLayout.removeAllViews();
                createBtn(view, tabWidgetLayout);
                callFragment(date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addListBtn = (ImageButton) view.findViewById(R.id.addListBtn);
        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick", "clicked");
                AddListFragment alFragment = new AddListFragment();
                alFragment.getInstance(fragmentPageNow, tabLayout, btnLayout);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, alFragment).addToBackStack(null).commit();
                tabLayout.setVisibility(View.INVISIBLE);
                btnLayout.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    public void createBtn(View view, LinearLayout tabWidgetLayout) {
        for (int i = 0; i < cal.getActualMaximum(cal.DAY_OF_MONTH); i++) {//해당월의 날짜 수 만큼 버튼 생성
            Button btn = new Button(view.getContext());//버튼 생성
            btn.setText((i + 1) + "일");
            btn.setId((thisMonth * 100) + (i + 1));
            btn.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.day_btn));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentPageNow = (int) btn.getId() % 100;
                    btn.setFocusableInTouchMode(true);
                    btn.requestFocus();
                    Log.e("test", "focus" + btn.isFocused());
                }
            });
            btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        callFragment(v.getId());
                        Log.e("left", "position" + btn.getLeft());
                        btnScroll.scrollTo(btn.getLeft() - 231, 0);
                        v.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.selecte_day_btn));
                        btn.setTextColor(Color.WHITE);
                    } else {
                        btn.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.day_btn));
                        btn.setTextColor(Color.BLACK);
                    }
                }
            });
            tabWidgetLayout.addView(btn);
        }
        Button todayBtn = tabWidgetLayout.findViewById(date);
        todayBtn.setLeft(231 * (date % 100 - 1));
        todayBtn.setFocusableInTouchMode(true);
        todayBtn.requestFocus();
    }

    @Override
    public void onDestroy() {//종료시 백스택리스너 삭제
        super.onDestroy();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.removeOnBackStackChangedListener(mListner);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {//홈버튼 누를때 상태 저장
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_NUMBER, mNumber);
    }

    private void callFragment(int fragment_no) {//프래그먼트 전환
        int i = fragment_no;
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, MyFragment.getInstace(i)).addToBackStack(null).commit();

    }
}