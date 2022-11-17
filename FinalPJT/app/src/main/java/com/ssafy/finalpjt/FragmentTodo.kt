package com.ssafy.finalpjt

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
import com.ssafy.finalpjt.databinding.FragmentTodoBinding
import java.util.*
private lateinit var binding: FragmentTodoBinding

class FragmentTodo constructor() : Fragment() {
    var cal: Calendar = Calendar.getInstance()
    var thisDay: Int = cal.get(Calendar.DAY_OF_MONTH)
    var thisMonth: Int = cal.get(Calendar.MONTH) + 1
    var date: Int = (thisMonth * 100) + thisDay
    var fragmentPageNow: Int = 0
    var Months: Array<String?> =
        arrayOf("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월")
    var tabLayout: LinearLayout? = null
    var btnLayout: LinearLayout? = null
    var monthSpinner: Spinner? = null
    var addBtn: Button? = null
    var addListBtn: ImageButton? = null
    var btnScroll: HorizontalScrollView? = null
    private var mNumber: Int = 0
    private val mListner: FragmentManager.OnBackStackChangedListener =
        FragmentManager.OnBackStackChangedListener {
            val fragmentManager: FragmentManager? = getFragmentManager()
            var count: Int = 0
            for (f: Fragment? in fragmentManager!!.fragments) {
                if (f != null) {
                    count++
                }
            }
            mNumber = count
        }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentTodoBinding.inflate(inflater,container,false)

        val fragmentManager: FragmentManager? = getFragmentManager()
        fragmentManager!!.addOnBackStackChangedListener(mListner)
        val fragment: Fragment? = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
        Log.d("MainActivity", "onCreate fragment =" + fragment)
        if (savedInstanceState == null) { //초기 프레그먼트 생성
            Log.d("enterIf", "savedInstanceState=null")
            fragmentManager.beginTransaction()
                .add(R.id.fragment_container, MyFragment.Companion.getInstace(date), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
            fragmentPageNow = thisDay
        }

        createBtn(binding.tabWidget)
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireContext(), R.layout.spin, Months)
        adapter.setDropDownViewResource(R.layout.spin_dropdown)
        monthSpinner!!.adapter = adapter
        monthSpinner!!.setSelection(thisMonth - 1)
        monthSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            public override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Log.e("month", "" + thisMonth)
                thisMonth = position + 1
                date = (thisMonth * 100) + thisDay
                Log.e("newMonth", "" + thisMonth)
                Log.e("dayofmonth", "" + cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                cal.set(Calendar.YEAR, thisMonth - 1, thisDay)
                Log.e("dayofmonth", "" + cal.getActualMaximum(Calendar.DAY_OF_MONTH))
                binding.tabWidget.removeAllViews()
                createBtn(binding.tabWidget)
                callFragment(date)
            }

            public override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
//        addListBtn = view.findViewById<View>(R.id.addListBtn) as ImageButton?
        binding.addListBtn.setOnClickListener {
            Log.d("onclick", "clicked")
            val alFragment: AddListFragment = AddListFragment()
            alFragment.getInstance(fragmentPageNow, tabLayout, btnLayout)
            fragmentManager.beginTransaction().replace(R.id.fragment_container, alFragment)
                .addToBackStack(null).commit()
            tabLayout!!.setVisibility(View.INVISIBLE)
            btnLayout!!.setVisibility(View.INVISIBLE)
        }
        return view
    }

    fun createBtn(tabWidgetLayout: LinearLayout) {
        for (i in 0 until cal.getActualMaximum(Calendar.DAY_OF_MONTH)) { //해당월의 날짜 수 만큼 버튼 생성
            val btn: Button = Button(requireContext()) //버튼 생성
            btn.text = (i + 1).toString() + "일"
            btn.id = (thisMonth * 100) + (i + 1)
            btn.background = ContextCompat.getDrawable(requireContext(), R.drawable.day_btn)
            btn.setOnClickListener {
                fragmentPageNow = btn.id % 100
                btn.isFocusableInTouchMode = true
                btn.requestFocus()
                Log.e("test", "focus" + btn.isFocused)
            }
            btn.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    callFragment(v.getId())
                    Log.e("left", "position" + btn.left)
                    btnScroll!!.scrollTo(btn.left - 231, 0)
                    v.background = ContextCompat.getDrawable(
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
        val todayBtn: Button = tabWidgetLayout.findViewById(date)
        todayBtn.left = 231 * (date % 100 - 1)
        todayBtn.isFocusableInTouchMode = true
        todayBtn.requestFocus()
    }

    public override fun onDestroy() { //종료시 백스택리스너 삭제
        super.onDestroy()
        val fragmentManager: FragmentManager? = getFragmentManager()
        fragmentManager!!.removeOnBackStackChangedListener(mListner)
    }

    public override fun onSaveInstanceState(outState: Bundle) { //홈버튼 누를때 상태 저장
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_NUMBER, mNumber)
    }

    private fun callFragment(fragment_no: Int) { //프래그먼트 전환
        val i: Int = fragment_no
        val fragmentManager: FragmentManager? = getFragmentManager()
        fragmentManager!!.beginTransaction()
            .replace(R.id.fragment_container, MyFragment.Companion.getInstace(i))
            .addToBackStack(null).commit()
    }

    companion object {
        private val FRAGMENT_TAG: String = "FRAGMENT_TAG"
        private val KEY_NUMBER: String = "KEY_NUMBER"
    }
}