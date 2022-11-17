package com.ssafy.finalpjt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AddListFragment constructor() : Fragment() {
    var addBtn: Button? = null
    var equest: Spinner? = null
    var ejob: EditText? = null
    var edate: DatePicker? = null
    var prevPage: Int = 0
    var t: LinearLayout? = null
    var b: LinearLayout? = null
    fun getInstance(prevPage: Int, t: LinearLayout?, b: LinearLayout?) {
        this.prevPage = prevPage
        this.t = t
        this.b = b
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: Bundle? = getArguments()
    }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.addlist_layout, null)
        addBtn = view.findViewById<View>(R.id.addBtn) as Button?
        equest = view.findViewById<View>(R.id.spinner1) as Spinner?
        edate = view.findViewById<View>(R.id.date) as DatePicker?
        ejob = view.findViewById<View>(R.id.job) as EditText?
        val dbHelper: DBHelper = DBHelper(view.getContext(), "QuestApp.db", null, 1)
        val questdata: Array<String?> = dbHelper.MainQuest().split("\n").toTypedArray()
        Log.e("quest", questdata.toString())
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(view.getContext(), R.layout.spin, questdata)
        adapter.setDropDownViewResource(R.layout.spin_dropdown)
        equest!!.setAdapter(adapter)
        addBtn!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val quest: String = equest!!.getSelectedItem().toString()
                val date: Int = ((edate!!.getMonth() + 1) * 100) + edate!!.getDayOfMonth()
                val job: String = ejob!!.getText().toString()
                dbHelper.insert(job, date, quest)
                val fragmentManager: FragmentManager? = getFragmentManager()
                fragmentManager!!.beginTransaction()
                    .replace(R.id.fragment_container, MyFragment.Companion.getInstace(prevPage))
                    .addToBackStack(null).commit() //이전 프레그먼트로 돌아가야함
                b!!.setVisibility(View.VISIBLE)
                t!!.setVisibility(View.VISIBLE)
            }
        })
        return view
    }
}