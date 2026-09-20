-- ==========================================================
-- Student Internship Tracker Database Schema
-- Database: internship_tracker
-- ==========================================================

CREATE DATABASE IF NOT EXISTS internship_tracker;
USE internship_tracker;

-- 1. Drop existing tables if they exist (in proper order for foreign keys)
DROP TABLE IF EXISTS internships;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS users;

-- 2. Users Table (Authentication & Role Management)
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL, -- ADMIN, STUDENT, FACULTY, HOD
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Students Table (Managed by Admin)
CREATE TABLE students (
    student_id VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    cgpa DOUBLE NOT NULL,
    user_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- 4. Internships Table (Submissions & Review Workflow)
CREATE TABLE internships (
    internship_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(30) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    company_location VARCHAR(100) NOT NULL,
    domain VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    internship_mode VARCHAR(20) NOT NULL, -- Online, Offline, Hybrid
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending', -- Pending, Approved, Rejected
    remark VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_internship_student FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);

-- ==========================================================
-- Seed Data for Testing
-- ==========================================================

-- Insert Users (admin, student1, student2, faculty1, hod1)
INSERT INTO users (username, password, role, full_name) VALUES
('admin', 'admin123', 'ADMIN', 'System Administrator'),
('student1', 'student123', 'STUDENT', 'Alice Smith'),
('student2', 'student123', 'STUDENT', 'Bob Johnson'),
('faculty1', 'faculty123', 'FACULTY', 'Dr. Robert Clark'),
('hod1', 'hod123', 'HOD', 'Dr. Sarah Williams');

-- Insert Students
INSERT INTO students (student_id, name, department, year, cgpa, user_id) VALUES
('STU101', 'Alice Smith', 'Computer Science', 3, 8.9, 2),
('STU102', 'Bob Johnson', 'Information Technology', 4, 8.4, 3),
('STU103', 'Charlie Brown', 'Electronics & Comm', 3, 7.8, NULL);

-- Insert Sample Internships
INSERT INTO internships (student_id, student_name, company_name, company_location, domain, role, internship_mode, start_date, end_date, status, remark) VALUES
('STU101', 'Alice Smith', 'Google India', 'Bengaluru', 'Software Engineering', 'SDE Intern', 'Hybrid', '2026-06-01', '2026-08-31', 'Pending', NULL),
('STU101', 'Alice Smith', 'Microsoft', 'Hyderabad', 'Cloud Computing', 'Azure Cloud Intern', 'Online', '2026-01-10', '2026-04-10', 'Approved', 'Verified offer letter and NOC'),
('STU102', 'Bob Johnson', 'Amazon', 'Chennai', 'Data Science', 'ML Intern', 'Offline', '2026-05-15', '2026-07-15', 'Rejected', 'Duration overlaps with semester exams'),
('STU102', 'Bob Johnson', 'Infosys', 'Mysuru', 'Web Development', 'Full Stack Intern', 'Online', '2026-09-01', '2026-11-30', 'Pending', NULL);
