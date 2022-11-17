package com.example.yoony.opensourceandroidproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.yoony.opensourceandroidproject.L;


public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "QuestApp.db";

    private static DbHelper mInstance;
    private Context context;

    //db Transaction 관리 위한 database 객체
    private SQLiteDatabase mSqliteDatabase = null;

    private DbHelper(Context context) throws SQLiteException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        mSqliteDatabase = super.getWritableDatabase();

    }

    public static synchronized DbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbHelper(context);
        }
        return mInstance;
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        return mSqliteDatabase;
    }

    public void beginTransaction() throws Exception {
        try {
            if (!mSqliteDatabase.inTransaction()) {
                mSqliteDatabase.beginTransaction();
            }
        } catch (Exception e) {
            L.e(":::::Exceoption in beginTransaction");
        }
    }

    public void setTransactionSuccessful() throws Exception {
        try {
            if (mSqliteDatabase.inTransaction()) {
                mSqliteDatabase.setTransactionSuccessful();
            }
        } catch (Exception e) {
            L.e("Exception in setTransactionSuccessful");
        }
    }

    public void endTransaction() {
        try {
            if (mSqliteDatabase.inTransaction()) {
                mSqliteDatabase.endTransaction();
            }
        } catch (Exception e) {
            L.e("Exception in endTransaction");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CommonDAO dao = new CommonDAO(db);
        try {
            dao.createTables();
        } catch (Exception e) {
            L.e("::: e : " + e.getMessage());
        }
    }

    public void close() {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
