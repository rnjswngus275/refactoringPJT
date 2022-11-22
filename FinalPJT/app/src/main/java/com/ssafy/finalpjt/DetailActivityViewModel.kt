package com.ssafy.finalpjt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.GoalSubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivityViewModel(id: Long) : ViewModel() {

    private var goalRepository = GoalRepository.get()
    private var goalSubRepository = GoalSubRepository.get()

    val goal: LiveData<Goal> = goalRepository.getGoal(id)

    private var mSubGoalList = mutableListOf<GoalSub>()
    val subGoalList: LiveData<MutableList<GoalSub>> = goalSubRepository.getGoalSub(id)

    fun deleteSubGoal(goalSub: GoalSub) {
        viewModelScope.launch {
            goalSubRepository.deleteGoalSub(goalSub)
        }
    }

}