CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE photos (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    photoId int references photos(id),
    cityId int references city(id)
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);

CREATE TABLE city (
    id serial primary key,
    name TEXT
);

INSERT INTO city(name) VALUES ('Moskow');
INSERT INTO city(name) VALUES ('Izhevsk');