-- ==========================================================
-- Batch Import Classmates (CSBS Department)
-- ==========================================================
USE internship_tracker;

-- Insert into users table
INSERT INTO users (username, password, role, full_name) VALUES
('25CSBS01', '4001', 'STUDENT', 'ABINAYA R'),
('25CSBS26', '4026', 'STUDENT', 'KAVIYADHARSHINI R'),
('25CSBS27', '4027', 'STUDENT', 'KEERTHI JOSHIYA N'),
('25CSBS28', '4028', 'STUDENT', 'KRISHA M'),
('25CSBS29', '4029', 'STUDENT', 'KRISHNA PRASATH K'),
('25CSBS30', '4030', 'STUDENT', 'LEELOBAR BANU Y'),
('25CSBS31', '4031', 'STUDENT', 'MAGDALENE SHARON SATHISHKUMAR'),
('25CSBS32', '4032', 'STUDENT', 'MITHRASHIVANI SHIVAKUMAR'),
('25CSBS33', '4033', 'STUDENT', 'MITHUN PRASATH LOGESH'),
('25CSBS34', '4034', 'STUDENT', 'MOHAMED RAYHAN M'),
('25CSBS35', '4035', 'STUDENT', 'NANDHAKUMAR R'),
('25CSBS36', '4036', 'STUDENT', 'NIKHIL ESWARAR K S'),
('25CSBS37', '4037', 'STUDENT', 'NIRANJAN R'),
('25CSBS38', '4038', 'STUDENT', 'NIRMAL KUMAR K'),
('25CSBS39', '4039', 'STUDENT', 'PATHMASREE T'),
('25CSBS40', '4040', 'STUDENT', 'PAVITHRA M'),
('25CSBS41', '4041', 'STUDENT', 'PRAGADEESH H'),
('25CSBS42', '4042', 'STUDENT', 'PRASANNA D'),
('25CSBS43', '4043', 'STUDENT', 'PRATHOSH R P'),
('25CSBS44', '4044', 'STUDENT', 'PREETHIVRAJAN S'),
('25CSBS45', '4045', 'STUDENT', 'R GIRISH'),
('25CSBS46', '4046', 'STUDENT', 'RAHUL M Y'),
('25CSBS47', '4047', 'STUDENT', 'RATHINA MARI P'),
('25CSBS48', '4048', 'STUDENT', 'RISHI S'),
('25CSBS49', '4049', 'STUDENT', 'RUDHUSHRI P'),
('25CSBS50', '4050', 'STUDENT', 'SAHANA A'),
('25CSBS51', '4051', 'STUDENT', 'SAHANA S'),
('25CSBS52', '4052', 'STUDENT', 'SANJAY S'),
('25CSBS53', '4053', 'STUDENT', 'SARAN S'),
('25CSBS54', '4054', 'STUDENT', 'SHAMIKSHA S'),
('25CSBS55', '4055', 'STUDENT', 'SHARMILA S'),
('25CSBS56', '4056', 'STUDENT', 'SHARMILY C'),
('25CSBS57', '4057', 'STUDENT', 'SHARVIKA GANESHRAJ'),
('25CSBS58', '4058', 'STUDENT', 'SHEETAL BATHRI NARAYANAN'),
('25CSBS59', '4059', 'STUDENT', 'SRIJITH S'),
('25CSBS60', '4060', 'STUDENT', 'SRINIVASAN P'),
('25CSBS61', '4061', 'STUDENT', 'SUVETHA J'),
('25CSBS62', '4062', 'STUDENT', 'THARUN C'),
('25CSBS63', '4063', 'STUDENT', 'THIRISHA J'),
('25CSBS64', '4064', 'STUDENT', 'V S MAYURI'),
('25CSBS65', '4065', 'STUDENT', 'V SRUSHTIKA RAJAN'),
('25CSBS66', '4066', 'STUDENT', 'VISHNU PRASATH J')
ON DUPLICATE KEY UPDATE full_name=VALUES(full_name), password=VALUES(password);

