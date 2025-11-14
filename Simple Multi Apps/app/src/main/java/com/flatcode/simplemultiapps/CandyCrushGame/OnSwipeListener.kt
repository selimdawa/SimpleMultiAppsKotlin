package com.flatcode.simplemultiapps.CandyCrushGame

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

open class OnSwipeListener(context: Context?) : OnTouchListener {

    var gestureDetector: GestureDetector

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float,
        ): Boolean {
            var result = false
            val yDiff = e2.y - e1!!.y
            val xDiff = e2.x - e1.x
            if (abs(xDiff) > abs(yDiff)) {
                if (abs(xDiff) > SWIPE_SOLD && abs(velocityX) > SWIPE_VELOCITY_SOLD) {
                    if (xDiff > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    result = true
                }
            } else if (abs(yDiff) > SWIPE_SOLD && abs(velocityY) > SWIPE_VELOCITY_SOLD) {
                if (yDiff > 0) {
                    onSwipeBottom()
                } else {
                    onSwipeTop()
                }
                result = true
            }
            return result
        }
    }

    companion object {
        const val SWIPE_SOLD = 100
        const val SWIPE_VELOCITY_SOLD = 100
    }

    open fun onSwipeLeft() {}
    open fun onSwipeRight() {}
    open fun onSwipeTop() {}
    open fun onSwipeBottom() {}

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}