package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentsGenerator {
    private final Random random;

    public StudentsGenerator(Random random) {
        this.random = random;
    }

    public List<Student> generateStudents(List<String> names, List<String> surnames,
                                          int maxStudents, int maxGroups) {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < maxStudents; i++) {
            students.add(Student.builder()
                    .withStudentId(i + 1)
                    .withGroupId(random.nextInt(maxGroups) + 1)
                    .withStudentName(names.get(random.nextInt(names.size())))
                    .withStudentSurname(surnames.get(random.nextInt(surnames.size())))
                    .build());
        }
        return students;
    }
}
