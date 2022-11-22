package com.ssafy.finalpjt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.dto.Todo
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.TodoRepository
import kotlinx.coroutines.*
import java.util.ArrayList

private const val TAG = "FragmentTodoListVeiwMod"

class FragmentTodoListViewModel(var num:Int) : ViewModel() {
    private var todoRepository = TodoRepository.get()
    var  originaltodoList : LiveData<MutableList<Todo>> = todoRepository.getTodayTodo(num.toLong())
    var mtodoList = mutableListOf<Todo>()
    private var _todoList= MutableLiveData<MutableList<Todo>>()

    val todoList : LiveData<MutableList<Todo>>
        get() = _todoList

    fun setTodolist(){
        _todoList.value =mtodoList
    }




//    suspend fun gettodolist(num: Int) {
//        val job=viewModelScope.async {
//            withContext(Dispatchers.IO){
//
//                return@withContext todoRepository.getTodayTodo(num.toLong())
//            }
//        }
//        Log.d(TAG, "gettodolist: ")
//        todoList.addAll(job.await())
//    }
//    suspend fun setTodolist(num:Int){
//
//            viewModelScope.async {
//                withContext(Dispatchers.IO){
//                    Log.d(TAG, "setTodolist: ${gettodolist(num)}")
//                    (gettodolist(num))
//                    Log.d(TAG, "Todolist: ${todoList}")
//
//                }
//            }
//
//    }


}