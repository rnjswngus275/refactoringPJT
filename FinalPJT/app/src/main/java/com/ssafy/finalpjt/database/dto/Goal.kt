package com.ssafy.finalpjt.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Goal")
data class Goal (
    var GoalTitle: String=""
) {
    @PrimaryKey(autoGenerate = true)
    var id :Long=0

    constructor(id:Long,GoalTitle: String):this(GoalTitle){
        this.id=id
    }
}