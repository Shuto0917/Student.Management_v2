CREATE TABLE IF NOT EXISTS students
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_Name VARCHAR(50) NOT NULL,
    furigana VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    email VARCHAR(50) NOT NULL,
    region VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    remark TEXT,
    deleted boolean
);

CREATE TABLE IF NOT EXISTS student_courses
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_name VARCHAR(36) NOT NULL,
    course_start_at TIMESTAMP,
    course_end_at TIMESTAMP,
    status ENUM('仮申込','本申込','受講中','受講終了') NOT NULL DEFAULT '仮申込'
);
