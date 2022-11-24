package com.ssafy.finalpjt.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.GoalSub
import com.ssafy.finalpjt.database.repository.GoalSubRepository

class FragmentMainAdapter : RecyclerView.Adapter<FragmentMainAdapter.FragmentMainViewHolder>() {
    var goalList : List<Goal> = emptyList()
    var subGoalList : List<GoalSub> = emptyList()
    lateinit var itemClickListener: ItemClickListener
    lateinit var menuItemClickListener: MenuItemClickListener
    private var goalSubRepository = GoalSubRepository.get()


    //10번 인덱스까지 색상을 변경할 배경을 만들도록한다.
    private val cardViewBackGround = intArrayOf(
        Color.parseColor("#e9f7e1"), Color.parseColor("#f4d9e3"),
        Color.parseColor("#fdf2d8"), Color.parseColor("#e5dee1"),
        Color.parseColor("#e3eedc"), Color.parseColor("#e5fef5"),
        Color.parseColor("#fcedd7"), Color.parseColor("#fbe7e5"),
        Color.parseColor("#d2efe3"), Color.parseColor("#d6e2fc")
    )

    inner class FragmentMainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        init {
            itemView.setOnCreateContextMenuListener(this)
        }
        private val mainText: TextView = itemView.findViewById(R.id.item_maintext)
        private val progressNum: TextView = itemView.findViewById(R.id.percent_num_main)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_main)
        private val cardView: CardView = itemView.findViewById(R.id.cv)

        @SuppressLint("SetTextI18n")
        fun bindInfo(goal: Goal) {
            mainText.text = goal.GoalTitle

            val percent = getPercentage(goal)

            progressNum.text = "$percent%"
            progressBar.progress = percent
            cardView.setBackgroundColor(cardViewBackGround[layoutPosition % 10])

            itemView.setOnClickListener {
                itemClickListener.onClick(it, layoutPosition)
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val menuItem = menu?.add(0, 0, 0, "삭제")
            menuItem?.setOnMenuItemClickListener {
                menuItemClickListener.onClick(goalList[layoutPosition])
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentMainViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.main_list_item, parent, false)
        return FragmentMainViewHolder(view)
    }

    override fun onBindViewHolder(holder: FragmentMainViewHolder, position: Int) {
        holder.bindInfo(goalList[position])
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    interface MenuItemClickListener {
        fun onClick(goal: Goal)
    }

    private fun getPercentage(goal: Goal) : Int {
        var isCompleted: Double = 0.0
        var total: Double = 0.0
        for (subGoal in subGoalList) {
            if (goal.id == subGoal.GoalId) {
                total += 1
                if (subGoal.Completed == 1) {
                    isCompleted += 1
                }
            }
        }

        return if (total == 0.0) {
            0
        } else {
            ((isCompleted / total) * 100).toInt()
        }
    }
}