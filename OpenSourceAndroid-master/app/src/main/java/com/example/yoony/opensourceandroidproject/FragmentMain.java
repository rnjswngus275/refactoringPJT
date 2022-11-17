package com.example.yoony.opensourceandroidproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yoony.opensourceandroidproject.db.GoalDataTask;
import com.example.yoony.opensourceandroidproject.db.factory.GoalDAOFactory;
import com.example.yoony.opensourceandroidproject.db.model.Goal;
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment {
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ImageButton btnAdd;
    Button btn;
    DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        dbHelper = new DBHelper(view.getContext(), "QuestApp.db", null, 1);

        recyclerView = view.findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new RecyclerAdapter() {
            @Override
            void setSelected(Goal goal) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("EXTRA_GOAL", goal);
                TedRxOnActivityResult.with(getContext())
                        .startActivityForResult(new Intent(intent))
                        .subscribe(activityResult -> {
                            //성공적으로 데이터 수정이 이루어졌다면?
                            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                                onGoalDataLoad();
                            }

                        }, Throwable::printStackTrace);
            }

            @Override
            void setDeleted(Goal goal, int index) {
                //롱클릭 눌렀을시 삭제
                try {
                    GoalDAOFactory.removeGoal(view.getContext(), goal);
                    recyclerAdapter.removeItem(index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("onclick", "onClick: ");
                TedRxOnActivityResult.with(getContext())
                        .startActivityForResult(new Intent(getActivity(), AddActivity.class))
                        .subscribe(activityResult -> {
                            L.i("[TedRxOnActivityResult] " + activityResult);
                            if (activityResult.getResultCode() == Activity.RESULT_OK) {
                                //Intent data = activityResult.getData();
                                onGoalDataLoad();
                            }

                        }, Throwable::printStackTrace);
            }
        });

        onGoalDataLoad();

        return view;
    }


    private void onGoalDataLoad() {
        //쓰레드를 이용하여 데이터를 호출한다.. 데이터호출로직은 백그라운드에서 처리되어야한다.
        GoalDataTask task = new GoalDataTask.Builder().setFetcher(new GoalDataTask.DataFetcher() {
            @Override
            public List<Goal> getData() {
                try {
                    //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                    return GoalDAOFactory.getGoalList(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }).setCallback(new GoalDataTask.TaskListener() {
            @Override
            public void onComplete(List<com.example.yoony.opensourceandroidproject.db.model.Goal> data) {
                L.i("::::[onGoalDataLoad] CallBack " + data);
                if (data != null) {
                    recyclerAdapter.updateItems(data);
                }

            }
        }).build();
        task.execute();
    }

    abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
        private List<Goal> listdata;
        //10번 인덱스까지 색상을 변경할 배경을 만들도록한다.
        private int[] caredViewBackGround = new int[]{
                Color.parseColor("#e9f7e1"), Color.parseColor("#f4d9e3"),
                Color.parseColor("#fdf2d8"), Color.parseColor("#e5dee1"),
                Color.parseColor("#e3eedc"), Color.parseColor("#e5fef5"),
                Color.parseColor("#fcedd7"), Color.parseColor("#fbe7e5"),
                Color.parseColor("#d2efe3"), Color.parseColor("#d6e2fc")
        };
        private AdapterView.OnItemClickListener mListener = null;

        public RecyclerAdapter() {

        }

        abstract void setSelected(Goal goal);

        abstract void setDeleted(Goal goal, int index);

        public void updateItems(List<Goal> items) {
            if (this.listdata == null) {
                listdata = new ArrayList<>();
            }
            this.listdata.clear();
            this.listdata.addAll(items);

            notifyDataSetChanged();
        }

        public void removeItem(int position) {
            if (this.listdata != null && position < this.listdata.size()) {
                this.listdata.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, this.listdata.size());
            }
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {//recycler 빼기
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return listdata == null ? 0 : listdata.size();
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerAdapter.ItemViewHolder itemViewHolder, int i) {
            Goal goal = listdata.get(i);

            itemViewHolder.maintext.setText(goal.getGoalTitle());

            if (dbHelper.isMainQuestinRate(goal.getGoalTitle()) == false) {
                itemViewHolder.progress_num.setText(String.valueOf("0%"));
            } else {
                itemViewHolder.progress_num.setText(String.valueOf(dbHelper.selectRate(goal.getGoalTitle())) + "%");
            }
            itemViewHolder.progressBar.setProgress(dbHelper.selectRate(goal.getGoalTitle()));
            itemViewHolder.cardView.setBackgroundColor(i < 10 ? caredViewBackGround[i] : Color.parseColor("#ffffff"));

            //Main 목표의 각 Item 하나씩 클릭할떄마다 발생하는 클릭이벤트
            itemViewHolder.itemView.setOnClickListener(view -> setSelected(goal));
            itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("삭제하시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setDeleted(goal, i);
                                    Toast.makeText(getActivity().getApplicationContext(), "삭제하였습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity().getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.show();

                    return true;
                }
            });

        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView maintext;
            private TextView progress_num;
            private ProgressBar progressBar;
            private CardView cardView;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);

                maintext = itemView.findViewById(R.id.item_maintext);
                progress_num = itemView.findViewById(R.id.percent_num_main);
                progressBar = itemView.findViewById(R.id.progress_main);
                cardView = itemView.findViewById(R.id.cv);

            }
        }
    }

}