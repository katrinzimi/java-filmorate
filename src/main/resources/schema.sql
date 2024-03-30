-- PUBLIC."USER" определение

-- Drop table

-- DROP TABLE PUBLIC."USER";

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
	ID INTEGER NOT NULL,
	EMAIL VARCHAR(100),
	LOGIN VARCHAR(100),
	NAME VARCHAR(100),
	BIRTHDAY DATE,
	CONSTRAINT USER_PK PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_USER_ID ON PUBLIC.USERS (ID);


-- PUBLIC.GENRE определение

-- Drop table

-- DROP TABLE PUBLIC.GENRE;

CREATE TABLE IF NOT EXISTS PUBLIC.GENRES (
	ID INTEGER NOT NULL,
	NAME VARCHAR(100),
	CONSTRAINT GENRE_PK PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_GENRE_ID ON PUBLIC.GENRES (ID);


-- PUBLIC.RATING определение

-- Drop table

-- DROP TABLE PUBLIC.RATING;

CREATE TABLE IF NOT EXISTS PUBLIC.RATING (
	ID INTEGER NOT NULL,
	NAME VARCHAR(100),
	CONSTRAINT RATING_PK PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_RATING_ID ON PUBLIC.RATING (ID);


-- PUBLIC.FILM определение

-- Drop table

-- DROP TABLE PUBLIC.FILM;

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (
	ID INTEGER NOT NULL,
	NAME VARCHAR(100),
	DESCRIPTION VARCHAR(100),
	RELEASEDATE DATE,
	DURATION INTEGER,
	RATING_ID INTEGER,
	CONSTRAINT FILM_PK PRIMARY KEY (ID) ,
	CONSTRAINT FILM_RATING_FK FOREIGN KEY (RATING_ID) REFERENCES PUBLIC.RATING(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX IF NOT EXISTS FILM_RATING_FK_INDEX_RATING_ID ON PUBLIC.FILMS (RATING_ID);
CREATE UNIQUE  INDEX IF NOT EXISTS PRIMARY_KEY_FILM_ID ON PUBLIC.FILMS (ID);


-- PUBLIC."LIKE" определение

-- Drop table

-- DROP TABLE PUBLIC."LIKE";

CREATE TABLE IF NOT EXISTS PUBLIC.LIKES (
	USER_ID INTEGER NOT NULL,
	FILM_ID INTEGER NOT NULL,
	CONSTRAINT LIKE_PK PRIMARY KEY (USER_ID,FILM_ID),
	CONSTRAINT LIKE_FILM_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT LIKE_USER_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX IF NOT EXISTS LIKE_FILM_FK_INDEX_FILM_ID ON PUBLIC.LIKES (FILM_ID);
CREATE INDEX IF NOT EXISTS LIKE_USER_FK_INDEX_USER_ID ON PUBLIC.LIKES (USER_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_LIKE_PK ON PUBLIC.LIKES (USER_ID,FILM_ID);


-- PUBLIC.FRIEND определение

-- Drop table

-- DROP TABLE PUBLIC.FRIEND;

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS (
	USER_ID INTEGER NOT NULL,
	FRIEND_ID INTEGER NOT NULL,
	CONSTRAINT FRIEND_PK PRIMARY KEY (USER_ID,FRIEND_ID),
	CONSTRAINT FRIEND_USER_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FRIEND_USER_FK_1 FOREIGN KEY (FRIEND_ID) REFERENCES PUBLIC.USERS(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);
CREATE INDEX IF NOT EXISTS FRIEND_USER_FK_1_INDEX_FRIEND_ID ON PUBLIC.FRIENDS (FRIEND_ID);
CREATE INDEX IF NOT EXISTS FRIEND_USER_FK_INDEX_USER_ID ON PUBLIC.FRIENDS (USER_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_FRIEND_ID ON PUBLIC.FRIENDS (USER_ID,FRIEND_ID);


-- PUBLIC.FILM_GENRE определение

-- Drop table

-- DROP TABLE PUBLIC.FILM_GENRE;

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_GENRE (
	FILM_ID INTEGER NOT NULL,
	GENRE_ID INTEGER NOT NULL,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (FILM_ID,GENRE_ID),
	CONSTRAINT FILM_GENRE_FILM_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(ID) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT FILM_GENRE_GENRE_FK FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRES(ID) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE INDEX IF NOT EXISTS FILM_GENRE_FILM_FK_INDEX_FILM_ID ON PUBLIC.FILM_GENRE (FILM_ID);
CREATE INDEX IF NOT EXISTS FILM_GENRE_GENRE_FK_INDEX_USER_ID ON PUBLIC.FILM_GENRE (GENRE_ID);
CREATE UNIQUE INDEX IF NOT EXISTS PRIMARY_KEY_FILM_GENRE_PK ON PUBLIC.FILM_GENRE (FILM_ID,GENRE_ID);

CREATE SEQUENCE IF NOT EXISTS "USERS_SEQUENCE"
MINVALUE 1
MAXVALUE 999999999
INCREMENT BY 1
START WITH 1
NOCACHE
NOCYCLE;
CREATE SEQUENCE IF NOT EXISTS "FILMS_SEQUENCE"
MINVALUE 1
MAXVALUE 999999999
INCREMENT BY 1
START WITH 1
NOCACHE
NOCYCLE;