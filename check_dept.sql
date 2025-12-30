-- Check departments
-- Check departments
DESCRIBE employee;
-- FORCE department_id to be a regular Integer (Fixes the 2->1 issue)
ALTER TABLE employee MODIFY department_id INT;

SELECT * FROM departments;

-- Check employees with their department_id
SELECT sn, employee_code, name, department_id FROM employee ORDER BY sn;

-- Check if department_id 2 exists in employee table
SELECT COUNT(*) as employees_with_dept_2 FROM employee WHERE department_id = 2;

-- Check if department_id 1 exists in employee table
SELECT COUNT(*) as employees_with_dept_1 FROM employee WHERE department_id = 1;

-- INSERT TEST DATA WITH DEPARTMENT ID 2
INSERT INTO employee (employee_code, name, gender, dob, join_date, resignation_date, salary, address, phone, email, education, designation, designation_type, department_id, company, shift_type, photo, present_status)
VALUES ('TEST002', 'Demo Employee', 'MALE', '1990-01-01', '2023-01-01', NULL, 50000, 'Test Address', '9999999999', 'test@test.com', 'B.Tech', 'Tester', 'FULL_TIME', 2, 'BroCode', 'MORNING', NULL, 'PRESENT');

-- VERIFY INSERTION
SELECT sn, name, department_id FROM employee WHERE employee_code = 'TEST002';
