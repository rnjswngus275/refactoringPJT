package com.ssafy.finalpjt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
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
import java.lang.Exception
import java.util.ArrayList

class DetailActivityUpdate constructor() : AppCompatActivity() {
    private var LL: LinearLayout? = null
    private var etGoal: EditText? = null
    private var btnUpdate: Button? = null
    private var mCurrentGoalItem: Goal? = null
    private val mSubItemViewList: ArrayList<SubItemView> = ArrayList()
    private var mCurrentGoalSubList: List<GoalSub?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_update)
        LL = findViewById(R.id.LL)
        etGoal = findViewById(R.id.etGoal)
        btnUpdate = findViewById(R.id.btn_update_complete)
        val intent: Intent = getIntent()
        mCurrentGoalItem = intent.getSerializableExtra("EXTRA_GOAL") as Goal?
        onGoalSubDataLoad(
            mCurrentGoalItem.getGoalTitle(),
            mCurrentGoalItem.getIndexNumber().toString()
        )
        btnUpdate.setOnClickListener(View.OnClickListener({ view: View? ->
            try {
                for (i in mSubItemViewList.indices) {
                    val goalSub: GoalSub? = mCurrentGoalSubList!!.get(i)
                    goalSub.setSubTitle(mSubItemViewList.get(i).etInput!!.getText().toString())
                    GoalSubDAOFactory.updateGoalSub(getApplicationContext(), goalSub)
                }
                mCurrentGoalItem.setGoalTitle(etGoal.getText().toString())
                GoalDAOFactory.updateGoal(getApplicationContext(), mCurrentGoalItem)
                setResult(RESULT_OK)
                finish()
            } catch (e: Exception) {
            }
        }))
    }

    val childView: View
        get() {
            val childView: View = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.item_sub_goal_holder, null)
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.weight = 1f
            childView.setLayoutParams(params)
            return childView
        }

    private fun addSubView(goalSub: GoalSub?, pos: Int) {
        val childView: View = childView
        val etSubGoal: EditText = childView.findViewById<View>(R.id.EditText2) as EditText
        etSubGoal.setText(goalSub.getSubTitle())
        mSubItemViewList.add(SubItemView(etSubGoal))
        LL!!.addView(childView)
    }

    private fun onGoalSubDataLoad(title: String?, addedByUser: String) {
        etGoal!!.setText(title)
        //쓰레드를 이용하여 데이터를 호출한다.. 데이터호출로직은 백그라운드에서 처리되어야한다.
        val task: GoalSubDataTask? =
            GoalSubDataTask.Builder().setFetcher(object : GoalSubDataTask.DataFetcher {
                //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                override val data: List<GoalSub?>?
                    get() {
                        try {
                            //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                            return GoalSubDAOFactory.getGoalSubList(
                                getApplicationContext(),
                                addedByUser
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return null
                        }
                    }
            }).setCallback(object : GoalSubDataTask.TaskListener {
                public override fun onComplete(data: List<GoalSub?>?) {
                    L.i("::::[onGoalSubDataLoad] CallBack " + data)
                    if (data != null) {
                        var index: Int = 0
                        mCurrentGoalSubList = data
                        for (goalSub: GoalSub? in data) {
                            L.i("::::goalSub : " + goalSub.toString())
                            addSubView(goalSub, index)
                            index += 1
                        }
                    }
                }
            }).build()
        task!!.execute()
    }
}