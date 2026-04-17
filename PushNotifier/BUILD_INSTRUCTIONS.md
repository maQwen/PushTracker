# Сборка APK на GitHub Actions

Этот проект настроен для автоматической сборки APK файла через GitHub Actions.

## Как это работает

1. При каждом push в ветку `main` или `master` запускается workflow сборки
2. GitHub Actions использует Ubuntu runner с JDK 17
3. Скрипт Gradle собирает debug версию приложения
4. APK файл загружается как артефакт и доступен для скачивания в течение 30 дней

## Как получить APK

### Вариант 1: Через GitHub Actions (автоматически)

1. Запушьте код в репозиторий на GitHub
2. Перейдите во вкладку **Actions** в вашем репозитории
3. Выберите запущенный workflow "Android APK Build"
4. Внизу страницы найдите секцию **Artifacts**
5. Скачайте файл `app-debug.zip` и распакуйте его - внутри будет `app-debug.apk`

### Вариант 2: Через релизы (при создании тега)

1. Создайте тег в репозитории: `git tag v1.0.0 && git push origin v1.0.0`
2. Workflow автоматически создаст релиз с прикрепленным APK
3. APK будет доступен в разделе **Releases**

## Локальная сборка

Если вы хотите собрать APK локально:

```bash
cd PushNotifier
chmod +x gradlew
./gradlew assembleDebug
```

APK файл будет создан в: `app/build/outputs/apk/debug/app-debug.apk`

## Требования

- Java 17 или выше
- Android SDK (устанавливается автоматически в GitHub Actions)
- Gradle 8.0

## Примечания

- Для production сборки рекомендуется настроить signing (подпись приложения)
- Debug версия не подходит для публикации в Google Play Store
- Для release сборки создайте отдельный workflow с подписью приложения
