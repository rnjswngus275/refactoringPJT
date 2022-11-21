package com.ssafy.finalpjt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.adapter.FragmentGoalsItemAdapter
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.GoalSubRepository
import com.ssafy.finalpjt.databinding.FragmentGoalsItemBinding

class FragmentGoalsItem : Fragment() {
    var id: Long? = null
    var maingoal: String? = null
    private var subQuestList: ArrayList<String> = arrayListOf()
    private lateinit var goalsItemAdapter: FragmentGoalsItemAdapter
    private lateinit var binding: FragmentGoalsItemBinding
    private lateinit var goalSubRepository: GoalSubRepository
    var goalsublist= mutableListOf<GoalSub>()

    fun getInstance(id: Long, mainGoal: String?) {
        this.id = id
        maingoal = mainGoal
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        goalSubRepository=GoalSubRepository.get()
        binding = FragmentGoalsItemBinding.inflate(inflater, container, false)
        binding.progressBar.progress = getRate()
        binding.percentNum.text = "${getRate()}%"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        binding.subQuestRecyclerview.apply {
            adapter = goalsItemAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initAdapter() {
        goalsItemAdapter = FragmentGoalsItemAdapter().apply {
            this.subQuestList = this@FragmentGoalsItem.subQuestList
            this.checkChangeListener = object : FragmentGoalsItemAdapter.CheckChangeListener {
                override fun onCheckChanged(
                    view: View,
                    position: Int,
                    compoundButton: CompoundButton,
                    isChecked: Boolean
                ) {

                    binding.progressBar.progress = getRate()
                    binding.percentNum.text = "${getRate()}%"
                }
            }
        }
    }

    private fun getRate():Int{
        goalSubRepository.getGoalSub(id!!).observe(viewLifecycleOwner){
            goalsublist=it
        }
        var total=goalsublist.size
        var complete=0
        for(i in goalsublist){
            if(i.Completed==0){//미완
                complete++
            }
        }
        return complete/total
    }

}

//for (i in str.indices) {
//    val goals_holder: FragmentGoalsSubItem = FragmentGoalsSubItem(context)
//    val sub_list: LinearLayout = itemView.findViewById<View>(R.id.sub_list) as LinearLayout
//    sub_list.addView(goals_holder)
//    val tv: TextView = goals_holder.findViewById(R.id.holder_text)
//    val check: CheckBox = goals_holder.findViewById(R.id.checkBox)
//    if (dbHelper.selectRate(main_) >= (((i + 1).toDouble() / str.size.toDouble()) * 100).toInt()) {
//        check.setChecked(true)
//        count++
//    }
//    val finalI: Int = i
//    check.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//        public override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
//            if (b) {
//                count++
//            } else count--
//            percent = ((count.toDouble() / str.size.toDouble()) * 100).toInt()
//            dbHelper.updateRate(main_, percent)
//            Log.e("progress", "percent: " + percent)
//            progress.setProgress(dbHelper.selectRate(main_))
//            textView.setText(percent.toString() + "%")
//        }
//    })
//    tv.setText(str.get(i))
//}