-- liquibase formatted sql

-- changeset IrinaSerebryakova:1
CREATE TABLE students (id SERIAL, name TEXT, age INTEGER);
CREATE TABLE faculties (id SERIAL, name TEXT, color TEXT);
CREATE TABLE avatars (id BIGINT, filePath TEXT, fileSize REAL, mediaType TEXT);

-- changeset IrinaSerebryakova:2
CREATE INDEX students_name_index ON students(name);

-- changeset IrinaSerebryakova:3
CREATE INDEX faculties_name_color_index ON faculties(name, color);

-- changeset IrinaSerebryakova:4
 ALTER TABLE faculties ADD PRIMARY KEY (id);

-- changeset IrinaSerebryakova:5
 ALTER TABLE students ADD PRIMARY KEY (id);


