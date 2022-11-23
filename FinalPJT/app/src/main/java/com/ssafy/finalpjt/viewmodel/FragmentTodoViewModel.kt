package com.ssafy.finalpjt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Todo
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.TodoRepository
import com.ssafy.finalpjt.database.repository.UserRepository
import kotlinx.coroutines.*
import java.util.ArrayList

private const val TAG = "FragmentTodoListVeiwMod"

class FragmentTodoViewModel() : ViewModel() {
    private var todoRepository = TodoRepository.get()
    var mtodoList = mutableListOf<Todo>()
    private var _todoList= MutableLiveData<MutableList<Todo>>()
    private var userRepository = UserRepository.get()

    val todoList : LiveData<MutableList<Todo>>
        get() = _todoList

    fun setTodolist(){
        _todoList.value =mtodoList
    }

    fun getTodoList(num: Int): LiveData<MutableList<Todo>> {
       return todoRepository.getTodayTodo(num.toLong())
    }

     fun updateAll(point:Int, name:String, todo:Todo){
         viewModelScope.launch {
             withContext(Dispatchers.IO){
                 updateTodo(point,name,todo)
             }
         }
    }
    suspend fun updateTodo(point:Int,name:String,todo:Todo){
        var job=viewModelScope.async {
            withContext(Dispatchers.IO){
                todoRepository.updateTodo(todo.Completed,todo.id)
                userRepository.updateUserPoint(point,name)
                Log.d(TAG, "updateTodo: ")
            }
        }
        job.await()
    }

}