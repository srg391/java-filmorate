drop table if exists ratings, films, genres, friends, likes, film_genres;

create table if not exists users
(
    user_id BIGINT auto_increment,
    email VARCHAR(50) not null,
    login VARCHAR(50) not null,
    user_name VARCHAR(50) not null,
    birthday DATE,
    constraint user_id_pk
        primary key (user_id)
);

create unique index user_email_unq
    on users (email);

create unique index user_login_unq
    on users (login);

create table if not exists ratings
(
    rating_id INT PRIMARY KEY,
    rating VARCHAR(10)
);

create table if not exists films
(
    film_id BIGINT auto_increment,
    film_name VARCHAR(50),
    description VARCHAR(200),
    release_date DATE,
    duration INT,
    rating_id INT,
    constraint film_id_pk
        primary key (film_id),
    constraint ratings_fk
        foreign key (rating_id) references ratings(rating_id)
);

create table if not exists genres
(
    id INT PRIMARY KEY,
    name VARCHAR(300)
);

create table if not exists friends
(
    user_id BIGINT,
    friend_id BIGINT,
    constraint friends_pk
        primary key (user_id, friend_id),
    constraint users_fk
        foreign key (user_id) references users (user_id),
    constraint friends_fk
        foreign key (friend_id) references users (user_id)
);

create table if not exists likes
(
    film_id BIGINT,
    user_id BIGINT,
    constraint films_fk
        foreign key (film_id) references films (film_id),
    constraint users_likes_fk
        foreign key (user_id) references users (user_id)
);

create table if not exists film_genres
(
    film_id BIGINT,
    genre_id BIGINT,
    constraint film_genres_pk
        primary key (film_id, genre_id),
    constraint film_genres_film_fk
        foreign key (film_id) references films (film_id),
    constraint film_genres_genre_fk
        foreign key (genre_id) references genres (id)
    );
