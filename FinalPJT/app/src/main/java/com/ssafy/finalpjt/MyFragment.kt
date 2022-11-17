package com.ssafy.finalpjt

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ssafy.finalpjt.db.model.Goal
import com.ssafy.finalpjt.db.DBConst.GoalTable
import com.ssafy.finalpjt.L
import com.ssafy.finalpjt.db.model.GoalSub
import com.ssafy.finalpjt.db.DBConst.SubGoalTable
import kotlin.Throws
import com.ssafy.finalpjt.db.DbHelper
import com.ssafy.finalpjt.db.dao.GoalDAO
import com.ssafy.finalpjt.db.dao.GoalSubDAO
import kotlin.jvm.Synchronized
import com.ssafy.finalpjt.db.CommonDAO
import com.ssafy.finalpjt.db.DBConst
import com.ssafy.finalpjt.db.BaseAsyncTask
import com.ssafy.finalpjt.db.GoalDataTask
import kotlin.jvm.Volatile
import com.ssafy.finalpjt.db.BaseAsyncTask.SerialExecutor
import com.ssafy.finalpjt.db.GoalSubDataTask
import com.ssafy.finalpjt.DBHelper
import com.ssafy.finalpjt.MainActivity
import com.ssafy.finalpjt.SampleData
import com.ssafy.finalpjt.MyFragment.MyAdapter
import com.ssafy.finalpjt.MyFragment
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.finalpjt.SubItemView
import com.ssafy.finalpjt.db.factory.GoalDAOFactory
import com.ssafy.finalpjt.db.factory.GoalSubDAOFactory
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.finalpjt.FragmentMain.RecyclerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.finalpjt.DetailActivity
import com.tedpark.tedonactivityresult.rx2.TedRxOnActivityResult
import com.ssafy.finalpjt.AddActivity
import com.ssafy.finalpjt.FragmentMain.RecyclerAdapter.ItemViewHolder
import androidx.cardview.widget.CardView
import com.ssafy.finalpjt.FragmentShop.SingerAdapter
import com.ssafy.finalpjt.SingerShopItem
import com.ssafy.finalpjt.SingerViewer
import com.ssafy.finalpjt.FragmentTodo
import com.ssafy.finalpjt.AddListFragment
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationView
import com.ssafy.finalpjt.MainActivity.AlarmHATT
import com.ssafy.finalpjt.FragmentMain
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.ssafy.finalpjt.FragmentGoals
import com.ssafy.finalpjt.FragmentShop
import com.ssafy.finalpjt.FragmentSetting
import com.ssafy.finalpjt.BroadcastD
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import com.ssafy.finalpjt.Goals_PagerAdapter
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.ssafy.finalpjt.DetailActivityUpdate
import com.ssafy.finalpjt.Goals_Sub
import androidx.fragment.app.FragmentPagerAdapter
import com.ssafy.finalpjt.Goals_Fragment1
import java.util.*

class MyFragment : Fragment() {
    var a = ArrayList<SampleData>() //데이터베이스
    var myAdapter: MyAdapter? = null
    var dbHelper: DBHelper? = null
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
        val view = inflater.inflate(R.layout.listfragment, null)
        dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
        if (dbHelper!!.sortTodo(arguments!!.getInt(ARG_NO, 0)) !== "") {
            createList()
        }
        myAdapter = MyAdapter(activity, a)
        val listView = view.findViewById<View>(R.id.listView) as ListView
        listView.divider = ColorDrawable(Color.TRANSPARENT)
        listView.dividerHeight = 15
        listView.adapter = myAdapter //어댑터 연결
        return view
    }

    fun createList() {
        val temp = dbHelper!!.sortTodo(arguments!!.getInt(ARG_NO, 0)).split("\n").toTypedArray()
        val data = Array(5) { arrayOfNulls<String>(temp.size) }
        for (i in temp.indices) {
            for (k in 0..4) {
                data[k][i] = temp[i].split("\\|").toTypedArray()[k]
            }
            a.add(
                SampleData(
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
        val no = arguments!!.getInt(ARG_NO, 0)
        val text = "" + no + "번째 프래그먼트"
        Log.d("MyFragment", "onCreate$text")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val no = arguments!!.getInt(ARG_NO, 0)
    }

    inner class MyAdapter(context: Context?, data: ArrayList<SampleData>) : BaseAdapter() {
        var mContext: Context? = null
        var mLayoutInflater: LayoutInflater? = null
        var sample: ArrayList<SampleData>
        override fun getCount(): Int {
            return sample.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): SampleData {
            return sample[position]
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            val view = mLayoutInflater!!.inflate(R.layout.list_data, null) //리스트 양식 샘플
            val questcolor = view.findViewById<View>(R.id.listLayout) as LinearLayout
            val shape = questcolor.background as GradientDrawable
            val todo_thing = view.findViewById<View>(R.id.todo_thing) as TextView
            val isDone = view.findViewById<View>(R.id.isDone) as CheckBox
            shape.setColor(sample[position].quest)
            todo_thing.text = sample[position].job
            isDone.isChecked = sample[position].isChecked
            sort()
            isDone.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    dbHelper!!.plusUserPoint(10)
                    Toast.makeText(
                        activity!!.applicationContext,
                        "10포인트가 적립되었습니다.",
                        Toast.LENGTH_LONG
                    ).show()
                    val dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
                    dbHelper.updateisDone(sample[position].id, 1)
                    sample[position].setIschecked(true)
                } else {
                    dbHelper!!.minusUserPoint(10)
                    val dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
                    dbHelper.updateisDone(sample[position].id, 0)
                    sample[position].setIschecked(false)
                }
                sort()
                myAdapter!!.notifyDataSetChanged()
            }
            return view
        }

        fun sort() {
            Collections.sort(sample) { o1, o2 -> //수행 여부로 정렬
                //메인퀘스트로 한번 더 정렬해야됨
                var x = 0
                if (o1.getisDone() < o2.getisDone()) {
                    x = -1
                } else if (o1.getisDone() == o2.getisDone()) {
                    if (o1.quest < o2.quest) {
                        x = -1
                    }
                }
                x
            }
        }

        init {
            mContext = context
            sample = data
            mLayoutInflater = LayoutInflater.from(mContext)
        }
    }

    companion object {
        private const val ARG_NO = "ARG_NO"
        fun getInstace(no: Int): MyFragment {
            val fragment = MyFragment()
            val args = Bundle()
            args.putInt(ARG_NO, no)
            fragment.arguments = args
            return fragment
        }
    }
}