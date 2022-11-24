package com.ssafy.finalpjt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.GoalSubRepository
import kotlinx.coroutines.launch

class FragmentMainViewModel : ViewModel() {

    private var goalRepository = GoalRepository.get()
    private var goalSubRepository = GoalSubRepository.get()

    val goalList: LiveData<MutableList<Goal>> = goalRepository.getAllGoals()

    val subGoalList : LiveData<MutableList<GoalSub>> = goalSubRepository.getAllGoalSub()

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch {
            goalRepository.deleteGoal(goal)
        }
    }

}