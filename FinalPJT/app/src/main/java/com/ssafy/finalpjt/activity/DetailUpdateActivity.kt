package com.ssafy.finalpjt.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.viewmodel.DetailUpdateActivityViewModel
import com.ssafy.finalpjt.adapter.AddAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.databinding.ActivityAddBinding

class DetailUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var detailUpdateActivityViewModel: DetailUpdateActivityViewModel
    private lateinit var detailUpdateAdapter: AddAdapter
    private var mSubGoalList : MutableList<GoalSub> = arrayListOf()
    private var mCurrentGoalId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mCurrentGoalId = intent.getLongExtra("goalID", 0)

        detailUpdateActivityViewModel = DetailUpdateActivityViewModel(mCurrentGoalId)

        detailUpdateAdapter = AddAdapter()

        detailUpdateActivityViewModel.originalList.observe(this) {
            detailUpdateActivityViewModel.mSubGoalList = it
            detailUpdateActivityViewModel.setSubGoal()
        }

        detailUpdateActivityViewModel.goal.observe(this) {
            binding.goalEt.setText(it.GoalTitle)
        }

        detailUpdateActivityViewModel.subGoalList.observe(this) {
            detailUpdateAdapter.subGoalList = it
            detailUpdateAdapter.notifyDataSetChanged()
        }

        binding.addRecyclerView.apply {
            adapter = detailUpdateAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.addSubBtn.setOnClickListener {
            detailUpdateActivityViewModel.addSubGoal()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnDone.setOnClickListener {
            if (binding.goalEt.text.isEmpty()) {
                Toast.makeText(this, "최종목표를 추가해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val goal = Goal(mCurrentGoalId, binding.goalEt.text.toString())
                try {
                    detailUpdateActivityViewModel.addAndUpdate(goal)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                finish()
            }
        }

    }

}
