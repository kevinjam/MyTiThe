package com.kevinjanvier.mytithe.service

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.kevinjanvier.mytithe.R
import com.kevinjanvier.mytithe.controller.MainActivity
import com.kevinjanvier.mytithe.utils.ALARMSERVICE

/**
 * Created by kevinjanvier on 26/01/2018.
 */
class AlarmService : IntentService(ALARMSERVICE) {
    lateinit var notification:NotificationManager

    override fun onCreate() {
        super.onCreate()
        println("====ON CREATE TO BE CALL===================")
    }


    override fun onHandleIntent(intent: Intent?) {

        sendNotification("Wake up now")
    }

    private fun sendNotification(msg: String) {
        println("Alarm Preparation Manager $msg")
        notification = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this, 0 ,
                Intent(this, MainActivity::class.java), 0)
        val alarmNotification = NotificationCompat.Builder(this)
                .setContentTitle("Alarm")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)

        alarmNotification.setContentIntent(pendingIntent)
        notification.notify(1, alarmNotification.build())
        Log.e("AlarmService", "Notification sent.")





    }
}