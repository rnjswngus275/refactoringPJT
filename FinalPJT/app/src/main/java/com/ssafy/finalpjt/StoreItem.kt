package com.ssafy.finalpjt

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ssafy.finalpjt.db.database.dto.Shop


class StoreItem : LinearLayout {
    lateinit var textView: TextView
    lateinit var textView2: TextView
    lateinit var imageView: ImageView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    fun init(context: Context) {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.store_item, this, true)
        textView = (findViewById<View>(R.id.textView) as TextView?)!!
        textView2 = (findViewById<View>(R.id.textView2) as TextView?)!!
        imageView = (findViewById<View>(R.id.imageView) as ImageView?)!!
    }

    fun setItem(storedto: Shop) {
        textView.setText(storedto.name)
        textView2.setText(storedto.cost.toString())
        imageView!!.setImageResource(storedto.image)
    }
}