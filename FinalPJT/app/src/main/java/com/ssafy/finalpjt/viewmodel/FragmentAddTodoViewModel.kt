package com.ssafy.finalpjt.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Todo
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.TodoRepository
import kotlinx.coroutines.*

class FragmentAddTodoViewModel : ViewModel(){
    private var goalRepository= GoalRepository.get()
    private var todoRepository= TodoRepository.get()

    var goalList=goalRepository.getAllGoals()

    suspend fun insertTodo(Todo:Todo){
       var a= viewModelScope.launch{
            withContext(Dispatchers.IO) {
                todoRepository.insertTodo(Todo)
            }
        }
        a.join()
    }
}