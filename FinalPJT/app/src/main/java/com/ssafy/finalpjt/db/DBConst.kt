package com.ssafy.finalpjt.db

import android.provider.BaseColumns

object DBConst {
    //최종목표 테이블을 만들어준다. id값이 주키 이며, 컬럼은 GoalTable class 에 있는 값이 된다.
    fun createGoalDataTable(): Array<String?> {
        val table = arrayOfNulls<String>(1)
        table[0] = "CREATE TABLE IF NOT EXISTS " +
                GoalTable.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                GoalTable.GOAL_TITLE + " TEXT NOT NULL" +
                ");"
        return table
    }

    //서브목표 테이블을 만들어준다. id값이 주키 이며, 컬럼은 SubGoalTable class 에 있는 속성값이다.
    fun createGoalSubTable(): Array<String?> {
        val table = arrayOfNulls<String>(1)
        table[0] = "CREATE TABLE IF NOT EXISTS " +
                SubGoalTable.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                SubGoalTable.ADDEDBYUSER + " TEXT NOT NULL," +
                SubGoalTable.SUBTITLE + " TEXT NOT NULL," +
                SubGoalTable.DISABLE + " REAL NOT NULL DEFAULT 0 " +
                ");"
        return table
    }

    object GoalTable : BaseColumns {
        const val TABLE_NAME = "IMPORT_DATA_GOAL" //테이블 이름
        const val GOAL_TITLE = "gaolTitle" //최종목표
    }

    object SubGoalTable : BaseColumns {
        const val TABLE_NAME = "IMPORT_DATA_SUB_GOAL" //테이블 이름
        const val ADDEDBYUSER = "addedByUser" //최종목표의 테이블 키값
        const val SUBTITLE = "subTitle" // 서브 목표
        const val DISABLE = "disable" // 활성화 여부.
    }
}