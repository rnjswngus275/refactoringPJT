package com.example.yoony.opensourceandroidproject.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yoony.opensourceandroidproject.L;
import com.example.yoony.opensourceandroidproject.db.DBConst;
import com.example.yoony.opensourceandroidproject.db.model.GoalSub;

import java.util.ArrayList;
import java.util.List;

public class GoalSubDAO {
    private Context context;
    private SQLiteDatabase mDatabase;

    public GoalSubDAO(Context context, SQLiteDatabase database) {
        this.context = context;
        this.mDatabase = database;
    }

    public boolean addDate(GoalSub info) {
        //최종목표 데이터를를 데이터베이스에 넣는함수
        boolean pass = false;
        try {
            ContentValues values = setContentValue(info);
            long _id = mDatabase.insert(DBConst.SubGoalTable.TABLE_NAME, null, values);
            if (_id != 0) {
                L.i(":::::[GOALSUB INSERT] _id : " + _id);
                pass = true;
            }
        } catch (Exception e) {
            L.e(":::::add Exception : " + e.getMessage());
        }
        return pass;
    }

    public boolean updateGoal(GoalSub info) {
        //최종목표를 삭제하는 함수이다.
        boolean pass = false;
        try {
            ContentValues reportValue = new ContentValues();
            reportValue.put(DBConst.SubGoalTable.SUBTITLE, info.getSubTitle());
            String whereClause = "_id=?";
            String[] whereArgs = new String[]{
                    String.valueOf(info.getIndexNumber())
            };
            mDatabase.update(DBConst.SubGoalTable.TABLE_NAME, reportValue, whereClause, whereArgs);
            return true;
        } catch (Exception e) {
            L.e(":::::update Exception : " + e.getMessage());
        }
        return pass;
    }

    public List<GoalSub> getGoalSubList(String condition) {

        //ADDEDBYUSER 값이 일치한 id의 서브목표의 리스트를 호출한다.
        String query = "SELECT * FROM " + DBConst.SubGoalTable.TABLE_NAME
                + " WHERE " + DBConst.SubGoalTable.ADDEDBYUSER + " = " + "'" + condition + "'";
        L.e(":::::[getGoalSubList query] " + query);
        List<GoalSub> list = null;
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

                            GoalSub goalSub = new GoalSub();

                            goalSub.setIndexNumber(_id);


                            columnIndex = cursor.getColumnIndex(DBConst.SubGoalTable.ADDEDBYUSER);
                            String addedByUser = "";
                            if (columnIndex >= 0) {
                                addedByUser = cursor.getString(columnIndex);
                                goalSub.setAddedByUser(addedByUser);
                            }
                            columnIndex = cursor.getColumnIndex(DBConst.SubGoalTable.SUBTITLE);

                            String vihicleName = "";
                            if (columnIndex >= 0) {
                                vihicleName = cursor.getString(columnIndex);
                                goalSub.setSubTitle(vihicleName);
                            }

                            columnIndex = cursor.getColumnIndex(DBConst.SubGoalTable.DISABLE);
                            int disable = 0;
                            if (columnIndex >= 0) {
                                disable = cursor.getInt(columnIndex);
                                goalSub.setDisable(disable);
                            }

                            L.i(":::[subGoal] : " + goalSub.toString());

                            //데이터  List add
                            list.add(goalSub);

                        }

                        cursor.moveToNext();

                    }
                }
            }
        } catch (Exception e) {
            L.e("::::::SELECT getGoalSubList Exception:::");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }


    public boolean removeGoalSub(String id, String addedByUser) {
        //서브목표를 삭제하기 위해 사용한다.
        boolean result = false;
        try {
            String sql = "DELETE FROM IMPORT_DATA_SUB_GOAL WHERE addedByUser=? AND _id=?";
            String[] bindArgsInfo = {
                    addedByUser,
                    id
            };
            mDatabase.execSQL(sql, bindArgsInfo);
            result = true;
        } catch (Exception e) {

        }
        return result;
    }

    public ContentValues setContentValue(GoalSub goalSub) {
        ContentValues calendarValues = new ContentValues();
        calendarValues.put(DBConst.SubGoalTable.ADDEDBYUSER, goalSub.getAddedByUser());
        calendarValues.put(DBConst.SubGoalTable.SUBTITLE, goalSub.getSubTitle());
        calendarValues.put(DBConst.SubGoalTable.DISABLE, goalSub.getDisable());
        return calendarValues;
    }


}
