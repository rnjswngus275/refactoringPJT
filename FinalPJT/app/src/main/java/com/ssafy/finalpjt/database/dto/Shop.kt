package com.ssafy.finalpjt.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Shop")
data class Shop ( var Item:String?,
                  var Price:Int){
    @PrimaryKey(autoGenerate = true)
    var id :Int=0

    constructor(id:Int,Item: String,Price: Int):this(Item,Price){
        this.id=id
    }
}