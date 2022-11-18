package com.ssafy.finalpjt.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.databinding.ActivityDetailBinding
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub


private lateinit var binding:ActivityDetailBinding
class DetailActivity constructor() : AppCompatActivity() {


    private val mSubItemViewList: ArrayList<SubItemView> = ArrayList()
    private var mCurrentGoalSubList=ArrayList<GoalSub>()
    private var mCurrentGoalItem: Goal = Goal()
    private val deleteClickListener: View.OnLongClickListener =
        View.OnLongClickListener { view ->
            try {
                val pos: Int = view.tag as Int
                val isDelete: Boolean = GoalSubDAOFactory.removeGoalSub(
                    applicationContext,
                    mCurrentGoalSubList[pos]
                )
                if (isDelete) {
                    if (mCurrentGoalSubList.size > 0) {
                        mCurrentGoalSubList.removeAt(pos)
                    }
                    binding.LL.removeAllViews()
                    onGoalSubDataLoad(
                        mCurrentGoalItem.GoalTitle,
                        mCurrentGoalItem._id.toString()
                    )
                }
            } catch (e: Exception) {
            }
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent: Intent = getIntent()
        mCurrentGoalItem = (intent.getSerializableExtra("EXTRA_GOAL") as Goal?)!!
        if (mCurrentGoalItem == null) return
//        tvGoal = findViewById(R.id.tvGoal)
//        btnUpdate = findViewById(R.id.btnRe)
//        LL = findViewById(R.id.LL)
        onGoalSubDataLoad(
            mCurrentGoalItem.GoalTitle,
            mCurrentGoalItem._id.toString()
        )
        findViewById<View>(R.id.btnDone).setOnClickListener(View.OnClickListener({ view: View? -> finish() }))
        binding.btnRe.setOnClickListener(View.OnClickListener {
            //수정화면으로 전환...
            val updateIntent: Intent = Intent(this, DetailActivityUpdate::class.java)
            updateIntent.putExtra("EXTRA_GOAL", mCurrentGoalItem)

            val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
                StartActivityForResult()
            ) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    //수정 화면에서 수정완료 버튼을 누를시. 해당 화면은 finish 처리한다.
                    setResult(RESULT_OK)
                    finish()
                }
            }
            launcher.launch(updateIntent)

        })
    }

    private val childView: View
        get() {
            val childView: View = LayoutInflater.from(applicationContext)
                .inflate(R.layout.item_sub_goal_holder_detail, null)
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
        val tvSubGoal: TextView = childView.findViewById<View>(R.id.tv_title) as TextView
        tvSubGoal.text = goalSub?.SubTitle
        tvSubGoal.tag = pos
        tvSubGoal.setOnLongClickListener(deleteClickListener)
        mSubItemViewList.add(SubItemView(tvSubGoal))
        binding.LL.addView(childView)
    }

    private fun onGoalSubDataLoad(title: String?, addedByUser: String) {
        binding.tvGoal.text = title
        //쓰레드를 이용하여 데이터를 호출한다.. 데이터호출로직은 백그라운드에서 처리되어야한다.
        val task: GoalSubDataTask? =
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
                public override fun onComplete(data: List<GoalSub?>?) {
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