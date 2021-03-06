Техническое задание
=======================
Название: Quizon

Команда: 8pus

Разработчики:

    * Борисов Вячеслав
    
    * Мащенко Елена
   
    * Силаков Николай
    
Репозиторий: https://github.com/wenzbr/Quizon

Назначение: мобильная игра-викторина

Целевая аудитория:

    * Школьники и студенты, желающие потренироваться перед реальной игрой, провести свободное время весело и с пользой для мозгов.
    
    
Функциональность приложения:

    1. Вход через ВК или Google 
    
    2. Одиночная игра против нескольких пользователей
    
    3. Личный кабинет пользователя:
    
    * Просмотр рейтинга: 
        - Рейтинг нужен для сбалансированного подбора игроков и вопросов викторины
    * Просмотр статистики
        - Общее количество игр
        - Количество побед
        - Максимальный балл за игру
        - Средний балл за все игры
        - Список вида: Номер игры : Рейтинг : Баллы : Дата : Место
    * Просмотр списка достижений
    
    4. Настройки
    
    * Выбор языка приложения



Игра будет проходить в 6 раундов:

    1. Разминка ( 6 вопросов по 1 баллу с выбором ответа) = максимум 6 баллов
    2. Блиц (6 вопросов на 20-30 секунд) — максимум 6 баллов
    3. Большие вопросы( 5 сложных вопросов на 60 секунд, можно ставить ставки (в этом раунде можно на вопрос поставить 1 балл, если игрок сделавший ставку отвечает правильно, то за ответ получает не 1, а 2 балла, если игрок отвечает неверно, то игрок получает штрафное очко (вычитается балл из его суммы)))
    4. Цепочка из блиц-вопросов: 4 вопроса на которые ответ да или нет, если на ответил игрок все то 4 балла, если на 2-3, то 2 балла иначе 0)
    5. Раунд с картинками — (угадать автора картины, местонахождения какого нить памятника…. И тп)
    6. Последний рывок (5 сложных вопросов на 60 секунд, можно ставить ставки (в этом раунде можно на вопрос поставить 1 или 2 балла, если игрок сделавший ставку отвечает правильно, то за ответ получает не 1 балл , а 1 + ставка баллов, если игрок отвечает неверно, то у игрока отнимается из его суммы очков, сделанная им ставка))

Экраны приложения:

    1. Главный экран(в случае, если пользователь не авторизован)
    
    * Кнопка «Авторизация»
    * Кнопка «Регистрация»
    * Кнопка «ВК»
    * Кнопка «Google»
    
    2. Фрагмент авторизации
    
    * Поле «Логин или почта»
    * Поле «Пароль»
    * Кнопка «Войти»(Ее имеет смысл сделать в виде стрелочки или чего-то подобного)
    * Кнопка «Забыл пароль»
    * Кнопка закрытия фрагмента(Крестик или стрелочка назад)

    3. Фрагмент регистрации
    
    * Поле «Почта»
    * Поле «Логин»
    * Поле «Пароль»
    * Поле «Повторить пароль»
    * Кнопка «Регистрация»

    4. Фрагмент регистрации через ВК
    
    * Поле «Логин»
    * Кнопка «Войти в ВК»
    * Кнопка «Регистрация» - активируется в случае входа в аккаунт ВК
    
    5. Фрагмент регистрации через ВК
    
    * Поле «Логин»
    * Кнопка «Войти в Google»
    * Кнопка «Регистрация» - активируется в случае входа в аккаунт Google
      
    6. Главный экран(Открывается после авторизации/регистрации)
    
    * Кнопка «Начать викторину»
    * Кнопка «Личный кабинет»
    * Кнопка «Настройки»
      
    7. Экран викторины
    
    Показывается название этапа. Коротко объясняется его правила. После небольшой задержи начинается викторина соответствующего этапа.  
    * Поле оставшегося времени
    * Поле вопроса
    * Поле ввода ответа
        - Несколько вариантов ответа
        - Ввод числа(Для ввода года/века и др)

    8. Экран личного кабинета
    
    * Поле с логином
        - Рядом добавить кнопку для смены логина, открывающую фрагмент:
            - Новый логин
            - Кнопка изменения(иконка-карандаш, например), при нажатии:
                - Если логин занят, выводится соответствующее сообщение
                - Если логин свободен, пользователь меняет его и фрагмент закрывается
            - Кнопка-крестик для выхода из фрагмента
    * Поле с рейтингом
    * Поле «Общее количество игр»
    * Поле «Количество побед»
    * Поле «Максимальный балл за игру»
    * Поле «Средний балл за все игры»
    * Кнопка «Назад»

    9. Экран настроек
    
    * Серия кнопок локализации(у нас будет две: русский и английский)
    * Вход через ВК или Google для синхронизации аккаунта пользователя(Новым пользователям предоставить возможность выбора логина и привязки аккаунта к ВК или Google)
    * Кнопка «Назад»

