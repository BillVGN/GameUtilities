package com.billvgn.gameutilities

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.widget.TimePicker
import android.widget.Toast
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainActivity : Activity(), TimePickerDialog.OnTimeSetListener {

    private val channelId: String = "GameUtilities"
    private val groupKey = "com.billvgn.gameutilities.NOTIFICATIONS"
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469
    var notifyIds: ArrayList<Int> = ArrayList(0)
    var overlayPermissionGranted = false

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

//        val svc = Intent(this, OverlayRefillService::class.java)
//        if (overlayPermissionGranted) {
//            startService(svc)
//        } else {
//            askForOverlayPermission()
//            if (overlayPermissionGranted) {
//                startService(svc)
//            }
//        }
    }

    private fun askForOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
        } else {
            overlayPermissionGranted = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                overlayPermissionGranted = true
            }
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

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
        cal.set(Calendar.MINUTE, minute)
        TimeChanger().setTime(cal)
    }
}
