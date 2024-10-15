package com.implementing.cozyspace.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.implementing.cozyspace.R
import com.implementing.cozyspace.mainscreens.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the received message and display a notification
        sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)

    }


    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
//        println(token)
        Log.d("myToken", "" + token)


        sendRegistrationToServer(token)


    }
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val channel = NotificationChannel(
            "default_channel",
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)


        val notificationStyle = NotificationCompat.BigPictureStyle()

        val notificationBuilder = NotificationCompat.Builder(this, "default_channel")
            .setContentTitle(title)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.dood_nn_bmp ))
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_stat_dood_space)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(1)
//            .setStyle(notificationStyle)




        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder.setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }


    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}