CREATE SCHEMA IF NOT EXISTS SCHOOL;

DROP TABLE IF EXISTS SCHOOL.students CASCADE;
DROP TABLE IF EXISTS SCHOOL.groups CASCADE;
DROP TABLE IF EXISTS SCHOOL.courses CASCADE;
DROP TABLE IF EXISTS SCHOOL.students_courses CASCADE;

CREATE TABLE SCHOOL.groups
(
    group_id   INT  NOT NULL,
    group_name VARCHAR    NOT NULL,
    PRIMARY KEY (group_id)
);

CREATE TABLE SCHOOL.students
(
    student_id        BIGINT UNIQUE NOT NULL,
    group_id          INT,
    student_name      VARCHAR    NOT NULL,
    student_last_name VARCHAR    NOT NULL,
    PRIMARY KEY (student_id),
    FOREIGN KEY (group_id) REFERENCES SCHOOL.groups ON DELETE SET NULL
);

CREATE TABLE SCHOOL.courses
(
    course_id   INT UNIQUE NOT NULL,
    course_name VARCHAR    NOT NULL,
    description VARCHAR    NOT NULL,
    PRIMARY KEY (course_id)
);

CREATE TABLE SCHOOL.students_courses
(
    student_id BIGINT,
    course_id  BIGINT,
    FOREIGN KEY (student_id) REFERENCES SCHOOL.students ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES SCHOOL.courses ON DELETE CASCADE
);

INSERT INTO SCHOOL.courses VALUES (1,'Literature','blabla');
INSERT INTO SCHOOL.courses VALUES (2,'Music','blablabla');

INSERT INTO SCHOOL.groups VALUES (1,'group one');
INSERT INTO SCHOOL.groups VALUES (2,'group two');
INSERT INTO SCHOOL.groups VALUES (3,'group three');

INSERT INTO SCHOOL.students VALUES (1, 2,'Mikhail','Boyarskiy' );
INSERT INTO SCHOOL.students VALUES (2, 2,'Fillip','Kirkorov' );

INSERT INTO SCHOOL.students_courses VALUES (1,1);
INSERT INTO SCHOOL.students_courses VALUES (1,2);
INSERT INTO SCHOOL.students_courses VALUES (2,2);
