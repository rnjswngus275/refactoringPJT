package com.ssafy.finalpjt.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.ssafy.finalpjt.db.model.Goal
import com.ssafy.finalpjt.db.model.GoalSub
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.databinding.ActivityDetailUpdateBinding
import java.lang.Exception
import java.util.ArrayList

class DetailUpdateActivity : AppCompatActivity() {
    private var mCurrentGoalItem: Goal = Goal()
    private val mSubItemViewList: ArrayList<SubItemView> = ArrayList()
    private var mCurrentGoalSubList = ArrayList<GoalSub>()
    private lateinit var binding: ActivityDetailUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        LL = findViewById(R.id.LL)
//        etGoal = findViewById(R.id.etGoal)
//        btnUpdate = findViewById(R.id.btn_update_complete)
        val intent: Intent = intent
        mCurrentGoalItem = (intent.getSerializableExtra("EXTRA_GOAL") as Goal?)!!
        onGoalSubDataLoad(
            mCurrentGoalItem.goalTitle,
            mCurrentGoalItem.indexNumber.toString()
        )
        binding.btnUpdateComplete.setOnClickListener(View.OnClickListener {
            try {
                for (i in mSubItemViewList.indices) {
                    val goalSub: GoalSub = mCurrentGoalSubList[i]
                    if (goalSub != null) {
                        goalSub.subTitle = (mSubItemViewList[i].etInput!!.text.toString())
                    }
                    GoalSubDAOFactory.updateGoalSub(applicationContext, goalSub)
                }
                mCurrentGoalItem.goalTitle = (binding.etGoal.text.toString())
                GoalDAOFactory.updateGoal(applicationContext, mCurrentGoalItem)
                setResult(RESULT_OK)
                finish()
            } catch (e: Exception) {
            }
        })
    }

    private val childView: View
        get() {
            val childView: View = LayoutInflater.from(applicationContext)
                .inflate(R.layout.item_sub_goal_holder, null)
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.weight = 1f
            childView.layoutParams = params
            return childView
        }

    private fun addSubView(goalSub: GoalSub?, pos: Int) {
        val childView: View = childView
        val etSubGoal: EditText = childView.findViewById<View>(R.id.EditText2) as EditText
        if (goalSub != null) {
            etSubGoal.setText(goalSub.subTitle)
        }
        mSubItemViewList.add(SubItemView(etSubGoal))
        binding.LL.addView(childView)
    }

    private fun onGoalSubDataLoad(title: String?, addedByUser: String) {
        binding.etGoal.setText(title)
        //쓰레드를 이용하여 데이터를 호출한다.. 데이터호출로직은 백그라운드에서 처리되어야한다.
        val task: GoalSubDataTask =
            GoalSubDataTask.Builder().setFetcher(object : GoalSubDataTask.DataFetcher {
                //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                override val data: List<GoalSub?>?
                    get() {
                        try {
                            //getGoalList 함수를 통해 최종목표 리스트들을 호출한다.
                            return GoalSubDAOFactory.getGoalSubList(
                                applicationContext,
                                addedByUser
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            return null
                        }
                    }
            }).setCallback(object : GoalSubDataTask.TaskListener {
                override fun onComplete(data: List<GoalSub?>?) {
                    if (data != null) {
                        var index: Int = 0
                        mCurrentGoalSubList = data as ArrayList<GoalSub>
                        for (goalSub: GoalSub? in data) {
                            addSubView(goalSub, index)
                            index += 1
                        }
                    }
                }
            }).build()
        task!!.execute()
    }
}
