package com.ssafy.finalpjt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.GoalSubRepository

class FragmentMyGoalsViewModel :ViewModel(){
    private var goalRepository = GoalRepository.get()
    private var goalSubRepository = GoalSubRepository.get()
//    val originalList=goalRepository.getAllGoals()
//    var mGoalList = mutableListOf<Goal>()
//    private var _goalList= MutableLiveData<MutableList<Goal>>()
//
//    val goalList:LiveData<MutableList<Goal>>
//        get()=_goalList
//
//    fun setGoalList() {
//        _goalList.value=mGoalList
//    }
    fun getAllGoal(): LiveData<MutableList<Goal>> {
        return goalRepository.getAllGoals()
    }

}