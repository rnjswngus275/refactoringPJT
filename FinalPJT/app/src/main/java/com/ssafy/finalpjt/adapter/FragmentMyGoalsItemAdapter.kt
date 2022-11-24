package com.ssafy.finalpjt.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.GoalSub

private const val TAG = "FragmentMyGoalsItemAdap"
class FragmentMyGoalsItemAdapter() :
    RecyclerView.Adapter<FragmentMyGoalsItemAdapter.FragmentGoalsItemViewHolder>() {
    var subQuestList = mutableListOf<GoalSub>()
    lateinit var checkChangeListener: CheckChangeListener

    inner class FragmentGoalsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val holderText: TextView = itemView.findViewById(R.id.holder_text)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        fun bindInfo(sub: GoalSub) {
            holderText.text = sub.SubTitle
            if(sub.Completed==1){
                checkBox.isChecked=true
            }else checkBox.isChecked=false

            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                checkChangeListener.onCheckChanged(this.itemView, layoutPosition, buttonView, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentGoalsItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_goals_sub_item, parent, false)
        return FragmentGoalsItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FragmentGoalsItemViewHolder, position: Int) {
        holder.bindInfo(subQuestList[position])
    }

    override fun getItemCount(): Int {
        return subQuestList.size
    }

    interface CheckChangeListener {
        fun onCheckChanged(view: View, position: Int, compoundButton: CompoundButton, isChecked: Boolean)
    }

}