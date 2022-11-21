package com.ssafy.finalpjt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.GoalSubRepository
import kotlinx.coroutines.launch

class DetailActivityViewModel(id: Long) : ViewModel() {

    private var goalRepository = GoalRepository.get()
    private var goalSubRepository = GoalSubRepository.get()

    val goal: LiveData<Goal>
    val originalSubGoalList: LiveData<MutableList<GoalSub>>

    init {
        goal = goalRepository.getGoal(id)
        originalSubGoalList = goalSubRepository.getGoalSub(id)
    }

    private var mSubGoalList: MutableList<GoalSub> = arrayListOf()
    private var _mSubGoalList = MutableLiveData<MutableList<GoalSub>>()
    val liveSubGoalList: LiveData<MutableList<GoalSub>>
        get() = _mSubGoalList

    fun setSubGoalList(list: MutableList<GoalSub>) {
        mSubGoalList.addAll(list)
    }

    fun deleteSubGoal(idList: MutableList<Long>) {
        viewModelScope.launch {
            for (id in idList) {
                goalSubRepository.deleteGoalSubById(id)
            }
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            goalRepository.updateGoal(goal)
        }
    }

    fun updateSubGoal() {
        viewModelScope.launch {
            for (subGoal in mSubGoalList) {
                goalSubRepository.updateGoalSub(subGoal)
            }
        }
    }


}