package com.ssafy.finalpjt

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import java.util.ArrayList

class FragmentShop() : Fragment() {
    var gridView: GridView? = null
    var editText: EditText? = null
    var editText2: EditText? = null
    var button: Button? = null
    var singerAdapter: SingerAdapter? = null
    var items = ArrayList<SingerShopItem>()
    var dbHelper: DBHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        dbHelper = DBHelper(view.context, "QuestApp.db", null, 1)
        if (dbHelper!!.isEmptyShopItem == 0) {
            createShopList()
        }
        gridView = view.findViewById<View>(R.id.gridView) as GridView
        editText = view.findViewById<View>(R.id.editText) as EditText
        editText2 = view.findViewById<View>(R.id.editText2) as EditText
        button = view.findViewById<View>(R.id.button) as Button
        singerAdapter = SingerAdapter()
        gridView!!.adapter = singerAdapter
        gridView!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(
                    (activity)!!
                )
                builder.setMessage("구매하시겠습니까?")
                builder.setPositiveButton("예",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            if (dbHelper!!.selectUserpoint() < items.get(i).cost) {
                                Toast.makeText(
                                    activity!!.applicationContext,
                                    (items.get(i).cost - dbHelper!!.selectUserpoint()).toString() + "포인트가 부족하여 구매할 수 없습니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                dbHelper!!.minusUserPoint(items.get(i).cost)
                                Toast.makeText(
                                    activity!!.applicationContext,
                                    items.get(i).cost.toString() + "포인트를 지불하여 구매하였습니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
                builder.setNegativeButton("아니오",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Toast.makeText(
                                activity!!.applicationContext,
                                "아니오를 선택했습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                builder.show()
            }
        }
        gridView!!.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                adapterView: AdapterView<*>?,
                view: View,
                i: Int,
                l: Long
            ): Boolean {
                val builder: AlertDialog.Builder = AlertDialog.Builder(
                    (activity)!!
                )
                builder.setMessage("삭제하시겠습니까?")
                builder.setPositiveButton("예",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            dbHelper!!.deleteShopItem(items.get(i).name)
                            Toast.makeText(
                                activity!!.applicationContext,
                                items.get(i).name + "아이템을 삭제하였습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            items.removeAt(i)
                            singerAdapter!!.notifyDataSetChanged()
                        }
                    })
                builder.setNegativeButton("아니오",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Toast.makeText(
                                activity!!.applicationContext,
                                "아니오를 선택했습니다.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                builder.show()
                return true
            }
        }
        button!!.setOnClickListener(View.OnClickListener {
            val name = editText!!.text.toString().trim { it <= ' ' }
            val cost = editText2!!.text.toString().trim { it <= ' ' }.toInt()
            items.add(
                SingerShopItem(
                    name,
                    cost,
                    resources.getIdentifier("index" + items.size, "drawable", context!!.packageName)
                )
            )
            singerAdapter!!.notifyDataSetChanged()
            dbHelper!!.setShopItem(name, cost)
            //singerAdapter.addItem(new SingerShopItem(name, cost, R.drawable.gift));
        })
        return view
    }

    fun createShopList() {
        items.clear()
        val temp = dbHelper!!.selectShopItem().split("\n").toTypedArray()
        val data = Array(2) { arrayOfNulls<String>(temp.size) }
        for (i in temp.indices) {
            for (k in 0..1) {
                data[k][i] = temp[i].split("\\|").toTypedArray()[k]
            }
            Log.e("sangeun", (data[0][i])!!)
            Log.e("sangeun", (data[1][i])!!)
            items.add(
                SingerShopItem(
                    data[0][i],
                    data[1][i]!!.toInt(),
                    resources.getIdentifier("index$i", "drawable", context!!.packageName)
                )
            )
        }
    }

    inner class SingerAdapter() : BaseAdapter() {
        override fun getCount(): Int {
            return items.size
        }

        fun addItem(singerItem: SingerShopItem) {
            items.add(singerItem)
        }

        override fun getItem(i: Int): SingerShopItem {
            return items[i]
        }

        override fun getItemId(i: Int): Long {
            return i.toLong()
        }

        override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
            val singerViewer = SingerViewer(activity!!.applicationContext)
            singerViewer.setItem(items[i])
            return singerViewer
        }
    }
}