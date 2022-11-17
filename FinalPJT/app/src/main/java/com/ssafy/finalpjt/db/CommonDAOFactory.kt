package com.ssafy.finalpjt.db

import android.content.Context
import java.lang.Exception

object CommonDAOFactory {
    fun executeCreate(context: Context): Boolean {
        var pass = false
        var dbHelper: DbHelper? = null
        try {
            //데이터베이스의 테이블이 없다면 테이블을 만들어주도록한다.
            dbHelper = DbHelper.Companion.getInstance(context)
            //CommonDAO 참조
            val commonDAO = CommonDAO(dbHelper!!.writableDatabase)
            dbHelper.beginTransaction()
            //데이터베이스 생성
            commonDAO.createTables()
            dbHelper.setTransactionSuccessful()
            pass = true
        } catch (e: Exception) {
        } finally {
            if (dbHelper != null) {
                dbHelper.endTransaction()
                dbHelper.close()
            }
        }
        return pass
    }
}