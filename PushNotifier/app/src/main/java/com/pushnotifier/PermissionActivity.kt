package com.pushnotifier

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if notification listener is already enabled
        if (isNotificationServiceEnabled()) {
            // Permission granted, start main activity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Show permission request UI
        showPermissionRequest()
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageName = this.packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (!flat.isNullOrEmpty()) {
            val names = flat.split(":")
            for (name in names) {
                val components = name.split("/")
                if (components.size == 2 && components[0] == packageName) {
                    return true
                }
            }
        }
        return false
    }

    private fun showPermissionRequest() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_required))
            .setMessage(getString(R.string.notification_listener_description))
            .setPositiveButton(getString(R.string.grant_permission)) { _, _ ->
                openNotificationListenerSettings()
            }
            .setNegativeButton(getString(R.string.settings)) { _, _ ->
                openNotificationListenerSettings()
            }
            .setCancelable(false)
            .show()
    }

    private fun openNotificationListenerSettings() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Check again if permission was granted
        if (isNotificationServiceEnabled()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
