# CalorieSense

Telegram-бот для подсчёта калорий: рассчитывает суточную норму калорий и БЖУ по формуле
Миффлина — Сан Жеора и ведёт дневник приёмов пищи.

![CI](https://github.com/Yuch1k/CalorieSense/actions/workflows/ci.yml/badge.svg)

## Возможности

| Кнопка / команда | Что делает |
|---|---|
| 📝 Регистрация — `/register` | Пошаговый ввод роста, веса, возраста, пола, цели и активности |
| 👤 Профиль — `/profile` | Просмотр профиля и изменение параметров (`/height`, `/weight`, `/age`, `/sex`, `/goal`, `/activity`) |
| 📊 Мои калории — `/calories` | Суточная норма калорий и БЖУ, прогресс за сегодня |
| 🍽️ Добавить приём пищи — `/addmeal` | Ручной ввод (`/manualinput`); ввод по фото (`/photoinput`) — в разработке |
| ❓ Помощь — `/help` | Справка по командам |

## Стек

- Java 17, Maven
- [TelegramBots](https://github.com/rubenlagus/TelegramBots) 6.9 (Long Polling)
- JUnit 5, Mockito
- Хранение данных — в памяти (`InMemoryUserRepository`, `InMemoryMealRepository`)

## Структура

```
src/main/java/ru/CalorieSense/
├── BotApp.java, Main.java      — запуск бота и регистрация команд
├── router/                     — CommandRouter: маршрутизация команд и кнопок
├── command/                    — команды: system, user (профиль, регистрация), nutrition
├── service/                    — расчёт нормы калорий, учёт приёмов пищи
├── model/                      — UserProfile, Meal, DailyProgress, ...
├── repository/                 — интерфейсы и in-memory реализации хранилищ
└── ui/                         — клавиатуры Telegram
```

## Запуск

1. Получите токен бота у [@BotFather](https://t.me/BotFather).
2. Задайте токен через переменную окружения (в репозиторий токен не коммитится):
   ```bash
   export BOT_TOKEN=<токен>             # Windows PowerShell: $env:BOT_TOKEN="<токен>"
   export BOT_USERNAME=CalorieSense_bot  # необязательно, по умолчанию берётся из config.properties
   ```
   В IntelliJ IDEA: Run → Edit Configurations → Environment variables.
3. Запустите `ru.CalorieSense.Main` из IDE или соберите проект: `mvn package`.

## Тесты

```bash
mvn test
```

Тесты автоматически запускаются в GitHub Actions на каждый push и pull request.

## Управление задачами

Задачи проекта ведутся в Jira (проект **CS**). Ключ задачи указывается в названии ветки,
в сообщении коммита и в заголовке pull request — например, `CS-24 README: ...`.
Благодаря интеграции «GitHub for Jira» ветки, коммиты, PR и сборки видны прямо в задаче.
