package com.ssafy.finalpjt.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.GoalSub

class AddAdapter : RecyclerView.Adapter<AddAdapter.AddViewHolder>() {
    var subGoalList : List<GoalSub> = emptyList()

    lateinit var menuItemClickListener: MenuItemClickListener

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnCreateContextMenuListener {

        private val editText: EditText = itemView.findViewById(R.id.sub_goal_et)

        fun bindInfo(goalSub: GoalSub) {
            editText.setText(goalSub.SubTitle)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val menuItem = menu?.add(0,0,0,"삭제")
            menuItem?.setOnMenuItemClickListener {
                menuItemClickListener.onClick(layoutPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sub_goal_holder, parent, false)
        return AddViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        holder.bindInfo(subGoalList[position])
    }

    override fun getItemCount(): Int {
        return subGoalList.size
    }

    interface MenuItemClickListener {
        fun onClick(position: Int)
    }
}