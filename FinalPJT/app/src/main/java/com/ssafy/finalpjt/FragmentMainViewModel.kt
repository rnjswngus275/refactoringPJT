package com.ssafy.finalpjt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.GoalSubRepository

class FragmentMainViewModel : ViewModel() {

    private var goalRepository = GoalRepository.get()
    private var goalSubRepository = GoalSubRepository.get()

    val goalList: LiveData<MutableList<Goal>> = goalRepository.getAllGoals()

    var selectedGoal = Goal()
        private set

    fun setSelectedGoal(goal: Goal) {
        selectedGoal = goal
    }
}