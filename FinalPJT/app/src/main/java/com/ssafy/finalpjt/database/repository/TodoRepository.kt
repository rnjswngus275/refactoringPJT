package com.ssafy.finalpjt.database.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.finalpjt.database.CarrotDatabase
import com.ssafy.finalpjt.database.dao.GoalDao
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.Todo

private const val TAG = "TodoRepository"
class TodoRepository (private val db: CarrotDatabase){


    private val todoDao=db.todoDao()

    fun getTodayTodo(date:Long):LiveData<MutableList<Todo>>{
        Log.d(TAG, "getTodayTodo: dao에서 찰칵 ^^ $date")
        return todoDao.getTodayTodo(date)
    }
    suspend fun insertTodo(todo: Todo)=db.withTransaction{
        todoDao.insertTodo(todo)
        Log.d(TAG, "insertTodo: dao에서 찰칵 ^^")
    }
    suspend fun updateCompleted(todo :Todo)=db.withTransaction{
        todoDao.updateCompleted(todo)
    }


    companion object{
        private var INSTANCE : TodoRepository? =null

        fun initialize(db: CarrotDatabase){
            if(INSTANCE == null){
                INSTANCE = TodoRepository(db)
            }
        }

        fun get() : TodoRepository {
            return INSTANCE ?:
            throw IllegalStateException("NoteRepository must be initialized")
        }
    }
}