package com.ssafy.finalpjt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ssafy.finalpjt.viewmodel.FragmentAddTodoViewModel
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.activity.MainActivity
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.database.dto.Todo
import com.ssafy.finalpjt.databinding.AddlistLayoutBinding
import kotlinx.coroutines.*
import java.util.*

private const val TAG = "AddTodoListFragment"
/*todo 추가 페이지*/
class AddTodoListFragment : Fragment() {
    private lateinit var binding: AddlistLayoutBinding
    private lateinit var mAdapter: ArrayAdapter<String>
    private var questdata = mutableListOf<String>()
    private var GoalList = mutableListOf<Goal>()
    private val viewmodel : FragmentAddTodoViewModel by viewModels()
    var prevPage: Int = 0
    var t: LinearLayout? = null
    var b: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddlistLayoutBinding.inflate(inflater, container, false)

        viewmodel.goalList.observe(viewLifecycleOwner){
            GoalList.addAll(it)

            for(i in it){
                questdata.add(i.GoalTitle)
            }
            initAdaper()
        }

        binding.addBtn.setOnClickListener {
            val cal:Calendar=Calendar.getInstance()
            val goaltitle = binding.spinner1.selectedItem.toString()
            cal.set(binding.datePicker.year,binding.datePicker.month ,binding.datePicker.dayOfMonth)

            val date=cal.timeInMillis

            val dateId=(date/1000/60/60/24)-1
            val todo = binding.job.text.toString()
            var id=0
            for(i in GoalList){
                if(i.GoalTitle==goaltitle) id= i.id.toInt()
            }
            val tempTodo = Todo(todo,dateId,id,0)

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO){
                    viewmodel.insertTodo(tempTodo)
                }
                var mainActivity = activity as MainActivity
                mainActivity.changeFragment(1)
            }

        }

        return binding.root
    }

    private fun initAdaper() {
        mAdapter = ArrayAdapter(requireContext(), R.layout.spin, questdata)
        mAdapter.setDropDownViewResource(R.layout.spin_dropdown)
        binding.spinner1.adapter = mAdapter
    }
}