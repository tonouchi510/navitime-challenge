package com.example.navitime_challenge

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class NotificationService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "通知のタイトル的情報を設定"
        val id = "casareal_foreground"
        val notifyDescription = "この通知の詳細情報を設定します"

        if (manager.getNotificationChannel(id) == null) {
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            manager.createNotificationChannel(mChannel)
        }

        val resultIntent : PendingIntent = PendingIntent.getActivity(this, 0, Intent(this,MainActivity::class.java),0)

        val notification = NotificationCompat.Builder(this,id).apply {
            setSmallIcon(R.drawable.ic_launcher_background)
            setContentTitle("通知のタイトル")
            setContentText("通知の内容")
            setContentIntent(resultIntent)
        }.build()

        Thread(
            Runnable {
                (0..5).map {
                    Thread.sleep(1000)
                }

                stopForeground(Service.STOP_FOREGROUND_DETACH)

            }).start()

        startForeground(1, notification)

        return START_STICKY

    }
}

