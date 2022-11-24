package com.ssafy.finalpjt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.GoalSubRepository
import kotlinx.coroutines.launch

class DetailUpdateActivityViewModel(var goalId: Long) : ViewModel() {

    private var goalRepository = GoalRepository.get()
    private var goalSubRepository = GoalSubRepository.get()

    val goal: LiveData<Goal> = goalRepository.getGoal(goalId)
    val originalList = goalSubRepository.getGoalSub(goalId)

    var mSubGoalList = mutableListOf<GoalSub>()
    private var _subGoalList = MutableLiveData<MutableList<GoalSub>>()
    val subGoalList: LiveData<MutableList<GoalSub>>
        get() = _subGoalList

    fun setSubGoal() {
        _subGoalList.value = mSubGoalList
    }

    fun addSubGoal() {
        mSubGoalList.add(GoalSub())
        _subGoalList.value = mSubGoalList
    }

    fun addAndUpdate(goal: Goal) {
        viewModelScope.launch {
            updateGoal(goal)
            updateGoalSub()
        }
    }

    private fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            goalRepository.updateGoal(goal)
        }
    }

    private fun updateGoalSub() {
        viewModelScope.launch {
            for (subGoal in mSubGoalList) {
                if (subGoal.SubTitle.isNullOrBlank()) {
                    if (subGoal.GoalId != -1L) {
                        goalSubRepository.deleteGoalSub(subGoal)
                    }
                } else {
                    if (subGoal.GoalId != -1L) {
                        goalSubRepository.updateGoalSub(subGoal)
                    } else {
                        subGoal.GoalId = goalId
                        goalSubRepository.insertGoalSub(GoalSub(goalId, subGoal.SubTitle, 0))
                    }
                }
            }
        }
    }
}