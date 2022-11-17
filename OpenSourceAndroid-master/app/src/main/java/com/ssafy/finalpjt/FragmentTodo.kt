package com.ssafy.finalpjt

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ssafy.finalpjt.db.model.Goal
import com.ssafy.finalpjt.db.DBConst.GoalTable
import com.ssafy.finalpjt.L
import com.ssafy.finalpjt.db.model.GoalSub
import com.ssafy.finalpjt.db.DBConst.SubGoalTable
import kotlin.Throws
import com.ssafy.finalpjt.db.DbHelper
import com.ssafy.finalpjt.db.dao.GoalDAO
import com.ssafy.finalpjt.db.dao.GoalSubDAO
import kotlin.jvm.Synchronized
import com.ssafy.finalpjt.db.CommonDAO
import com.ssafy.finalpjt.db.DBConst
import com.ssafy.finalpjt.db.BaseAsyncTask
import com.ssafy.finalpjt.db.GoalDataTask
import kotlin.jvm.Volatile
import com.ssafy.finalpjt.db.BaseAsyncTask.SerialExecutor
import com.ssafy.finalpjt.db.GoalSubDataTask
import com.ssafy.finalpjt.DBHelper
import com.ssafy.finalpjt.MainActivity
import com.ssafy.finalpjt.SampleData
import com.ssafy.finalpjt.MyFragment.MyAdapter
import com.ssafy.finalpjt.MyFragment
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.db.factory.GoalDAOFactory
import com.ssafy.finalpjt.db.factory.GoalSubDAOFactory
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.FragmentMain.RecyclerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.DetailActivity
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import com.ssafy.finalpjt.AddActivity
import com.ssafy.finalpjt.FragmentMain.RecyclerAdapter.ItemViewHolder
import androidx.cardview.widget.CardView
import com.ssafy.finalpjt.FragmentShop.SingerAdapter
import com.ssafy.finalpjt.SingerShopItem
import com.ssafy.finalpjt.SingerViewer
import com.ssafy.finalpjt.FragmentTodo
import com.ssafy.finalpjt.AddListFragment
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationView
import com.ssafy.finalpjt.MainActivity.AlarmHATT
import com.ssafy.finalpjt.FragmentMain
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ssafy.finalpjt.FragmentGoals
import com.ssafy.finalpjt.FragmentShop
import com.ssafy.finalpjt.FragmentSetting
import com.ssafy.finalpjt.BroadcastD
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import com.ssafy.finalpjt.Goals_PagerAdapter
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.ssafy.finalpjt.DetailActivityUpdate
import com.ssafy.finalpjt.Goals_Sub
import androidx.fragment.app.FragmentPagerAdapter
import com.ssafy.finalpjt.Goals_Fragment1
import java.util.*

