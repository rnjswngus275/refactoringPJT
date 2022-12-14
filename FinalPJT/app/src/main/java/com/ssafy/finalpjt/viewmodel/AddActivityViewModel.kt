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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddActivityViewModel : ViewModel() {
    private var goalRepository = GoalRepository.get()
    private var goalSubRepository = GoalSubRepository.get()

    private var mSubGoalList = mutableListOf<GoalSub>()
    private var _subGoalList = MutableLiveData<MutableList<GoalSub>>()
    val subGoalList: LiveData<MutableList<GoalSub>>
        get() = _subGoalList

    fun setSubGoalList(list: MutableList<GoalSub>) {
        mSubGoalList = list
        _subGoalList.value = mSubGoalList
    }

    fun insertGoalAndSubGoal(goal: Goal, subGoalList: List<GoalSub>) {
        viewModelScope.launch {
            val goalId = insertGoal(goal)
            if (goalId != -1L) {
                insertSubGoalList(goalId, subGoalList)
            }
        }
    }

    private suspend fun insertGoal(goal: Goal) : Long {
        val job = viewModelScope.async {
            withContext(Dispatchers.IO) {
                return@withContext goalRepository.insertGoal(goal)
            }
        }
        return job.await()
    }

    private fun insertSubGoalList(goalId: Long, subGoalList: List<GoalSub>) {
        viewModelScope.launch {
            for (subGoal in subGoalList) {
                if (!subGoal.SubTitle.isNullOrBlank()) {
                    goalSubRepository.insertGoalSub(GoalSub(goalId, subGoal.SubTitle, 0))
                }
            }
        }
    }


}