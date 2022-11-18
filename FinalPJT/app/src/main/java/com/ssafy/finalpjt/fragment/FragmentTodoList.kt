package com.ssafy.finalpjt.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.Todo
import java.util.*

class FragmentTodoList : Fragment() {
    var todoList = ArrayList<Todo>() //데이터베이스
    var myAdapter: MyAdapter? = null
    var dbHelper: DBHelper? = null
    var num: Int = 0
    private val color = intArrayOf(
        Color.parseColor("#e9f7e1"), Color.parseColor("#f4d9e3"),
        Color.parseColor("#fdf2d8"), Color.parseColor("#e5dee1"),
        Color.parseColor("#e3eedc"), Color.parseColor("#e5fef5"),
        Color.parseColor("#fcedd7"), Color.parseColor("#fbe7e5"),
        Color.parseColor("#d2efe3"), Color.parseColor("#d6e2fc")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.today_todo_list, null)
        dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
        if (dbHelper!!.sortTodo(requireArguments().getInt(ARG_NO, 0)) !== "") {
            createList()
        }
        myAdapter = MyAdapter(activity, todoList)
        val listView = view.findViewById<View>(R.id.today_todo_listview) as ListView
        listView.apply {
            divider = ColorDrawable(Color.TRANSPARENT)
            dividerHeight = 15
            adapter = myAdapter
        }
        return view
    }

    private fun createList() {
        val temp = dbHelper!!.sortTodo(requireArguments().getInt(ARG_NO, 0)).split("\n").toTypedArray()
        val data = Array(5) { arrayOfNulls<String>(temp.size) }
        for (i in temp.indices) {
            for (k in 0..4) {
                data[k][i] = temp[i].split("\\|").toTypedArray()[k]
            }
            todoList.add(
                Todo(
                    data[0][i]!!.toInt(),
                    color[dbHelper!!.MainQuestIndex(data[3][i])],
                    data[1][i],
                    data[4][i]!!
                        .toInt()
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        num = requireArguments().getInt(ARG_NO, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        num = requireArguments().getInt(ARG_NO, 0)
    }

    inner class MyAdapter(context: Context?, data: ArrayList<Todo>) : BaseAdapter() {
        var mContext: Context? = null
        var mLayoutInflater: LayoutInflater? = null
        var sample: ArrayList<Todo>
        override fun getCount(): Int {
            return sample.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Todo {
            return sample[position]
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            val view = mLayoutInflater!!.inflate(R.layout.today_todo_item, null) //리스트 양식 샘플
            val questcolor = view.findViewById<View>(R.id.todo_list_linear_layout) as LinearLayout
            val shape = questcolor.background as GradientDrawable
            val todoThing = view.findViewById<View>(R.id.todo_thing) as TextView
            val isDone = view.findViewById<View>(R.id.isDone) as CheckBox
            shape.setColor(sample[position].quest)
            todoThing.text = sample[position].job
            isDone.isChecked = sample[position].isChecked
            sort()
            isDone.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    dbHelper!!.plusUserPoint(10)
                    Toast.makeText(
                        requireContext(),
                        "10포인트가 적립되었습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                    val dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
                    dbHelper.updateisDone(sample[position].id, 1)
                    sample[position].isChecked = true
                } else {
                    dbHelper!!.minusUserPoint(10)
                    val dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
                    dbHelper.updateisDone(sample[position].id, 0)
                    sample[position].isChecked = false
                }
                sort()
                myAdapter!!.notifyDataSetChanged()
            }
            return view
        }

        private fun sort() {
            sample.sortWith(Comparator { o1, o2 -> //수행 여부로 정렬
                //메인퀘스트로 한번 더 정렬해야됨
                var x = 0
                if (o1.isDone < o2.isDone) {
                    x = -1
                } else if (o1.isDone == o2.isDone) {
                    if (o1.quest < o2.quest) {
                        x = -1
                    }
                }
                x
            })
        }

        init {
            mContext = context
            sample = data
            mLayoutInflater = LayoutInflater.from(mContext)
        }
    }

    companion object {
        private const val ARG_NO = "ARG_NO"
        fun newInstance(no: Int): FragmentTodoList {
            val fragment = FragmentTodoList()
            val args = Bundle()
            args.putInt(ARG_NO, no)
            fragment.arguments = args
            return fragment
        }
    }
}