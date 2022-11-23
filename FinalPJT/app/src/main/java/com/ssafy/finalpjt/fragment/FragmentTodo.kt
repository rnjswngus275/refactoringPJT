package com.ssafy.finalpjt.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.ssafy.finalpjt.database.repository.TodoRepository
import com.ssafy.finalpjt.database.repository.UserRepository
import com.ssafy.finalpjt.databinding.FragmentTodoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/*todo 보여주는 페이지*/
private const val TAG = "FragmentTodo"

class FragmentTodo : Fragment() {
    var cal: Calendar = Calendar.getInstance()
    var thisYear: Int = cal.get(Calendar.YEAR)
    var thisDay: Int = cal.get(Calendar.DAY_OF_MONTH)
    var thisMonth: Int = cal.get(Calendar.MONTH)
    var date: Long = 0L
    var fragmentPageNow: Int = 0
    var dateForDB = 0L
    private lateinit var todoAdapter: TodoAdapter
    private val viewmodel: FragmentTodoViewModel by viewModels()
    private lateinit var userRepository: UserRepository
    private lateinit var todoRepository: TodoRepository
    lateinit var user: User

    private var monthList: Array<String?> =
        arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")

    private lateinit var mAdapter: ArrayAdapter<Any?>
    private lateinit var binding: FragmentTodoBinding
    private var mNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        userRepository = UserRepository.get()
        todoRepository = TodoRepository.get()

        cal.set(cal.get(Calendar.YEAR), thisMonth, thisDay)
        date = cal.timeInMillis
        dateForDB = ((date / 1000 / 60 / 60 / 24) - 1)
        Log.d(TAG, "thisyear: $thisYear vs ${cal.get(Calendar.YEAR)}")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTodoBinding.inflate(inflater, container, false)
        userRepository.getAllUser().observe(viewLifecycleOwner) {
            user = it[0]
        }
        initTodoAdapter()
        initAdapter()

        viewmodel.getTodoList(dateForDB.toInt()).observe(viewLifecycleOwner) {
            Log.d(TAG, "onCreateView: $it")
            todoAdapter.list = it
            todoAdapter.notifyDataSetChanged()
        }

        createBtn(binding.tabWidget)



        binding.addListBtn.setOnClickListener {
            Log.d("onclick", "clicked")
            var main = activity as MainActivity
            main.changeFragment(2)
        }

        binding.recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = todoAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() { //종료시 백스택리스너 삭제
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) { //홈버튼 누를때 상태 저장
        super.onSaveInstanceState(outState)
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
                Log.d(TAG, "onclickListener: ${it.id}")
                viewmodel.getTodoList(btn.id).observe(viewLifecycleOwner) {
                    todoAdapter.list = it
                    todoAdapter.notifyDataSetChanged()

                }
                btn.requestFocus()
                Log.e("test", "focus" + btn.isFocused)
            }
            btn.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    Log.d(TAG, "createBtn: view id로  callfragment ${view.id}")
                    viewmodel.getTodoList(view.id).observe(viewLifecycleOwner) {
                        todoAdapter.list = it
                        todoAdapter.notifyDataSetChanged()

                    }
//                    Log.d(TAG, "position" + btn.left)
                    binding.btnScroll.scrollTo(btn.left - 231, 0)
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

        Log.d(TAG, "createBtn: ${(date / 1000 / 60 / 60 / 24) - 1}")
        cal.set(thisYear, thisMonth, thisDay)
        date = cal.timeInMillis
        val todayBtn: Button =
            tabWidgetLayout.findViewById(((date / 1000 / 60 / 60 / 24) - 1).toInt())
        Log.d(TAG, "createBtn: $thisDay")
        todayBtn.left = (231 * thisDay) - 231
        todayBtn.isFocusableInTouchMode = true
        todayBtn.requestFocus()
    }

    private fun initAdapter() {
        Log.d(TAG, "initAdapter: ")
        mAdapter = ArrayAdapter<Any?>(requireContext(), R.layout.spin, monthList)
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
                    Log.d(TAG, "onItemSelected: item selected에서 callfragment")

                    viewmodel.getTodoList(dateForDB.toInt()).observe(viewLifecycleOwner) {
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
                        CoroutineScope(Dispatchers.IO).launch {
                            userRepository.updateUserPoint(user.Point+10, user.UserName)
                        }
                        Toast.makeText(
                            requireContext(),
                            "10포인트가 적립되었습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                        var todo = Todo(
                            todoAdapter.list[position].Todo,
                            todoAdapter.list[position].Date,
                            todoAdapter.list[position].GoalId,
                            1
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            todoRepository.updateCompleted(todo)
                        }
                        todoAdapter.list[position].Completed = 1
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            userRepository.updateUserPoint(user.Point-10, user.UserName)
                        }
                        var todo = Todo(
                            todoAdapter.list[position].Todo,
                            todoAdapter.list[position].Date,
                            todoAdapter.list[position].GoalId,
                            0
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            todoRepository.updateCompleted(todo)
                        }
                        todoAdapter.list[position].Completed = 0
                    }
                }
            }
        }
    }


    companion object {
        private val FRAGMENT_TAG: String = "FRAGMENT_TAG"
        private val KEY_NUMBER: String = "KEY_NUMBER"
    }
}