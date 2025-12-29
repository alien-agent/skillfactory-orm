-- Users
INSERT INTO users (name, email, role) VALUES ('Admin User', 'admin@example.com', 'ADMIN');
INSERT INTO users (name, email, role) VALUES ('Teacher One', 'teacher1@example.com', 'TEACHER');
INSERT INTO users (name, email, role) VALUES ('Teacher Two', 'teacher2@example.com', 'TEACHER');
INSERT INTO users (name, email, role) VALUES ('Student One', 'student1@example.com', 'STUDENT');
INSERT INTO users (name, email, role) VALUES ('Student Two', 'student2@example.com', 'STUDENT');

-- Categories
INSERT INTO category (name) VALUES ('Programming');
INSERT INTO category (name) VALUES ('Design');
INSERT INTO category (name) VALUES ('Marketing');

-- Courses
-- Assuming IDs 1, 2, 3...
INSERT INTO course (title, description, category_id, teacher_id) 
VALUES ('Java Basics', 'Learn Java from scratch', 1, 2);

INSERT INTO course (title, description, category_id, teacher_id) 
VALUES ('Advanced Spring', 'Deep dive into Spring Boot', 1, 2);

INSERT INTO course (title, description, category_id, teacher_id) 
VALUES ('UI/UX Principles', 'Design better interfaces', 2, 3);

-- Modules
INSERT INTO module (title, description, course_id) VALUES ('Introduction', 'Setup environment', 1);
INSERT INTO module (title, description, course_id) VALUES ('Syntax', 'Variables and loops', 1);

-- Lessons
INSERT INTO lesson (title, content, video_url, module_id) VALUES ('Hello World', 'First program', 'http://video.url/1', 1);

-- Assignments
INSERT INTO assignment (title, description, due_date, max_score, lesson_id) 
VALUES ('Write Hello World', 'Print Hello World to console', '2025-12-31T23:59:59', 100, 1);

-- Quizzes
INSERT INTO quiz (title, module_id) VALUES ('Java Basics Quiz', 1);

-- Questions
INSERT INTO question (text, type, quiz_id) VALUES ('What is the size of int in Java?', 'SINGLE_CHOICE', 1);

-- Answer Options
INSERT INTO answer_option (text, is_correct, question_id) VALUES ('32 bit', true, 1);
INSERT INTO answer_option (text, is_correct, question_id) VALUES ('64 bit', false, 1);
