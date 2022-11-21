package com.ssafy.finalpjt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.database.dto.Todo
import com.ssafy.finalpjt.database.repository.GoalRepository
import com.ssafy.finalpjt.database.repository.TodoRepository
import com.ssafy.finalpjt.databinding.AddlistLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/*todo 추가 페이지*/
class AddTodoListFragment : Fragment() {
    private lateinit var binding: AddlistLayoutBinding
    private lateinit var mAdapter: ArrayAdapter<Any?>
    private var questdata = mutableListOf<String>()
    private lateinit var goalRepository:GoalRepository
    private lateinit var todoRepository: TodoRepository

    var prevPage: Int = 0
    var t: LinearLayout? = null
    var b: LinearLayout? = null
    fun getInstance(prevPage: Int, t: LinearLayout?, b: LinearLayout?) {
        this.prevPage = prevPage
        this.t = t
        this.b = b
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Bundle? = arguments
        goalRepository=GoalRepository.get()
        todoRepository=TodoRepository.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddlistLayoutBinding.inflate(inflater, container, false)

        goalRepository.getGoalTitle().observe(viewLifecycleOwner){
            questdata =it
        }


        Log.e("quest", questdata.toString())

        initAdaper()

        binding.addBtn.setOnClickListener {
            var cal:Calendar=Calendar.getInstance()
            val goaltitle = binding.spinner1.selectedItem.toString()
            cal.set(binding.datePicker.year,binding.datePicker.month ,binding.datePicker.dayOfMonth)
            var date=cal.timeInMillis

            val todo = binding.job.text.toString()

            val id=goalRepository.getGoalId(goaltitle)
            var Todo= Todo(todo,date,id,0)
            CoroutineScope(Dispatchers.IO).launch {
                todoRepository.insertTodo(Todo)
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentTodoList.newInstance(prevPage))
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun initAdaper() {
        mAdapter = ArrayAdapter(requireContext(), R.layout.spin, questdata as List<Any?>)
        mAdapter.setDropDownViewResource(R.layout.spin_dropdown)
        binding.spinner1.adapter = mAdapter
    }
}