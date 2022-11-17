package com.example.yoony.opensourceandroidproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.yoony.opensourceandroidproject.db.GoalSubDataTask;
import com.example.yoony.opensourceandroidproject.db.factory.GoalDAOFactory;
import com.example.yoony.opensourceandroidproject.db.factory.GoalSubDAOFactory;
import com.example.yoony.opensourceandroidproject.db.model.Goal;
import com.example.yoony.opensourceandroidproject.db.model.GoalSub;

import java.util.ArrayList;
import java.util.List;

public class DetailActivityUpdate extends AppCompatActivity {
    private LinearLayout LL;
    private EditText etGoal;
    private Button btnUpdate;
    private Goal mCurrentGoalItem;
    private ArrayList<SubItemView> mSubItemViewList = new ArrayList<>();
    private List<GoalSub> mCurrentGoalSubList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_update);

        LL = findViewById(R.id.LL);
        etGoal = findViewById(R.id.etGoal);
        btnUpdate = findViewById(R.id.btn_update_complete);

        Intent intent = getIntent();
        mCurrentGoalItem = (Goal) intent.getSerializableExtra("EXTRA_GOAL");
        onGoalSubDataLoad(mCurrentGoalItem.getGoalTitle(), String.valueOf(mCurrentGoalItem.getIndexNumber()));

        btnUpdate.setOnClickListener(view -> {
            try {
                for (int i = 0; i < mSubItemViewList.size(); i++) {
                    GoalSub goalSub = mCurrentGoalSubList.get(i);
                    goalSub.setSubTitle(mSubItemViewList.get(i).etInput.getText().toString());
                    GoalSubDAOFactory.updateGoalSub(getApplicationContext(), goalSub);
                }
                mCurrentGoalItem.setGoalTitle(etGoal.getText().toString());
                GoalDAOFactory.updateGoal(getApplicationContext(), mCurrentGoalItem);
                setResult(RESULT_OK);
                finish();
            } catch (Exception e) {

            }
        });
    }

    public View getChildView() {
        View childView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_sub_goal_holder, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        childView.setLayoutParams(params);
        return childView;
    }

    private void addSubView(GoalSub goalSub, int pos) {
        View childView = getChildView();
        EditText etSubGoal = (EditText) childView.findViewById(R.id.EditText2);
        etSubGoal.setText(goalSub.getSubTitle());
        mSubItemViewList.add(new SubItemView(etSubGoal));
        LL.addView(childView);
    }

    private void onGoalSubDataLoad(String title, String addedByUser) {
        etGoal.setText(title);
        //쓰레드를 이용하여 데이터를 호출한다.. 데이터호출로직은 백그라운드에서 처리되어야한다.
        GoalSubDataTask task = new GoalSubDataTask.Builder().setFetcher(new GoalSubDataTask.DataFetcher() {
            @Override
            public List<GoalSub> getData() {
                try {
                    //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                    return GoalSubDAOFactory.getGoalSubList(getApplicationContext(), addedByUser);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).setCallback(new GoalSubDataTask.TaskListener() {
            @Override
            public void onComplete(List<GoalSub> data) {
                L.i("::::[onGoalSubDataLoad] CallBack " + data);
                if (data != null) {
                    int index = 0;
                    mCurrentGoalSubList = data;
                    for (GoalSub goalSub : data) {
                        L.i("::::goalSub : " + goalSub.toString());
                        addSubView(goalSub, index);
                        index += 1;
                    }
                }

            }
        }).build();
        task.execute();
    }

}
