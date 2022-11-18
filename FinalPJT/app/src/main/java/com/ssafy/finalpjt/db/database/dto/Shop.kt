package com.ssafy.finalpjt.db.database.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Shop")
data class Shop ( var Item:String?,
                  var Price:Int,
                  var Image: Int){
    @PrimaryKey(autoGenerate = true)
    var id :Int=0

    constructor(id:Int,Item: String,Price: Int,Image:Int):this(Item,Price,Image){
        this.id=id
    }
}