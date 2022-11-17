package com.ssafy.finalpjt.db.model

class GoalSub {
    var indexNumber = 0
    var addedByUser: String? = null
    var subTitle: String? = null
    var disable = 0

    constructor() {}
    constructor(addedByUser: String?, subTitle: String?, disable: Int) {
        this.addedByUser = addedByUser
        this.subTitle = subTitle
        this.disable = disable
    }

    override fun toString(): String {
        return "SubGoal{" +
                "indexNumber=" + indexNumber +
                ", addedByUser='" + addedByUser + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", disable=" + disable +
                '}'
    }
}