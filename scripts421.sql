ALTER TABLE students ADD CONSTRAINT age_constraint CHECK (age > 16); ALTER COLUMN name TEXT UNIQUE NOT NULL;
ALTER TABLE faculties (facultyid REAL, name TEXT PRIMARY KEY, color TEXT REFERENCES faculties (name), ALTER TABLE students (
     ..., age INT DEFAULT 20, ... );