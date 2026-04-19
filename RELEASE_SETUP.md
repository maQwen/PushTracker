# Инструкция по настройке подписывания APK и создания релизов

## Настройка подписывания APK

### 1. Создание keystore файла

Для подписывания APK необходимо создать keystore файл. Выполните команду:

```bash
keytool -genkey -v -keystore keystore/release.keystore -alias your_alias_name -keyalg RSA -keysize 2048 -validity 10000
```

Запомните пароли, которые вы введете при создании keystore.

### 2. Локальная сборка (для разработки)

Создайте файл `keystore.properties` в корне проекта на основе `keystore.properties.example`:

```bash
cp keystore.properties.example keystore.properties
```

Отредактируйте `keystore.properties`, указав ваши данные:

```properties
storeFile=keystore/release.keystore
storePassword=ваш_пароль_keystore
keyAlias=ваш_alias
keyPassword=ваш_пароль_key
```

**Важно:** Файл `keystore.properties` добавлен в `.gitignore` и не должен коммититься в репозиторий!

### 3. Настройка GitHub Secrets для CI/CD

Добавьте следующие секреты в настройки вашего GitHub репозитория (Settings → Secrets and variables → Actions):

- `KEYSTORE_BASE64` - содержимое keystore файла в кодировке base64
- `KEYSTORE_PASSWORD` - пароль от keystore
- `KEY_ALIAS` - alias ключа
- `KEY_PASSWORD` - пароль ключа

#### Добавление keystore файла в GitHub Secrets

Keystore файл нужно закодировать в base64 и добавить как секрет:

```bash
# На Linux/Mac
base64 -w 0 keystore/release.keystore

# Или используйте OpenSSL
openssl base64 -in keystore/release.keystore | tr -d '\n'
```

Затем добавьте секрет с именем `KEYSTORE_BASE64` содержащий base64 строку:

1. Перейдите в Settings → Secrets and variables → Actions
2. Нажмите "New repository secret"
3. Имя: `KEYSTORE_BASE64`
4. Значение: скопируйте вывод команды base64

## Создание релиза

### Автоматическое создание релиза

Релиз создается автоматически при пуше тега с префиксом `v`:

```bash
# Создайте тег
git tag v1.0.0

# Отправьте тег в удаленный репозиторий
git push origin v1.0.0
```

GitHub Actions автоматически:
1. Соберет подписанный release APK
2. Создаст релиз на GitHub с прикрепленным APK файлом
3. Сгенерирует заметки о релизе на основе коммитов

### Ручной запуск сборки

Также можно запустить сборку вручную через вкладку Actions на GitHub.

## Структура workflow

Файл `.github/workflows/release.yaml` содержит:
- Триггер на пуш тега с префиксом `v*`
- Сборку signed release APK
- Создание релиза с прикрепленным APK

## Проверка сборки

После настройки проверьте работу:

1. Убедитесь, что все секреты добавлены
2. Создайте тестовый тег: `git tag v0.0.1-test && git push origin v0.0.1-test`
3. Проверьте вкладку Actions на GitHub
4. Убедитесь, что релиз создан и APK прикреплен
