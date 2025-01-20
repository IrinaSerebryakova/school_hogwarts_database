
SELECT students.name, students.age, faculties.name, FROM students INNER JOIN faculties ON students.facultyid = faculties.facultyid;
SELECT students.name, avatars.file_path FROM students INNER JOIN avatars ON students.studentid = avatars.student_studentid;