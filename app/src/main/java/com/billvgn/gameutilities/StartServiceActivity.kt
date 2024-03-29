package com.billvgn.gameutilities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import java.lang.Math.*
import java.sql.Time

class StartServiceActivity : Activity() {

    private var overlayPermissionGranted: Boolean = false
    private val rc = random().toInt()
    private var serviceIntent: Intent? = null
    private var tc: TimeChanger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceIntent = Intent(this, OverlayRefillService::class.java)
        if (!overlayPermissionGranted) {
            askForOverlayPermission()
            if (overlayPermissionGranted) {
                startService(serviceIntent)
            }
        } else {
            startService(serviceIntent)
        }
        tc = TimeChanger()
        finish()
    }

    private fun askForOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
            startActivityForResult(intent, rc)
        } else {
            overlayPermissionGranted = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (rc == requestCode) {
            if (resultCode == RESULT_OK) {
                overlayPermissionGranted = true
            }
        }
    }
}
