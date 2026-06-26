package com.flatcode.simplemultiapps.StopWatch

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.flatcode.simplemultiapps.R
import com.flatcode.simplemultiapps.Unit.THEME
import com.flatcode.simplemultiapps.databinding.ActivityStopWatchBinding

class StopWatchActivity : AppCompatActivity() {

    private var _binding: ActivityStopWatchBinding? = null
    private val binding get() = _binding!!

    val context: Context = this@StopWatchActivity
    private var isResume = false
    private var handler: Handler? = null
    private var tMilliSec: Long = 0
    private var tStart: Long = 0
    private var tBuff: Long = 0
    private var tUpdate = 0L
    private var sec = 0
    private var min = 0
    private var milliSec = 0

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            tMilliSec = SystemClock.uptimeMillis() - tStart
            tUpdate = tBuff + tMilliSec
            sec = (tUpdate / 1000).toInt()
            min = sec / 60
            sec %= 60
            milliSec = (tUpdate % 100).toInt()

            binding.chronometer.text = String.format("%02d:%02d:%02d", min, sec, milliSec)
            handler?.postDelayed(this, 60)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        _binding = ActivityStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.nameSpace.text = getString(R.string.stop_watch)
        handler = Handler(Looper.getMainLooper())

        binding.btStart.setOnClickListener {
            if (!isResume) {
                tStart = SystemClock.uptimeMillis()
                handler?.postDelayed(runnable, 0)
                binding.chronometer.start()
                isResume = true
                binding.btStop.visibility = View.GONE
                binding.btStart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pause))
            } else {
                tBuff += tMilliSec
                handler?.removeCallbacks(runnable)
                binding.chronometer.stop()
                isResume = false
                binding.btStop.visibility = View.VISIBLE
                binding.btStart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play))
            }
        }

        binding.btStop.setOnClickListener {
            if (!isResume) {
                binding.btStart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play))
                tMilliSec = 0L
                tStart = 0L
                tBuff = 0L
                tUpdate = 0L
                sec = 0
                min = 0
                milliSec = 0
                binding.lastTimeDate.text = binding.chronometer.text.toString()
                binding.chronometer.text = "00:00:00"
            }
        }
    }

    override fun onDestroy() {
        handler?.removeCallbacks(runnable)
        super.onDestroy()
        _binding = null
    }
}