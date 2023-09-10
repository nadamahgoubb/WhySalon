package com.dot_jo.whysalon.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

private const val CHANNEL_ID = "my_channel"

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("islam", "onNewToken: $s")
        Log.d("islam", "onNewToken: aaa")
        PrefsHelper.setFCMToken(s)
        sendRegistrationToServer(s)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendBroadCast()
        Log.d("isllam", "onMessageReceived: ${remoteMessage.data}")
        showNotification(remoteMessage.data)
    }

    private fun sendRegistrationToServer(token: String?) {
        Log.d("islam", "sendRegistrationTokenToServer($token)")
    }

    private fun showNotification(remoteMessage: Map<String, String>) {


        Log.d("isllam", "showNotification: $remoteMessage")
        val barberId = remoteMessage["barber_id"]
        val orderId = remoteMessage["order_id"]
        val orderStatus = remoteMessage["order_status"]
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.action = Constants.OPEN_NOTIFICATION
        if (orderStatus == "2") {
            if (barberId != null && orderId != null) {
                intent.putExtra(Constants.BARBER_ID, barberId.toString())
                intent.putExtra(Constants.ORDER_ID, orderId.toString())
            }
        }



        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext, 100, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val imageUrl = remoteMessage["imageUrl"]
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(remoteMessage["title"])
            .setContentText(remoteMessage["body"])
            .setContentIntent(pendingIntent)
            .setVibrate(pattern)

        var futureTarget = Glide.with(this)
            .asBitmap()
            .load(R.drawable.logo)
            .submit()
        if (imageUrl != null) {
            futureTarget = Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .submit()
        }
        val bitmap = futureTarget.get()
        notification.setLargeIcon(bitmap)
        Glide.with(this).clear(futureTarget)


        notificationManager.notify(Random().nextInt(), notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channelName = "Why Salon Channel"
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        val channel = NotificationChannel(
            CHANNEL_ID, channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "A notification from Why Salon"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendBroadCast() {
        val intent = Intent()
        intent.action = "com.mrhbaa.whysalon.Notify"
        sendBroadcast(intent)
    }
}