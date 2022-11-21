package com.ssafy.finalpjt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Todo
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList

private const val TAG = "FragmentTodoListVeiwMod"

class FragmentTodoListViewModel : ViewModel() {
    private var goalRepository = GoalRepository.get()
    private var todoRepository = TodoRepository.get()
     var todoList = mutableListOf<Todo>()

    suspend fun gettodolist(num: Int):MutableList<Todo>{
        val job=viewModelScope.async {
            withContext(Dispatchers.IO){
                return@withContext todoRepository.getTodayTodo(num.toLong())
            }
        }

        return job.await()
    }
    fun setTodolist(num:Int){
        viewModelScope.launch {
            Log.d(TAG, "setTodolist: ${gettodolist(num)}")
            todoList.addAll(gettodolist(num))
        }
    }


}