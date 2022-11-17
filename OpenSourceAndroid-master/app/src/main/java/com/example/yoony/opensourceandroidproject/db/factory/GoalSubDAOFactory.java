package com.example.yoony.opensourceandroidproject.db.factory;

import android.content.Context;

import com.example.yoony.opensourceandroidproject.L;
import com.example.yoony.opensourceandroidproject.db.DbHelper;
import com.example.yoony.opensourceandroidproject.db.dao.GoalSubDAO;
import com.example.yoony.opensourceandroidproject.db.model.GoalSub;

import java.util.List;


public class GoalSubDAOFactory {

    public GoalSubDAOFactory() {
    }


    public static boolean addGoalSub(Context context, GoalSub goalSub) throws Exception {
        //db에 최종목표를 추가 시키는 함수이다.
        boolean pass = false;
        DbHelper dbHelper = null;
        try {
            dbHelper = DbHelper.getInstance(context);
            GoalSubDAO goalSubDAO = new GoalSubDAO(context, dbHelper.getWritableDatabase());
            pass = goalSubDAO.addDate(goalSub);
            L.e("::::add success : " + pass);
        } catch (Exception e) {
            L.e(":::::GoalSubDAOFactory addGoal Exception");
        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
        return pass;
    }

    public static boolean updateGoalSub(Context context, GoalSub goalSub) throws Exception {
        //db에 최종목표를 수정하는 함수이다
        boolean result = false;
        DbHelper dbHelper = null;
        try {
            dbHelper = DbHelper.getInstance(context);
            GoalSubDAO goalSubDAO = new GoalSubDAO(context, dbHelper.getWritableDatabase());
            result = goalSubDAO.updateGoal(goalSub);
            L.e("::::delete success : " + result);
        } catch (Exception e) {
            L.e(":::::GoalDAOFactory updateGoal Exception");
        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
        return result;
    }

    public static boolean removeGoalSub(Context context, GoalSub goalSub) throws Exception {
        //서브목표 삭제를 하기위한 함수이다.
        boolean pass = false;
        DbHelper dbHelper = null;
        try {
            dbHelper = DbHelper.getInstance(context);
            GoalSubDAO goalSubDAO = new GoalSubDAO(context, dbHelper.getWritableDatabase());
            pass = goalSubDAO.removeGoalSub(String.valueOf(goalSub.getIndexNumber()), goalSub.getAddedByUser());

            L.e("::::delete success : " + pass);
        } catch (Exception e) {
            L.e(":::::GoalSubDAOFactory delete Exception");
        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
        return pass;
    }

    public static List<GoalSub> getGoalSubList(Context context, String goalID) throws Exception {
        //db에 있는 서브목표들을 호출한다.. 최종목표에서 설정한 하위 서브목표들..
        List<GoalSub> list = null;
        DbHelper dbHelper = null;
        try {
            dbHelper = DbHelper.getInstance(context);
            GoalSubDAO goalSubDAO = new GoalSubDAO(context, dbHelper.getWritableDatabase());
            list = goalSubDAO.getGoalSubList(goalID);
        } catch (Exception e) {
            L.e(":::::GoalSubDAOFactory getGoalList Exception");
        } finally {
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
        return list;
    }

}
