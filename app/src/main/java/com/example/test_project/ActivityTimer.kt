package com.example.test_project

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.test_project.databinding.ActivityMainBinding
import com.example.test_project.databinding.ActivityTimerBinding
import kotlin.math.roundToInt
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ActivityTimer : AppCompatActivity()
{
    private lateinit var binding: ActivityTimerBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ActivityTimer", "onCreate called")
        binding = ActivityTimerBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
        setContentView(binding.root)

        binding.startStopButton.setOnClickListener{startStopTimer()}
        binding.resetButton.setOnClickListener{resetTimer()}
//       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//      }

        serviceIntent = Intent(applicationContext,TimerService::class.java)
//        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED), Context.RECEIVER_NOT_EXPORTED)

        LocalBroadcastManager.getInstance(this).registerReceiver(
            updateTime,
            IntentFilter(TimerService.TIMER_UPDATED)
        )

        Log.d("ActivityTimer", "BroadcastReceiver registered")


    }

    private fun resetTimer()
    {
        stopTimer()
        time = 0.0
        binding.timeTV.text = getTimeStringFromDouble(time)

    }

    private fun startStopTimer()
    {
        if (timerStarted)
            stopTimer()
        else
            startTimer()

    }

    private fun startTimer()
    {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        Log.d("ActivityTimer", "Service started")
        binding.startStopButton.text = "stop"
        binding.startStopButton.icon = getDrawable(R.drawable.baseline_pause_24)
        timerStarted = true
    }

    private fun stopTimer()
    {
        stopService(serviceIntent)
        binding.startStopButton.text = "start"
        binding.startStopButton.icon = getDrawable(R.drawable.baseline_play_arrow_24)
        timerStarted = false
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent)
        {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
            //Toast.makeText(context, "Time received: $time", Toast.LENGTH_SHORT).show()
            binding.timeTV.text = getTimeStringFromDouble(time)
        }
    }

    private fun getTimeStringFromDouble(time: Double):String
    {
       val resultInt = time.roundToInt()
        val  hours = resultInt % 86400 / 3600
        val  minutes = resultInt % 86400 % 3600 / 60
        val  seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)

    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d",hour, min, sec)



}