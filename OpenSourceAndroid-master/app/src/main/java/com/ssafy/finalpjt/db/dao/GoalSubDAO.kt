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

class GoalSubDAO(private val context: Context, private val mDatabase: SQLiteDatabase?) {
    fun addDate(info: GoalSub): Boolean {
        //최종목표 데이터를를 데이터베이스에 넣는함수
        var pass = false
        try {
            val values = setContentValue(info)
            val _id = mDatabase!!.insert(SubGoalTable.TABLE_NAME, null, values)
            if (_id != 0L) {
                L.i(":::::[GOALSUB INSERT] _id : $_id")
                pass = true
            }
        } catch (e: Exception) {
            L.e(":::::add Exception : " + e.message)
        }
        return pass
    }

    fun updateGoal(info: GoalSub?): Boolean {
        //최종목표를 삭제하는 함수이다.
        val pass = false
        try {
            val reportValue = ContentValues()
            reportValue.put(SubGoalTable.SUBTITLE, info.getSubTitle())
            val whereClause = "_id=?"
            val whereArgs = arrayOf(info.getIndexNumber().toString())
            mDatabase!!.update(SubGoalTable.TABLE_NAME, reportValue, whereClause, whereArgs)
            return true
        } catch (e: Exception) {
            L.e(":::::update Exception : " + e.message)
        }
        return pass
    }

    fun getGoalSubList(condition: String): List<GoalSub?>? {

        //ADDEDBYUSER 값이 일치한 id의 서브목표의 리스트를 호출한다.
        val query = ("SELECT * FROM " + SubGoalTable.TABLE_NAME
                + " WHERE " + SubGoalTable.ADDEDBYUSER + " = " + "'" + condition + "'")
        L.e(":::::[getGoalSubList query] $query")
        var list: MutableList<GoalSub?>? = null
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
                            val goalSub = GoalSub()
                            goalSub.indexNumber = _id
                            columnIndex = cursor.getColumnIndex(SubGoalTable.ADDEDBYUSER)
                            var addedByUser: String? = ""
                            if (columnIndex >= 0) {
                                addedByUser = cursor.getString(columnIndex)
                                goalSub.addedByUser = addedByUser
                            }
                            columnIndex = cursor.getColumnIndex(SubGoalTable.SUBTITLE)
                            var vihicleName: String? = ""
                            if (columnIndex >= 0) {
                                vihicleName = cursor.getString(columnIndex)
                                goalSub.subTitle = vihicleName
                            }
                            columnIndex = cursor.getColumnIndex(SubGoalTable.DISABLE)
                            var disable = 0
                            if (columnIndex >= 0) {
                                disable = cursor.getInt(columnIndex)
                                goalSub.disable = disable
                            }
                            L.i(":::[subGoal] : $goalSub")

                            //데이터  List add
                            list.add(goalSub)
                        }
                        cursor.moveToNext()
                    }
                }
            }
        } catch (e: Exception) {
            L.e("::::::SELECT getGoalSubList Exception:::")
        } finally {
            cursor?.close()
        }
        return list
    }

    fun removeGoalSub(id: String?, addedByUser: String?): Boolean {
        //서브목표를 삭제하기 위해 사용한다.
        var result = false
        try {
            val sql = "DELETE FROM IMPORT_DATA_SUB_GOAL WHERE addedByUser=? AND _id=?"
            val bindArgsInfo = arrayOf(
                addedByUser,
                id
            )
            mDatabase!!.execSQL(sql, bindArgsInfo)
            result = true
        } catch (e: Exception) {
        }
        return result
    }

    fun setContentValue(goalSub: GoalSub): ContentValues {
        val calendarValues = ContentValues()
        calendarValues.put(SubGoalTable.ADDEDBYUSER, goalSub.addedByUser)
        calendarValues.put(SubGoalTable.SUBTITLE, goalSub.subTitle)
        calendarValues.put(SubGoalTable.DISABLE, goalSub.disable)
        return calendarValues
    }
}