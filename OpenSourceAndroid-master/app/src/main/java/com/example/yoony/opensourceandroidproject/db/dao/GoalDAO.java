package com.example.yoony.opensourceandroidproject.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yoony.opensourceandroidproject.L;
import com.example.yoony.opensourceandroidproject.db.DBConst;
import com.example.yoony.opensourceandroidproject.db.model.Goal;

import java.util.ArrayList;
import java.util.List;

public class GoalDAO {
    private Context context;
    private SQLiteDatabase mDatabase;

    public GoalDAO(Context context, SQLiteDatabase database) {
        this.context = context;
        this.mDatabase = database;
    }

    public long addDate(Goal info) {
        //최종목표 데이터를를 데이터베이스에 넣는함수
        long pass = -1;
        try {
            ContentValues values = setContentValue(info);
            long _id = mDatabase.insert(DBConst.GoalTable.TABLE_NAME, null, values);
            if (_id != 0) {
                L.i(":::::[GOAL INSERT] _id : " + _id);
                pass = _id;
            }
        } catch (Exception e) {
            L.e(":::::add Exception : " + e.getMessage());
        }
        return pass;
    }

    public boolean removeGoal(Goal info) {
        //최종목표를 삭제하는 함수이다.
        boolean pass = false;
        try {
            //최종목표의 seq값을 찾는 sql문을 만들어 삭제시킨다.
            String sql = "DELETE FROM IMPORT_DATA_GOAL WHERE _id=?";
            String[] bindArgsInfo = {String.valueOf(info.getIndexNumber())};
            mDatabase.execSQL(sql, bindArgsInfo);
            return true;
        } catch (Exception e) {
            L.e(":::::add Exception : " + e.getMessage());
        }
        return pass;
    }


    public boolean updateGoal(Goal info) {
        //최종목표를 삭제하는 함수이다.
        boolean pass = false;
        try {
            ContentValues reportValue = new ContentValues();
            reportValue.put(DBConst.GoalTable.GOAL_TITLE, info.getGoalTitle());
            String whereClause = "_id=?";
            String[] whereArgs = new String[]{
                    String.valueOf(info.getIndexNumber())
            };
            mDatabase.update(DBConst.GoalTable.TABLE_NAME, reportValue, whereClause, whereArgs);
            return true;
        } catch (Exception e) {
            L.e(":::::update Exception : " + e.getMessage());
        }
        return pass;
    }

    public List<Goal> getGoalList() {

        //ADDEDBYUSER 값이 일치한 id의 차량만 호출하는 쿼리문이다.
        String query = "SELECT * FROM " + DBConst.GoalTable.TABLE_NAME;
        L.e(":::::[getGoalList query] " + query);
        List<Goal> list = null;
        Cursor cursor = null;
        try {
            cursor = mDatabase.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                list = new ArrayList<>();
                cursor.moveToFirst();
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {

                        int columnIndex = cursor.getColumnIndex(DBConst.GoalTable._ID);

                        if (columnIndex == -1) {
                            cursor.moveToNext();
                            continue;
                        }

                        int _id = cursor.getInt(columnIndex);
                        if (_id >= 0) {

                            Goal goal = new Goal();

                            goal.setIndexNumber(_id);


                            columnIndex = cursor.getColumnIndex(DBConst.GoalTable.GOAL_TITLE);
                            String goalTitle = "";
                            if (columnIndex >= 0) {
                                goalTitle = cursor.getString(columnIndex);
                                goal.setGoalTitle(goalTitle);
                            }
                            L.i(":::[Goal] : " + goal.toString());

                            //데이터  List add
                            list.add(goal);

                        }

                        cursor.moveToNext();

                    }
                }
            }
        } catch (Exception e) {
            L.e("::::::SELECT getVihicle Exception:::");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }


    public ContentValues setContentValue(Goal goal) {
        ContentValues calendarValues = new ContentValues();
        calendarValues.put(DBConst.GoalTable.GOAL_TITLE, goal.getGoalTitle());
        return calendarValues;
    }

}
