package com.ssafy.finalpjt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.finalpjt.database.DatabaseApplicationClass
import com.ssafy.finalpjt.database.dto.Todo
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.TodoRepository
import com.ssafy.finalpjt.database.repository.UserRepository
import kotlinx.coroutines.*
import java.util.ArrayList

private const val TAG = "FragmentTodoListVeiwMod"

class FragmentTodoViewModel() : ViewModel() {

    private val sharedPreferencesUtil = DatabaseApplicationClass.sharedPreferencesUtil
    private var userRepository = UserRepository.get()

    var userName = sharedPreferencesUtil.getUserName()
    val user: LiveData<User> = userRepository.getUserByName(userName)

    private var todoRepository = TodoRepository.get()

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
    private suspend fun updateTodo(point:Int, name:String, todo:Todo){
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