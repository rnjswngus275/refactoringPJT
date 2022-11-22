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
import androidx.fragment.app.FragmentManager
import com.ssafy.finalpjt.R
import com.ssafy.finalpjt.databinding.FragmentTodoBinding
import java.util.*
/*todo 보여주는 페이지*/
private const val TAG = "FragmentTodo"
class FragmentTodo : Fragment() {
    var cal: Calendar = Calendar.getInstance()
    var thisYear:Int=cal.get(Calendar.YEAR)
    var thisDay: Int = cal.get(Calendar.DAY_OF_MONTH)
    var thisMonth: Int = cal.get(Calendar.MONTH)
    var date: Long =0L
    var fragmentPageNow: Int = 0
    private var monthList: Array<String?> =
        arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
//    var tabLayout: LinearLayout? = null
//    var btnLayout: LinearLayout? = null
    private lateinit var mAdapter: ArrayAdapter<Any?>
    private lateinit var binding: FragmentTodoBinding
    private var mNumber: Int = 0
    private val mListner: FragmentManager.OnBackStackChangedListener =
        FragmentManager.OnBackStackChangedListener {
            var count: Int = 0
            for (f: Fragment? in requireActivity().supportFragmentManager.fragments) {
                if (f != null) {
                    count++
                }
            }
            mNumber = count
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        cal.set(cal.get(Calendar.YEAR),thisMonth,thisDay)
        date= cal.timeInMillis
        Log.d(TAG, "thisyear: $thisYear vs ${cal.get(Calendar.YEAR)}")
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoBinding.inflate(inflater, container, false)

        requireActivity().supportFragmentManager.addOnBackStackChangedListener(mListner)

        if (savedInstanceState == null) { //초기 프레그먼트 생성
            Log.d("enterIf", "savedInstanceState=null")
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, FragmentTodoList.newInstance(((date/1000/60/60/24)-1).toInt()), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
            fragmentPageNow = ((date/1000/60/60/24)-1).toInt()
            Log.d(TAG, "onCreateView: fragmentapgeNow : $fragmentPageNow")
        }
        initAdapter()
        createBtn(binding.tabWidget)



        binding.addListBtn.setOnClickListener {
            Log.d("onclick", "clicked")
            val addTodoListFragment = AddTodoListFragment()
            addTodoListFragment.getInstance(fragmentPageNow, binding.tabLayout, binding.btnLayout)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addTodoListFragment)
                .addToBackStack(null).commit()
            binding.tabLayout.visibility = View.INVISIBLE
            binding.btnLayout.visibility = View.INVISIBLE
        }
        return binding.root
    }


    override fun onDestroy() { //종료시 백스택리스너 삭제
        super.onDestroy()
        requireActivity().supportFragmentManager.removeOnBackStackChangedListener(mListner)
    }

    override fun onSaveInstanceState(outState: Bundle) { //홈버튼 누를때 상태 저장
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_NUMBER, mNumber)
    }

    private fun callFragment(fragmentNum: Int) { //프래그먼트 전환
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentTodoList.newInstance(fragmentNum))
            .addToBackStack(null).commit()
    }

    private fun createBtn(tabWidgetLayout: LinearLayout) {
        for (i in 0 until cal.getActualMaximum(Calendar.DAY_OF_MONTH)) { //해당월의 날짜 수 만큼 버튼 생성
            val btn: Button = Button(requireContext()) //버튼 생성
            btn.text = (i + 1).toString() + "일"
            cal.set(thisYear,thisMonth,i)
//            Log.d(TAG, " year: ${thisYear}")
//            Log.d(TAG, "month: $thisMonth")
//            Log.d(TAG, "day: $i")
            var id=(cal.timeInMillis/1000/60/60/24).toInt()
            btn.id = id
//            Log.d(TAG, "createBtn: $id")
            btn.background = ContextCompat.getDrawable(requireContext(), R.drawable.day_btn)

            btn.setOnClickListener {
                cal.timeInMillis=btn.id.toLong()
                fragmentPageNow =((date/1000/60/60/24)-1).toInt()
                btn.isFocusableInTouchMode = true
                btn.requestFocus()
                Log.e("test", "focus" + btn.isFocused)
            }
            btn.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    Log.d(TAG, "createBtn: view id로  callfragment ${view.id}")
                    callFragment(view.id)
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

        Log.d(TAG, "createBtn: ${(date/1000/60/60/24)-1}")
        val todayBtn: Button = tabWidgetLayout.findViewById(((date/1000/60/60/24)-1).toInt())
        Log.d(TAG, "createBtn: $thisDay")
        todayBtn.left = (231 * thisDay)-231
        todayBtn.isFocusableInTouchMode = true
        todayBtn.requestFocus()
    }

    private fun initAdapter() {
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
                    cal.set(cal.get(Calendar.YEAR),thisMonth,thisDay)
                    date= cal.timeInMillis
                    cal.set(Calendar.YEAR, thisMonth - 1, thisDay)
                    binding.tabWidget.removeAllViews()
                    createBtn(binding.tabWidget)
                    Log.d(TAG, "onItemSelected: item selected에서 callfragment")
                    callFragment(((date/1000/60/60/24)-1).toInt())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    companion object {
        private val FRAGMENT_TAG: String = "FRAGMENT_TAG"
        private val KEY_NUMBER: String = "KEY_NUMBER"
    }
}