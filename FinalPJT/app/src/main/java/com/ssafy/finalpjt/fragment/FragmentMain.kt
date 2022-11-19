package com.ssafy.finalpjt.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.FragmentMainViewModel
import com.ssafy.finalpjt.activity.AddActivity
import com.ssafy.finalpjt.activity.DetailActivity
import com.ssafy.finalpjt.adapter.FragmentMainAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.databinding.FragmentMainBinding

class FragmentMain : Fragment() {
    private var goalList: MutableList<Goal> = arrayListOf()
    private lateinit var binding: FragmentMainBinding
    private lateinit var fragmentMainAdapter: FragmentMainAdapter
    private val fragmentMainViewModel: FragmentMainViewModel by viewModels()
    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            //수정 화면에서 수정완료 버튼을 누를시. 해당 화면은 finish 처리한다.
            onGoalDataLoad()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        initView()

        onGoalDataLoad()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 0) {
            val position = fragmentMainAdapter.getPosition()
            try {
                GoalDAOFactory.removeGoal(requireContext(), goalList[position])
                if (position < goalList.size) {
                    goalList.removeAt(position)
                    refreshData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun initAdapter() {
        fragmentMainAdapter = FragmentMainAdapter().apply {
            this.itemClickListener = object : FragmentMainAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    fragmentMainViewModel.setSelectedGoal(goalList[position])
                    launcher.launch(intent)
                }
            }
        }
        fragmentMainViewModel.goalList.observe(viewLifecycleOwner) {
            fragmentMainAdapter.goalList = it
            fragmentMainAdapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        binding.recyclerview.apply {
            adapter = fragmentMainAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        binding.btnAdd.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, AddActivity::class.java)
            launcher.launch(intent)
        })
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
                        refreshData()
                    }
                }
            }).build()
        task.execute()
    }

    private fun refreshData() {
        fragmentMainAdapter.goalList = this@FragmentMain.goalList
        (binding.recyclerview.adapter as FragmentMainAdapter).notifyDataSetChanged()
    }

    //        recyclerAdapter = object : RecyclerAdapter() {
//            override fun setSelected(goal: Goal?) {
//                val intent = Intent(requireContext(), DetailActivity::class.java)
//                intent.putExtra("EXTRA_GOAL", goal)
//                launcher.launch(intent)
//            }
//            override fun setDeleted(goal: Goal?, index: Int) {
//                //롱클릭 눌렀을시 삭제
//                try {
//                    if (goal != null) {
//                        GoalDAOFactory.removeGoal(requireContext(), goal)
//                    }
//                    recyclerAdapter.removeItem(index)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }


//    abstract inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {
//        private var listdata = mutableListOf<Goal>()
//
//        //10번 인덱스까지 색상을 변경할 배경을 만들도록한다.
//        private val cardViewBackGround = intArrayOf(
//            Color.parseColor("#e9f7e1"), Color.parseColor("#f4d9e3"),
//            Color.parseColor("#fdf2d8"), Color.parseColor("#e5dee1"),
//            Color.parseColor("#e3eedc"), Color.parseColor("#e5fef5"),
//            Color.parseColor("#fcedd7"), Color.parseColor("#fbe7e5"),
//            Color.parseColor("#d2efe3"), Color.parseColor("#d6e2fc")
//        )
//        abstract fun setSelected(goal: Goal?)
//        abstract fun setDeleted(goal: Goal?, index: Int)
//        fun updateItems(items: List<Goal>?) {
//            if (listdata == null) {
//                listdata = ArrayList<Goal>()
//            }
//            listdata!!.clear()
//            listdata!!.addAll(items!!)
//            notifyDataSetChanged()
//        }
//
//        fun removeItem(position: Int) {
//            if (listdata != null && position < listdata!!.size) {
//                listdata!!.removeAt(position)
//                notifyItemRemoved(position)
//                notifyItemRangeChanged(position, listdata!!.size)
//            }
//        }
//        override fun onCreateViewHolder(
//            viewGroup: ViewGroup,
//            i: Int
//        ): ItemViewHolder { //recycler 빼기
//            val view =
//                LayoutInflater.from(viewGroup.context)
//                    .inflate(R.layout.main_list_item, viewGroup, false)
//            return ItemViewHolder(view)
//        }
//        override fun onBindViewHolder(itemViewHolder: ItemViewHolder, i: Int) {
//            val goal: Goal = listdata[i]
//            itemViewHolder.bindInfo(goal)
//            //Main 목표의 각 Item 하나씩 클릭할떄마다 발생하는 클릭이벤트
//            itemViewHolder.itemView.setOnClickListener(View.OnClickListener { view: View? ->
//                setSelected(
//                    goal
//                )
//            })
//            itemViewHolder.itemView.setOnLongClickListener(View.OnLongClickListener {
//                val builder = AlertDialog.Builder(
//                    activity!!
//                )
//                builder.setMessage("삭제하시겠습니까?")
//                builder.setPositiveButton(
//                    "예"
//                ) { dialog, which ->
//                    setDeleted(goal, i)
//                    Toast.makeText(activity!!.applicationContext, "삭제하였습니다.", Toast.LENGTH_LONG)
//                        .show()
//                }
//                builder.setNegativeButton(
//                    "아니오"
//                ) { dialog, which ->
//                    Toast.makeText(
//                        activity!!.applicationContext,
//                        "아니오를 선택했습니다.",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                builder.show()
//                true
//            })
//        }
//
//        override fun getItemCount(): Int {
//            return listdata.size
//        }
//        inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//            val maintext: TextView = itemView.findViewById(R.id.item_maintext)
//            val progressNum: TextView = itemView.findViewById(R.id.percent_num_main)
//            val progressBar: ProgressBar = itemView.findViewById(R.id.progress_main)
//            val cardView: CardView = itemView.findViewById(R.id.cv)
//
//            fun bindInfo(goal: Goal) {
//                maintext.text = goal.goalTitle
//                progressNum.text = if (dbHelper!!.isMainQuestinRate(goal.goalTitle)) {
//                    "0%"
//                } else {
//                    "${dbHelper!!.selectRate(goal.goalTitle)}%"
//                }
//                progressBar.progress = dbHelper!!.selectRate(goal.goalTitle)
//                cardView.setBackgroundColor(cardViewBackGround[layoutPosition % 10])
//            }
//        }
//    }
}