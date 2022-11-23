package com.ssafy.finalpjt.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.viewmodel.AddActivityViewModel
import com.ssafy.finalpjt.adapter.AddAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.databinding.ActivityAddBinding
import java.lang.Exception

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var addAdapter: AddAdapter
    private val addActivityViewModel: AddActivityViewModel by viewModels()
    private var mSubGoalList: MutableList<GoalSub> = arrayListOf(GoalSub())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addAdapter = AddAdapter()

        initObserve()

        initView()
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

        binding.btnDone.setOnClickListener {
            if (binding.goalEt.text.isEmpty()) {
                Toast.makeText(this, "최종목표를 추가해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val goalTitle = binding.goalEt.text.toString()
                try {
                    addActivityViewModel.insertGoalAndSubGoal(Goal(goalTitle), mSubGoalList)
                    setResult(RESULT_OK)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

}
