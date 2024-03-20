/*-- Create database if not exists
CREATE DATABASE IF NOT EXISTS collegeSubjects;

-- Use the created database
USE collegeSubjects;
*/
-- Commented above because in some online compiler database, give error, uncomment if it will execute in local 

-- Create Users table
CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Create Subjects table
CREATE TABLE IF NOT EXISTS Subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(255) NOT NULL,
    degree VARCHAR(255) NOT NULL
);

-- Create Enrollment table
CREATE TABLE IF NOT EXISTS Enrollment (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    subject_id INT,
    semester VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (subject_id) REFERENCES Subjects(subject_id)
);

-- Insert sample data into Users table
INSERT INTO Users (username, email) VALUES
('Wesley Luiz', 'wesleyluiz@example.com'),
('Brenda Oliveira', 'brendaoliveira@example.com');


-- Insert sample data into Subjects table with Computer Science Bachelor degree
INSERT INTO Subjects (subject_name, degree) VALUES
('Logic and Programming Language', 'Computer Science Bachelor'),  
('Introduction to Computer Science', 'Computer Science Bachelor'),
('Introduction to Math', 'Computer Science Bachelor'),
('Digital Circuits', 'Computer Science Bachelor'),
('Data Struct', 'Computer Science Bachelor'),
('Computer Architecture', 'Computer Science Bachelor'),
('Calculus', 'Computer Science Bachelor'),
('Algebra', 'Computer Science Bachelor'),
('Statistical Probability', 'Computer Science Bachelor')
('Data File Organization', 'Computer Science Bachelor')
('Oriented Object Programming', 'Computer Science Bachelor'),
('Data Structures and Algorithms', 'Computer Science Bachelor'),
('Database Management Systems', 'Computer Science Bachelor'),
('Formal languages and automata', 'Computer Science Bachelor'),
('Computer Theory', 'Computer Science Bachelor'),
('Operating Systems', 'Computer Science Bachelor'),
('Computer Networks', 'Computer Science Bachelor'),
('Software Engineering', 'Computer Science Bachelor'),
('Artificial Intelligence', 'Computer Science Bachelor');

-- Display subjects related to Computer Science Bachelor
SELECT subject_name
FROM Subjects
WHERE degree = 'Computer Science Bachelor';


-- Inserting enrollment for Wesley Luiz in Oriented Object Programming in semester 2024.1 
INSERT INTO Enrollment (user_id, subject_id, semester) 
VALUES (
    (SELECT user_id FROM Users WHERE username = 'Wesley Luiz'), -- Getting user_id for Wesley Luiz
    (SELECT subject_id FROM Subjects WHERE subject_name = 'Oriented Object Programming'), -- Getting subject_id for Oriented Object Programming
    '2024.1'
);

INSERT INTO Enrollment (user_id, subject_id, semester) 
VALUES (
    (SELECT user_id FROM Users WHERE username = 'Wesley Luiz'), 
    (SELECT subject_id FROM Subjects WHERE subject_name = 'Introduction to Math'), -- Getting subject_id for Introduction to Math
    '2024.2'
);

INSERT INTO Enrollment (user_id, subject_id, semester) 
VALUES (
    (SELECT user_id FROM Users WHERE username = 'Brenda Oliveira'),
    (SELECT subject_id FROM Subjects WHERE subject_name = 'Oriented Object Programming'), -- Getting subject_id for Oriented Object Programming
    '2024.1'
);

-- Display enrollment information
SELECT Users.username, Users.email, Subjects.subject_name, Enrollment.semester
FROM Enrollment
JOIN Users ON Enrollment.user_id = Users.user_id
JOIN Subjects ON Enrollment.subject_id = Subjects.subject_id;
