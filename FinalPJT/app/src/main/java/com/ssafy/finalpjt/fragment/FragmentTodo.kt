package com.ssafy.finalpjt.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.viewmodel.FragmentTodoViewModel
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.activity.MainActivity
import com.ssafy.finalpjt.adapter.TodoAdapter
import com.ssafy.finalpjt.database.dto.Todo
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.databinding.FragmentTodoBinding
import java.util.*
const val btn_width=231
class FragmentTodo : Fragment() {

    var cal: Calendar = Calendar.getInstance()
    var thisYear: Int = cal.get(Calendar.YEAR)
    var thisDay: Int = cal.get(Calendar.DAY_OF_MONTH)
    var thisMonth: Int = cal.get(Calendar.MONTH)
    var date: Long = 0L
    var dateForDB = 0L
    private lateinit var todoAdapter: TodoAdapter
    private val viewModel: FragmentTodoViewModel by viewModels()
    lateinit var user: User

    private var monthList: Array<String?> =
        arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")

    private lateinit var mAdapter: ArrayAdapter<Any?>
    private lateinit var binding: FragmentTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        cal.set(cal.get(Calendar.YEAR), thisMonth, thisDay)
        date = cal.timeInMillis
        dateForDB = ((date / 1000 / 60 / 60 / 24) - 1)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTodoBinding.inflate(inflater, container, false)

        viewModel.user.observe(viewLifecycleOwner) {
            user = it
        }

        initTodoAdapter()
        initAdapter()

        viewModel.getTodoList(dateForDB.toInt()).observe(viewLifecycleOwner) {
            todoAdapter.list = it
            todoAdapter.notifyDataSetChanged()
        }

        createBtn(binding.tabWidget)



        binding.addListBtn.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.changeFragment(2)
        }

        binding.recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = todoAdapter

        return binding.root
    }

    private fun createBtn(tabWidgetLayout: LinearLayout) {
        for (i in 0 until cal.getActualMaximum(Calendar.DAY_OF_MONTH)) { //해당월의 날짜 수 만큼 버튼 생성
            val btn: Button = Button(requireContext()) //버튼 생성
            btn.text = (i).toString() + "일"
            cal.set(thisYear, thisMonth, i)
            date = cal.timeInMillis
            var id = ((date / 1000 / 60 / 60 / 24) - 1).toInt()
            btn.id = id

            btn.background = ContextCompat.getDrawable(requireContext(), R.drawable.day_btn)

            btn.setOnClickListener {
                btn.isFocusableInTouchMode = true
                viewModel.getTodoList(btn.id).observe(viewLifecycleOwner) {
                    todoAdapter.list = it
                    todoAdapter.notifyDataSetChanged()

                }
                btn.requestFocus()
            }
            btn.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    viewModel.getTodoList(view.id).observe(viewLifecycleOwner) {
                        todoAdapter.list = it
                        todoAdapter.notifyDataSetChanged()

                    }
                    binding.btnScroll.scrollTo(btn.left - btn_width, 0)
                    view.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.selecte_day_btn
                    )
                    btn.setTextColor(Color.WHITE)
                } else {
                    btn.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.day_btn
                    )
                    btn.setTextColor(Color.BLACK)
                }
            }
            tabWidgetLayout.addView(btn)
        }

        cal.set(thisYear, thisMonth, thisDay)
        date = cal.timeInMillis
        val todayBtn: Button =
            tabWidgetLayout.findViewById(((date / 1000 / 60 / 60 / 24) - 1).toInt())
        todayBtn.left = (btn_width * thisDay) - btn_width
        todayBtn.isFocusableInTouchMode = true
        todayBtn.requestFocus()
    }

    private fun initAdapter() {
        mAdapter = ArrayAdapter<Any?>(requireContext(), R.layout.goal_list_spinner, monthList)
        mAdapter.setDropDownViewResource(R.layout.spin_dropdown)

        binding.monthSpinner.apply {
            adapter = mAdapter
            setSelection(thisMonth - 1)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    thisMonth = position + 1
                    cal.set(cal.get(Calendar.YEAR), thisMonth, thisDay)
                    date = cal.timeInMillis
                    cal.set(Calendar.YEAR, thisMonth - 1, thisDay)
                    binding.tabWidget.removeAllViews()
                    createBtn(binding.tabWidget)

                    viewModel.getTodoList(dateForDB.toInt()).observe(viewLifecycleOwner) {
                        todoAdapter.list = it
                        todoAdapter.notifyDataSetChanged()
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

    }

    private fun initTodoAdapter() {
        todoAdapter = TodoAdapter().apply {
            this.checkChangeListener = object : TodoAdapter.CheckChangeListener {
                override fun onCheckChanged(
                    view: View,
                    position: Int,
                    compoundButton: CompoundButton,
                    isChecked: Boolean
                ) {
                    if (isChecked) {
                        var todo = Todo(
                            todoAdapter.list[position].id,
                            todoAdapter.list[position].Todo,
                            todoAdapter.list[position].Date,
                            todoAdapter.list[position].GoalId,
                            1
                        )
                        viewModel.updateAll(user.Point+10,user.UserName,todo)
                        Toast.makeText(
                            requireContext(),
                            "10포인트가 적립되었습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {

                        var todo = Todo(
                            todoAdapter.list[position].id,
                            todoAdapter.list[position].Todo,
                            todoAdapter.list[position].Date,
                            todoAdapter.list[position].GoalId,
                            0
                        )
                        if(user.Point>=10) viewModel.updateAll(user.Point-10,user.UserName,todo)
                    }
                }
            }
        }
    }
}