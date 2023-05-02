-- Создание таблиц

CREATE TABLE genres (
id integer auto_increment primary key,
genre_name varchar(50) not NULL,
CONSTRAINT uk_genres_name unique(genre_name)
);

CREATE TABLE films (
id integer auto_increment primary key,
film_name varchar(100) not NULL,
CONSTRAINT uk_films_name unique(film_name)
);

CREATE TABLE films_genres (
film_id integer NOT NULL,
genre_id integer NOT NULL,
CONSTRAINT pk_films_genres unique(film_id, genre_id),
CONSTRAINT fk_films_genres_genre foreign key (genre_id) references genres (id),
CONSTRAINT fk_films_genres_film foreign key (film_id) references films (id)
);

CREATE TABLE users (
id integer auto_increment primary key,
user_name varchar(100) not NULL,
is_promoted boolean not NULL,
CONSTRAINT uk_users_name unique(user_name)
);

CREATE TABLE scores (
user_id integer NOT NULL,
film_id integer NOT NULL,
score integer NOT NULL,
CONSTRAINT uk_scores_user_score unique(film_id, user_id),
CONSTRAINT fk_scores_user foreign key (user_id) references users (id),
CONSTRAINT fk_scores_film foreign key (film_id) references films (id),
CONSTRAINT ch_scores_score_greater_zero check(score >= 0),
CONSTRAINT ch_scores_score_lower_five check(score <= 5)
);

-- Вставка тестовых значений

INSERT INTO genres (genre_name) VALUES
('genre1'),
('genre2'),
('genre3'),
('genre4'),
('genre5'),
('genre6'),
('genre7');

INSERT INTO films (film_name) VALUES
('film1'),
('film2'),
('film3');

INSERT INTO films_genres (film_id, genre_id) VALUES
(1 , 1),
(1 , 2),
(1 , 3),
(2 , 6),
(2 , 7),
(3 , 3),
(3 , 4),
(3 , 5),
(3 , 6);

INSERT INTO users (user_name, is_promoted) VALUES
('user0', true),
('user1', false),
('user2', false);

INSERT INTO scores (USER_ID, FILM_ID, SCORE) VALUES
(1 , 1, 3),
(1 , 3, 1),
(2 , 2, 4),
(3 , 1, 0),
(3 , 2, 1),
(3 , 3, 2);

-- Прочие команды, используемые в проекте

SELECT FILMS.ID, FILMS.FILM_NAME
FROM FILMS
JOIN films_genres ON FILMS.id = films_genres.FILM_id
JOIN genres ON films_genres.genre_id = genres.id
WHERE genres.genre_name = 'genre3';


SELECT films.FILM_NAME, AVG(scores.score) AS average_score
FROM films
JOIN films_genres ON films.id = films_genres.film_id
JOIN genres ON films_genres.genre_id = genres.id
JOIN scores ON films.id = scores.film_id
WHERE genres.genre_name = 'genre3'
GROUP BY films.id
ORDER BY average_score DESC;

SELECT f.ID, FILM_NAME, GENRE_NAME
FROM FILMS f
JOIN films_genres ON f.id = films_genres.film_id
JOIN genres ON films_genres.genre_id = genres.id
WHERE f.FILM_NAME = 'film2';

SELECT *
FROM USERS u
WHERE u.USER_NAME = 'user0';

SELECT f.ID, FILM_NAME, GENRE_NAME
FROM FILMS f
LEFT JOIN films_genres ON f.id = films_genres.film_id
LEFT JOIN genres ON films_genres.genre_id = genres.id
WHERE f.FILM_NAME = 'film2';


SELECT films.FILM_NAME, AVG(scores.score) AS average_score
FROM films
JOIN films_genres ON films.id = films_genres.film_id
JOIN genres ON films_genres.genre_id = genres.id
JOIN scores ON films.id = scores.film_id
GROUP BY films.id
ORDER BY average_score DESC;