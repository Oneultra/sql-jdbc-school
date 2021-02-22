package org.pavelkondrashov.sqljdbcschool.entity;

import java.util.Objects;

public class Student {

    private final Integer studentId;
    private final Integer groupId;
    private final String studentName;
    private final String studentSurname;

    private Student(Builder builder) {
        this.studentId = builder.studentId;
        this.groupId = builder.groupId;
        this.studentName = builder.studentName;
        this.studentSurname = builder.studentSurname;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getStudentId() {
        return studentId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    @Override
    public String toString() {
        return "Student: " +
                "studentId = " + studentId +
                ", groupId = " + groupId +
                ", studentName = '" + studentName + '\'' +
                ", studentSurname = '" + studentSurname + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId) &&
                Objects.equals(groupId, student.groupId) &&
                Objects.equals(studentName, student.studentName) &&
                Objects.equals(studentSurname, student.studentSurname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, groupId, studentName, studentSurname);
    }

    public static class Builder {
        private Integer studentId;
        private Integer groupId;
        private String studentName;
        private String studentSurname;

        public Builder withStudentId(Integer studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder withGroupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder withStudentName(String studentName) {
            this.studentName = studentName;
            return this;
        }

        public Builder withStudentSurname(String studentSurname) {
            this.studentSurname = studentSurname;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}
