package com.billvgn.gameutilities

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class CCFSLauncherWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val intent = Intent(context, ExecuteGameBroadcastReceiver::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.putExtra("packageName", "com.king.candycrush4")
    val pending = PendingIntent.getBroadcast(context, 0, intent, 0)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.ccfslauncher_widget)
    views.setOnClickPendingIntent(R.id.ccfs_launcher_imgbutton, pending)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}