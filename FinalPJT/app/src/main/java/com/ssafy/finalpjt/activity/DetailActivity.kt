package com.ssafy.finalpjt.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.DetailActivityViewModel
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.adapter.AddAdapter
import com.ssafy.finalpjt.adapter.DetailAdapter
import com.ssafy.finalpjt.databinding.ActivityDetailBinding
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.databinding.ActivityAddBinding


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
        Log.d("싸피", "onCreate: $mCurrentGoalId")

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
            addItemDecoration(DividerItemDecoration(this@DetailActivity, LinearLayoutManager.VERTICAL))
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
