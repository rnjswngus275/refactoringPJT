package com.ssafy.finalpjt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalSubRepository

class FragmentMyGoalsItemViewModel :ViewModel() {
    private var goalSubRepository = GoalSubRepository.get()

    fun getGoalSub(id:Long): LiveData<MutableList<GoalSub>> {
        return goalSubRepository.getGoalSub(id)
    }
    suspend fun updateGoalSub(sub:GoalSub){
        goalSubRepository.updateGoalSub(sub)
    }
}