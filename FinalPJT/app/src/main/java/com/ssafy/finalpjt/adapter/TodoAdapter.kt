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
import com.ssafy.finalpjt.database.dto.Todo

private const val TAG = "TodoAdapter"
class TodoAdapter() :
    RecyclerView.Adapter<TodoAdapter.FragmentTodoItemViewHolder>() {
    var list= mutableListOf<Todo>()
    lateinit var checkChangeListener: CheckChangeListener

    inner class FragmentTodoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var todo= itemView.findViewById<TextView>(R.id.todo_thing)
        var completed = itemView.findViewById<CheckBox>(R.id.isDone)

        fun bindInfo(info: Todo) {
            Log.d(TAG, "bindInfo: $info")
            todo.text=info.Todo
            if(info.Completed==0)completed.isChecked=false
            else true

            completed.setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d(TAG, "bindInfo: setoncheckedlistener")
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