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

object GoalSubDAOFactory {
    @Throws(Exception::class)
    fun addGoalSub(context: Context, goalSub: GoalSub): Boolean {
        //db에 최종목표를 추가 시키는 함수이다.
        var pass = false
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalSubDAO = GoalSubDAO(context, dbHelper!!.writableDatabase)
            pass = goalSubDAO.addDate(goalSub)
            L.e("::::add success : $pass")
        } catch (e: Exception) {
            L.e(":::::GoalSubDAOFactory addGoal Exception")
        } finally {
            dbHelper?.close()
        }
        return pass
    }

    @Throws(Exception::class)
    fun updateGoalSub(context: Context, goalSub: GoalSub?): Boolean {
        //db에 최종목표를 수정하는 함수이다
        var result = false
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalSubDAO = GoalSubDAO(context, dbHelper!!.writableDatabase)
            result = goalSubDAO.updateGoal(goalSub)
            L.e("::::delete success : $result")
        } catch (e: Exception) {
            L.e(":::::GoalDAOFactory updateGoal Exception")
        } finally {
            dbHelper?.close()
        }
        return result
    }

    @Throws(Exception::class)
    fun removeGoalSub(context: Context, goalSub: GoalSub?): Boolean {
        //서브목표 삭제를 하기위한 함수이다.
        var pass = false
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalSubDAO = GoalSubDAO(context, dbHelper!!.writableDatabase)
            pass = goalSubDAO.removeGoalSub(
                goalSub.getIndexNumber().toString(),
                goalSub.getAddedByUser()
            )
            L.e("::::delete success : $pass")
        } catch (e: Exception) {
            L.e(":::::GoalSubDAOFactory delete Exception")
        } finally {
            dbHelper?.close()
        }
        return pass
    }

    @Throws(Exception::class)
    fun getGoalSubList(context: Context, goalID: String): List<GoalSub?>? {
        //db에 있는 서브목표들을 호출한다.. 최종목표에서 설정한 하위 서브목표들..
        var list: List<GoalSub?>? = null
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalSubDAO = GoalSubDAO(context, dbHelper!!.writableDatabase)
            list = goalSubDAO.getGoalSubList(goalID)
        } catch (e: Exception) {
            L.e(":::::GoalSubDAOFactory getGoalList Exception")
        } finally {
            dbHelper?.close()
        }
        return list
    }
}