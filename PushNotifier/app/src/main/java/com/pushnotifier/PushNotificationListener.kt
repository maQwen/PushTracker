package com.pushnotifier

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.pushnotifier.data.AppDatabase
import com.pushnotifier.data.PushNotification

class PushNotificationListener : NotificationListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        Log.d("PushNotifier", "NotificationListenerService created")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try {
            val packageName = sbn.packageName
            
            // Skip our own app notifications
            if (packageName == this.packageName) {
                return
            }

            val extras = sbn.notification.extras
            val title = extras.getString(android.app.Notification.EXTRA_TITLE)
            val message = extras.getString(android.app.Notification.EXTRA_TEXT)
            
            // Convert bundle extras to string for storage
            val extrasString = bundleToString(extras)

            val notification = PushNotification(
                packageName = packageName,
                title = title,
                message = message,
                timestamp = System.currentTimeMillis(),
                extras = extrasString
            )

            scope.launch {
                database.pushNotificationDao().insert(notification)
                Log.d("PushNotifier", "Notification saved: $packageName - $title")
            }
        } catch (e: Exception) {
            Log.e("PushNotifier", "Error processing notification", e)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Optional: Handle notification removal if needed
        Log.d("PushNotifier", "Notification removed: ${sbn.packageName}")
    }

    private fun bundleToString(bundle: Bundle): String {
        val stringBuilder = StringBuilder()
        for (key in bundle.keySet()) {
            val value = bundle.get(key)
            stringBuilder.append("$key: $value\n")
        }
        return stringBuilder.toString()
    }
}
