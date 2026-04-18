package com.pushtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "push_notifications")
data class PushNotification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val title: String?,
    val message: String?,
    val timestamp: Long,
    val extras: String?
)
