package com.ssafy.finalpjt.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.ssafy.finalpjt.db.model.Goal
import com.ssafy.finalpjt.db.model.GoalSub
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.databinding.ActivityAddBinding
import java.lang.Exception
import java.util.ArrayList

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private lateinit var mLinearLayout: LinearLayout
    private var mSubItemViewList = ArrayList<SubItemView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mLinearLayout = findViewById(R.id.addLinearLayout)
        addSubView()
        binding.addSubBtn.setOnClickListener { addSubView() }
        binding.btnDone.setOnClickListener(View.OnClickListener {
            if (binding.edtGoal.text.isEmpty()) {
                Toast.makeText(this, "최종목표를 추가해주세요", Toast.LENGTH_SHORT).show()
            }
            val goalTitle = binding.edtGoal.text.toString()
            try {
                var goal:Goal=Goal()
                goal.goalTitle=goalTitle
                val id = GoalDAOFactory.addGoal(applicationContext, goal)
                if (id != -1L) {
                    // 최종목표 db에 성공적으로 저장되었으면 서브 타이틀 db에 데이터를 입력한다.
                    for (subItemView in mSubItemViewList) {
                        if (subItemView.etInput!!.text.isNotEmpty()) {
                            GoalSubDAOFactory.addGoalSub(
                                applicationContext,
                                GoalSub(-1,id.toString(), subItemView.etInput!!.text.toString(), 0)
                            )
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
