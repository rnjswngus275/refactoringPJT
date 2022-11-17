package com.ssafy.finalpjt.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.ssafy.finalpjt.db.model.Goal
import com.ssafy.finalpjt.db.DBConst.GoalTable
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
                pass = _id
            }
        } catch (e: Exception) {
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

        }
        return pass
    }//데이터  List add

    //ADDEDBYUSER 값이 일치한 id의 차량만 호출하는 쿼리문이다.
    val goalList: List<Goal?>?
        get() {

            //ADDEDBYUSER 값이 일치한 id의 차량만 호출하는 쿼리문이다.
            val query = "SELECT * FROM " + GoalTable.TABLE_NAME
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

                                //데이터  List add
                                list.add(goal)
                            }
                            cursor.moveToNext()
                        }
                    }
                }
            } catch (e: Exception) {
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