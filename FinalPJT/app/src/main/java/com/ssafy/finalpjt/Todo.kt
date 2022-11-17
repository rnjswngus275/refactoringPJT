package com.ssafy.finalpjt

class Todo(val id: Int, val quest: Int, val job: String?, ischecked: Int) {
    var isChecked = false
    val isDone: Int
        get() = if (isChecked) 1 else 0

    init {
        isChecked = ischecked != 0
    }
}