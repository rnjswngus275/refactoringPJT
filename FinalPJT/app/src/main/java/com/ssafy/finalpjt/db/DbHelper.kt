package com.ssafy.finalpjt.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.Throws
import kotlin.jvm.Synchronized
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
        }
    }

    @Throws(Exception::class)
    fun setTransactionSuccessful() {
        try {
            if (mSqliteDatabase!!.inTransaction()) {
                mSqliteDatabase!!.setTransactionSuccessful()
            }
        } catch (e: Exception) {

        }
    }

    fun endTransaction() {
        try {
            if (mSqliteDatabase!!.inTransaction()) {
                mSqliteDatabase!!.endTransaction()
            }
        } catch (e: Exception) {
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val dao = CommonDAO(db)
        try {
            dao.createTables()
        } catch (e: Exception) {

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