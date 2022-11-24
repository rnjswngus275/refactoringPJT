package com.ssafy.finalpjt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.adapter.FragmentMyGoalsItemAdapter
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.databinding.FragmentGoalsItemBinding
import com.ssafy.finalpjt.viewmodel.FragmentMyGoalsItemViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "FragmentMyGoalsItem"
class FragmentMyGoalsItem(var id:Long) : Fragment() {

    private lateinit var goalsItemAdapter: FragmentMyGoalsItemAdapter
    private lateinit var binding: FragmentGoalsItemBinding
    var goalsublist= mutableListOf<GoalSub>()
    var maingoal: String? = null

    private val viewmodel:FragmentMyGoalsItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalsItemBinding.inflate(inflater, container, false)
        initAdapter()
        
        Log.d(TAG, "onCreateView: $id")
        viewmodel.getGoalSub(id!!.toLong()).observe(viewLifecycleOwner){
            Log.d(TAG, "onCreateView: $it")
            goalsItemAdapter.subQuestList=it
            goalsublist=it
            var total=it.size.toDouble()
            var complete=0.toDouble()
            for(i in it){
                if(i.Completed==1){     //완료
                    complete++
                }
            }
            binding.subQuestRecyclerview.apply {
                adapter = goalsItemAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
//            Log.d(TAG, "onCreateView: ${complete}, $total")
//            Log.d(TAG, "onCreateView: goalsublist $goalsublist")
//            Log.d(TAG, "onCreateView: complete/total ${complete/total}")
            binding.progressBar.progress=((complete/total)*100).toInt()
            binding.percentNum.text ="${(complete/total*100).toInt()}%"

        }
        Log.d(TAG, "onCreateView: 123")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initAdapter() {
        goalsItemAdapter = FragmentMyGoalsItemAdapter().apply {
            this.subQuestList = this@FragmentMyGoalsItem.goalsublist
            this.checkChangeListener = object : FragmentMyGoalsItemAdapter.CheckChangeListener {
                override fun onCheckChanged(
                    view: View,
                    position: Int,
                    compoundButton: CompoundButton,
                    isChecked: Boolean
                ) {
                    viewmodel.getGoalSub(id!!.toLong()).observe(viewLifecycleOwner){
                        goalsublist=it
                        goalsItemAdapter.subQuestList=it


                    }

                    if (isChecked) {
                        //update completed
                        Log.d(TAG, "onCheckChanged: $goalsublist , $id")
                        var sub= GoalSub(goalsublist[position].id,id!!,goalsublist[position].SubTitle,1)
                        CoroutineScope(Dispatchers.IO).launch {
                            viewmodel.updateGoalSub(sub)
                        }
                    } else {
                        var sub= GoalSub(goalsublist[position].id,id!!,goalsublist[position].SubTitle,0)
                        CoroutineScope(Dispatchers.IO).launch {
                            viewmodel.updateGoalSub(sub)
                        }
                    }
                }
            }
        }
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