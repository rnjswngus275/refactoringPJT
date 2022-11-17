package com.ssafy.finalpjt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.ssafy.finalpjt.databinding.AddlistLayoutBinding

class AddListFragment : Fragment() {
    private lateinit var binding: AddlistLayoutBinding
    private lateinit var mAdapter: ArrayAdapter<Any?>
    private var questdata = arrayOf<String>()
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddlistLayoutBinding.inflate(inflater, container, false)

        val dbHelper: DBHelper = DBHelper(requireContext(), "QuestApp.db", null, 1)
        questdata = dbHelper.MainQuest().split("\n").toTypedArray()

        Log.e("quest", questdata.toString())

        initAdaper()

        binding.addBtn.setOnClickListener {
            val quest = binding.spinner1.selectedItem.toString()
            val date = ((binding.datePicker.month + 1) * 100) + binding.datePicker.dayOfMonth
            val job = binding.job.text.toString()
            dbHelper.insert(job, date, quest)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentTodoList.newInstance(prevPage))
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun initAdaper() {
        mAdapter = ArrayAdapter(requireContext(), R.layout.spin, questdata)
        mAdapter.setDropDownViewResource(R.layout.spin_dropdown)
        binding.spinner1.adapter = mAdapter
    }
}