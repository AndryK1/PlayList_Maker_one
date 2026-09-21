# Документация проекта PlayList Maker

## Назначение

Android-приложение для поиска музыкальных треков через iTunes Search API, прослушивания их preview-версий, ведения истории поиска, избранного и пользовательских плейлистов.

## Быстрый запуск

1. Открыть проект в Android Studio.
2. Использовать JDK 17 и Android SDK с compileSdk 35.
3. Собрать debug-вариант командой:

   ```powershell
   .\gradlew.bat assembleDebug
   ```

4. Установить APK на устройство или эмулятор с Android 12 и выше.

Для уведомления проигрывателя на Android 13 и выше приложению требуется разрешение POST_NOTIFICATIONS.

## Архитектура

Проект использует single-activity подход, XML layouts, ViewBinding и MVVM с разделением на data, domain и ui.

```text
Fragment или Activity
        -> ViewModel
        -> Interactor или Repository interface
        -> Repository implementation
        -> Room, SharedPreferences, Retrofit или MediaPlayer
```

### Слои

| Слой | Каталог | Ответственность |
| --- | --- | --- |
| UI | ui | Экраны, адаптеры, кастомные View, состояния и ViewModel |
| Domain | domain | Сущности, контракты репозиториев, интеракторы и мапперы |
| Data | data | Room, SharedPreferences, Retrofit, реализации репозиториев и проигрыватель |
| DI | di | Граф зависимостей Koin |
| Resources | res | XML-layouts, навигация, темы, строки и drawable-ресурсы |

## Навигация и экраны

RootActivity содержит NavHostFragment и нижнюю навигацию. Маршруты описаны в app/src/main/res/navigation/navigation_graph.xml.

| Экран | Класс | Назначение |
| --- | --- | --- |
| Поиск | FragmentSearch | Поиск треков, история и переход в карточку |
| Карточка трека | AudioFragment | Метаданные трека, избранное, плейлисты и управление preview |
| Медиатека | FragmentMedia | Контейнер вкладок избранного и плейлистов |
| Избранное | FragmentFavorites | Список сохранённых треков |
| Плейлисты | FragmentPlayList | Список пользовательских плейлистов |
| Текущий плейлист | FragmentCurrentPlaylist | Состав и операции над плейлистом |
| Создание и редактирование | FragmentCreateList, FragmentEditPlaylist | Управление данными плейлиста |
| Настройки | FragmentSettings | Тема, поддержка и внешние ссылки |

## Данные и хранение

### Сеть

Retrofit и ItunesAPI запрашивают iTunes Search API. RetrofitNetworkClient реализует контракт NetworkClient. TrackRepositoryImpl преобразует ответ API в доменные TrackData и дополняет их признаком избранного.

### Локальная база

Room-база AppDatabase содержит таблицы треков и плейлистов. DAO расположены в data/db/dao. TrackDbConverter и PlayListDbConverter преобразуют модели базы и доменные сущности.

### SharedPreferences

- SharedPrefsTracksImpl сохраняет историю поиска.
- SharedPrefsThemeImpl хранит выбранную тему.
- TrackHistoryManagerImpl управляет историей в памяти и её синхронизацией с хранилищем.

## Зависимости

Koin запускается в ui/track/App. Основные модули:

| Модуль | Содержимое |
| --- | --- |
| NetworkModule | Retrofit, ItunesAPI, NetworkClient |
| StorageModule | Room, Gson, SharedPreferences и менеджеры настроек |
| TrackModule | Репозитории, интеракторы и маппер треков |
| MediaPlayerModule | TrackPlayer и Handler |
| ViewModule | Все ViewModel |

## Воспроизведение preview

MusicService является единственным владельцем активного TrackPlayer и публикует состояние через StateFlow.

```text
AudioFragment -> AudioViewModel -> MusicService -> TrackPlayerImpl -> MediaPlayer
                                  <- StateFlow playerState и timerState
```

Правила жизненного цикла:

1. При открытии той же карточки MusicService не пересоздаёт плеер, а публикует текущее состояние и оставшееся время.
2. При открытии другой карточки текущий MediaPlayer освобождается до подготовки нового preview.
3. Каждый асинхронный callback MediaPlayer связан с конкретным экземпляром. Callback освобождённого плеера игнорируется, поэтому быстрая смена карточек не меняет состояние нового трека.
4. При уходе из карточки воспроизведение может продолжаться. В фоне сервис показывает foreground-уведомление с паузой.
5. Пауза в уведомлении останавливает воспроизведение и синхронизирует UI. Смахивание уведомления освобождает плеер и завершает сервис.
6. После завершения preview состояние становится Finished, а таймер возвращается к начальной длительности.

## Состояния UI

- SearchState описывает загрузку, результаты, пустой результат и ошибку поиска.
- FavoritesState описывает пустое избранное и его содержимое.
- PlayListState описывает пустой список плейлистов и его содержимое.
- PlayerState содержит Playing, Paused и Finished.

## Проверка изменений

Перед коммитом выполнить:

```powershell
.\gradlew.bat assembleDebug --no-daemon
git diff --check
```

Для ручной проверки проигрывателя:

1. Запустить трек, открыть другую карточку и сразу нажать Play.
2. Вернуться в исходную карточку и убедиться, что кнопка и таймер соответствуют реальному состоянию.
3. Свернуть приложение, проверить уведомление, нажать паузу и затем повторить смахивание уведомления.

## Ограничения и заметки

- Воспроизводятся preview-ссылки, возвращаемые iTunes, а не полные аудиофайлы.
- Room настроен с fallbackToDestructiveMigration. Изменение схемы может удалить локальные данные при обновлении приложения.
- Сборка выводит предупреждение, что используемая Android Gradle Plugin протестирована до compileSdk 34. Это предупреждение не мешает текущей debug-сборке.
