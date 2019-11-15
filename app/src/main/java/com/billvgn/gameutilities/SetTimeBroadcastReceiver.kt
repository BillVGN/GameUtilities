package com.billvgn.gameutilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class SetTimeBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            "plus"  -> addHours()
            "minus" -> subtractHours()
            "refill" -> jumpThree()
        }
    }

    private fun addHours() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, 3)
        TimeChanger().setTime(cal)
    }

    private fun subtractHours() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, -3)
        TimeChanger().setTime(cal)
    }

    private fun jumpThree() {
        TimeChanger().thereAndBackAgain(3)
    }
}