package com.ssafy.finalpjt.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.GoalSub

class AddAdapter : RecyclerView.Adapter<AddAdapter.AddViewHolder>() {
    var subGoalList = mutableListOf<GoalSub>()

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val editText: EditText = itemView.findViewById(R.id.sub_goal_et)

        fun bindInfo(goalSub: GoalSub) {
            editText.apply {
                setText(goalSub.SubTitle)
                addTextChangedListener( object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                        subGoalList[layoutPosition].SubTitle = s.toString()
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sub_goal_holder_et, parent, false)
        return AddViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        holder.bindInfo(subGoalList[position])
    }

    override fun getItemCount(): Int {
        return subGoalList.size
    }

    override fun onViewAttachedToWindow(holder: AddViewHolder) {
        holder.itemView.requestFocus()
        super.onViewAttachedToWindow(holder)
    }

}