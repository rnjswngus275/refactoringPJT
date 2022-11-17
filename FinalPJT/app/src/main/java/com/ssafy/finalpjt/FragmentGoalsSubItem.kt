package com.ssafy.finalpjt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

/**
 * Created by user on 2016-12-23.
 */
class FragmentGoalsSubItem : LinearLayout {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?) : super(context) {
        init(context)
    }

    private fun init(context: Context?) {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.goals_sub_item_fragment, this, true)
    }
}