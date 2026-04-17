package com.pushnotifier

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pushnotifier.data.AppDatabase
import com.pushnotifier.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: PushNotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Push Notifications"

        database = AppDatabase.getDatabase(this)

        setupRecyclerView()
        observeNotifications()
    }

    private fun setupRecyclerView() {
        adapter = PushNotificationAdapter { notification ->
            deleteNotification(notification.id)
        }
        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotifications.adapter = adapter
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            database.pushNotificationDao().getAllNotifications().collectLatest { notifications ->
                adapter.submitList(notifications)
                updateEmptyState(notifications.isEmpty())
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyView.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.GONE
        binding.recyclerViewNotifications.visibility = if (isEmpty) android.view.GONE else android.view.View.VISIBLE
    }

    private fun deleteNotification(id: Long) {
        lifecycleScope.launch {
            database.pushNotificationDao().deleteById(id)
            Toast.makeText(this@MainActivity, "Notification deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_all -> {
                clearAllNotifications()
                true
            }
            R.id.action_refresh -> {
                // Just refresh by observing again (already automatic)
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearAllNotifications() {
        lifecycleScope.launch {
            database.pushNotificationDao().deleteAll()
            Toast.makeText(this@MainActivity, "All notifications cleared", Toast.LENGTH_SHORT).show()
        }
    }
}
