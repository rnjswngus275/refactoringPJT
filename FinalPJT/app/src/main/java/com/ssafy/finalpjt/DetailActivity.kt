package com.ssafy.finalpjt

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
import com.gun0912.tedonactivityresult.model.ActivityResult
import com.ssafy.finalpjt.Goals_Fragment1
import io.reactivex.functions.Consumer
import java.lang.Exception
import java.util.ArrayList

class DetailActivity constructor() : AppCompatActivity() {
    var LL: LinearLayout? = null
    var tvGoal: TextView? = null
    var btnUpdate: Button? = null
    private val mSubItemViewList: ArrayList<SubItemView> = ArrayList()
    private var mCurrentGoalSubList: List<GoalSub?>? = null
    private var mCurrentGoalItem: Goal? = null
    private val deleteClickListener: View.OnLongClickListener = object : View.OnLongClickListener {
        public override fun onLongClick(view: View): Boolean {
            L.i(":::이벤트 발생.. " + view.getTag())
            try {
                val pos: Int = view.getTag() as Int
                val isDelete: Boolean = GoalSubDAOFactory.removeGoalSub(
                    getApplicationContext(),
                    mCurrentGoalSubList!!.get(pos)
                )
                if (isDelete) {
                    if (mCurrentGoalSubList != null && mCurrentGoalSubList!!.size > 0) {
                        mCurrentGoalSubList.removeAt(pos)
                    }
                    LL!!.removeAllViews()
                    onGoalSubDataLoad(
                        mCurrentGoalItem.getGoalTitle(),
                        mCurrentGoalItem.getIndexNumber().toString()
                    )
                }
            } catch (e: Exception) {
            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val intent: Intent = getIntent()
        mCurrentGoalItem = intent.getSerializableExtra("EXTRA_GOAL") as Goal?
        if (mCurrentGoalItem == null) return
        tvGoal = findViewById(R.id.tvGoal)
        btnUpdate = findViewById(R.id.btnRe)
        LL = findViewById(R.id.LL)
        onGoalSubDataLoad(
            mCurrentGoalItem.getGoalTitle(),
            mCurrentGoalItem.getIndexNumber().toString()
        )
        findViewById<View>(R.id.btnDone).setOnClickListener(View.OnClickListener({ view: View? -> finish() }))
        btnUpdate.setOnClickListener(View.OnClickListener({ view: View? ->
            //수정화면으로 전환...
            val updateIntent: Intent = Intent(this, DetailActivityUpdate::class.java)
            updateIntent.putExtra("EXTRA_GOAL", mCurrentGoalItem)
            TedRxOnActivityResult.with(this@DetailActivity)
                .startActivityForResult(updateIntent)
                .subscribe(Consumer({ activityResult: ActivityResult ->
                    if (activityResult.getResultCode() == RESULT_OK) {
                        //수정 화면에서 수정완료 버튼을 누를시. 해당 화면은 finish 처리한다.
                        setResult(RESULT_OK)
                        finish()
                    }
                }), Consumer({ error: Throwable? -> })
                )
        }))
    }

    val childView: View
        get() {
            val childView: View = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.item_sub_goal_holder_detail, null)
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
        val tvSubGoal: TextView = childView.findViewById<View>(R.id.tv_title) as TextView
        tvSubGoal.setText(goalSub.getSubTitle())
        tvSubGoal.setTag(pos)
        tvSubGoal.setOnLongClickListener(deleteClickListener)
        mSubItemViewList.add(SubItemView(tvSubGoal))
        LL!!.addView(childView)
    }

    private fun onGoalSubDataLoad(title: String?, addedByUser: String) {
        tvGoal!!.setText(title)
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
                            addSubView(goalSub, index)
                            index += 1
                        }
                    }
                }
            }).build()
        task!!.execute()
    }
}