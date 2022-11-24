package com.ssafy.finalpjt.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.Todo

class TodoAdapter() :
    RecyclerView.Adapter<TodoAdapter.FragmentTodoItemViewHolder>() {
    var list= mutableListOf<Todo>()
    lateinit var checkChangeListener: CheckChangeListener
    private val color = intArrayOf(
        Color.parseColor("#e9f7e1"), Color.parseColor("#f4d9e3"),
        Color.parseColor("#fdf2d8"), Color.parseColor("#e5dee1"),
        Color.parseColor("#e3eedc"), Color.parseColor("#e5fef5"),
        Color.parseColor("#fcedd7"), Color.parseColor("#fbe7e5"),
        Color.parseColor("#d2efe3"), Color.parseColor("#d6e2fc")
    )
    inner class FragmentTodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var todo= itemView.findViewById<TextView>(R.id.todo_thing)
        var completed = itemView.findViewById<CheckBox>(R.id.isDone)
        val questcolor = itemView.findViewById<View>(R.id.todo_list_linear_layout) as LinearLayout
        val shape = questcolor.background as GradientDrawable
        fun bindInfo(info: Todo) {
            todo.text=info.Todo
            completed.isChecked = info.Completed != 0
            shape.setColor(color[layoutPosition%10])

            completed.setOnCheckedChangeListener { buttonView, isChecked ->
                checkChangeListener.onCheckChanged(this.itemView, adapterPosition, buttonView, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentTodoItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.today_todo_item, parent, false)
        return FragmentTodoItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: FragmentTodoItemViewHolder, position: Int) {
        list.let { holder.bindInfo(it[position]) }
    }


    override fun getItemCount(): Int {
        return list.size
    }


    interface CheckChangeListener {
        fun onCheckChanged(view: View, position: Int, compoundButton: CompoundButton, isChecked: Boolean)
    }


}