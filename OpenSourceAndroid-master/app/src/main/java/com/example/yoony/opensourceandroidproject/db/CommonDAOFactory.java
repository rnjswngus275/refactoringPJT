package com.example.yoony.opensourceandroidproject.db;

import android.content.Context;

public class CommonDAOFactory {
    public CommonDAOFactory() {
    }

    public static boolean executeCreate(Context context) {
        boolean pass = false;
        DbHelper dbHelper = null;
        try {
            //데이터베이스의 테이블이 없다면 테이블을 만들어주도록한다.
            dbHelper = DbHelper.getInstance(context);
            //CommonDAO 참조
            CommonDAO commonDAO = new CommonDAO(dbHelper.getWritableDatabase());
            dbHelper.beginTransaction();
            //데이터베이스 생성
            commonDAO.createTables();
            dbHelper.setTransactionSuccessful();
            pass = true;
        } catch (Exception e) {

        } finally {
            if (dbHelper != null) {
                dbHelper.endTransaction();
                dbHelper.close();
            }
        }

        return pass;
    }
}

