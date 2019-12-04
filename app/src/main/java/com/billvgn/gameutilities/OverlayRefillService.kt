package com.billvgn.gameutilities

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.Icon
import android.media.Image
import android.net.Uri
import android.os.IBinder
import android.view.WindowManager.LayoutParams
import android.view.*
import android.view.Gravity
import android.view.WindowManager
import android.view.MotionEvent
import android.widget.ImageButton
import java.io.File
import kotlin.math.abs
import android.R.attr.y
import android.R.attr.x
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.view.ViewCompat.animate
import android.R.attr.x
import android.R.attr.y
import android.R.attr.start
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class OverlayRefillService : Service(), View.OnTouchListener, View.OnClickListener /*, View.OnLongClickListener*/{

    private var topLeftView: View? = null

    private var overlayedButton: ImageButton? = null
    private var offsetX: Float = 0f
    private var offsetY: Float = 0f
    private var originalXPos: Int = 0
    private var originalYPos: Int = 0
    private var moving: Boolean = false
    private var wm: WindowManager? = null

    override fun onCreate() {
        super.onCreate()

        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        overlayedButton = ImageButton(this)
        overlayedButton?.setImageResource(R.drawable.ic_game_utilities_icon)
        overlayedButton?.background = getDrawable(R.drawable.round_shape)
        overlayedButton?.setOnTouchListener(this)
        overlayedButton?.setOnClickListener(this)
//        overlayedButton?.setOnLongClickListener(this)
        overlayedButton?.tag = "icone"

        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_APPLICATION_OVERLAY,
            LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.LEFT or Gravity.TOP
        params.x = 0
        params.y = 0
        wm?.addView(overlayedButton, params)


        topLeftView = View(this)
        val topLeftParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_APPLICATION_OVERLAY,
            LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSPARENT
        )
        /* Context Menu */
        topLeftView!!.setOnCreateContextMenuListener { menu, _, _ ->
            val inflater = MenuInflater(this)
            inflater.inflate(R.menu.overlay_context_menu, menu)
        }

        topLeftParams.gravity = Gravity.LEFT or Gravity.TOP
        topLeftParams.x = 0
        topLeftParams.y = 0
        topLeftParams.width = 0
        topLeftParams.height = 0

        wm?.addView(topLeftView, topLeftParams)

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.rawX
            val y = event.rawY

            moving = false

            val location = IntArray(2)
            overlayedButton?.getLocationOnScreen(location)

            originalXPos = location[0]
            originalYPos = location[1]

            offsetX = originalXPos - x
            offsetY = originalYPos - y

        } else if (event.action == MotionEvent.ACTION_MOVE) {
            val topLeftLocationOnScreen = IntArray(2)
            topLeftView?.getLocationOnScreen(topLeftLocationOnScreen)

            val x = event.rawX
            val y = event.rawY

            val params = overlayedButton?.layoutParams as LayoutParams

            val newX = (offsetX + x).toInt()
            val newY = (offsetY + y).toInt()

            if (abs(newX - originalXPos) < 1 && abs(newY - originalYPos) < 1 && !moving) {
                return false
            }

            params.x = newX - topLeftLocationOnScreen[0]
            params.y = newY - topLeftLocationOnScreen[1]

            wm?.updateViewLayout(overlayedButton, params)
            moving = true
        } else if (event.action == MotionEvent.ACTION_UP) {
            if (moving) {
                return true
            }
        }

        return false
    }

    override fun onClick(v: View) {
        TimeChanger().thereAndBackAgain(3)
    }

//    override fun onLongClick(v: View): Boolean {
//        if (!moving) {
//            this.stopSelf()
//        }
//        return true
//    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayedButton != null) {
            clearOverlay()
        }
    }

    private fun clearOverlay() {
        wm?.removeView(overlayedButton)
        wm?.removeView(topLeftView)
        overlayedButton = null
        topLeftView = null
    }

}