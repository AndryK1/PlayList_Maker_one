# 🎵 Music Player — тестовый проект

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-green)](https://developer.android.com/topic/architecture)
[![Coroutines](https://img.shields.io/badge/Coroutines-1.7.3-blue)](https://kotlinlang.org/docs/coroutines-overview.html)
[![Room](https://img.shields.io/badge/Room-2.7.2-orange)](https://developer.android.com/training/data-storage/room)
[![Retrofit](https://img.shields.io/badge/Retrofit-2.9.0-brightgreen)](https://square.github.io/retrofit/)
[![Navigation](https://img.shields.io/badge/Navigation-2.5.3-ff69b4)](https://developer.android.com/guide/navigation)
[![ViewBinding](https://img.shields.io/badge/ViewBinding-✓-yellow)](https://developer.android.com/topic/libraries/view-binding)
[![ViewPager2](https://img.shields.io/badge/ViewPager2-1.0.0-lightgrey)](https://developer.android.com/jetpack/androidx/releases/viewpager2)

**Music Player** — это учебное приложение для Android, созданное с целью освоения современных инструментов разработки, паттернов проектирования и работы с Git. Проект представляет собой музыкальный плеер с гибкой возможностью создания плейлистов и потоковой передачи аудио.


## ✨ Особенности

- 📱 single Activity, навигация через Navigation Graph (фрагменты).
- 🎼 Создание и управление плейлистами, добавление треков в понравившиеся, история поиска.
- 🎧 Потоковое воспроизведение аудио Media Player.
- 🔍 Поиск треков через Itunes API.
- 💾 Локальное кэширование данных с помощью Room.
- 🌐 Взаимодействие с REST API через Retrofit (сериализация JSON).
- 🔄 Асинхронные операции на корутинах.

---

## 🏗 Архитектура

Проект построен на принципах **Clean Architecture** и паттерна **MVVM** (Model-View-ViewModel). Код разделён на пять основных слоёв:

app/
├── data/       # Работа с источниками данных и преобразованием данных
├── di/         # Зависимости через Koin
├── domain/     # Бизнес-логика, модели данных, интерфейсы репозиториев
├── ui/         # ViewModel, фрагменты, кастомные View, адаптеры
└── utils/      # Утилитарные классы и функции

## 🛠 Технологический стек

| Компонент             | Технология                                                                 |
|-----------------------|----------------------------------------------------------------------------|
| Язык                  | Kotlin                                                                     |
| UI                    | XML, Custom Views, View Binding                                            |
| Навигация             | Jetpack Navigation Component                                               |
| Асинхронность         | Coroutines, Flow                                                           |
| Локальная БД          | Room                                                                       |
| Сетевой слой          | Retrofit, OkHttp, JSON                                                     |
| Архитектура           | Clean Architecture, MVVM, Use Cases                                        |
| Работа с изображениями| Glide                                                                      |

---

## 🚀 Начало работы

1. **Клонируйте репозиторий:**
   ```bash
   git clone https://github.com/yourusername/music-player.git
   
Откройте проект в Android Studio (рекомендуется версия Hedgehog или новее).

Синхронизируйте зависимости и соберите проект.

Запустите приложение на эмуляторе или реальном устройстве.

Примечание: для работы с сетью может потребоваться API-ключ или базовая настройка эндпоинта — проверьте файл gradle.properties или BuildConfig.

🧪 Тестирование
Проект предусматривает написание юнит-тестов для Use Cases и ViewModel, а также инструментальных тестов для DAO и репозиториев. Тесты расположены в соответствующих директориях (src/test и src/androidTest).

🤝 Вклад
Поскольку это учебный проект, я буду рад любым предложениям и улучшениям! Если вы хотите помочь:

Форкните репозиторий.

Создайте ветку для вашей функции (git checkout -b feature/amazing-feature).
Закоммитьте изменения (git commit -m 'Add some amazing feature').
Запушьте ветку (git push origin feature/amazing-feature).
Откройте Pull Request.

Спасибо за интерес к проекту!
