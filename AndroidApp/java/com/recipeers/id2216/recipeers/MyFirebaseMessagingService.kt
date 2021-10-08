package com.recipeers.id2216.recipeers

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.FirebaseMessagingService
import java.lang.Exception


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.e("test", "From: " + remoteMessage!!.from!!)
        Log.e("test", "Notification Message Body: " + remoteMessage.notification!!.body!!)
        if (remoteMessage.notification != null) {
            val title = remoteMessage.data.get("title")
            displayNotification(this, "title", "text")
        }
    }

    override fun onNewToken(token: String?) {
        Log.e("test", token)
        var tokenMaybe: String
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            tokenMaybe = task.result!!.token
            Log.e("test", tokenMaybe)
            var data: MutableMap<String, Any> = HashMap()
            data.put("TokenFCM", tokenMaybe)
            db.collection("Users").document(auth.uid.toString()).set(data, SetOptions.merge())
        }
    }

    companion object {
        private val TAG = "FCM Service"
        val auth = FirebaseAuth.getInstance()

        fun displayNotification(context: Context, title: String, text: String) {
            val resultItent = Intent(context, MainActivity::class.java)
            resultItent.putExtra("Fragment", "Message")
            resultItent.putExtra("Sender", title)
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(resultItent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val notification = NotificationCompat.Builder(context, R.string.default_notification_channel_id.toString())
                .setSmallIcon(R.drawable.ic_stat_ic_notifications)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(resultPendingIntent)
                .setStyle(NotificationCompat.BigTextStyle())
                .build()

            with(NotificationManagerCompat.from(context)) {
                notify(1111, notification)
            }
        }
    }

    override fun onMessageSent(p0: String?) {
        Log.e("test", "message sent")
    }

    override fun onSendError(p0: String?, p1: Exception?) {
        Log.e("test", "message not sent")
    }
}

/*
taken from:
https://www.codementor.io/flame3/send-push-notifications-to-android-with-firebase-du10860kb
and also with special thanks to:
https://www.youtube.com/playlist?list=PLk7v1Z2rk4hjM2NPKqtWQ_ndCuoqUj5Hh
 */