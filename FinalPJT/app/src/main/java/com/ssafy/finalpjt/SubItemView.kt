package com.ssafy.finalpjt

import android.widget.EditText
import android.widget.TextView

class SubItemView {
    var etInput: EditText? = null
    var tvInput: TextView? = null

    constructor(etInput: EditText?) {
        this.etInput = etInput
    }

    constructor(tvInput: TextView?) {
        this.tvInput = tvInput
    }
}