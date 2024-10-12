package com.example.test_project

import android.content.Intent
import android.os.IBinder
import android.app.Service
import java.util.Timer
import java.util.TimerTask
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class TimerService:Service()
{
    override fun onBind(p0: Intent?): IBinder? = null

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        val time = intent.getDoubleExtra(TIME_EXTRA,0.0)
        Log.d("TimerService", "Service started with initial time: $time")
        timer.scheduleAtFixedRate(TimeTask(time), 0,1000)
        return START_NOT_STICKY
    }

    override fun onDestroy()
    {
        timer.cancel()
        super.onDestroy()
    }

    private inner class TimeTask(private var time: Double) : TimerTask()
    {
        override fun run()
        {
            val intent = Intent(TIMER_UPDATED)
            time++
            //Log.d("TimerTask", "Time incremented: $time")
            intent.putExtra(TIME_EXTRA,time)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            Log.d("TimerService", "Local broadcast sent with time: $time")

        }
    }

    companion object
    {
        const val TIMER_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timerExtra"
    }


}