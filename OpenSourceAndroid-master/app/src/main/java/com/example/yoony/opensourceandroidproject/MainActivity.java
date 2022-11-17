package com.example.yoony.opensourceandroidproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static int ONE_MINUTE = 5626;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("당근과 채찍");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//OREO이상 버전 부터는 알림채널을 만들어주어야한다.
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("당근_채찍", "퀘스트앱", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("channel description");
            //불빛,색상,진동패턴 등 해당 채널의 알림동작 설정
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        new AlarmHATT(getApplicationContext()).Alarm();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentMain fragmentmain = new FragmentMain();
        transaction.replace(R.id.main_fragment, fragmentmain);
        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                final DBHelper dbHelper = new DBHelper(getApplicationContext(), "QuestApp.db", null, 1);

                try {
                    dbHelper.selectUsername();
                } catch (Exception e) {
                    dbHelper.setUserNickname("user", 0);
                }

                //setContentView(R.layout.nav_header_main);
                EditText nickname = (EditText) findViewById(R.id.user_nickname);
                nickname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        dbHelper.updateUserNickname(nickname.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                nickname.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(nickname.getWindowToken(), 0);
                            return true;
                        }
                        return false;
                    }
                });
                TextView point = (TextView) findViewById(R.id.user_point);

                if (dbHelper.selectUsername() == "") {
                    dbHelper.setUserNickname("user", 0);
                } else {
                    String ds = dbHelper.selectUsername();
                    int dd = dbHelper.selectUserpoint();
                    String de = String.valueOf(dd);
                    nickname.setText(ds);
                    point.setText(de + " point");
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };

        //drawer.setDrawerListener(toggle); // 이건 Deprecated 되었다니깐 아래와 같이 바꿔줍니다.
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragmentMain fragmentmain = new FragmentMain();
            transaction.replace(R.id.main_fragment, fragmentmain);
            transaction.commit();
        } else if (id == R.id.nav_todo) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragmentTodo fragmenttodo = new FragmentTodo();
            transaction.replace(R.id.main_fragment, fragmenttodo);
            transaction.commit();
        } else if (id == R.id.nav_goals) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragmentGoals fragmentgoals = new FragmentGoals();
            transaction.replace(R.id.main_fragment, fragmentgoals);
            transaction.commit();
        } else if (id == R.id.nav_shop) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragmentShop fragmentshop = new FragmentShop();
            transaction.replace(R.id.main_fragment, fragmentshop);
            transaction.commit();
        } else if (id == R.id.nav_setting) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            settingPreferenceFragment settingPreferencefragment = new settingPreferenceFragment();
//            transaction.replace(R.id.main_fragment, settingPreferencefragment);
//            transaction.commit();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragmentSetting fragmentSetting = new FragmentSetting();
            transaction.replace(R.id.main_fragment, fragmentSetting);
            transaction.commit();
//            settingPreferenceFragment setting = new settingPreferenceFragment();
//            getFragmentManager().beginTransaction().replace(R.id.main_fragment, setting).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {
            this.context = context;
        }

        public void Alarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, BroadcastD.class);

            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)+1, 0,0,0);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, sender);
        }
    }
}
