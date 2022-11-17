package com.ssafy.finalpjt

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import com.ssafy.finalpjt.db.model.Goal
import com.ssafy.finalpjt.db.model.GoalSub
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpjt.db.factory.GoalDAOFactory
import com.ssafy.finalpjt.db.factory.GoalSubDAOFactory
import java.lang.Exception
import java.util.ArrayList

class AddActivity : AppCompatActivity() {
    var mSubItemViewList = ArrayList<SubItemView>()
    var LL: LinearLayout? = null
    var edtText: EditText? = null
    //test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        edtText = findViewById(R.id.edtGoal)
        val button1 = findViewById<ImageButton>(R.id.addSubBtn)
        LL = findViewById(R.id.LL)
        addSubView()
        button1.setOnClickListener { addSubView() }
        findViewById<View>(R.id.btnDone).setOnClickListener(View.OnClickListener {
            if (isempty(edtText)) {
                return@OnClickListener
            }
            val goalTitle = edtText.getText().toString()
            try {
                val id = GoalDAOFactory.addGoal(applicationContext, Goal(goalTitle))
                if (id != -1L) {
                    // 최종목표 db에 성공적으로 저장되었으면 서브 타이틀 db에 데이터를 입력한다.
                    for (subItemView in mSubItemViewList) {
                        if (!isempty(subItemView.etInput)) {
                            GoalSubDAOFactory.addGoalSub(
                                applicationContext,
                                GoalSub(id.toString(), subItemView.etInput!!.text.toString(), 0)
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
        findViewById<View>(R.id.btnNo).setOnClickListener { finish() }
    }

    val childView: View
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
        LL!!.addView(childView)
    }

    private fun isempty(et: EditText?): Boolean {
        return TextUtils.isEmpty(et!!.text.toString()) or et.text.toString()
            .equals("", ignoreCase = true)
    }
}