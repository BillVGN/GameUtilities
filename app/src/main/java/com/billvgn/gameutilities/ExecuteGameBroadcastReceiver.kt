package com.billvgn.gameutilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ExecuteGameBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appIntent = context.packageManager.getLaunchIntentForPackage(intent.getStringExtra("packageName")!!)
        context.startActivity(appIntent)
    }
}