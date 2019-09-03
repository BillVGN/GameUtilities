package com.billvgn.gameutilities.com.billvgn.gameutilities

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*
import kotlin.concurrent.timer

class SetTimeBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            "plus"  -> addHours(context)
            "minus" -> subtractHours(context)
            "refill" -> jumpThree(context)
        }
    }

    fun addHours(context: Context) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, 3)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setTime(cal.timeInMillis)
    }

    fun subtractHours(context: Context) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, -3)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setTime(cal.timeInMillis)
    }

    fun jumpThree(context: Context) {
        addHours(context)
        Thread.sleep(1000)
        subtractHours(context)
    }

}