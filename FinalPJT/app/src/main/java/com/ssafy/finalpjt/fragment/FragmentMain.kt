package com.ssafy.finalpjt.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.viewmodel.FragmentMainViewModel
import com.ssafy.finalpjt.activity.AddActivity
import com.ssafy.finalpjt.activity.DetailActivity
import com.ssafy.finalpjt.adapter.FragmentMainAdapter
import com.ssafy.finalpjt.database.dto.Goal
import com.ssafy.finalpjt.databinding.FragmentMainBinding

class FragmentMain : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var fragmentMainAdapter: FragmentMainAdapter
    private val fragmentMainViewModel: FragmentMainViewModel by viewModels()
    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            //수정 화면에서 수정완료 버튼을 누를시. 해당 화면은 finish 처리한다.
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        initView()

    }

    private fun initAdapter() {
        fragmentMainAdapter = FragmentMainAdapter().apply {
            this.itemClickListener = object : FragmentMainAdapter.ItemClickListener {
                override fun onClick(view: View, position: Int) {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra("goalID", goalList[position].id)
                    launcher.launch(intent)
                }
            }
            this.menuItemClickListener = object : FragmentMainAdapter.MenuItemClickListener {
                override fun onClick(goal: Goal) {
                    fragmentMainViewModel.deleteGoal(goal)
                }
            }
        }
        fragmentMainViewModel.goalList.observe(viewLifecycleOwner) {
            fragmentMainAdapter.goalList = it
            fragmentMainAdapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        binding.recyclerview.apply {
            adapter = fragmentMainAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        binding.btnAdd.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, AddActivity::class.java).apply {
                putExtra("goalID", -1)
            }
            launcher.launch(intent)
        })
    }
}