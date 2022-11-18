package com.ssafy.finalpjt.db.factory

import android.content.Context
import com.ssafy.finalpjt.db.database.dto.GoalSub
import kotlin.Throws
import com.ssafy.finalpjt.db.DbHelper
import com.ssafy.finalpjt.db.dao.GoalSubDAO
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
        } catch (e: Exception) {
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
        } catch (e: Exception) {
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
            dbHelper = DbHelper.getInstance(context)
            val goalSubDAO = GoalSubDAO(context, dbHelper!!.writableDatabase)
            if (goalSub != null) {
                pass = goalSubDAO.removeGoalSub(
                    goalSub._id.toString(),
                    goalSub.GoalId
                )
            }
        } catch (e: Exception) {
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
        } finally {
            dbHelper?.close()
        }
        return list
    }
}