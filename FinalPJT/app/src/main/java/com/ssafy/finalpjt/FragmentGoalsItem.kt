package com.ssafy.finalpjt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ssafy.finalpjt.databinding.FragmentGoalsBinding

class FragmentGoalsItem constructor() : Fragment() {
    var main: Long? = null
    var main_: String? = null
    var sub_goal_1: TextView? = null
    var sub_list: LinearLayout? = null
    var sleep: TextView? = null
    var sleep2: TextView? = null
    var sleep3: TextView? = null
    var count: Int = 0
    var percent: Int = 0
    fun getInstance(str: String, main: String?) {
        this.main = str.toLong()
        main_ = main
    }
    private lateinit var binding: FragmentGoalsBinding

    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.goals_item_fragment, container, false)
        val view2: View = inflater.inflate(R.layout.goals_sub_item_fragment, container, false)
        val linear: LinearLayout = view2.findViewById(R.id.linear)
        val dbHelper: DBHelper = DBHelper(view.getContext(), "QuestApp.db", null, 1)
        val str: Array<String> = dbHelper.SubQuest(main).split("\n").toTypedArray()
        for (a in str.indices) {
            Log.e("sub", "" + str.get(a))
        }
        val textView: TextView = view.findViewById<View>(R.id.percent_num) as TextView
        val progress: ProgressBar = view.findViewById<View>(R.id.progress) as ProgressBar
        progress.setProgress(dbHelper.selectRate(main_))
        textView.setText(dbHelper.selectRate(main_).toString() + "%")
        for (i in str.indices) {
            val goals_holder: FragmentGoalsSubItem = FragmentGoalsSubItem(getContext())
            val sub_list: LinearLayout = view.findViewById<View>(R.id.sub_list) as LinearLayout
            sub_list.addView(goals_holder)
            val tv: TextView = goals_holder.findViewById(R.id.holder_text)
            val check: CheckBox = goals_holder.findViewById(R.id.checkBox)
            if (dbHelper.selectRate(main_) >= (((i + 1).toDouble() / str.size.toDouble()) * 100).toInt()) {
                check.setChecked(true)
                count++
            }
            val finalI: Int = i
            check.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                public override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
                    if (b) {
                        count++
                    } else count--
                    percent = ((count.toDouble() / str.size.toDouble()) * 100).toInt()
                    dbHelper.updateRate(main_, percent)
                    Log.e("progress", "percent: " + percent)
                    progress.setProgress(dbHelper.selectRate(main_))
                    textView.setText(percent.toString() + "%")
                }
            })
            Log.e("str", "onCreateView: " + str.get(i))
            tv.setText(str.get(i))
        }
        return view
    }
}