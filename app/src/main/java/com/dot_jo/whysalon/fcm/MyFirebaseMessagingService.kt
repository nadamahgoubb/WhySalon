package com.dot_jo.whysalon.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.bumptech.glide.Glide
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.param.UpdateFcmTokenParam
import com.dot_jo.whysalon.ui.activity.MainActivity
import com.dot_jo.whysalon.util.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random
import javax.inject.Inject

private const val CHANNEL_ID = "my_channel"
@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val TAG = "MyFirebaseMessagingServ"
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
    }
    @Inject
    lateinit var fcmUseCase: FcmUseCase
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("islam", "onNewToken: $s")
        Log.d("islam", "onNewToken: aaa")
        PrefsHelper.setFCMToken(s)
        sendRegistrationToServer(s)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
         Log.d("isllam", "onMessageReceived: ${remoteMessage.data}")
        showNotification(remoteMessage.data)
    }

    private fun sendRegistrationToServer(token: String?) {
token?.let {fcmUseCase.sendFcmTokenToServer( UpdateFcmTokenParam(it, 0, PrefsHelper.getLanguage())) } }

    private fun showNotification(remoteMessage: Map<String, String>) {
        var bundel = Bundle()


        Log.d("isllam", "showNotification: $remoteMessage")
        val barberId = remoteMessage["barber_id"]
        val orderId = remoteMessage["order_id"]
        val barber_image = remoteMessage["barber_image"]
        val orderStatus = remoteMessage["order_status"]

        val contentIntent: PendingIntent? = (if (orderStatus != null) {

            if (orderStatus == "2") {
                /* if (barberId != null && orderId != null) {
                    intent.putExtra(Constants.BARBER_ID, barberId.toString())
                    intent.putExtra(Constants.ORDER_ID, orderId.toString())
                    intent.putExtra(Constants.BARBER, barber_image.toString())
                }*/
                sendRealTimeBroadcast(FcmResponse(barberId, orderId, barber_image))
                 NavDeepLinkBuilder(applicationContext).setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.main_nav).setDestination(R.id.rateBottomSheet)
                    .setArguments(    bundleOf(
                        Constants.BARBER_ID to barberId,
                        Constants.ORDER_ID to orderId,
                        Constants.BARBER to barber_image
                    )
                    )
                    .createPendingIntent()

            } else {
                val intent = Intent(this, MainActivity::class.java)
                PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)
            }

        } else {
            val intent = Intent(this, MainActivity::class.java)
            PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)

        })



      /*  val intent = Intent(this, MainActivity::class.java)
        intent.action = Constants.OPEN_NOTIFICATION
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        if (orderStatus == "2") {
            if (barberId != null && orderId != null) {
                intent.putExtra(Constants.BARBER_ID, barberId.toString())
                intent.putExtra(Constants.ORDER_ID, orderId.toString())
                intent.putExtra(Constants.BARBER, barber_image.toString())
            }
            sendRealTimeBroadcast(FcmResponse( barberId, orderId, barber_image ))
        }
    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)

        bundel.putParcelable(
            Constants.Notifaction,
            FcmResponse(barberId, orderId, barber_image )
        )
        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext, 100, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
*/

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }


    //   myRemoteViews.setOnClickPendingIntent(R.id.widget_button, myPendingIntent)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(remoteMessage["title"])
            .setContentText(remoteMessage["body"])
            .setContentIntent(contentIntent)
            .setVibrate(pattern)


    notificationManager.notify(Random().nextInt(), notification.build())
}

private fun sendRealTimeBroadcast(response:FcmResponse ) {
    val intent =
        Intent(MainActivity.MAIN_SCREEN_ACTION) //used to receive in intent filter when register the broadcast
    intent.putExtra(Constants.Notifaction, response)
    sendBroadcast(intent)

}
/*        val pendingIntent =
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
     //   notification.setLargeIcon(bitmap)
        Glide.with(this).clear(futureTarget)


        notificationManager.notify(Random().nextInt(), notification.build())
    }*/

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


}