package com.example.test_project

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver()
{

    override fun onReceive(context: Context, intent: Intent)
    {

        val notificationIntent = Intent(context, NotificationsActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority to HIGH
            .setCategory(NotificationCompat.CATEGORY_CALL) // Use CALL or ALARM for heads-up
            .setAutoCancel(true) // Automatically dismiss when clicked
            .setFullScreenIntent(pendingIntent, true) // Add full-screen intent to ensure heads-up
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}