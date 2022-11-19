package com.ssafy.finalpjt.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.GoalSubRepository
import com.ssafy.finalpjt.database.repository.TodoRepository
import com.ssafy.finalpjt.databinding.ActivityAddBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var mLinearLayout: LinearLayout
    private var mSubItemViewList = ArrayList<SubItemView>()
    private lateinit var goalRepository: GoalRepository
    private lateinit var goalSubRepository: GoalSubRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        goalRepository=GoalRepository.get()
        mLinearLayout = findViewById(R.id.addLinearLayout)
        addSubView()
        binding.addSubBtn.setOnClickListener { addSubView() }
        binding.btnDone.setOnClickListener(View.OnClickListener {
            if (binding.edtGoal.text.isEmpty()) {
                Toast.makeText(this, "최종목표를 추가해주세요", Toast.LENGTH_SHORT).show()
            }
            val goalTitle = binding.edtGoal.text.toString()
            try {
                var goal =Goal()

                goal.GoalTitle=goalTitle

                CoroutineScope(Dispatchers.IO).launch {
                    goalRepository.insertGoal(goal)
                }
                val id = goalRepository.getGoalId(goal.GoalTitle)
                if (id != -1) {
                    // 최종목표 db에 성공적으로 저장되었으면 서브 타이틀 db에 데이터를 입력한다.
                    for (subItemView in mSubItemViewList) {
                        if (subItemView.etInput!!.text.isNotEmpty()) {
                            var goalsub= GoalSub(id,subItemView.etInput!!.text.toString(),0)
                            CoroutineScope(Dispatchers.IO).launch {
                                goalSubRepository.insertGoal(goalsub)
                            }
                        }
                    }
                    setResult(RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        binding.btnNo.setOnClickListener {
            finish()
        }
    }

    private val childView: View
        get() {
            val childView =
                LayoutInflater.from(applicationContext).inflate(R.layout.item_sub_goal_holder, null)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.weight = 1f
            childView.layoutParams = params
            return childView
        }

    private fun addSubView() {
        val childView = childView
        val etSubGoal = childView.findViewById<View>(R.id.EditText2) as EditText
        mSubItemViewList.add(SubItemView(etSubGoal))
        mLinearLayout.addView(childView)
    }
}
