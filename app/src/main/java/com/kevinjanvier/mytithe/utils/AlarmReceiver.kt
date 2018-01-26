package com.kevinjanvier.mytithe.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.support.v4.content.WakefulBroadcastReceiver
import com.kevinjanvier.mytithe.controller.MainActivity
import com.kevinjanvier.mytithe.service.AlarmService




/**
 * Created by kevinjanvier on 26/01/2018.
 */
@Suppress("DEPRECATION")
class AlarmReceiver: WakefulBroadcastReceiver() {

    lateinit var ringtone:Ringtone

    override fun onReceive(context: Context?, intent: Intent?) {
        //this will update my ui
        MainActivity().setAlarm("Pay you Tithe Today ")
        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)
        val alarmUri:Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone.play()

        println("laaaaaa $alarmUri")

        //After 1s stop the alarm
        // You can adjust the time depending upon your requirement.
        val handler = Handler()
        handler.postDelayed({ ringtone.stop() }, 20000)

        // this will send the notification message

        val comp = ComponentName(context?.packageName, AlarmService::class.java.name)
        WakefulBroadcastReceiver.startWakefulService(context, intent!!.setComponent(comp))
        resultCode = Activity.RESULT_OK

    }
}

