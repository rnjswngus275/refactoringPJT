package com.ssafy.finalpjt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalSubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "FragmentMyGoalsItemView"
class FragmentMyGoalsItemViewModel :ViewModel() {
    private var goalSubRepository = GoalSubRepository.get()

    fun getGoalSub(id:Long): LiveData<MutableList<GoalSub>> {
        return goalSubRepository.getGoalSub(id)
    }
    suspend fun updateGoalSub(sub:GoalSub){
        val job=viewModelScope.async {
            withContext(Dispatchers.IO){
                goalSubRepository.updateGoalSub(sub)
                Log.d(TAG, "updateGoalSub: ")
            }
        }
        job.await()

    }
}