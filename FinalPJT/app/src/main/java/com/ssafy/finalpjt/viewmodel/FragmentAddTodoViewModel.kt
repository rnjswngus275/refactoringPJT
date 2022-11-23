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

private const val TAG = "FragmentAddTodoViewModel"
class FragmentAddTodoViewModel : ViewModel(){
    private var goalRepository= GoalRepository.get()
    private var todoRepository= TodoRepository.get()

    var goalTitleList: LiveData<MutableList<String>> = goalRepository.getGoalTitle()
    var goalList=goalRepository.getAllGoals()

//    suspend fun getGoalId(goaltitle:String,todo:String,date:Long){
//        val job=viewModelScope.async {
//            withContext(Dispatchers.IO){
//                return@withContext goalRepository.getGoalId(goaltitle)
//            }
//        }
//        var todo =Todo(todo,date,job.await(),0)
//        Log.d(TAG, "getGoalId: $todo")
//        insertTodo(todo)
//    }
    @SuppressLint("LongLogTag")
    suspend fun insertTodo(Todo:Todo){
       var a= viewModelScope.launch{
            withContext(Dispatchers.IO) {
                todoRepository.insertTodo(Todo)
                Log.d(TAG, "insertTodo: $Todo")
            }
        }
        a.join()
    }
}