package com.w2c.dailyversescompose.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.w2c.dailyversescompose.model.TodayVerse

fun showNotification(context: Context, todayVerse: TodayVerse?) {
    with(NotificationManagerCompat.from(context)) {
        notify(1997, getNotificationBuilder(context, todayVerse).build())
    }
}

private fun getNotificationBuilder(context: Context, todayVerse: TodayVerse?): NotificationCompat.Builder {
    createNotificationChannel(context)
    val verse = Util.formatVerse(todayVerse)
    return NotificationCompat.Builder(context, "verseOfTheDay")
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setContentTitle("Verse of the day")
        .setContentText(verse.split("\n\n")[0])
        .setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(
                    verse
                )
        )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
}

private fun createNotificationChannel(context: Context) {
    val name = "Today Verse"
    val descriptionText = "This channel is to display verse for the day"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel("verseOfTheDay", name, importance).apply {
        description = descriptionText
    }
    // Register the channel with the system.
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}