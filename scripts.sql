--1. Получить всех студентов, возраст которых находится между
--10 и 20 (можно подставить любые числа, главное, чтобы нижняя граница
--была меньше верхней).
SELECT * FROM students WHERE age>10 AND age<20;

--2. Получить всех студентов, но отобразить только список их имен.
SELECT name FROM students;

--3. Получить всех студентов, у которых в имени присутствует буква
--«О» (или любая другая).
SELECT * FROM students WHERE name LIKE '%о%';


--4. Получить всех студентов, у которых возраст меньше идентификатора.
SELECT * FROM students WHERE age<id;

--5. Получить всех студентов упорядоченных по возрасту.
SELECT * FROM students GROUP BY age;

INSERT INTO public.faculties (id, color, name) VALUES (1, 'красный', 'Гриффиндор');
INSERT INTO public.faculties (id, color, name) VALUES (2, 'зеленый', 'Слизерин');
INSERT INTO public.faculties (id, color, name) VALUES (3, 'желтый', 'Пуффендуй');
INSERT INTO public.faculties (id, color, name) VALUES (4, 'синий', 'Когтевран');
INSERT INTO public.students (age, facultyid, id, name) VALUES (15, 1, 1, 'Гарри Поттер');
INSERT INTO public.students (age, facultyid, id, name) VALUES (16, 1, 2, 'Гермиона Грейнджер');
INSERT INTO public.students (age, facultyid, id, name) VALUES (14, 2, 3, 'Малфой');
INSERT INTO public.students (age, facultyid, id, name) VALUES (10, 3, 4, 'Седрик Диггори');
INSERT INTO public.students (age, facultyid, id, name) VALUES (14, 4, 5, 'Вайнона Палмз');
INSERT INTO public.students (age, facultyid, id, name) VALUES (16, 2, 6, 'Северус Снегг');
INSERT INTO public.students (age, facultyid, id, name) VALUES (35, 2, 7, 'Беллетриса Лестрейндж');
INSERT INTO public.students (age, facultyid, id, name) VALUES (15, 1, 8, 'Рон Уизли');
SELECT * FROM students OFFSET 3;
SELECT COUNT(*) FROM students;
SELECT AVG(age) FROM students;

--ALTER TABLE public.avatars ADD id integer NULL;
--ALTER TABLE public.avatars ADD filePath varchar NULL;
--ALTER TABLE public.avatars ADD fileSize integer NULL;
--ALTER TABLE public.avatars ADD mediaType varchar NULL;
--ALTER TABLE public.avatars ADD studentid integer NULL;