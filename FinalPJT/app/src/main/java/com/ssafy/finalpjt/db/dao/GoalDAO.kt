package com.ssafy.finalpjt.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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
import java.lang.Exception
import java.util.ArrayList

class GoalDAO(private val context: Context, private val mDatabase: SQLiteDatabase?) {
    fun addDate(info: Goal): Long {
        //최종목표 데이터를를 데이터베이스에 넣는함수
        var pass: Long = -1
        try {
            val values = setContentValue(info)
            val _id = mDatabase!!.insert(GoalTable.TABLE_NAME, null, values)
            if (_id != 0L) {
                L.i(":::::[GOAL INSERT] _id : $_id")
                pass = _id
            }
        } catch (e: Exception) {
            L.e(":::::add Exception : " + e.message)
        }
        return pass
    }

    fun removeGoal(info: Goal): Boolean {
        //최종목표를 삭제하는 함수이다.
        val pass = false
        try {
            //최종목표의 seq값을 찾는 sql문을 만들어 삭제시킨다.
            val sql = "DELETE FROM IMPORT_DATA_GOAL WHERE _id=?"
            val bindArgsInfo = arrayOf(info.indexNumber.toString())
            mDatabase!!.execSQL(sql, bindArgsInfo)
            return true
        } catch (e: Exception) {
            L.e(":::::add Exception : " + e.message)
        }
        return pass
    }

    fun updateGoal(info: Goal?): Boolean {
        //최종목표를 삭제하는 함수이다.
        val pass = false
        try {
            val reportValue = ContentValues()
            reportValue.put(GoalTable.GOAL_TITLE, info.getGoalTitle())
            val whereClause = "_id=?"
            val whereArgs = arrayOf(info.getIndexNumber().toString())
            mDatabase!!.update(GoalTable.TABLE_NAME, reportValue, whereClause, whereArgs)
            return true
        } catch (e: Exception) {
            L.e(":::::update Exception : " + e.message)
        }
        return pass
    }//데이터  List add

    //ADDEDBYUSER 값이 일치한 id의 차량만 호출하는 쿼리문이다.
    val goalList: List<Goal?>?
        get() {

            //ADDEDBYUSER 값이 일치한 id의 차량만 호출하는 쿼리문이다.
            val query = "SELECT * FROM " + GoalTable.TABLE_NAME
            L.e(":::::[getGoalList query] $query")
            var list: MutableList<Goal?>? = null
            var cursor: Cursor? = null
            try {
                cursor = mDatabase!!.rawQuery(query, null)
                if (cursor != null && cursor.count > 0) {
                    list = ArrayList()
                    cursor.moveToFirst()
                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast) {
                            var columnIndex = cursor.getColumnIndex(BaseColumns._ID)
                            if (columnIndex == -1) {
                                cursor.moveToNext()
                                continue
                            }
                            val _id = cursor.getInt(columnIndex)
                            if (_id >= 0) {
                                val goal = Goal()
                                goal.indexNumber = _id
                                columnIndex = cursor.getColumnIndex(GoalTable.GOAL_TITLE)
                                var goalTitle: String? = ""
                                if (columnIndex >= 0) {
                                    goalTitle = cursor.getString(columnIndex)
                                    goal.goalTitle = goalTitle
                                }
                                L.i(":::[Goal] : $goal")

                                //데이터  List add
                                list.add(goal)
                            }
                            cursor.moveToNext()
                        }
                    }
                }
            } catch (e: Exception) {
                L.e("::::::SELECT getVihicle Exception:::")
            } finally {
                cursor?.close()
            }
            return list
        }

    fun setContentValue(goal: Goal): ContentValues {
        val calendarValues = ContentValues()
        calendarValues.put(GoalTable.GOAL_TITLE, goal.goalTitle)
        return calendarValues
    }
}