class FragmentTodo constructor() : Fragment() {
    var cal: Calendar = Calendar.getInstance()
    var thisDay: Int = cal.get(Calendar.DAY_OF_MONTH)
    var thisMonth: Int = cal.get(Calendar.MONTH) + 1
    var date: Int = (thisMonth * 100) + thisDay
    var fragmentPageNow: Int = 0
    var Months: Array<String?> =
        arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
    var tabLayout: LinearLayout? = null
    var btnLayout: LinearLayout? = null
    var monthSpinner: Spinner? = null
    var addBtn: Button? = null
    var addListBtn: ImageButton? = null
    var btnScroll: HorizontalScrollView? = null
    private var mNumber: Int = 0
    private val mListner: FragmentManager.OnBackStackChangedListener =
        object : FragmentManager.OnBackStackChangedListener {
            public override fun onBackStackChanged() {
                val fragmentManager: FragmentManager? = getFragmentManager()
                var count: Int = 0
                for (f: Fragment? in fragmentManager!!.getFragments()) {
                    if (f != null) {
                        count++
                    }
                }
                mNumber = count
            }
        }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.today_todo, null)
        tabLayout = view.findViewById<View>(R.id.tabLayout) as LinearLayout?
        btnLayout = view.findViewById<View>(R.id.btnLayout) as LinearLayout?
        monthSpinner = view.findViewById<View>(R.id.monthSpinner) as Spinner?
        val fragmentManager: FragmentManager? = getFragmentManager()
        fragmentManager!!.addOnBackStackChangedListener(mListner)
        val fragment: Fragment? = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
        Log.d("MainActivity", "onCreate fragment =" + fragment)
        if (savedInstanceState == null) { //초기 프레그먼트 생성
            Log.d("enterIf", "savedInstanceState=null")
            fragmentManager.beginTransaction()
                .add(R.id.fragment_container, MyFragment.Companion.getInstace(date), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
            fragmentPageNow = thisDay
        }
        val tabWidgetLayout: LinearLayout = view.findViewById<View>(R.id.tabWidget) as LinearLayout
        btnScroll = view.findViewById(R.id.btnScroll)
        createBtn(view, tabWidgetLayout)
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(view.getContext(), R.layout.spin, Months)
        adapter.setDropDownViewResource(R.layout.spin_dropdown)
        monthSpinner!!.setAdapter(adapter)
        monthSpinner!!.setSelection(thisMonth - 1)
        monthSpinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            public override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.e("month", "" + thisMonth)
                thisMonth = position + 1
                date = (thisMonth * 100) + thisDay
                Log.e("newMonth", "" + thisMonth)
                Log.e("dayofmonth", "" + cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.YEAR, thisMonth - 1, thisDay)
                Log.e("dayofmonth", "" + cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                tabWidgetLayout.removeAllViews()
                createBtn(view, tabWidgetLayout)
                callFragment(date)
            }

            public override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        addListBtn = view.findViewById<View>(R.id.addListBtn) as ImageButton?
        addListBtn!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                Log.d("onclick", "clicked")
                val alFragment: AddListFragment = AddListFragment()
                alFragment.getInstance(fragmentPageNow, tabLayout, btnLayout)
                fragmentManager.beginTransaction().replace(R.id.fragment_container, alFragment)
                    .addToBackStack(null).commit()
                tabLayout!!.setVisibility(View.INVISIBLE)
                btnLayout!!.setVisibility(View.INVISIBLE)
            }
        })
        return view
    }

    fun createBtn(view: View, tabWidgetLayout: LinearLayout) {
        for (i in 0 until cal.getActualMaximum(Calendar.DAY_OF_MONTH)) { //해당월의 날짜 수 만큼 버튼 생성
            val btn: Button = Button(view.getContext()) //버튼 생성
            btn.setText((i + 1).toString() + "일")
            btn.setId((thisMonth * 100) + (i + 1))
            btn.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.day_btn))
            btn.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View) {
                    fragmentPageNow = btn.getId() % 100
                    btn.setFocusableInTouchMode(true)
                    btn.requestFocus()
                    Log.e("test", "focus" + btn.isFocused())
                }
            })
            btn.setOnFocusChangeListener(object : View.OnFocusChangeListener {
                public override fun onFocusChange(v: View, hasFocus: Boolean) {
                    if (hasFocus) {
                        callFragment(v.getId())
                        Log.e("left", "position" + btn.getLeft())
                        btnScroll!!.scrollTo(btn.getLeft() - 231, 0)
                        v.setBackground(
                            ContextCompat.getDrawable(
                                view.getContext(),
                                R.drawable.selecte_day_btn
                            )
                        )
                        btn.setTextColor(Color.WHITE)
                    } else {
                        btn.setBackground(
                            ContextCompat.getDrawable(
                                view.getContext(),
                                R.drawable.day_btn
                            )
                        )
                        btn.setTextColor(Color.BLACK)
                    }
                }
            })
            tabWidgetLayout.addView(btn)
        }
        val todayBtn: Button = tabWidgetLayout.findViewById(date)
        todayBtn.setLeft(231 * (date % 100 - 1))
        todayBtn.setFocusableInTouchMode(true)
        todayBtn.requestFocus()
    }

    public override fun onDestroy() { //종료시 백스택리스너 삭제
        super.onDestroy()
        val fragmentManager: FragmentManager? = getFragmentManager()
        fragmentManager!!.removeOnBackStackChangedListener(mListner)
    }

    public override fun onSaveInstanceState(outState: Bundle) { //홈버튼 누를때 상태 저장
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_NUMBER, mNumber)
    }

    private fun callFragment(fragment_no: Int) { //프래그먼트 전환
        val i: Int = fragment_no
        val fragmentManager: FragmentManager? = getFragmentManager()
        fragmentManager!!.beginTransaction()
            .replace(R.id.fragment_container, MyFragment.Companion.getInstace(i))
            .addToBackStack(null).commit()
    }

    companion object {
        private val FRAGMENT_TAG: String = "FRAGMENT_TAG"
        private val KEY_NUMBER: String = "KEY_NUMBER"
    }
}