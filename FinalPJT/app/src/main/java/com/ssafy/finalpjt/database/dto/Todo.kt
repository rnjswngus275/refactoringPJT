package com.ssafy.finalpjt.database.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName="Todo",
    foreignKeys=[
        ForeignKey(
            entity = Goal::class,
            parentColumns= ["id"],
            childColumns = ["GoalId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Todo(    var Todo: String = "",
                    var Date: Long = 0,
                    var GoalId:Int=0,
                    var Completed: Int = 0) {

    @PrimaryKey(autoGenerate = true)
    var id :Long=0

    constructor(id:Long,Todo: String,Date: Long,GoalId: Int,Completed: Int):this(Todo,Date,GoalId,Completed){
        this.id=id
    }
}