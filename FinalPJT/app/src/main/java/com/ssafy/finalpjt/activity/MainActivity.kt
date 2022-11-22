package com.ssafy.finalpjt.activity

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import com.ssafy.finalpjt.*
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.database.repository.TodoRepository
import com.ssafy.finalpjt.database.repository.UserRepository
import com.ssafy.finalpjt.fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    var userRepository=UserRepository.get()
    var todoRepository= TodoRepository.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "당근과 채찍"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //OREO이상 버전 부터는 알림채널을 만들어주어야한다.
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel =
                NotificationChannel("당근_채찍", "퀘스트앱", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.description = "channel description"
            //불빛,색상,진동패턴 등 해당 채널의 알림동작 설정
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 100, 200)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(notificationChannel)
        }
//        AlarmHATT(applicationContext).alarm()
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // 초기화면 설정
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment, FragmentMain())
            .commit()
        val drawer: DrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
            }

            @SuppressLint("SetTextI18n")
            override fun onDrawerOpened(drawerView: View) {
                var user=User("test",500)
                super.onDrawerOpened(drawerView)
//                val dbHelper = DBHelper(applicationContext, "QuestApp.db", null, 1)

                    userRepository.getUser().observe(this@MainActivity){
                        if(it==null){
                            CoroutineScope(Dispatchers.IO).launch {
                                userRepository.insertUser(user)
                            }
                        }else   user=it
                    }



                //setContentView(R.layout.nav_header_main);
                val nickname: TextView = findViewById<View>(R.id.user_nickname) as TextView
                val point: TextView = findViewById<View>(R.id.user_point) as TextView



                nickname.text = user.UserName
                    point.text = "${user.Point} point"

            }
        }

        //drawer.setDrawerListener(toggle); // 이건 Deprecated 되었다니깐 아래와 같이 바꿔줍니다.
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView: NavigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id: Int = item.itemId
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id: Int = item.itemId
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        when(id) {
            R.id.nav_main -> {
                transaction.replace(R.id.main_fragment, FragmentMain()).commit()
            }
            R.id.nav_todo -> {
                transaction.replace(R.id.main_fragment, FragmentTodo()).commit()
            }
            R.id.nav_goals -> {
                transaction.replace(R.id.main_fragment, FragmentGoals()).commit()
            }
            R.id.nav_shop -> {
                transaction.replace(R.id.main_fragment, FragmentShop()).commit()
            }
            R.id.nav_setting -> {
                transaction.replace(R.id.main_fragment, FragmentSetting()).commit()
            }
        }

        val drawer: DrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    fun changeFragment(index :Int){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        when(index){
            1->{
                transaction.replace(R.id.main_fragment, FragmentTodo()).commit()
            }
            2->{
                transaction.replace(R.id.main_fragment, AddTodoListFragment()).commit()
            }
        }
    }
//
//    inner class AlarmHATT constructor(private val context: Context) {
//        fun alarm() {
//            todoRepository.getTodayTodo(System.currentTimeMillis()).observe(viewlifecycle)
//            val am: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//            val intent: Intent = Intent(this@MainActivity, BroadcastD::class.java)
//            val sender: PendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//            val calendar: Calendar = Calendar.getInstance()
//            calendar.set(
//                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
//                    Calendar.DATE
//                ) + 1, 0, 0, 0
//            )
//            am.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                AlarmManager.INTERVAL_DAY,
//                sender
//            )
//        }
//    }

    companion object {
        private val ONE_MINUTE: Int = 5626
    }
}