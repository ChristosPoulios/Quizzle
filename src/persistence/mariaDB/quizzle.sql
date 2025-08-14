-- Quizzle Datenbankstruktur 
CREATE DATABASE IF NOT EXISTS quizzle_db;
USE quizzle_db;

-- Tabelle Theme
CREATE TABLE Theme (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT
);

-- Tabelle Question
CREATE TABLE Questions (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           text TEXT,
                           theme_id INT NOT NULL,
                           FOREIGN KEY (theme_id) REFERENCES Theme(id) ON DELETE CASCADE
);

-- Tabelle Answer
CREATE TABLE Answers (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         text TEXT NOT NULL,
                         isCorrect BOOLEAN NOT NULL DEFAULT FALSE,
                         question_id INT NOT NULL,
                         FOREIGN KEY (question_id) REFERENCES Questions(id) ON DELETE CASCADE
);