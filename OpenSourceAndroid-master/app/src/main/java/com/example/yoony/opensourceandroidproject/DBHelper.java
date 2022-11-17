package com.example.yoony.opensourceandroidproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yoony.opensourceandroidproject.db.DBConst;

public class DBHelper extends SQLiteOpenHelper {

    //DBHelper 생성자로 관리할 DB이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //새로운 테이블 생성
        db.execSQL("CREATE TABLE Todo(_id INTEGER PRIMARY KEY AUTOINCREMENT,job TEXT,date INTEGER,quest TEXT,isdone INTEGER DEFAULT '0' );");
        db.execSQL("CREATE TABLE User(nickname TEXT,point INTEGER);");
        db.execSQL("CREATE TABLE Shop(item TEXT,price INTEGER);");
        db.execSQL("CREATE TABLE Rate(rate_main TEXT,rate INTEGER);");
        Log.e("tableCreate", "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//db버전이 다를경우

    }

    public void setShopItem(String item, int price) {
        SQLiteDatabase db = getWritableDatabase();

        Log.e("sange", "어이템셋");
        db.execSQL("insert into Shop values('" + item + "'," + price + ");");
        db.close();
    }

    public String selectShopItem() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("select * from Shop", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) + "|" + cursor.getInt(1) + "\n";
        }
        Log.e("sangeun", result);
        return result;
    }

    public int isEmptyShopItem() {
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.execSQL("CREATE TABLE Shop(item TEXT,price INTEGER);");
        } catch (Exception e) {
        }
        String result = "";

        Cursor cursor = db.rawQuery("select * from Shop", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) + "|" + cursor.getInt(1) + "\n";
            Log.e("sangeun", result);
        }
        if (result == "")
            return 1;
        else
            return 0;

    }

    public void deleteShopItem(String item) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from Shop where item='" + item + "';");
    }

    public void setUserNickname(String nickname, int point) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("CREATE TABLE User(nickname TEXT,point INTEGER);");
        } catch (Exception e) {
        }

        db.execSQL("insert into User values('" + nickname + "'," + point + ");");
        db.close();
    }

    public void updateUserNickname(String nickname) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("update User set nickname='" + nickname + "';");
        db.close();
    }

    public void plusUserPoint(int point) {
        SQLiteDatabase db = getWritableDatabase();
        int result = selectUserpoint();
        result += point;

        db.execSQL("update User set point='" + result + "';");
        db.close();
    }

    public void minusUserPoint(int point) {
        SQLiteDatabase db = getWritableDatabase();
        int result = selectUserpoint();
        result -= point;

        db.execSQL("update User set point='" + result + "';");
        db.close();
    }

    public String selectUsername() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("select nickname from User", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }
        return result;
    }

    public int selectUserpoint() {
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;

        Cursor cursor = db.rawQuery("select point from User", null);
        while (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        return result;
    }

    public void insert(String job, int date, String quest) {//할일 추가
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into Todo values(null,'" + job + "'," + date + ",'" + quest + "'," + 0 + ");");
        db.close();
    }

    public void updateisDone(int id, int isdone) {//할일 수행여부 수정
        SQLiteDatabase db = getWritableDatabase();
        Log.e("id", "" + id);
        db.execSQL("update Todo set isdone=" + isdone + " where _id=" + id + " ;");
        db.close();
    }

    public String MainQuest() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("select distinct " + DBConst.GoalTable.GOAL_TITLE + " from " + DBConst.GoalTable.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) + "\n";
        }
        Log.e("TodoQuest", "" + result);
        return result;
    }

    public int MainQuestIndex(String quest) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("select  " + DBConst.GoalTable._ID + " from " + DBConst.GoalTable.TABLE_NAME + " where " + DBConst.GoalTable.GOAL_TITLE + "='" + quest + "'", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }
        Log.e("TodoQuestindex", "" + result);
        if (result == "") {
            return 0;
        }
        return Integer.parseInt(result) - 1;
    }

    public String addedByUser() {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("select distinct " + DBConst.GoalTable._ID + " from " + DBConst.GoalTable.TABLE_NAME, null);
        while (cursor.moveToNext()) {
            result += "" + cursor.getLong(0) + "\n";
        }
        Log.e("아앙ㄴㄹ나ㅜ", "addedByUser: " + result);
        return result;
    }

    public String SubQuest(Long main) {
        SQLiteDatabase db = getReadableDatabase();
        String result = "";
        Cursor cursor = db.rawQuery("select distinct " + DBConst.SubGoalTable.SUBTITLE + " from " + DBConst.SubGoalTable.TABLE_NAME + " where " + DBConst.SubGoalTable.ADDEDBYUSER + "='" + main + "'", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) + "\n";
        }
        Log.e("SubQuest", "sub:" + result);
        return result;
    }

    public void setRate(String rate_main, int rate) {
        SQLiteDatabase db = getWritableDatabase();
        if (isMainQuestinRate(rate_main) == false) {
            db.execSQL("insert into Rate values('" + rate_main + "'," + rate + ");");
        }
        db.close();
    }

    public void updateRate(String rate_main, int rate) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("update Rate set rate=" + rate + " where rate_main='" + rate_main + "';");
        db.close();
    }

    public int selectRate(String rate_main) {
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;
        Cursor cursor = db.rawQuery("select rate from Rate where rate_main='" + rate_main + "'", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0);
        }
        return result;
    }

    public boolean isMainQuestinRate(String rate_main) {
        SQLiteDatabase db = getReadableDatabase();
        try {
            db.execSQL("CREATE TABLE Rate(rate_main TEXT, rate INTEGER);");
        } catch (Exception e) {
        }
        String result = "";
        Cursor cursor = db.rawQuery("select rate_main from Rate where rate_main='" + rate_main + "'", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0) + "\n";
        }
        if (result == "")
            return false;
        else
            return true;
    }

    public String sortTodo(int date) {

        SQLiteDatabase db = getReadableDatabase();
        try {
            db.execSQL("CREATE TABLE Todo(_id INTEGER PRIMARY KEY AUTOINCREMENT,job TEXT,date INTEGER,quest TEXT,isdone INTEGER DEFAULT '0' );");
        } catch (Exception e) {
        }
        String result = "";

        Cursor cursor = db.rawQuery("select * from Todo where date=" + date + " order by isdone asc, job asc", null);
        while (cursor.moveToNext()) {
            result += cursor.getInt(0) + "|" + cursor.getString(1) + "|" + cursor.getInt(2) + "|" + cursor.getString(3) + "|" + cursor.getInt(4) + "\n";
        }
        Log.e("sortTodo", "" + date);
        Log.e("sortTodo", "" + result);
        return result;
    }
}
