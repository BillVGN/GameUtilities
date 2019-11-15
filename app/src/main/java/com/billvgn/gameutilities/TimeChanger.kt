package com.billvgn.gameutilities

import android.text.format.DateFormat
import java.io.DataOutputStream
import java.util.*

class TimeChanger {
    private fun addHours(amount: Int) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, amount)
        setTime(cal)
    }

    private fun subtractHours(amount: Int) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, -amount)
        setTime(cal)
    }

    fun thereAndBackAgain(amount: Int) {
        addHours(amount)
        Thread.sleep(2000)
        subtractHours(amount)
    }

    fun setTime(calendar: Calendar) {
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