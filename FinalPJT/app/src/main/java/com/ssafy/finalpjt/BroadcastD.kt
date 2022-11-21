//package com.ssafy.finalpjt
//
//import android.app.Notification
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.graphics.BitmapFactory
//import android.os.Build
//import com.ssafy.finalpjt.activity.MainActivity
//import com.ssafy.finalpjt.database.repository.TodoRepository
//import java.util.*
//
//class BroadcastD : BroadcastReceiver() {
//    var INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED
//    var todoRepository=TodoRepository.get()
//    override fun onReceive(context: Context, intent: Intent) { //알람 시간이 되었을때 onReceive를 호출함
//        //NotificationManager 안드로이드 상태바에 메세지를 던지기위한 서비스 불러오고
//        val cal = Calendar.getInstance()
//        if ( !== "") {
//            val notificationmanager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val pendingIntent = PendingIntent.getActivity(
//                context,
//                0,
//                Intent(context, MainActivity::class.java),
//                PendingIntent.FLAG_UPDATE_CURRENT
//            )
//            var builder: Notification.Builder? = null
//            builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Notification.Builder(context, "당근_채찍")
//            } else Notification.Builder(context)
//            builder.setLargeIcon(
//                BitmapFactory.decodeResource(
//                    context.resources,
//                    R.mipmap.ic_launcher
//                )
//            )
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("오늘 할일이 있습니다")
//                .setWhen(System.currentTimeMillis())
//                .setNumber(10)
//                .setContentTitle("당근과 채찍")
//                .setContentText("오늘의 할일")
//                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setPriority(Notification.PRIORITY_MAX)
//            val inboxStyle = Notification.InboxStyle(builder)
//            val temp =
//                dbHelper.sortTodo((cal[Calendar.MONTH] + 1) * 100 + cal[Calendar.DAY_OF_MONTH])
//                    .split("\n").toTypedArray()
//            val data = Array(5) { arrayOfNulls<String>(temp.size) }
//            for (i in temp.indices) {
//                for (k in 0..4) {
//                    data[k][i] = temp[i].split("\\|").toTypedArray()[k]
//                }
//                inboxStyle.addLine(data[1][i])
//            }
//            inboxStyle.setSummaryText("더 보기")
//            builder.style = inboxStyle
//            notificationmanager.notify(1, builder.build())
//        }
//    }
//}