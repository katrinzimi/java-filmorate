# java-filmorate

Template repository for Filmorate project.

![Схема базы данных](database.png)

Примеры типовых запросов:

- Выбрать пользователя по идентификатору:
  ```sql
  select * from USERS where id = ?
  ```
- Выбрать фильм по идентификатору:
  ```sql
  select * from FILMS where id = ?
  ```
- Получение списка друзей конкретного пользователя:
  ```sql
  select * from USERS where id in ( select friend_id from FRIENDS where user_id = ?)
  ```
- Получение списка популярных фильмов:
  ```sql
  SELECT FILM_ID FROM LIKES GROUP BY FILM_ID ORDER BY COUNT(*) DESC  limit ?
  ```