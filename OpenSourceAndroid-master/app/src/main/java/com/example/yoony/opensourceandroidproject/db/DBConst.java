package com.example.yoony.opensourceandroidproject.db;

import android.provider.BaseColumns;

public class DBConst {

    //최종목표 테이블을 만들어준다. id값이 주키 이며, 컬럼은 GoalTable class 에 있는 값이 된다.
    public static String[] createGoalDataTable() {
        String[] table = new String[1];
        table[0] = "CREATE TABLE IF NOT EXISTS " +
                GoalTable.TABLE_NAME + " (" +
                GoalTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                GoalTable.GOAL_TITLE + " TEXT NOT NULL" +
                ");";
        return table;
    }


    //서브목표 테이블을 만들어준다. id값이 주키 이며, 컬럼은 SubGoalTable class 에 있는 속성값이다.
    public static String[] createGoalSubTable() {

        String[] table = new String[1];
        table[0] = "CREATE TABLE IF NOT EXISTS " +
                SubGoalTable.TABLE_NAME + " (" +
                SubGoalTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                SubGoalTable.ADDEDBYUSER + " TEXT NOT NULL," +
                SubGoalTable.SUBTITLE + " TEXT NOT NULL," +
                SubGoalTable.DISABLE + " REAL NOT NULL DEFAULT 0 " +
                ");";
        return table;
    }


    public static class GoalTable implements BaseColumns {
        public static final String TABLE_NAME = "IMPORT_DATA_GOAL"; //테이블 이름
        public static final String GOAL_TITLE = "gaolTitle"; //최종목표
    }

    public static class SubGoalTable implements BaseColumns {
        public static final String TABLE_NAME = "IMPORT_DATA_SUB_GOAL"; //테이블 이름
        public static final String ADDEDBYUSER = "addedByUser"; //최종목표의 테이블 키값
        public static final String SUBTITLE = "subTitle"; // 서브 목표
        public static final String DISABLE = "disable"; // 활성화 여부.
    }
}
