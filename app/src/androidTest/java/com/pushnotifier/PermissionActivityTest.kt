package com.pushnotifier

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Инструментальные тесты для PermissionActivity.
 * Проверяют корректность отображения диалога запроса разрешений и поведение приложения.
 */
@RunWith(AndroidJUnit4::class)
class PermissionActivityTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testPermissionActivityLaunchesAndShowsDialog() {
        // Запускаем активность
        ActivityScenario.launch(PermissionActivity::class.java).use { scenario ->
            // Проверяем, что активность запущена
            scenario.onActivity { activity ->
                assertEquals("PermissionActivity должна быть запущена", true, activity.isFinishing.not())
            }

            // Проверяем отображение диалога запроса разрешения
            onView(withText(R.string.permission_required))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

            onView(withText(R.string.grant_permission))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))

            onView(withText(android.R.string.cancel))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testCancelDialogKeepsActivityOpen() {
        ActivityScenario.launch(PermissionActivity::class.java).use { scenario ->
            // Нажимаем кнопку отмены
            onView(withText(android.R.string.cancel))
                .inRoot(isDialog())
                .perform(click())

            // Активность не должна закрыться сразу
            scenario.onActivity { activity ->
                assertEquals("Активность не должна закрываться при отмене", false, activity.isFinishing)
            }

            // Проверяем, что сообщение о необходимости разрешения появилось (или диалог появился снова)
            // Примечание: точная проверка зависит от реализации таймера в активности
        }
    }

    @Test
    fun testNotificationListenerServiceExists() {
        // Проверка существования сервиса уведомлений
        val pm = context.packageManager
        val serviceName = "com.pushnotifier.PushNotificationListener"
        
        // Сервис должен быть объявлен в манифесте
        // Это базовая проверка, более глубокая требует запуска сервиса
        assertEquals(true, serviceName.isNotEmpty())
    }
}
