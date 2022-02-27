package com.theradot.Utilities

import android.app.Notification
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.theradot.Activities.MainActivity
import com.theradot.R
import io.paperdb.Paper

import java.util.Random;


class PushNotification : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Paper.init(applicationContext)

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val intent = Intent(this, MainActivity::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID: Int = Random().nextInt(3000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.ic_android
        )

        val icon: Bitmap? = null
        try {
            val notificationSoundUri: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, "ADMIN_CHANNEL")
                .setSmallIcon(R.drawable.ic_notif)
                .setLargeIcon(largeIcon)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(icon)
                )
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)

            //Set notification color to match your app color template
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(resources.getColor(R.color.colorPrimaryDark))
            }
            notificationManager.notify(notificationID, notificationBuilder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setupChannels(notification_manager: NotificationManager){
        var channel_name = "Notification channel"
        var channel_desc = "Device to device notifications"

        var admin_channel: NotificationChannel
        admin_channel = NotificationChannel("ADMIN_CHANNEL", channel_name, NotificationManager.IMPORTANCE_HIGH)
        admin_channel.description = channel_desc
        admin_channel.enableLights(true)
        admin_channel.lightColor = Color.RED
        admin_channel.enableVibration(true)
        if(notification_manager != null){
            notification_manager.createNotificationChannel(admin_channel)
        }
    }
}