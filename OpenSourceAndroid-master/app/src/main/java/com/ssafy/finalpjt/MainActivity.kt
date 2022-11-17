package com.ssafy.finalpjt

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
import androidx.appcompat.widget.Toolbar
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
import androidx.fragment.app.FragmentTransaction
import com.ssafy.finalpjt.Goals_Fragment1
import java.lang.Exception
import java.util.*

class MainActivity constructor() : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("당근과 채찍")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //OREO이상 버전 부터는 알림채널을 만들어주어야한다.
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel: NotificationChannel =
                NotificationChannel("당근_채찍", "퀘스트앱", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.setDescription("channel description")
            //불빛,색상,진동패턴 등 해당 채널의 알림동작 설정
            notificationChannel.enableLights(true)
            notificationChannel.setLightColor(Color.GREEN)
            notificationChannel.enableVibration(true)
            notificationChannel.setVibrationPattern(longArrayOf(100, 200, 100, 200))
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        AlarmHATT(getApplicationContext()).Alarm()
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */
        val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        val fragmentmain: FragmentMain = FragmentMain()
        transaction.replace(R.id.main_fragment, fragmentmain)
        transaction.commit()
        val drawer: DrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            public override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
            }

            public override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                val dbHelper: DBHelper = DBHelper(getApplicationContext(), "QuestApp.db", null, 1)
                try {
                    dbHelper.selectUsername()
                } catch (e: Exception) {
                    dbHelper.setUserNickname("user", 0)
                }

                //setContentView(R.layout.nav_header_main);
                val nickname: EditText = findViewById<View>(R.id.user_nickname) as EditText
                nickname.addTextChangedListener(object : TextWatcher {
                    public override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                    }

                    public override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                        dbHelper.updateUserNickname(nickname.getText().toString())
                    }

                    public override fun afterTextChanged(editable: Editable) {}
                })
                nickname.setOnKeyListener(object : View.OnKeyListener {
                    public override fun onKey(view: View, i: Int, keyEvent: KeyEvent): Boolean {
                        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                            val imm: InputMethodManager =
                                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(nickname.getWindowToken(), 0)
                            return true
                        }
                        return false
                    }
                })
                val point: TextView = findViewById<View>(R.id.user_point) as TextView
                if (dbHelper.selectUsername() === "") {
                    dbHelper.setUserNickname("user", 0)
                } else {
                    val ds: String? = dbHelper.selectUsername()
                    val dd: Int = dbHelper.selectUserpoint()
                    val de: String = dd.toString()
                    nickname.setText(ds)
                    point.setText(de + " point")
                }
            }

            public override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }

            public override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
            }
        }

        //drawer.setDrawerListener(toggle); // 이건 Deprecated 되었다니깐 아래와 같이 바꿔줍니다.
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView: NavigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
    }

    public override fun onBackPressed() {
        val drawer: DrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        // Inflate the menu; this adds items to the action bar if it is present.
    //        getMenuInflater().inflate(R.menu.main, menu);
    //        return true;
    //    }
    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id: Int = item.getItemId()
        if (id == R.id.nav_main) {
            val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
            val fragmentmain: FragmentMain = FragmentMain()
            transaction.replace(R.id.main_fragment, fragmentmain)
            transaction.commit()
        } else if (id == R.id.nav_todo) {
            val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
            val fragmenttodo: FragmentTodo = FragmentTodo()
            transaction.replace(R.id.main_fragment, fragmenttodo)
            transaction.commit()
        } else if (id == R.id.nav_goals) {
            val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
            val fragmentgoals: FragmentGoals = FragmentGoals()
            transaction.replace(R.id.main_fragment, fragmentgoals)
            transaction.commit()
        } else if (id == R.id.nav_shop) {
            val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
            val fragmentshop: FragmentShop = FragmentShop()
            transaction.replace(R.id.main_fragment, fragmentshop)
            transaction.commit()
        } else if (id == R.id.nav_setting) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            settingPreferenceFragment settingPreferencefragment = new settingPreferenceFragment();
//            transaction.replace(R.id.main_fragment, settingPreferencefragment);
//            transaction.commit();
            val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
            val fragmentSetting: FragmentSetting = FragmentSetting()
            transaction.replace(R.id.main_fragment, fragmentSetting)
            transaction.commit()
            //            settingPreferenceFragment setting = new settingPreferenceFragment();
//            getFragmentManager().beginTransaction().replace(R.id.main_fragment, setting).commit();
        }
        val drawer: DrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    inner class AlarmHATT constructor(private val context: Context) {
        fun Alarm() {
            val am: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent: Intent = Intent(this@MainActivity, BroadcastD::class.java)
            val sender: PendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, 0)
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
                    Calendar.DATE
                ) + 1, 0, 0, 0
            )
            am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                sender
            )
        }
    }

    companion object {
        private val ONE_MINUTE: Int = 5626
    }
}