package com.ssafy.finalpjt.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.viewmodel.DetailActivityViewModel
import com.ssafy.finalpjt.adapter.DetailAdapter
import com.ssafy.finalpjt.databinding.ActivityDetailBinding
import com.ssafy.finalpjt.database.dto.GoalSub


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailActivityViewModel: DetailActivityViewModel
    private lateinit var detailAdapter: DetailAdapter
    private var mCurrentGoalId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent: Intent = intent
        mCurrentGoalId = intent.getLongExtra("goalID", 0)

        detailActivityViewModel = DetailActivityViewModel(mCurrentGoalId)

        detailAdapter = DetailAdapter().apply {
            this.menuItemClickListener = object : DetailAdapter.MenuItemClickListener {
                override fun onClick(goalSub: GoalSub) {
                    detailActivityViewModel.deleteSubGoal(goalSub)
                }
            }
        }

        initView()
    }

    private fun initView() {
        detailActivityViewModel.goal.observe(this) {
            binding.goalTv.text = it.GoalTitle
        }

        detailActivityViewModel.subGoalList.observe(this) {
            detailAdapter.subGoalList = it
            detailAdapter.notifyDataSetChanged()
        }

        binding.detailRecyclerView.apply {
            adapter = detailAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                DividerItemDecoration(
                    this@DetailActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        binding.btnUpdate.setOnClickListener {
            val intent = Intent(this, DetailUpdateActivity::class.java).apply {
                putExtra("goalID", mCurrentGoalId)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }
    }
}
