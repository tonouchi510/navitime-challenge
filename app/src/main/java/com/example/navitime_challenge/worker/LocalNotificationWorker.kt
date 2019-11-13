package com.example.navitime_challenge

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class LocalNotificationWorker(context: Context, params: WorkerParameters): Worker(context, params)  {
    override fun doWork(): Result {
        val pendingIntent = PendingIntent.getActivity(applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java), PendingIntent.FLAG_ONE_SHOT)

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle(inputData.getString("title")) // enqueue元から渡されたタイトルテキストを通知にセット
            .setContentText(inputData.getString("message")) // enqueue元から渡されたテキストを通知にセット
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(1, notification)

        return Result.success()
    }
}