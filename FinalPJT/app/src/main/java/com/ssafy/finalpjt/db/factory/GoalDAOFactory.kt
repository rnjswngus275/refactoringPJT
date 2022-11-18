package com.ssafy.finalpjt.db.factory

import android.content.Context
import com.ssafy.finalpjt.db.database.dto.Goal
import kotlin.Throws
import com.ssafy.finalpjt.db.DbHelper
import com.ssafy.finalpjt.db.dao.GoalDAO
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
        } catch (e: Exception) {
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
        } catch (e: Exception) {

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

        } catch (e: Exception) {

        } finally {
            dbHelper?.close()
        }
        return result
    }

    @Throws(Exception::class)
    fun getGoalList(context: Context): List<Goal>? {
        //db에 있는 최종 목표 리스트를 호출하는 함수이다.
        var list: List<Goal>? = null
        var dbHelper: DbHelper? = null
        try {
            dbHelper = DbHelper.Companion.getInstance(context)
            val goalDAO = GoalDAO(context, dbHelper!!.writableDatabase)
            list = goalDAO.goalList as List<Goal>?
        } catch (e: Exception) {

        } finally {
            dbHelper?.close()
        }
        return list
    }
}