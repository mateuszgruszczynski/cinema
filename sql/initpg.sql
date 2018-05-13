CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  firstname VARCHAR(32) NOT NULL,
  lastname VARCHAR(32) NOT NULL,
  username VARCHAR(64) UNIQUE NOT NULL,
  email VARCHAR(64) UNIQUE NOT NULL,
  city VARCHAR(64) NOT NULL,
  address VARCHAR(64) NOT NULL,
  birthDate VARCHAR(64) NOT NULL,
  passhash VARCHAR(128) DEFAULT '',
  token VARCHAR(256) DEFAULT ''
);

CREATE TABLE wallets (
  id SERIAL PRIMARY KEY,
  userId INT NOT NULL,
  value DECIMAL NOT NULL DEFAULT 0.0
);

CREATE TABLE movies (
  id SERIAL PRIMARY KEY,
  title VARCHAR(64) NOT NULL,
  director VARCHAR(64) NOT NULL,
  genre VARCHAR(64) NOT NULL,
  country VARCHAR(64) NOT NULL,
  year INT NOT NULL,
  tags TEXT,
  ageLimit INT NOT NULL DEFAULT 0
);

CREATE TABLE screenings (
  id SERIAL PRIMARY KEY,
  movieId INT NOT NULL,
  city VARCHAR(64) NOT NULL,
  roomNumber INT NOT NULL,
  seatsLimit INT NOT NULL,
  seatsTaken INT NOT NULL,
  time VARCHAR(64) NOT NULL,
  price DECIMAL NOT NULL
);

CREATE TABLE orders (
  id SERIAL PRIMARY KEY,
  userId INT NOT NULL,
  screeningId INT NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'BOOKED',
  ticketsCount INT NOT NULL DEFAULT 1,
  totalPrice DECIMAL NOT NULL
);

INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('The Hobbit: An Unexpected Journey', 'Peter Jackson', 'Fantasy', 'USA', 2012, 'tolkien, middle-earth, lotr', 13);
INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('The Godfather', 'Francis Ford Coppola', 'Crime', 'USA', 1972, 'mafia', 15);
INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('The Shining', 'Stanley Kubrick', 'Horror', 'USA', 1980, 'hotel, insanity', 13);
INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('Pulp Fiction', 'Quentin Tarantino', 'Crime', 'USA', 1994, '', 15);
INSERT INTO movies(title, director, genre, country, year, tags, ageLimit) VALUES ('Forrest Gump', 'Robert Zemeckis', 'Drama', 'USA', 1994, '', 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, 'Krakow', 1, 100, 0, '2018-04-01 12:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, 'Krakow', 2, 100, 0, '2018-04-01 12:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, 'Krakow', 3, 100, 0, '2018-04-01 12:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, 'Krakow', 4, 100, 0, '2018-04-01 12:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (1, 'Krakow', 5, 100, 0, '2018-04-01 12:00:00', 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, 'Krakow', 1, 100, 0, '2018-04-01 15:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, 'Krakow', 2, 100, 0, '2018-04-01 15:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, 'Krakow', 3, 100, 0, '2018-04-01 15:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, 'Krakow', 4, 100, 0, '2018-04-01 15:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (2, 'Krakow', 5, 100, 0, '2018-04-01 15:00:00', 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, 'Krakow', 1, 100, 0, '2018-04-01 18:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, 'Krakow', 2, 100, 0, '2018-04-01 18:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, 'Krakow', 3, 100, 0, '2018-04-01 18:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, 'Krakow', 4, 100, 0, '2018-04-01 18:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (3, 'Krakow', 5, 100, 0, '2018-04-01 18:00:00', 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, 'Krakow', 1, 100, 0, '2018-04-01 09:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, 'Krakow', 2, 100, 0, '2018-04-01 09:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, 'Krakow', 3, 100, 0, '2018-04-01 09:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, 'Krakow', 4, 100, 0, '2018-04-01 09:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (4, 'Krakow', 5, 100, 0, '2018-04-01 09:00:00', 15);

INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, 'Krakow', 1, 100, 0, '2018-04-01 21:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, 'Krakow', 2, 100, 0, '2018-04-01 21:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, 'Krakow', 3, 100, 0, '2018-04-01 21:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, 'Krakow', 4, 100, 0, '2018-04-01 21:00:00', 15);
INSERT INTO screenings(movieId, city, roomnumber, seatsLimit, seatsTaken, time, price) VALUES (5, 'Krakow', 5, 100, 0, '2018-04-01 21:00:00', 15);

