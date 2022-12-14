package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.dto.Todo
@Dao

interface TodoDao {
    @Query("SELECT * FROM Todo WHERE Date=(:date)")
    fun getTodayTodo(date:Long):LiveData<MutableList<Todo>>

    @Insert
    suspend fun insertTodo(todo: Todo)

    @Query("UPDATE Todo SET Completed=(:Completed) WHERE id=(:id)")
    suspend fun updateTodo(Completed :Int,id:Long)


}