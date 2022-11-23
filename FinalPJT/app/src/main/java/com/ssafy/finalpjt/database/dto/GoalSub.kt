package com.ssafy.finalpjt.database.dto

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName="GoalSub",
    foreignKeys=[
        ForeignKey(
            entity = Goal::class,
            parentColumns= ["id"],
            childColumns = ["GoalId"],
            onDelete = CASCADE
        )
    ]
)

data class GoalSub(
    var GoalId: Long = -1L,
    var SubTitle: String = "",
    var Completed: Int = 0
){
    @PrimaryKey(autoGenerate = true)
    var id :Long=0

    constructor(id:Long,GoalId: Long,SubTitle:String,Completed: Int):this(GoalId,SubTitle,Completed){
        this.id=id
    }

}