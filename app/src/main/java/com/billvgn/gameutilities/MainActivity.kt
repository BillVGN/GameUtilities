package com.billvgn.gameutilities

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.text.format.DateFormat
import android.view.View
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import android.text.format.DateFormat.is24HourFormat
import java.io.DataOutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainActivity : Activity(), TimePickerDialog.OnTimeSetListener {

    var am: AlarmManager? = null
    private val channelId: String = "GameUtilities"
    private val groupKey = "com.billvgn.gameutilities.NOTIFICATIONS"
    var notifyIds: ArrayList<Int> = ArrayList(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        try {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, MainActivity::class.java)
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val refill = Intent(this, SetTimeBroadcastReceiver::class.java).apply {
                action = "refill"
            }

            // PendingIntents plus and minus
            val refillPendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, refill, 0)

            val actionBuilder = Notification.Action.Builder(
                Icon.createWithResource(this, R.drawable.ic_refill),
                getString(R.string.refillAction),
                refillPendingIntent
            )

            val builder = Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.mainNotificationTitle))
                .setContentText(getString(R.string.mainNotificationText))
                .setGroup(groupKey)
                .setGroupSummary(true)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .addAction(actionBuilder.build())

            val notMan: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            createNotificationChannel(notMan)

            notifyIds.add(Random.nextInt(101))

            val sbNotification: Array<out StatusBarNotification> = notMan.activeNotifications
            if (sbNotification.isEmpty()) {
                notMan.notify(channelId, notifyIds.last(), builder.build())
            } else {
                notMan.notify(channelId, sbNotification[0].id, builder.build())
            }
        } catch (ex: Exception) {
            Toast.makeText(this, ex.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = getString(R.string.channelName)
        val descriptionText = getString(R.string.channelDescription)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        notificationManager.createNotificationChannel(channel)
    }

    fun notifyBtnClicked(view: View) {
        createNotification(
            "Mais um teste",
            "Teste",
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    private fun createNotification(content: String, title: String, pIntent: PendingIntent) {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = Notification.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentText(content)
            .setContentTitle(title)
            .setGroup(groupKey)
            .setGroupSummary(notificationManager.activeNotifications.isEmpty())
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pIntent)

        var id: Int
        do id = Random.nextInt(101) while (notifyIds.contains(id))
        notifyIds.add(id)
        notificationManager.notify(channelId, id, builder.build())
    }

    fun showTimePickerDialog(view: View) {
        val cal = Calendar.getInstance()
        TimePickerDialog(
            this,
            this,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            is24HourFormat(this)
        ).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        setTime(cal)
    }

    private fun getDateString(calendar: Calendar):CharSequence {
        return DateFormat.format("MMddHHmm", calendar)
    }

    private fun setTime(calendar: Calendar) {
        val command = "date " + getDateString(calendar) + "\n"
        val su = Runtime.getRuntime().exec("su")
        val dos = DataOutputStream(su.outputStream)
        dos.writeBytes(command)
        dos.writeBytes("exit\n")
        dos.flush()
        dos.close()
        su.waitFor()
    }

    fun addHours(view: View) {
        val cal = Calendar.getInstance()
        val spinHours = findViewById<Spinner>(R.id.spnHours).selectedItem.toString().toInt()
        cal.add(Calendar.HOUR_OF_DAY, spinHours)
        setTime(cal)
    }

    fun subtractHours(view: View) {
        val cal = Calendar.getInstance()
        val spinHours = findViewById<Spinner>(R.id.spnHours).selectedItem.toString().toInt()
        cal.add(Calendar.HOUR_OF_DAY, -spinHours)
        setTime(cal)
    }
}
