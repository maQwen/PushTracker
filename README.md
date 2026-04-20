# Push Notifier - Android Application

Android application written in Kotlin that captures all incoming push notifications on the phone, saves them to an SQLite database, and provides a UI for viewing them.

## Features

- **Notification Capture**: Uses NotificationListenerService to capture all incoming notifications from other apps
- **SQLite Storage**: Saves notifications using Room persistence library
- **Real-time Viewing**: Displays notifications in a RecyclerView with real-time updates
- **Delete Functionality**: Allows deleting individual notifications or clearing all
- **Permission Management**: Guides users through enabling notification access permission

## Project Structure

```
PushNotifier/
├── app/
│   ├── src/main/
│   │   ├── java/com/pushnotifier/
│   │   │   ├── data/
│   │   │   │   ├── AppDatabase.kt          # Room database configuration
│   │   │   │   ├── PushNotification.kt     # Notification entity
│   │   │   │   └── PushNotificationDao.kt  # Data Access Object
│   │   │   ├── MainActivity.kt             # Main UI activity
│   │   │   ├── PermissionActivity.kt       # Permission request activity
│   │   │   ├── PushNotificationAdapter.kt  # RecyclerView adapter
│   │   │   └── PushNotificationListener.kt # Notification listener service
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   └── item_notification.xml
│   │   │   ├── menu/
│   │   │   │   └── main_menu.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   └── strings.xml
│   │   │   └── mipmap-*/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Requirements

- Android Studio Arctic Fox or later
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Kotlin 1.9.0

## Dependencies

- AndroidX Core KTX
- AndroidX AppCompat
- Material Design Components
- Room Database (for SQLite)
- Kotlin Coroutines
- AndroidX Lifecycle

## Setup Instructions

1. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the PushNotifier folder

2. **Sync Gradle**
   - Let Android Studio sync the Gradle files
   - Wait for dependencies to download

3. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" button or press Shift+F10

4. **Grant Permission**
   - On first launch, the app will request notification access
   - Tap "Grant Permission" to open settings
   - Find "Push Notifier" in the list and enable it
   - Return to the app

## How It Works

### NotificationListenerService
The `PushNotificationListener` class extends `NotificationListenerService` to intercept all notifications posted by other apps. When a notification arrives:

1. The system calls `onNotificationPosted()`
2. The app extracts title, message, package name, and extras
3. Creates a `PushNotification` entity
4. Saves it to the Room database using coroutines

### Room Database
The app uses Room for SQLite database operations with Kotlin Flow for reactive data:

- **Entity**: `PushNotification` - stores notification data (id, title, message, package, timestamp)
- **DAO**: `PushNotificationDao` - database access methods with Flow-based queries
- **Database**: `AppDatabase` - Room database configuration with singleton pattern

### UI Architecture
- **PermissionActivity**: Entry point that checks and requests notification access permission
  - Shows an AlertDialog explaining the required permission
  - Opens system settings for the user to enable the notification listener
  - Uses `onResume()` to detect when permission is granted and navigates to MainActivity
  - Does NOT close immediately - waits for user action
  
- **MainActivity**: Displays notifications in a RecyclerView
  - Uses ViewBinding for type-safe view access
  - Observes database changes via Flow with `collectLatest`
  - Automatically updates UI when new notifications arrive
  - Shows empty state view when no notifications exist
  
- **PushNotificationAdapter**: Binds notification data to list items
  - Supports individual item deletion
  - Displays formatted timestamps

## Usage

1. Launch the app - PermissionActivity opens first
2. Grant notification access permission:
   - Tap "Grant Permission" in the dialog
   - Find "Push Notifier" in the system settings list
   - Enable the toggle
   - Return to the app (it will automatically navigate to the main screen)
3. **View captured notifications**: The main screen displays a list of all captured notifications in real-time
   - If no notifications have been captured yet, an empty state message is shown
   - New notifications appear automatically as they arrive
4. Use the toolbar menu to:
   - Refresh the list (manual refresh)
   - Clear all notifications at once
5. Delete individual notifications using the "Delete" button on each item

## Important Notes

- The app cannot capture notifications from itself (to avoid infinite loops)
- Some apps may mark notifications as private, limiting accessible content
- Notification history is stored locally on the device
- Clearing app data will delete all stored notifications

## Permissions

- `BIND_NOTIFICATION_LISTENER_SERVICE`: Required to listen to notifications
- `POST_NOTIFICATIONS`: For Android 13+ compatibility

## License

This project is provided as-is for educational purposes.
