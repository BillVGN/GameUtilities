package com.billvgn.gameutilities

import android.app.*
import android.icu.util.TimeZone
import android.os.Bundle
import android.widget.TimePicker
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import java.util.*


class TimePickerFragment : DialogFragment, TimePickerDialog.OnTimeSetListener {

    var ac: MainActivity? = null

    constructor(activity: MainActivity) : super() {
        this.ac = activity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
        ac?.setTime(c.timeInMillis)
    }
}