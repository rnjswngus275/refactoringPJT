package com.ssafy.finalpjt

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.databinding.FragmentMainBinding
import com.ssafy.finalpjt.db.GoalDataTask
import com.ssafy.finalpjt.db.factory.GoalDAOFactory
import com.ssafy.finalpjt.db.model.Goal

private lateinit var binding: FragmentMainBinding

class FragmentMain : Fragment() {
    //    var recyclerView: RecyclerView? = null
    lateinit var recyclerAdapter: RecyclerAdapter

    //    var btnAdd: ImageButton? = null
//    var btn: Button? = null
    var dbHelper: DBHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        dbHelper = DBHelper(view?.context, "QuestApp.db", null, 1)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.layoutManager = linearLayoutManager
        recyclerAdapter = object : RecyclerAdapter() {
            override fun setSelected(goal: Goal?) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EXTRA_GOAL", goal)

                val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result: ActivityResult ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        //성공적으로 데이터 수정이 이루어졌다면?
                        onGoalDataLoad()
                    }
                }
                launcher.launch(intent)
            }

            override fun setDeleted(goal: Goal?, index: Int) {
                //롱클릭 눌렀을시 삭제
                try {
                    if (goal != null) {
                        GoalDAOFactory.removeGoal(requireContext(), goal)
                    }
                    recyclerAdapter.removeItem(index)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        binding.recyclerview.adapter = recyclerAdapter

        binding.btnAdd.setOnClickListener(View.OnClickListener {
            var intent = Intent(activity, AddActivity::class.java)
            val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    //수정 화면에서 수정완료 버튼을 누를시. 해당 화면은 finish 처리한다.
                    onGoalDataLoad()
                }
            }
            launcher.launch(intent)
        })
        onGoalDataLoad()
        return binding.root
    }

    private fun onGoalDataLoad() {
        //쓰레드를 이용하여 데이터를 호출한다.. 데이터호출로직은 백그라운드에서 처리되어야한다.
        val task: GoalDataTask =
            GoalDataTask.Builder().setFetcher(object : GoalDataTask.DataFetcher {
                //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                override val data: List<Goal>?
                    get() = try {
                        //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                        activity?.let { GoalDAOFactory.getGoalList(it) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
            }).setCallback( object : GoalDataTask.TaskListener {
                override fun onComplete(data: List<Goal>?) {
                    if (data != null) {
                        recyclerAdapter.updateItems(data)
                    }
                }
            }).build()
        task.execute()
    }

    abstract inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {
        private var listdata = mutableListOf<Goal>()

        //10번 인덱스까지 색상을 변경할 배경을 만들도록한다.
        private val cardViewBackGround = intArrayOf(
            Color.parseColor("#e9f7e1"), Color.parseColor("#f4d9e3"),
            Color.parseColor("#fdf2d8"), Color.parseColor("#e5dee1"),
            Color.parseColor("#e3eedc"), Color.parseColor("#e5fef5"),
            Color.parseColor("#fcedd7"), Color.parseColor("#fbe7e5"),
            Color.parseColor("#d2efe3"), Color.parseColor("#d6e2fc")
        )
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
                LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.main_list_item, viewGroup, false)
            return ItemViewHolder(view)
        }
        override fun onBindViewHolder(itemViewHolder: ItemViewHolder, i: Int) {
            val goal: Goal = listdata[i]
            itemViewHolder.bindInfo(goal)
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

        override fun getItemCount(): Int {
            return listdata.size
        }
        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val maintext: TextView = itemView.findViewById(R.id.item_maintext)
            val progressNum: TextView = itemView.findViewById(R.id.percent_num_main)
            val progressBar: ProgressBar = itemView.findViewById(R.id.progress_main)
            val cardView: CardView = itemView.findViewById(R.id.cv)

            fun bindInfo(goal: Goal) {
                maintext.text = goal.goalTitle
                progressNum.text = if (dbHelper!!.isMainQuestinRate(goal.goalTitle)) {
                    "0%"
                } else {
                    "${dbHelper!!.selectRate(goal.goalTitle)}%"
                }
                progressBar.progress = dbHelper!!.selectRate(goal.goalTitle)
                cardView.setBackgroundColor(cardViewBackGround[layoutPosition % 10])
            }
        }
    }
}