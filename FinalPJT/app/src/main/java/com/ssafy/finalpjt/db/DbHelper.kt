package com.ssafy.finalpjt.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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

class DbHelper private constructor(private val context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    //db Transaction 관리 위한 database 객체
    private var mSqliteDatabase: SQLiteDatabase? = null
    @Synchronized
    override fun getWritableDatabase(): SQLiteDatabase {
        return mSqliteDatabase!!
    }

    @Throws(Exception::class)
    fun beginTransaction() {
        try {
            if (!mSqliteDatabase!!.inTransaction()) {
                mSqliteDatabase!!.beginTransaction()
            }
        } catch (e: Exception) {
            L.e(":::::Exceoption in beginTransaction")
        }
    }

    @Throws(Exception::class)
    fun setTransactionSuccessful() {
        try {
            if (mSqliteDatabase!!.inTransaction()) {
                mSqliteDatabase!!.setTransactionSuccessful()
            }
        } catch (e: Exception) {
            L.e("Exception in setTransactionSuccessful")
        }
    }

    fun endTransaction() {
        try {
            if (mSqliteDatabase!!.inTransaction()) {
                mSqliteDatabase!!.endTransaction()
            }
        } catch (e: Exception) {
            L.e("Exception in endTransaction")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val dao = CommonDAO(db)
        try {
            dao.createTables()
        } catch (e: Exception) {
            L.e("::: e : " + e.message)
        }
    }

    override fun close() {}
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "QuestApp.db"
        private var mInstance: DbHelper? = null
        @Synchronized
        fun getInstance(context: Context): DbHelper? {
            if (mInstance == null) {
                mInstance = DbHelper(context)
            }
            return mInstance
        }
    }

    init {
        mSqliteDatabase = super.getWritableDatabase()
    }
}