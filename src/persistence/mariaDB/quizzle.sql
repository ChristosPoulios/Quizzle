-- Quizzle Datenbankstruktur 
CREATE DATABASE IF NOT EXISTS quizzle_db;
USE quizzle_db;

-- Tabelle Theme
CREATE TABLE Theme (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(100) NOT NULL,
                        description TEXT(500)
);

-- Tabelle Question
CREATE TABLE Questions (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           title VARCHAR(200) NOT NULL,
                           text TEXT(1000),
                           theme_id INT NOT NULL,
                           FOREIGN KEY (theme_id) REFERENCES Theme(id) ON DELETE CASCADE
);

-- Tabelle Answer
CREATE TABLE Answers (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         text TEXT(500) NOT NULL,
                         isCorrect BOOLEAN NOT NULL DEFAULT FALSE,
                         question_id INT NOT NULL,
                         FOREIGN KEY (question_id) REFERENCES Questions(id) ON DELETE CASCADE
);

-- Tabelle QuizSession 
CREATE TABLE QuizSession (
    						id INT AUTO_INCREMENT PRIMARY KEY,
    						timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    						user_id INT NOT NULL
);

-- Tabelle UserAnswer 
CREATE TABLE UserAnswer (
    						id INT AUTO_INCREMENT PRIMARY KEY,
   							quizsession_id INT NOT NULL,
    						question_id INT NOT NULL,
   							answer_id INT NOT NULL,
    						isSelected BOOLEAN NOT NULL DEFAULT FALSE,
    						isCorrect BOOLEAN NOT NULL DEFAULT FALSE,
    						FOREIGN KEY (quizsession_id) REFERENCES QuizSession(id) ON DELETE CASCADE,
    						FOREIGN KEY (question_id) REFERENCES Questions(id) ON DELETE CASCADE,
    						FOREIGN KEY (answer_id) REFERENCES Answers(id) ON DELETE CASCADE
);