package com.ssafy.finalpjt.activity

import android.annotation.SuppressLint
import android.app.*
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.ssafy.finalpjt.*
import com.ssafy.finalpjt.database.DatabaseApplicationClass
import com.ssafy.finalpjt.database.dto.User
import com.ssafy.finalpjt.database.repository.UserRepository
import com.ssafy.finalpjt.fragment.*

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private val userRepository = UserRepository.get()
    private val sharedPreferencesUtil = DatabaseApplicationClass.sharedPreferencesUtil
    lateinit var user : User
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = Firebase.analytics


        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.METHOD,"test")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

        title = "당근과 채찍"


        userRepository.getUserByName(sharedPreferencesUtil.getUserName()).observe(this) {
            user = it
        }

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // 초기화면 설정
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_containter, FragmentMain())
            .commit()

        initDrawer()

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        when(id) {
            R.id.nav_main -> {
                transaction.replace(R.id.main_fragment_containter, FragmentMain()).commit()
            }
            R.id.nav_todo -> {
                transaction.replace(R.id.main_fragment_containter, FragmentTodo()).commit()
            }
            R.id.nav_goals -> {
                transaction.replace(R.id.main_fragment_containter, FragmentMyGoals()).commit()
            }
            R.id.nav_shop -> {
                transaction.replace(R.id.main_fragment_containter, FragmentShop()).commit()
            }
            R.id.nav_setting -> {
                transaction.replace(R.id.main_fragment_containter, FragmentSetting()).commit()
            }
        }

        val drawer: DrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initDrawer() {
        val drawer: DrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
            }

            @SuppressLint("SetTextI18n")
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                val nickname: TextView = findViewById<View>(R.id.user_nickname) as TextView
                val point: TextView = findViewById<View>(R.id.user_point) as TextView

                nickname.text = user.UserName
                point.text = "${user.Point} point"

            }
        }
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun changeFragment(index :Int){
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        when(index){
            1->{
                transaction.replace(R.id.main_fragment_containter, FragmentTodo()).commit()
            }
            2->{
                transaction.replace(R.id.main_fragment_containter, AddTodoListFragment()).commit()
            }
        }
    }

}