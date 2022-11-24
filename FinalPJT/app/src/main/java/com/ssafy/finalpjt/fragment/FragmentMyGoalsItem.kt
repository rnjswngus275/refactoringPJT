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

class FragmentMyGoalsItem(var id:Long) : Fragment() {

    private lateinit var goalsItemAdapter: FragmentMyGoalsItemAdapter
    private lateinit var binding: FragmentGoalsItemBinding
    var goalsublist= mutableListOf<GoalSub>()

    private val viewmodel:FragmentMyGoalsItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGoalsItemBinding.inflate(inflater, container, false)
        initAdapter()
        
        viewmodel.getGoalSub(id).observe(viewLifecycleOwner){
            goalsItemAdapter.subQuestList=it
            goalsublist=it
            var total=it.size.toDouble()
            var complete=0.toDouble()
            for(i in it){
                if(i.Completed==1){
                    complete++
                }
            }
            binding.subQuestRecyclerview.apply {
                adapter = goalsItemAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            var rate=(complete/total)*100
            binding.progressBar.progress=(rate).toInt()
            binding.percentNum.text ="${rate.toInt()}%"

        }
        return binding.root
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
                    viewmodel.getGoalSub(id.toLong()).observe(viewLifecycleOwner){
                        goalsublist=it
                        goalsItemAdapter.subQuestList=it
                    }

                    if (isChecked) {
                        //update completed
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