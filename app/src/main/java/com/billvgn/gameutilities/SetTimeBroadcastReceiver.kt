package com.billvgn.gameutilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import java.io.DataOutputStream
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
        setTime(cal)
    }

    private fun subtractHours() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, -3)
        setTime(cal)
    }

    private fun jumpThree() {
        addHours()
        Thread.sleep(2000)
        subtractHours()
    }

    private fun setTime(calendar: Calendar) {
        val command = "date " + DateFormat.format("MMddHHmm", calendar) + "\n"
        val su = Runtime.getRuntime().exec("su")
        val dos = DataOutputStream(su.outputStream)
        dos.writeBytes(command)
        dos.writeBytes("exit\n")
        dos.flush()
        dos.close()
        su.waitFor()
    }
}