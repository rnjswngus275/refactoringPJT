package com.example.yoony.opensourceandroidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yoony.opensourceandroidproject.db.GoalSubDataTask;
import com.example.yoony.opensourceandroidproject.db.factory.GoalSubDAOFactory;
import com.example.yoony.opensourceandroidproject.db.model.Goal;
import com.example.yoony.opensourceandroidproject.db.model.GoalSub;
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    LinearLayout LL;
    TextView tvGoal;
    Button btnUpdate;
    private ArrayList<SubItemView> mSubItemViewList = new ArrayList<>();
    private List<GoalSub> mCurrentGoalSubList;
    private Goal mCurrentGoalItem;
    private View.OnLongClickListener deleteClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            L.i(":::이벤트 발생.. " + view.getTag());

            try {
                int pos = (int) view.getTag();
                boolean isDelete = GoalSubDAOFactory.removeGoalSub(getApplicationContext(), mCurrentGoalSubList.get(pos));
                if (isDelete) {
                    if (mCurrentGoalSubList != null && mCurrentGoalSubList.size() > 0) {
                        mCurrentGoalSubList.remove(pos);
                    }
                    LL.removeAllViews();
                    onGoalSubDataLoad(mCurrentGoalItem.getGoalTitle(), String.valueOf(mCurrentGoalItem.getIndexNumber()));
                }
            } catch (Exception e) {

            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        mCurrentGoalItem = (Goal) intent.getSerializableExtra("EXTRA_GOAL");

        if (mCurrentGoalItem == null) return;


        tvGoal = findViewById(R.id.tvGoal);
        btnUpdate = findViewById(R.id.btnRe);
        LL = findViewById(R.id.LL);

        onGoalSubDataLoad(mCurrentGoalItem.getGoalTitle(), String.valueOf(mCurrentGoalItem.getIndexNumber()));

        findViewById(R.id.btnDone).setOnClickListener(view -> finish());

        btnUpdate.setOnClickListener(view -> {
            //수정화면으로 전환...

            Intent updateIntent = new Intent(this, DetailActivityUpdate.class);
            updateIntent.putExtra("EXTRA_GOAL", mCurrentGoalItem);
            TedRxOnActivityResult.with(DetailActivity.this)
                    .startActivityForResult(updateIntent)
                    .subscribe(activityResult -> {
                                if (activityResult.getResultCode() == Activity.RESULT_OK) {
                                    //수정 화면에서 수정완료 버튼을 누를시. 해당 화면은 finish 처리한다.
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }, error -> {
                                //에러발생시. 예외처리
                            }
                    );
        });

    }

    public View getChildView() {
        View childView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_sub_goal_holder_detail, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        childView.setLayoutParams(params);
        return childView;
    }

    private void addSubView(GoalSub goalSub, int pos) {
        View childView = getChildView();
        TextView tvSubGoal = (TextView) childView.findViewById(R.id.tv_title);
        tvSubGoal.setText(goalSub.getSubTitle());
        tvSubGoal.setTag(pos);
        tvSubGoal.setOnLongClickListener(deleteClickListener);
        mSubItemViewList.add(new SubItemView(tvSubGoal));
        LL.addView(childView);
    }

    private void onGoalSubDataLoad(String title, String addedByUser) {
        tvGoal.setText(title);
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
                        addSubView(goalSub, index);
                        index += 1;
                    }
                }

            }
        }).build();
        task.execute();
    }

}
