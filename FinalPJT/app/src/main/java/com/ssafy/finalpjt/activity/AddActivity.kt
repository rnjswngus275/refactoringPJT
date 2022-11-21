package com.ssafy.finalpjt.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.AddActivityViewModel
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.adapter.AddAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.databinding.ActivityAddBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var addAdapter: AddAdapter
    private val addActivityViewModel: AddActivityViewModel by viewModels()
    private var mSubGoalList: MutableList<GoalSub> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()

        initObserve()

        initView()
    }

    private fun initAdapter() {
        addAdapter = AddAdapter().apply {
            this.menuItemClickListener = object : AddAdapter.MenuItemClickListener {
                override fun onClick(position: Int) {
                    mSubGoalList.removeAt(position)
                    addActivityViewModel.setSubGoalList(mSubGoalList)
                }
            }
        }
    }

    private fun initObserve() {
        addActivityViewModel.subGoalList.observe(this) {
            addAdapter.subGoalList = it
            addAdapter.notifyDataSetChanged()
        }

        addActivityViewModel.setSubGoalList(mSubGoalList)
    }

    private fun initView() {
        binding.addRecyclerView.apply {
            adapter = addAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.addSubBtn.setOnClickListener {
            mSubGoalList.add(GoalSub())
            addActivityViewModel.setSubGoalList(mSubGoalList)
        }

        binding.btnDone.setOnClickListener(View.OnClickListener {
            if (binding.goalEt.text.isEmpty()) {
                Toast.makeText(this, "최종목표를 추가해주세요", Toast.LENGTH_SHORT).show()
            }
            val goalTitle = binding.goalEt.text.toString()
            try {
                val goal = Goal()
                goal.GoalTitle = goalTitle
                val goalId = addActivityViewModel.insertGoal(goal)
                if (goalId != -1L) {
                    // 최종목표 db에 성공적으로 저장되었으면 서브 타이틀 db에 데이터를 입력한다.
                    addActivityViewModel.insertSubGoalList(goalId,
                        mSubGoalList as ArrayList<GoalSub>
                    )
                    setResult(RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

}
