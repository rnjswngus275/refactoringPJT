package com.ssafy.finalpjt.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.GoalSub

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    var subGoalList : List<GoalSub> = emptyList()
    lateinit var menuItemClickListener: MenuItemClickListener


    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnCreateContextMenuListener {
        init {
            itemView.setOnCreateContextMenuListener(this)
        }
        private val textView: TextView = itemView.findViewById(R.id.sub_goal_tv)

        fun bindInfo(goalSub: GoalSub) {
            textView.text = goalSub.SubTitle
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val menuItem = menu?.add(0,0,0,"삭제")
            menuItem?.setOnMenuItemClickListener {
                menuItemClickListener.onClick(subGoalList[layoutPosition])
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sub_goal_holder_tv, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bindInfo(subGoalList[position])
    }

    override fun getItemCount(): Int {
        return subGoalList.size
    }

    interface MenuItemClickListener {
        fun onClick(goalSub: GoalSub)
    }

}