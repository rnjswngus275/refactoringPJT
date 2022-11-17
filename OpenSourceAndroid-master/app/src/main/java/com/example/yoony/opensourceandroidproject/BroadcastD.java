package com.example.yoony.opensourceandroidproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import java.util.Calendar;

public class BroadcastD extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출함
        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
        Calendar cal = Calendar.getInstance();
        DBHelper dbHelper = new DBHelper(context, "QuestApp.db", null, 1);
        if (dbHelper.sortTodo(((cal.get(Calendar.MONTH) + 1) * 100) + cal.get(Calendar.DAY_OF_MONTH)) != "") {
            NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                builder = new Notification.Builder(context, "당근_채찍");
            } else builder = new Notification.Builder(context);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("오늘 할일이 있습니다")
                    .setWhen(System.currentTimeMillis())
                    .setNumber(10)
                    .setContentTitle("당근과 채찍")
                    .setContentText("오늘의 할일")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX);

            Notification.InboxStyle inboxStyle = new Notification.InboxStyle(builder);

            String[] temp = dbHelper.sortTodo((cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH)).split("\n");
            String data[][] = new String[5][temp.length];
            for (int i = 0; i < temp.length; i++) {
                for (int k = 0; k < 5; k++) {
                    data[k][i] = temp[i].split("\\|")[k];
                }
                inboxStyle.addLine(data[1][i]);
            }
            inboxStyle.setSummaryText("더 보기");
            builder.setStyle(inboxStyle);

            notificationmanager.notify(1, builder.build());
        }
    }
}