-- Insert into students table linking to user_id
INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS01', 'ABINAYA R', 'CSBS', 2, 8.90, user_id FROM users WHERE username='25CSBS01'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS26', 'KAVIYADHARSHINI R', 'CSBS', 2, 8.85, user_id FROM users WHERE username='25CSBS26'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS27', 'KEERTHI JOSHIYA N', 'CSBS', 2, 8.70, user_id FROM users WHERE username='25CSBS27'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS28', 'KRISHA M', 'CSBS', 2, 8.92, user_id FROM users WHERE username='25CSBS28'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS29', 'KRISHNA PRASATH K', 'CSBS', 2, 8.45, user_id FROM users WHERE username='25CSBS29'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS30', 'LEELOBAR BANU Y', 'CSBS', 2, 9.10, user_id FROM users WHERE username='25CSBS30'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS31', 'MAGDALENE SHARON SATHISHKUMAR', 'CSBS', 2, 9.25, user_id FROM users WHERE username='25CSBS31'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS32', 'MITHRASHIVANI SHIVAKUMAR', 'CSBS', 2, 8.80, user_id FROM users WHERE username='25CSBS32'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS33', 'MITHUN PRASATH LOGESH', 'CSBS', 2, 8.35, user_id FROM users WHERE username='25CSBS33'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS34', 'MOHAMED RAYHAN M', 'CSBS', 2, 8.65, user_id FROM users WHERE username='25CSBS34'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS35', 'NANDHAKUMAR R', 'CSBS', 2, 9.20, user_id FROM users WHERE username='25CSBS35'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS36', 'NIKHIL ESWARAR K S', 'CSBS', 2, 8.50, user_id FROM users WHERE username='25CSBS36'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS37', 'NIRANJAN R', 'CSBS', 2, 8.75, user_id FROM users WHERE username='25CSBS37'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS38', 'NIRMAL KUMAR K', 'CSBS', 2, 8.40, user_id FROM users WHERE username='25CSBS38'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS39', 'PATHMASREE T', 'CSBS', 2, 8.95, user_id FROM users WHERE username='25CSBS39'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS40', 'PAVITHRA M', 'CSBS', 2, 9.05, user_id FROM users WHERE username='25CSBS40'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS41', 'PRAGADEESH H', 'CSBS', 2, 9.30, user_id FROM users WHERE username='25CSBS41'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS42', 'PRASANNA D', 'CSBS', 2, 8.60, user_id FROM users WHERE username='25CSBS42'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS43', 'PRATHOSH R P', 'CSBS', 2, 8.70, user_id FROM users WHERE username='25CSBS43'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS44', 'PREETHIVRAJAN S', 'CSBS', 2, 8.85, user_id FROM users WHERE username='25CSBS44'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS45', 'R GIRISH', 'CSBS', 2, 8.55, user_id FROM users WHERE username='25CSBS45'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS46', 'RAHUL M Y', 'CSBS', 2, 8.75, user_id FROM users WHERE username='25CSBS46'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS47', 'RATHINA MARI P', 'CSBS', 2, 8.90, user_id FROM users WHERE username='25CSBS47'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS48', 'RISHI S', 'CSBS', 2, 8.65, user_id FROM users WHERE username='25CSBS48'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS49', 'RUDHUSHRI P', 'CSBS', 2, 9.15, user_id FROM users WHERE username='25CSBS49'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS50', 'SAHANA A', 'CSBS', 2, 8.80, user_id FROM users WHERE username='25CSBS50'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS51', 'SAHANA S', 'CSBS', 2, 9.00, user_id FROM users WHERE username='25CSBS51'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS52', 'SANJAY S', 'CSBS', 2, 8.40, user_id FROM users WHERE username='25CSBS52'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS53', 'SARAN S', 'CSBS', 2, 9.40, user_id FROM users WHERE username='25CSBS53'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS54', 'SHAMIKSHA S', 'CSBS', 2, 8.90, user_id FROM users WHERE username='25CSBS54'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS55', 'SHARMILA S', 'CSBS', 2, 9.10, user_id FROM users WHERE username='25CSBS55'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS56', 'SHARMILY C', 'CSBS', 2, 8.75, user_id FROM users WHERE username='25CSBS56'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS57', 'SHARVIKA GANESHRAJ', 'CSBS', 2, 9.05, user_id FROM users WHERE username='25CSBS57'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS58', 'SHEETAL BATHRI NARAYANAN', 'CSBS', 2, 9.20, user_id FROM users WHERE username='25CSBS58'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS59', 'SRIJITH S', 'CSBS', 2, 8.85, user_id FROM users WHERE username='25CSBS59'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS60', 'SRINIVASAN P', 'CSBS', 2, 8.50, user_id FROM users WHERE username='25CSBS60'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS61', 'SUVETHA J', 'CSBS', 2, 8.95, user_id FROM users WHERE username='25CSBS61'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS62', 'THARUN C', 'CSBS', 2, 8.60, user_id FROM users WHERE username='25CSBS62'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS63', 'THIRISHA J', 'CSBS', 2, 9.00, user_id FROM users WHERE username='25CSBS63'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS64', 'V S MAYURI', 'CSBS', 2, 8.80, user_id FROM users WHERE username='25CSBS64'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS65', 'V SRUSHTIKA RAJAN', 'CSBS', 2, 9.15, user_id FROM users WHERE username='25CSBS65'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);

INSERT INTO students (student_id, name, department, year, cgpa, user_id)
SELECT '25CSBS66', 'VISHNU PRASATH J', 'CSBS', 2, 8.70, user_id FROM users WHERE username='25CSBS66'
ON DUPLICATE KEY UPDATE name=VALUES(name), department=VALUES(department), year=VALUES(year), cgpa=VALUES(cgpa);
