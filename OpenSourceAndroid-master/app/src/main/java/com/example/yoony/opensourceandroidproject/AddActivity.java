package com.example.yoony.opensourceandroidproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.yoony.opensourceandroidproject.db.factory.GoalDAOFactory;
import com.example.yoony.opensourceandroidproject.db.factory.GoalSubDAOFactory;
import com.example.yoony.opensourceandroidproject.db.model.Goal;
import com.example.yoony.opensourceandroidproject.db.model.GoalSub;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    public ArrayList<SubItemView> mSubItemViewList = new ArrayList<>();
    LinearLayout LL;
    EditText edtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edtText = findViewById(R.id.edtGoal);

        ImageButton button1 = findViewById(R.id.addSubBtn);
        LL = findViewById(R.id.LL);


        addSubView();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubView();
            }

        });

        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isempty(edtText)) {
                    return;
                }

                String goalTitle = edtText.getText().toString();

                try {
                    long id = GoalDAOFactory.addGoal(getApplicationContext(), new Goal(goalTitle));
                    if (id != -1) {
                        // 최종목표 db에 성공적으로 저장되었으면 서브 타이틀 db에 데이터를 입력한다.
                        for (SubItemView subItemView : mSubItemViewList) {
                            if (!isempty(subItemView.etInput)) {
                                GoalSubDAOFactory.addGoalSub(getApplicationContext(), new GoalSub(String.valueOf(id), subItemView.etInput.getText().toString(), 0));
                            }
                        }
                        setResult(RESULT_OK);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    private void addSubView() {
        View childView = getChildView();
        EditText etSubGoal = (EditText) childView.findViewById(R.id.EditText2);
        mSubItemViewList.add(new SubItemView(etSubGoal));
        LL.addView(childView);
    }

    private boolean isempty(EditText et) {
        return TextUtils.isEmpty(et.getText().toString()) | et.getText().toString().equalsIgnoreCase("");
    }
}
