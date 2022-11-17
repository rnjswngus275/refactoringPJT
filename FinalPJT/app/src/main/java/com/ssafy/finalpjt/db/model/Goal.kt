package com.ssafy.finalpjt.db.model

import java.io.Serializable

class Goal : Serializable {
    var indexNumber = 0
    var goalTitle: String? = null

    constructor() {}
    constructor(goalTitle: String?) {
        this.goalTitle = goalTitle
    }

    override fun toString(): String {
        return "Goal{" +
                "indexNumber=" + indexNumber +
                ", goalTitle='" + goalTitle + '\'' +
                '}'
    }
}