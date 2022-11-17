package com.ssafy.finalpjt

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.gun0912.tedonactivityresult.model.ActivityResult
import com.ssafy.finalpjt.db.GoalDataTask
import com.ssafy.finalpjt.db.model.Goal
import io.reactivex.functions.Consumer
import java.lang.Exception
import java.util.ArrayList

class FragmentMain : Fragment() {
    var recyclerView: RecyclerView? = null
    var recyclerAdapter: RecyclerAdapter? = null
    var btnAdd: ImageButton? = null
    var btn: Button? = null
    var dbHelper: DBHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.setLayoutManager(linearLayoutManager)
        recyclerAdapter = object : RecyclerAdapter() {
            override fun setSelected(goal: Goal?) {
                val intent = Intent(view.context, DetailActivity::class.java)
                intent.putExtra("EXTRA_GOAL", goal)
                TedRxOnActivityResult.with(context)
                    .startActivityForResult(Intent(intent))
                    .subscribe(Consumer { activityResult: ActivityResult ->
                        //성공적으로 데이터 수정이 이루어졌다면?
                        if (activityResult.resultCode == Activity.RESULT_OK) {
                            onGoalDataLoad()
                        }
                    }, Consumer { obj: Throwable -> obj.printStackTrace() })
            }

            override fun setDeleted(goal: Goal?, index: Int) {
                //롱클릭 눌렀을시 삭제
                try {
                    GoalDAOFactory.removeGoal(view.context, goal)
                    recyclerAdapter!!.removeItem(index)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        recyclerView.setAdapter(recyclerAdapter)
        btnAdd = view.findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener(View.OnClickListener {
            Log.d("onclick", "onClick: ")
            TedRxOnActivityResult.with(context)
                .startActivityForResult(Intent(activity, AddActivity::class.java))
                .subscribe(Consumer { activityResult: ActivityResult ->
                    L.i(
                        "[TedRxOnActivityResult] $activityResult"
                    )
                    if (activityResult.resultCode == Activity.RESULT_OK) {
                        //Intent data = activityResult.getData();
                        onGoalDataLoad()
                    }
                }, Consumer { obj: Throwable -> obj.printStackTrace() })
        })
        onGoalDataLoad()
        return view
    }

    private fun onGoalDataLoad() {
        //쓰레드를 이용하여 데이터를 호출한다.. 데이터호출로직은 백그라운드에서 처리되어야한다.
        val task: GoalDataTask? =
            GoalDataTask.Builder().setFetcher(object : GoalDataTask.DataFetcher {
                //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                override val data: List<Goal>?
                    get() = try {
                        //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                        GoalDAOFactory.getGoalList(activity)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
            }).setCallback { data ->
                L.i("::::[onGoalDataLoad] CallBack $data")
                if (data != null) {
                    recyclerAdapter!!.updateItems(data)
                }
            }.build()
        task.execute()
    }

    abstract inner class RecyclerAdapter : RecyclerView.Adapter<ItemViewHolder?>() {
        private var listdata: MutableList<Goal>? = null

        //10번 인덱스까지 색상을 변경할 배경을 만들도록한다.
        private val caredViewBackGround = intArrayOf(
            Color.parseColor("#e9f7e1"), Color.parseColor("#f4d9e3"),
            Color.parseColor("#fdf2d8"), Color.parseColor("#e5dee1"),
            Color.parseColor("#e3eedc"), Color.parseColor("#e5fef5"),
            Color.parseColor("#fcedd7"), Color.parseColor("#fbe7e5"),
            Color.parseColor("#d2efe3"), Color.parseColor("#d6e2fc")
        )
        private val mListener: AdapterView.OnItemClickListener? = null
        abstract fun setSelected(goal: Goal?)
        abstract fun setDeleted(goal: Goal?, index: Int)
        fun updateItems(items: List<Goal>?) {
            if (listdata == null) {
                listdata = ArrayList<Goal>()
            }
            listdata!!.clear()
            listdata!!.addAll(items!!)
            notifyDataSetChanged()
        }

        fun removeItem(position: Int) {
            if (listdata != null && position < listdata!!.size) {
                listdata!!.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, listdata!!.size)
            }
        }

        override fun onCreateViewHolder(
            viewGroup: ViewGroup,
            i: Int
        ): ItemViewHolder { //recycler 빼기
            val view =
                LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item, viewGroup, false)
            return ItemViewHolder(view)
        }

        val itemCount: Int
            get() = if (listdata == null) 0 else listdata!!.size

        override fun onBindViewHolder(itemViewHolder: ItemViewHolder, i: Int) {
            val goal: Goal = listdata!![i]
            itemViewHolder.maintext.setText(goal.getGoalTitle())
            if (dbHelper!!.isMainQuestinRate(goal.getGoalTitle()) == false) {
                itemViewHolder.progress_num.text = "0%"
            } else {
                itemViewHolder.progress_num.text =
                    dbHelper!!.selectRate(goal.getGoalTitle()).toString() + "%"
            }
            itemViewHolder.progressBar.progress = dbHelper!!.selectRate(goal.getGoalTitle())
            itemViewHolder.cardView.setBackgroundColor(
                if (i < 10) caredViewBackGround[i] else Color.parseColor(
                    "#ffffff"
                )
            )

            //Main 목표의 각 Item 하나씩 클릭할떄마다 발생하는 클릭이벤트
            itemViewHolder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
                setSelected(
                    goal
                )
            })
            itemViewHolder.itemView.setOnLongClickListener(View.OnLongClickListener {
                val builder = AlertDialog.Builder(
                    activity!!
                )
                builder.setMessage("삭제하시겠습니까?")
                builder.setPositiveButton(
                    "예"
                ) { dialog, which ->
                    setDeleted(goal, i)
                    Toast.makeText(activity!!.applicationContext, "삭제하였습니다.", Toast.LENGTH_LONG)
                        .show()
                }
                builder.setNegativeButton(
                    "아니오"
                ) { dialog, which ->
                    Toast.makeText(
                        activity!!.applicationContext,
                        "아니오를 선택했습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                builder.show()
                true
            })
        }

        internal inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val maintext: TextView
            val progress_num: TextView
            val progressBar: ProgressBar
            val cardView: CardView

            init {
                maintext = itemView.findViewById(R.id.item_maintext)
                progress_num = itemView.findViewById(R.id.percent_num_main)
                progressBar = itemView.findViewById(R.id.progress_main)
                cardView = itemView.findViewById<CardView>(R.id.cv)
            }
        }
    }
}