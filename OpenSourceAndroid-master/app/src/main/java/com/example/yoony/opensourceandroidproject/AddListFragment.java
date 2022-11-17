package com.example.yoony.opensourceandroidproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class AddListFragment extends Fragment {
    Button addBtn;
    Spinner equest;
    EditText ejob;
    DatePicker edate;
    int prevPage = 0;
    LinearLayout t, b;

    public AddListFragment() {
    }

    public void getInstance(int prevPage, LinearLayout t, LinearLayout b) {
        this.prevPage = prevPage;
        this.t = t;
        this.b = b;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.addlist_layout, null);
        addBtn = (Button) view.findViewById(R.id.addBtn);
        equest = (Spinner) view.findViewById(R.id.spinner1);
        edate = (DatePicker) view.findViewById(R.id.date);
        ejob = (EditText) view.findViewById(R.id.job);

        DBHelper dbHelper = new DBHelper(view.getContext(), "QuestApp.db", null, 1);

        String[] questdata = dbHelper.MainQuest().split("\n");
        Log.e("quest", questdata.toString());

        ArrayAdapter adapter = new ArrayAdapter(view.getContext(), R.layout.spin, questdata);
        adapter.setDropDownViewResource(R.layout.spin_dropdown);

        equest.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quest = equest.getSelectedItem().toString();
                int date = ((edate.getMonth() + 1) * 100) + edate.getDayOfMonth();
                String job = ejob.getText().toString();

                dbHelper.insert(job, date, quest);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, MyFragment.getInstace(prevPage)).addToBackStack(null).commit();//이전 프레그먼트로 돌아가야함
                b.setVisibility(View.VISIBLE);
                t.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}
