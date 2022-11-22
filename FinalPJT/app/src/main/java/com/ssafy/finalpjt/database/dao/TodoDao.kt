package com.ssafy.finalpjt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.dto.Todo
@Dao

interface TodoDao {
    @Query("SELECT * FROM Todo WHERE Date=(:date) ORDER BY Completed ASC")
    fun getTodayTodo(date:Long):LiveData<MutableList<Todo>>

    @Insert
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateCompleted(todo :Todo)


}