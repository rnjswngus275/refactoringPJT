package com.ssafy.finalpjt.db.model

import java.io.Serializable


data class Goal (
    var indexNumber :Int=0,
    var goalTitle: String=""
) :Serializable