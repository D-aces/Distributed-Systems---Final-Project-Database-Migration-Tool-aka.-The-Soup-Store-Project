-- SQLite
CREATE TABLE chunky_soup (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price REAL NOT NULL
);

INSERT INTO chunky_soup (name, price) VALUES ('Chicken Noodle', 3.99);
INSERT INTO chunky_soup (name, price) VALUES ('Tomato', 2.99);
INSERT INTO chunky_soup (name, price) VALUES ('Clam Chowder', 4.99);
INSERT INTO chunky_soup (name, price) VALUES ('Beef Stew', 5.99);

CREATE TABLE smooth_soup (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price REAL NOT NULL
);

INSERT INTO smooth_soup (name, price) VALUES ('Cream of Mushroom', 3.99);
INSERT INTO smooth_soup (name, price) VALUES ('Butternut Squash', 4.99);
INSERT INTO smooth_soup (name, price) VALUES ('Broccoli Cheddar', 3.99);
INSERT INTO smooth_soup (name, price) VALUES ('Lobster Bisque', 6.99);

CREATE TABLE spicy_soup (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price REAL NOT NULL
);

INSERT INTO spicy_soup (name, price) VALUES ('Jalapeno', 2.99);
INSERT INTO spicy_soup (name, price) VALUES ('Hot & Sour', 3.99);
INSERT INTO spicy_soup (name, price) VALUES ('Curry', 4.99);
INSERT INTO spicy_soup (name, price) VALUES ('Chili', 5.99);


