package com.ssafy.finalpjt.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.ssafy.finalpjt.db.model.GoalSub
import com.ssafy.finalpjt.db.DBConst.SubGoalTable
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
                pass = true
            }
        } catch (e: Exception) {
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
        }
        return pass
    }

    fun getGoalSubList(condition: String): List<GoalSub?>? {

        //ADDEDBYUSER 값이 일치한 id의 서브목표의 리스트를 호출한다.
        val query = ("SELECT * FROM " + SubGoalTable.TABLE_NAME
                + " WHERE " + SubGoalTable.ADDEDBYUSER + " = " + "'" + condition + "'")
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

                            //데이터  List add
                            list.add(goalSub)
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