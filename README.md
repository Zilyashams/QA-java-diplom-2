Дипломный проект Задание 2: API на курсе "Автоматизатор тестирования на Java"
Тестируемый сайт:
https://stellarburgers.nomoreparties.site/

API-документация:
https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf

Технологии проекта:
JUnit 4, RestAssured, Allure

Чтобы запустить тесты:
mvn clean test

Что нужно сделать:
Создание пользователя:
создать уникального пользователя;
создать пользователя, который уже зарегистрирован;
создать пользователя и не заполнить одно из обязательных полей.
Логин пользователя:
логин под существующим пользователем,
логин с неверным логином и паролем.
Изменение данных пользователя:
с авторизацией,
без авторизации,
Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
Создание заказа:
с авторизацией,
без авторизации,
с ингредиентами,
без ингредиентов,
с неверным хешем ингредиентов.
Получение заказов конкретного пользователя:
авторизованный пользователь,
неавторизованный пользователь.
