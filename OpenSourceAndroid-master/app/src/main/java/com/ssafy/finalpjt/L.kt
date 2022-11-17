package com.ssafy.finalpjt

import android.text.TextUtils
import android.util.Log
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
import java.lang.StringBuilder
import java.util.*

object L {
    const val VERBOSE = 1
    const val DEBUG = 2
    const val INFO = 3
    const val WARN = 4
    const val ERROR = 5
    const val ASSET = 6
    const val JSON = 7
    const val XML = 8
    private val ct = System.getProperty("line.separator")
    var LOG_ENABLED = true
    var LOG_LEVEL = 1
    private var TAG = "YLogger"
    private const val cu = false
    fun initialize(var0: String, var1: Boolean) {
        TAG = var0
        LOG_ENABLED = var1
    }

    fun v(var0: String?) {
        a(LOG_ENABLED, 1, TAG, var0)
    }

    fun v(var0: Boolean, var1: String?) {
        a(var0, 1, TAG, var1)
    }

    fun v(var0: Boolean, var1: String, var2: String?) {
        a(var0, 1, var1, var2)
    }

    fun d(var0: String?) {
        a(LOG_ENABLED, 2, TAG, var0)
    }

    fun d(var0: Boolean, var1: String?) {
        a(var0, 2, TAG, var1)
    }

    fun d(var0: Boolean, var1: String, var2: String?) {
        a(var0, 2, var1, var2)
    }

    fun i(var0: String?) {
        a(LOG_ENABLED, 3, TAG, var0)
    }

    fun i(var0: Boolean, var1: String?) {
        a(var0, 3, TAG, var1)
    }

    fun i(var0: Boolean, var1: String, var2: String?) {
        a(var0, 3, var1, var2)
    }

    fun w(var0: String?) {
        a(LOG_ENABLED, 4, TAG, var0)
    }

    fun w(var0: Boolean, var1: String?) {
        a(var0, 4, TAG, var1)
    }

    fun w(var0: Boolean, var1: String, var2: String?) {
        a(var0, 4, var1, var2)
    }

    fun e(var0: String?) {
        a(LOG_ENABLED, 5, TAG, var0)
    }

    fun e(var0: Boolean, var1: String?) {
        a(var0, 5, TAG, var1)
    }

    fun e(var0: Boolean, var1: String, var2: String?) {
        a(var0, 5, var1, var2)
    }

    private fun a(var0: Boolean, var1: Int, var2: String, var3: Any?) {
        if (var0) {
            val var4: Array<String> = a(var2, var3)
            if (var4 != null && var4.size >= 3) {
                val var5 = var4[0]
                val var6 = var4[1]
                val var7 = var4[2]
                a(var1, var5, var7 + var6)
            } else {
                a(var1, var2, var3 as String?)
            }
        }
    }

    private fun a(var0: Int, var1: String, var2: String?) {
        val var3 = var0 or LOG_LEVEL
        if (var3 and 6 == 6) {
            Log.wtf(var1, var2)
        } else if (var3 and 5 == 5) {
            Log.e(var1, var2!!)
        } else if (var3 and 4 == 4) {
            Log.w(var1, var2!!)
        } else if (var3 and 3 == 3) {
            Log.i(var1, var2!!)
        } else if (var3 and 2 == 2) {
            Log.d(var1, var2!!)
        } else if (var3 and 1 == 1) {
            Log.v(var1, var2!!)
        }
    }

    private fun a(var0: String?, vararg var1: Any): Array<String>? {
        val var2 = Thread.currentThread().stackTrace
        return if (var2 != null && var2.size >= 6) {
            val var3: Byte = 5
            var var4 = var2[var3.toInt()].className
            val var5 = var4.split("\\.").toTypedArray()
            if (var5.size > 0) {
                var4 = var5[var5.size - 1] + ".java"
            }
            if (var4.contains("$")) {
                var4 = var4.split("\\$").toTypedArray()[0] + ".java"
            }
            val var6 = var2[var3.toInt()].methodName
            var var7 = var2[var3.toInt()].lineNumber
            if (var7 < 0) {
                var7 = 0
            }
            val var8 = var6.substring(0, 1).uppercase(Locale.getDefault()) + var6.substring(1)
            var var9 = var0 ?: var4
            if (TextUtils.isEmpty(var9)) {
                var9 = TAG
            }
            val var10 = if (var1 == null) "" else a(*var1)
            val var11 = StringBuilder()
            var11.append("[ (").append(var4).append(":").append(var7).append(")#").append(var8)
                .append(" ]")
            val var12 = var11.toString()
            arrayOf(var9, var10, var12)
        } else {
            null
        }
    }

    private fun a(vararg var0: Any): String {
        return if (var0.size > 1) {
            val var4 = StringBuilder()
            var4.append("\n")
            for (var2 in 0 until var0.size) {
                val var3 = var0[var2]
                if (var3 == null) {
                    var4.append("Param").append("[").append(var2).append("]").append(" = ")
                        .append("null").append("\n")
                } else {
                    var4.append("Param").append("[").append(var2).append("]").append(" = ")
                        .append(var3.toString()).append("\n")
                }
            }
            var4.toString()
        } else {
            val var1 = var0[0]
            var1?.toString() ?: "null"
        }
    }
}