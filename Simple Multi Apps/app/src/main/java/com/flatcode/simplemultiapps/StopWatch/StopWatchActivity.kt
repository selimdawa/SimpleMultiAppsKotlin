package com.flatcode.simplemultiapps.StopWatch

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityStopWatchBinding

class StopWatchActivity : AppCompatActivity() {

    private var binding: ActivityStopWatchBinding? = null
    var context: Context = this@StopWatchActivity
    private var isResume = false
    var handler: Handler? = null
    var tMilliSec: Long = 0
    var tStart: Long = 0
    var tBuff: Long = 0
    var tUpdate = 0L
    var sec = 0
    var min = 0
    var milliSec = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityStopWatchBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.text = getString(R.string.stop_watch)
        handler = Handler()
        binding!!.btStart.setOnClickListener {
            if (!isResume) {
                tStart = SystemClock.uptimeMillis()
                handler!!.postDelayed(runnable, 0)
                binding!!.chronometer.start()
                isResume = true
                binding!!.btStop.visibility = View.GONE
                binding!!.btStart.setImageDrawable(resources.getDrawable(R.drawable.ic_pause))
            } else {
                tBuff += tMilliSec
                handler!!.removeCallbacks(runnable)
                binding!!.chronometer.stop()
                isResume = false
                binding!!.btStop.visibility = View.VISIBLE
                binding!!.btStart.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
            }
        }
        binding!!.btStop.setOnClickListener {
            if (!isResume) {
                binding!!.btStart.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
                tMilliSec = 0L
                tStart = 0L
                tBuff = 0L
                tUpdate = 0L
                sec = 0
                min = 0
                milliSec = 0
                binding!!.lastTimeDate.text = binding!!.chronometer.text.toString()
                binding!!.chronometer.text = "00:00:00"
            }
        }
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            tMilliSec = SystemClock.uptimeMillis() - tStart
            tUpdate = tBuff + tMilliSec
            sec = (tUpdate / 1000).toInt()
            min = sec / 60
            sec %= 60
            milliSec = (tUpdate % 100).toInt()
            binding!!.chronometer.text = (String.format("%02d", min) + ":"
                    + String.format("%02d", sec) + ":" + String.format("%02d", milliSec))
            handler!!.postDelayed(this, 60)
        }
    }
}