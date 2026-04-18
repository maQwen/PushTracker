package com.pushnotifier.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PushNotificationDao {
    @Query("SELECT * FROM push_notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<PushNotification>>

    @Query("SELECT * FROM push_notifications ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentNotifications(limit: Int): List<PushNotification>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: PushNotification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<PushNotification>)

    @Query("DELETE FROM push_notifications WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM push_notifications")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM push_notifications")
    suspend fun getCount(): Int
}
