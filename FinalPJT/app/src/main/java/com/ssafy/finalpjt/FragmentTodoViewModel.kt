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

class FragmentTodoViewModel() : ViewModel() {
    private var todoRepository = TodoRepository.get()
    var mtodoList = mutableListOf<Todo>()
    private var _todoList= MutableLiveData<MutableList<Todo>>()

    val todoList : LiveData<MutableList<Todo>>
        get() = _todoList

    fun setTodolist(){
        _todoList.value =mtodoList
    }

    fun getTodoList(num: Int): LiveData<MutableList<Todo>> {
       return todoRepository.getTodayTodo(num.toLong())
    }
}