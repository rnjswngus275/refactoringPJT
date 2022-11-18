package com.ssafy.finalpjt.adapter

import android.graphics.Color
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.db.model.Goal

class FragmentMainAdapter() : RecyclerView.Adapter<FragmentMainAdapter.FragmentMainViewHolder>() {
    var goalList = mutableListOf<Goal>()
    private var rPosition = RecyclerView.NO_POSITION
    lateinit var dbHelper: DBHelper
    lateinit var itemClickListener: ItemClickListener

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
        val maintext: TextView = itemView.findViewById(R.id.item_maintext)
        val progressNum: TextView = itemView.findViewById(R.id.percent_num_main)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_main)
        val cardView: CardView = itemView.findViewById(R.id.cv)

        fun bindInfo(goal: Goal) {
            maintext.text = goal.goalTitle
            progressNum.text = if (dbHelper!!.isMainQuestinRate(goal.goalTitle)) {
                "0%"
            } else {
                "${dbHelper!!.selectRate(goal.goalTitle)}%"
            }
            progressBar.progress = dbHelper!!.selectRate(goal.goalTitle)
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
            rPosition = this.adapterPosition
            menu?.add(0, 0, 0, "삭제")
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

    fun getPosition(): Int = rPosition
}