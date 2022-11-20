package com.ssafy.finalpjt.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.DetailActivityViewModel
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.adapter.AddAdapter
import com.ssafy.finalpjt.databinding.ActivityDetailBinding
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.databinding.ActivityAddBinding


class DetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddBinding
    private lateinit var detailActivityViewModel: DetailActivityViewModel
    private lateinit var detailAdapter: AddAdapter
    private var mSubGoalList: MutableList<GoalSub> = arrayListOf()
    private var deleteSubGoalId: MutableList<Long> = arrayListOf()
    private var mCurrentGoalId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = intent
        mCurrentGoalId = intent.getLongExtra("goalID", 0)

        initData()

        detailAdapter = AddAdapter().apply {
            this.menuItemClickListener = object : AddAdapter.MenuItemClickListener {
                override fun onClick(position: Int) {
                    mSubGoalList.removeAt(position)
                    deleteSubGoalId.add(mSubGoalList[position].id)
                }
            }
        }

        initView()
    }

    private fun initData() {
        detailActivityViewModel = DetailActivityViewModel(mCurrentGoalId)

        detailActivityViewModel.originalSubGoalList.value?.let {
            detailActivityViewModel.setSubGoalList(it)
        }
    }

    private fun initView() {
        detailActivityViewModel.goal.observe(this) {
            binding.goalEt.setText(it.GoalTitle)
        }

        detailActivityViewModel.liveSubGoalList.observe(this) {
            detailAdapter.subGoalList = it
            detailAdapter.notifyDataSetChanged()
        }

        binding.addRecyclerView.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.btnDone.setOnClickListener {
            if (binding.goalEt.text.isEmpty()) {
                Toast.makeText(this, "최종목표를 추가해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val goal = Goal().apply {
                    this.id = mCurrentGoalId
                    this.GoalTitle = binding.goalEt.text.toString()
                }
                detailActivityViewModel.updateGoal(goal)
                try {
                    detailActivityViewModel.deleteSubGoal(deleteSubGoalId)
                    detailActivityViewModel.updateSubGoal()
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
