-- FIX THE DEPARTMENT ID COLUMN TYPE
USE ems_db;
-- It is likely currently TINYINT(1) or BOOLEAN, which forces values to 0 or 1.
-- This command changes it to a standard Integer.

ALTER TABLE employee MODIFY department_id INT;

-- VERIFY THE FIX by updating the test user
UPDATE employee SET department_id = 2 WHERE employee_code = 'TEST002';

-- CHECK THE RESULT
SELECT sn, name, department_id FROM employee WHERE employee_code = 'TEST002';
