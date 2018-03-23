# noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE users (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  firstname VARCHAR(32) NOT NULL,
  lastname VARCHAR(32) NOT NULL,
  username VARCHAR(64) UNIQUE NOT NULL,
  email VARCHAR(64) UNIQUE NOT NULL,
  city VARCHAR(64) NOT NULL,
  address VARCHAR(64) NOT NULL,
  birthDate DATETIME NOT NULL,
  passhash VARCHAR(128) DEFAULT "",
  token VARCHAR(256) DEFAULT ""
);

CREATE TABLE wallets (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  userId INT UNSIGNED NOT NULL,
  value DECIMAL NOT NULL DEFAULT 0.0
);

CREATE TABLE movies (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(64) NOT NULL,
  director VARCHAR(64) NOT NULL,
  genre VARCHAR(64) NOT NULL,
  country VARCHAR(64) NOT NULL,
  year INT UNSIGNED NOT NULL,
  tags TEXT,
  ageLimit INT UNSIGNED NOT NULL DEFAULT 0
);

CREATE TABLE screenings (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  movieId INT UNSIGNED NOT NULL,
  city VARCHAR(64) NOT NULL,
  roomNumber INT UNSIGNED NOT NULL,
  seatsLimit INT UNSIGNED NOT NULL,
  seatsTaken INT UNSIGNED NOT NULL,
  time DATETIME NOT NULL,
  price DECIMAL NOT NULL
);

CREATE TABLE orders (
  id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  userId INT UNSIGNED NOT NULL,
  screeningId INT UNSIGNED NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT "BOOKED",
  ticketsCount INT UNSIGNED NOT NULL DEFAULT 1,
  totalPrice DECIMAL NOT NULL
);

INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('The Hobbit: An Unexpected Journey', 'Peter Jackson', 'Fantasy', 'USA', 2012, 'tolkien, middle-earth, lotr', 13);
INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('The Godfather', 'Francis Ford Coppola', 'Crime', 'USA', 1972, 'mafia', 15);
INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('The Shining', 'Stanley Kubrick', 'Horror', 'USA', 1980, 'hotel, insanity', 13);
INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('Pulp Fiction', 'Quentin Tarantino', 'Crime', 'USA', 1994, '', 15);
INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('Forrest Gump', 'Robert Zemeckis', 'Drama', 'USA', 1994, '', 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, "Krakow", 1, 100, 0, "2018-04-01 12:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, "Krakow", 2, 100, 0, "2018-04-01 12:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, "Krakow", 3, 100, 0, "2018-04-01 12:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, "Krakow", 4, 100, 0, "2018-04-01 12:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, "Krakow", 5, 100, 0, "2018-04-01 12:00:00", 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, "Krakow", 1, 100, 0, "2018-04-01 15:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, "Krakow", 2, 100, 0, "2018-04-01 15:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, "Krakow", 3, 100, 0, "2018-04-01 15:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, "Krakow", 4, 100, 0, "2018-04-01 15:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, "Krakow", 5, 100, 0, "2018-04-01 15:00:00", 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, "Krakow", 1, 100, 0, "2018-04-01 18:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, "Krakow", 2, 100, 0, "2018-04-01 18:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, "Krakow", 3, 100, 0, "2018-04-01 18:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, "Krakow", 4, 100, 0, "2018-04-01 18:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, "Krakow", 5, 100, 0, "2018-04-01 18:00:00", 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, "Krakow", 1, 100, 0, "2018-04-01 09:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, "Krakow", 2, 100, 0, "2018-04-01 09:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, "Krakow", 3, 100, 0, "2018-04-01 09:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, "Krakow", 4, 100, 0, "2018-04-01 09:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, "Krakow", 5, 100, 0, "2018-04-01 09:00:00", 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, "Krakow", 1, 100, 0, "2018-04-01 21:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, "Krakow", 2, 100, 0, "2018-04-01 21:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, "Krakow", 3, 100, 0, "2018-04-01 21:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, "Krakow", 4, 100, 0, "2018-04-01 21:00:00", 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, "Krakow", 5, 100, 0, "2018-04-01 21:00:00", 15);
