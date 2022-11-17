package com.ssafy.finalpjt.db.factory

import android.content.Context
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

object GoalDAOFactory {
    @Throws(Exception::class)
    fun addGoal(context: Context, goal: Goal): Long {
        //db에 최종목표를 추가 시키는 함수이다.
        var pass: Long = -1
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalDAO = GoalDAO(context, dbHelper!!.writableDatabase)
            pass = goalDAO.addDate(goal)
            L.e("::::add success : $pass")
        } catch (e: Exception) {
            L.e(":::::GoalDAOFactory addGoal Exception")
        } finally {
            dbHelper?.close()
        }
        return pass
    }

    @Throws(Exception::class)
    fun updateGoal(context: Context, goal: Goal?): Boolean {
        //db에 최종목표를 수정하는 함수이다
        var result = false
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalDAO = GoalDAO(context, dbHelper!!.writableDatabase)
            result = goalDAO.updateGoal(goal)
            L.e("::::delete success : $result")
        } catch (e: Exception) {
            L.e(":::::GoalDAOFactory updateGoal Exception")
        } finally {
            dbHelper?.close()
        }
        return result
    }

    @Throws(Exception::class)
    fun removeGoal(context: Context, goal: Goal): Boolean {
        //db에 최종목표를 추가 시키는 함수이다.
        var result = false
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalDAO = GoalDAO(context, dbHelper!!.writableDatabase)
            result = goalDAO.removeGoal(goal)
            L.e("::::delete success : $result")
        } catch (e: Exception) {
            L.e(":::::GoalDAOFactory removeGoal Exception")
        } finally {
            dbHelper?.close()
        }
        return result
    }

    @Throws(Exception::class)
    fun getGoalList(context: Context): List<Goal?>? {
        //db에 있는 최종 목표 리스트를 호출하는 함수이다.
        var list: List<Goal?>? = null
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalDAO = GoalDAO(context, dbHelper!!.writableDatabase)
            list = goalDAO.goalList
        } catch (e: Exception) {
            L.e(":::::GoalDAOFactory getGoalList Exception")
        } finally {
            dbHelper?.close()
        }
        return list
    }
}