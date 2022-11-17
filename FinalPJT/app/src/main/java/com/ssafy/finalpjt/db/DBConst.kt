package com.ssafy.finalpjt.db

import android.provider.BaseColumns
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

object DBConst {
    //최종목표 테이블을 만들어준다. id값이 주키 이며, 컬럼은 GoalTable class 에 있는 값이 된다.
    fun createGoalDataTable(): Array<String?> {
        val table = arrayOfNulls<String>(1)
        table[0] = "CREATE TABLE IF NOT EXISTS " +
                GoalTable.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                GoalTable.GOAL_TITLE + " TEXT NOT NULL" +
                ");"
        return table
    }

    //서브목표 테이블을 만들어준다. id값이 주키 이며, 컬럼은 SubGoalTable class 에 있는 속성값이다.
    fun createGoalSubTable(): Array<String?> {
        val table = arrayOfNulls<String>(1)
        table[0] = "CREATE TABLE IF NOT EXISTS " +
                SubGoalTable.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                SubGoalTable.ADDEDBYUSER + " TEXT NOT NULL," +
                SubGoalTable.SUBTITLE + " TEXT NOT NULL," +
                SubGoalTable.DISABLE + " REAL NOT NULL DEFAULT 0 " +
                ");"
        return table
    }

    object GoalTable : BaseColumns {
        const val TABLE_NAME = "IMPORT_DATA_GOAL" //테이블 이름
        const val GOAL_TITLE = "gaolTitle" //최종목표
    }

    object SubGoalTable : BaseColumns {
        const val TABLE_NAME = "IMPORT_DATA_SUB_GOAL" //테이블 이름
        const val ADDEDBYUSER = "addedByUser" //최종목표의 테이블 키값
        const val SUBTITLE = "subTitle" // 서브 목표
        const val DISABLE = "disable" // 활성화 여부.
    }
}