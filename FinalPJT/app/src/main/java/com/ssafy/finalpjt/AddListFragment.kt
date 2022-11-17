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

class AddListFragment constructor() : Fragment() {
    var addBtn: Button? = null
    var equest: Spinner? = null
    var ejob: EditText? = null
    var edate: DatePicker? = null
    var prevPage: Int = 0
    var t: LinearLayout? = null
    var b: LinearLayout? = null
    fun getInstance(prevPage: Int, t: LinearLayout?, b: LinearLayout?) {
        this.prevPage = prevPage
        this.t = t
        this.b = b
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Bundle? = getArguments()
    }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.addlist_layout, null)
        addBtn = view.findViewById<View>(R.id.addBtn) as Button?
        equest = view.findViewById<View>(R.id.spinner1) as Spinner?
        edate = view.findViewById<View>(R.id.date) as DatePicker?
        ejob = view.findViewById<View>(R.id.job) as EditText?
        val dbHelper: DBHelper = DBHelper(view.getContext(), "QuestApp.db", null, 1)
        val questdata: Array<String?> = dbHelper.MainQuest().split("\n").toTypedArray()
        Log.e("quest", questdata.toString())
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(view.getContext(), R.layout.spin, questdata)
        adapter.setDropDownViewResource(R.layout.spin_dropdown)
        equest!!.setAdapter(adapter)
        addBtn!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val quest: String = equest!!.getSelectedItem().toString()
                val date: Int = ((edate!!.getMonth() + 1) * 100) + edate!!.getDayOfMonth()
                val job: String = ejob!!.getText().toString()
                dbHelper.insert(job, date, quest)
                val fragmentManager: FragmentManager? = getFragmentManager()
                fragmentManager!!.beginTransaction()
                    .replace(R.id.fragment_container, MyFragment.Companion.getInstace(prevPage))
                    .addToBackStack(null).commit() //이전 프레그먼트로 돌아가야함
                b!!.setVisibility(View.VISIBLE)
                t!!.setVisibility(View.VISIBLE)
            }
        })
        return view
    }
}