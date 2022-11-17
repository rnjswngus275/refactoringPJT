package com.ssafy.finalpjt

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
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

class DBHelper  //DBHelper 생성자로 관리할 DB이름과 버전 정보를 받음
    (context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        //새로운 테이블 생성
        db.execSQL("CREATE TABLE Todo(_id INTEGER PRIMARY KEY AUTOINCREMENT,job TEXT,date INTEGER,quest TEXT,isdone INTEGER DEFAULT '0' );")
        db.execSQL("CREATE TABLE User(nickname TEXT,point INTEGER);")
        db.execSQL("CREATE TABLE Shop(item TEXT,price INTEGER);")
        db.execSQL("CREATE TABLE Rate(rate_main TEXT,rate INTEGER);")
        Log.e("tableCreate", "")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { //db버전이 다를경우
    }

    fun setShopItem(item: String, price: Int) {
        val db = writableDatabase
        Log.e("sange", "어이템셋")
        db.execSQL("insert into Shop values('$item',$price);")
        db.close()
    }

    fun selectShopItem(): String {
        val db = readableDatabase
        var result = ""
        val cursor = db.rawQuery("select * from Shop", null)
        while (cursor.moveToNext()) {
            result += """
                ${cursor.getString(0)}|${cursor.getInt(1)}
                
                """.trimIndent()
        }
        Log.e("sangeun", result)
        return result
    }

    val isEmptyShopItem: Int
        get() {
            val db = readableDatabase
            try {
                db.execSQL("CREATE TABLE Shop(item TEXT,price INTEGER);")
            } catch (e: Exception) {
            }
            var result = ""
            val cursor = db.rawQuery("select * from Shop", null)
            while (cursor.moveToNext()) {
                result += """
                ${cursor.getString(0)}|${cursor.getInt(1)}
                
                """.trimIndent()
                Log.e("sangeun", result)
            }
            return if (result === "") 1 else 0
        }

    fun deleteShopItem(item: String?) {
        val db = writableDatabase
        db.execSQL("delete from Shop where item='$item';")
    }

    fun setUserNickname(nickname: String, point: Int) {
        val db = writableDatabase
        try {
            db.execSQL("CREATE TABLE User(nickname TEXT,point INTEGER);")
        } catch (e: Exception) {
        }
        db.execSQL("insert into User values('$nickname',$point);")
        db.close()
    }

    fun updateUserNickname(nickname: String) {
        val db = writableDatabase
        db.execSQL("update User set nickname='$nickname';")
        db.close()
    }

    fun plusUserPoint(point: Int) {
        val db = writableDatabase
        var result = selectUserpoint()
        result += point
        db.execSQL("update User set point='$result';")
        db.close()
    }

    fun minusUserPoint(point: Int) {
        val db = writableDatabase
        var result = selectUserpoint()
        result -= point
        db.execSQL("update User set point='$result';")
        db.close()
    }

    fun selectUsername(): String {
        val db = readableDatabase
        var result = ""
        val cursor = db.rawQuery("select nickname from User", null)
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
        }
        return result
    }

    fun selectUserpoint(): Int {
        val db = readableDatabase
        var result = 0
        val cursor = db.rawQuery("select point from User", null)
        while (cursor.moveToNext()) {
            result = cursor.getInt(0)
        }
        return result
    }

    fun insert(job: String, date: Int, quest: String) { //할일 추가
        val db = writableDatabase
        db.execSQL("insert into Todo values(null,'$job',$date,'$quest',0);")
        db.close()
    }

    fun updateisDone(id: Int, isdone: Int) { //할일 수행여부 수정
        val db = writableDatabase
        Log.e("id", "" + id)
        db.execSQL("update Todo set isdone=$isdone where _id=$id ;")
        db.close()
    }

    fun MainQuest(): String {
        val db = readableDatabase
        var result = ""
        val cursor = db.rawQuery(
            "select distinct " + GoalTable.GOAL_TITLE + " from " + GoalTable.TABLE_NAME,
            null
        )
        while (cursor.moveToNext()) {
            result += """
                ${cursor.getString(0)}
                
                """.trimIndent()
        }
        Log.e("TodoQuest", "" + result)
        return result
    }

    fun MainQuestIndex(quest: String?): Int {
        val db = readableDatabase
        var result = ""
        val cursor = db.rawQuery(
            "select  " + BaseColumns._ID + " from " + GoalTable.TABLE_NAME + " where " + GoalTable.GOAL_TITLE + "='" + quest + "'",
            null
        )
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
        }
        Log.e("TodoQuestindex", "" + result)
        return if (result === "") {
            0
        } else result.toInt() - 1
    }

    fun addedByUser(): String {
        val db = readableDatabase
        var result = ""
        val cursor = db.rawQuery(
            "select distinct " + BaseColumns._ID + " from " + GoalTable.TABLE_NAME,
            null
        )
        while (cursor.moveToNext()) {
            result += """
                ${cursor.getLong(0)}
                
                """.trimIndent()
        }
        Log.e("아앙ㄴㄹ나ㅜ", "addedByUser: $result")
        return result
    }

    fun SubQuest(main: Long?): String {
        val db = readableDatabase
        var result = ""
        val cursor = db.rawQuery(
            "select distinct " + SubGoalTable.SUBTITLE + " from " + SubGoalTable.TABLE_NAME + " where " + SubGoalTable.ADDEDBYUSER + "='" + main + "'",
            null
        )
        while (cursor.moveToNext()) {
            result += """
                ${cursor.getString(0)}
                
                """.trimIndent()
        }
        Log.e("SubQuest", "sub:$result")
        return result
    }

    fun setRate(rate_main: String, rate: Int) {
        val db = writableDatabase
        if (isMainQuestinRate(rate_main) == false) {
            db.execSQL("insert into Rate values('$rate_main',$rate);")
        }
        db.close()
    }

    fun updateRate(rate_main: String?, rate: Int) {
        val db = writableDatabase
        db.execSQL("update Rate set rate=$rate where rate_main='$rate_main';")
        db.close()
    }

    fun selectRate(rate_main: String?): Int {
        val db = readableDatabase
        var result = 0
        val cursor = db.rawQuery("select rate from Rate where rate_main='$rate_main'", null)
        while (cursor.moveToNext()) {
            result += cursor.getInt(0)
        }
        return result
    }

    fun isMainQuestinRate(rate_main: String): Boolean {
        val db = readableDatabase
        try {
            db.execSQL("CREATE TABLE Rate(rate_main TEXT, rate INTEGER);")
        } catch (e: Exception) {
        }
        var result = ""
        val cursor = db.rawQuery("select rate_main from Rate where rate_main='$rate_main'", null)
        while (cursor.moveToNext()) {
            result += """
                ${cursor.getString(0)}
                
                """.trimIndent()
        }
        return if (result === "") false else true
    }

    fun sortTodo(date: Int): String {
        val db = readableDatabase
        try {
            db.execSQL("CREATE TABLE Todo(_id INTEGER PRIMARY KEY AUTOINCREMENT,job TEXT,date INTEGER,quest TEXT,isdone INTEGER DEFAULT '0' );")
        } catch (e: Exception) {
        }
        var result = ""
        val cursor =
            db.rawQuery("select * from Todo where date=$date order by isdone asc, job asc", null)
        while (cursor.moveToNext()) {
            result += """
                ${cursor.getInt(0)}|${cursor.getString(1)}|${cursor.getInt(2)}|${cursor.getString(3)}|${
                cursor.getInt(
                    4
                )
            }
                
                """.trimIndent()
        }
        Log.e("sortTodo", "" + date)
        Log.e("sortTodo", "" + result)
        return result
    }
}