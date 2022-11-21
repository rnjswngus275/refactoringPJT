package com.ssafy.finalpjt

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

class AddActivityViewModel : ViewModel() {
    private var goalRepository = GoalRepository.get()
    private var goalSubRepository = GoalSubRepository.get()

    private var _subGoalList = MutableLiveData<MutableList<GoalSub>>()
    private var mSubGoalList = mutableListOf<GoalSub>()
    val subGoalList: LiveData<MutableList<GoalSub>>
        get() = _subGoalList

    fun setSubGoalList(list: MutableList<GoalSub>) {
        mSubGoalList = list
        _subGoalList.value = mSubGoalList
    }

    fun insertGoal(goal: Goal) : Long {
        var id = 0L
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                id = goalRepository.insertGoal(goal)
            }
        }
        return id
    }

    fun insertSubGoalList(goalId: Long, subGoalList: MutableList<GoalSub>) {
        viewModelScope.launch {
            for (subGoal in subGoalList) {
                goalSubRepository.insertGoalSub(subGoal)
            }
        }
    }


}