package com.ssafy.finalpjt.db

import android.database.sqlite.SQLiteDatabase
import kotlin.Throws
import java.lang.Exception

class CommonDAO(  //공통
    private val mDatabase: SQLiteDatabase?
) {
    @Throws(Exception::class)
    fun createTables(): Boolean {
        //데이터베이스를 만든다, 유저정보 테이블과,자동차 관련 테이블이다.
        var pass = false
        val createTables = arrayOf(
            DBConst.createGoalDataTable(),
            DBConst.createGoalSubTable()
        )
        try {
            for (i in createTables.indices) {
                for (j in 0 until createTables[i].size) {
                    mDatabase!!.execSQL(createTables[i]!![j])
                }
            }
            pass = true
        } catch (e: Exception) {

        }
        return pass
    }
}