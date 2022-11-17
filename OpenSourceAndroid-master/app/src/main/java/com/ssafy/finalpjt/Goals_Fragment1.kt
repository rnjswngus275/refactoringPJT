package com.ssafy.finalpjt

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

class Goals_Fragment1 constructor() : Fragment() {
    var main: Long? = null
    var main_: String? = null
    var sub_goal_1: TextView? = null
    var sub_list: LinearLayout? = null
    var sleep: TextView? = null
    var sleep2: TextView? = null
    var sleep3: TextView? = null
    var count: Int = 0
    var percent: Int = 0
    fun getInstance(str: String, main: String?) {
        this.main = str.toLong()
        main_ = main
    }

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.goals_fragment1, container, false)
        val view2: View = inflater.inflate(R.layout.goals_holder, container, false)
        val linear: LinearLayout = view2.findViewById(R.id.linear)
        val dbHelper: DBHelper = DBHelper(view.getContext(), "QuestApp.db", null, 1)
        val str: Array<String> = dbHelper.SubQuest(main).split("\n").toTypedArray()
        for (a in str.indices) {
            Log.e("sub", "" + str.get(a))
        }
        val textView: TextView = view.findViewById<View>(R.id.percent_num) as TextView
        val progress: ProgressBar = view.findViewById<View>(R.id.progress) as ProgressBar
        progress.setProgress(dbHelper.selectRate(main_))
        textView.setText(dbHelper.selectRate(main_).toString() + "%")
        for (i in str.indices) {
            val goals_holder: Goals_Sub = Goals_Sub(getContext())
            val sub_list: LinearLayout = view.findViewById<View>(R.id.sub_list) as LinearLayout
            sub_list.addView(goals_holder)
            val tv: TextView = goals_holder.findViewById(R.id.holder_text)
            val check: CheckBox = goals_holder.findViewById(R.id.checkBox)
            if (dbHelper.selectRate(main_) >= (((i + 1).toDouble() / str.size.toDouble()) * 100).toInt()) {
                check.setChecked(true)
                count++
            }
            val finalI: Int = i
            check.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                public override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
                    if (b) {
                        count++
                    } else count--
                    percent = ((count.toDouble() / str.size.toDouble()) * 100).toInt()
                    dbHelper.updateRate(main_, percent)
                    Log.e("progress", "percent: " + percent)
                    progress.setProgress(dbHelper.selectRate(main_))
                    textView.setText(percent.toString() + "%")
                }
            })
            Log.e("str", "onCreateView: " + str.get(i))
            tv.setText(str.get(i))
        }
        return view
    }
}