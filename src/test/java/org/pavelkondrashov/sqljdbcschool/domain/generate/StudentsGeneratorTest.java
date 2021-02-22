package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentsGeneratorTest {
    @InjectMocks
    private StudentsGenerator studentsGenerator;

    @Mock
    private Random mockedRandom;

    @Test
    void generateStudentsShouldReturnListOfStudentsWhenGetsAllParameters() {
        List<String> names = new ArrayList<>();

        names.add("Mikhail");
        names.add("Fillip");

        List<String> surnames = new ArrayList<>();
        surnames.add("Boyarskiy");
        surnames.add("Kirkorov");

        List<Student> expected = new ArrayList<>();

        expected.add(Student.builder().withStudentId(1).withGroupId(1)
                .withStudentName("Mikhail").withStudentSurname("Boyarskiy").build());
        expected.add(Student.builder().withStudentId(2).withGroupId(2)
                .withStudentName("Fillip").withStudentSurname("Kirkorov").build());

        when(mockedRandom.nextInt(anyInt())).thenReturn(0).thenReturn(0).thenReturn(0)
                .thenReturn(1).thenReturn(1).thenReturn(1);

        List<Student> actual = studentsGenerator.generateStudents(names, surnames, 2, 2);
        assertThat(expected).isEqualTo(actual);
    }
}
