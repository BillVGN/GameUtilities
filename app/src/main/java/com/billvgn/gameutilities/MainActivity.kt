package com.billvgn.gameutilities

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.billvgn.gameutilities.com.billvgn.gameutilities.SetTimeBroadcastReceiver
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var am: AlarmManager? = null
    private val CHANNEL_ID: String = "GameUtilities"
    private val GROUP_KEY = "com.billvgn.gameutilities.NOTIFICATIONS"
    var notifyIds: ArrayList<Int> = ArrayList(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        try {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Create an explicit intent for an Activity in your app
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            createNotificationChannel()

            // Action buttons plus and minus
//            val plus = Intent(this, SetTimeBroadcastReceiver::class.java).apply {
//                action = "plus"
//            }
//
//            val minus = Intent(this, SetTimeBroadcastReceiver::class.java).apply {
//                action = "minus"
//            }

            val refill = Intent(this, SetTimeBroadcastReceiver::class.java).apply {
                action = "refill"
            }


            // PendingIntents plus and minus
//            val plusPendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0, plus,0)
//            val minusPendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0, minus,0)
            val refillPendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0, refill,0)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.mainNotificationTitle))
                .setContentText(getString(R.string.mainNotificationText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
//                .addAction(R.drawable.ic_add, getString(R.string.addAction), plusPendingIntent)
//                .addAction(R.drawable.ic_minus, getString(R.string.minusAction), minusPendingIntent)
                .addAction(R.drawable.ic_refill, getString(R.string.refillAction), refillPendingIntent)


            notifyIds.add(Random.nextInt(101))
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(CHANNEL_ID ,notifyIds.last(), builder.build())
            }
        } catch (ex: Exception) {
            Toast.makeText(this, ex.localizedMessage, Toast.LENGTH_LONG)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channelName)
            val descriptionText = getString(R.string.channelDescription)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun notifyBtnClicked(view: View) {
        createNotification(
            "Mais um teste",
            "Teste",
            PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }, 0)
        )
    }

    private fun createNotification(content: String, title: String, pIntent: PendingIntent) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentText(content)
            .setContentTitle(title)
            .setGroup(GROUP_KEY)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pIntent)

        var id = 0
        do id = Random.nextInt(101) while (notifyIds.contains(id))
        notifyIds.add(id)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(CHANNEL_ID ,id, builder.build())
        }
    }

    fun showTimePickerDialog(view: View) {
        TimePickerFragment(this).show(this.supportFragmentManager, "timePicker")
    }

    fun setTime(timeInMillis: Long) {
        if (am !is AlarmManager) am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am?.setTime(timeInMillis)
    }

    fun addHours(view: View) {
        val cal = Calendar.getInstance()
        val spinHours = findViewById<Spinner>(R.id.spnHours).selectedItem.toString().toInt()
        cal.add(Calendar.HOUR_OF_DAY, spinHours)
        if (am !is AlarmManager) am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am?.setTime(cal.timeInMillis)
    }

    fun subtractHours(view: View) {
        val cal = Calendar.getInstance()
        val spinHours = findViewById<Spinner>(R.id.spnHours).selectedItem.toString().toInt()
        cal.add(Calendar.HOUR_OF_DAY, -spinHours)
        if (am !is AlarmManager) am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am?.setTime(cal.timeInMillis)
    }
}
