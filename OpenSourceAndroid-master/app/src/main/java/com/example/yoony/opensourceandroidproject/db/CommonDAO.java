package com.example.yoony.opensourceandroidproject.db;

import android.database.sqlite.SQLiteDatabase;

import com.example.yoony.opensourceandroidproject.L;


public class CommonDAO {
    //공통
    private final SQLiteDatabase mDatabase;

    public CommonDAO(SQLiteDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public boolean createTables() throws Exception {
        //데이터베이스를 만든다, 유저정보 테이블과,자동차 관련 테이블이다.
        boolean pass = false;
        String createTables[][] = {
                DBConst.createGoalDataTable(),
                DBConst.createGoalSubTable()
        };

        try {
            for (int i = 0; i < createTables.length; ++i) {
                for (int j = 0; j < createTables[i].length; ++j) {
                    mDatabase.execSQL(createTables[i][j]);
                }
            }
            pass = true;
        } catch (Exception e) {
            L.e(":::::CREATE Exception ==> createTables() : CREATE Tables");
            L.d(e.getMessage());
        }

        return pass;
    }


}
