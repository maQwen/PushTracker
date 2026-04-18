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
4. Saves it to the Room database

### Room Database
The app uses Room for SQLite database operations:

- **Entity**: `PushNotification` - stores notification data
- **DAO**: `PushNotificationDao` - database access methods
- **Database**: `AppDatabase` - Room database configuration

### UI
- **PermissionActivity**: Checks and requests notification access permission
- **MainActivity**: Displays notifications in a RecyclerView
- **PushNotificationAdapter**: Binds notification data to list items

## Usage

1. Launch the app and grant notification access permission
2. All incoming notifications from other apps will be captured
3. View notifications in the main screen
4. Use the toolbar menu to:
   - Refresh the list
   - Clear all notifications
5. Delete individual notifications using the "Delete" button

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
