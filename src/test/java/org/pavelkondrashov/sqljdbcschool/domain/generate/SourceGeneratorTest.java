package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pavelkondrashov.sqljdbcschool.domain.datasource.DBSource;
import org.pavelkondrashov.sqljdbcschool.domain.reader.FileReader;
import org.pavelkondrashov.sqljdbcschool.entity.Course;
import org.pavelkondrashov.sqljdbcschool.entity.Group;
import org.pavelkondrashov.sqljdbcschool.entity.Student;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SourceGeneratorTest {
    @InjectMocks
    private SourceGenerator sourceGenerator;

    @Mock
    private GroupsGenerator mockedGroupsGenerator;

    @Mock
    private CourseGenerator mockedCourseGenerator;

    @Mock
    private StudentsGenerator mockedStudentGenerator;

    @Mock
    private FileReader mockedFileReader;

    @Test
    void generateDBSourceShouldReturnSourceObjectWhenGetAllFilePaths() {
        List<String> studentNames = new ArrayList<>();
        studentNames.add("Mikhail");
        studentNames.add("Fillip");

        List<String> studentSurnames = new ArrayList<>();
        studentSurnames.add("Boyarskiy");
        studentSurnames.add("Kirkorov");

        List<String> coursesAndDescr = new ArrayList<>();
        coursesAndDescr.add("course one_descr one");
        coursesAndDescr.add("course two_descr two");

        List<Student> students = new ArrayList<>();
        students.add(Student.builder()
                .withStudentId(1).withGroupId(1)
                .withStudentName("Mikhail").withStudentSurname("Boyarskiy").build());
        students.add(Student.builder()
                .withStudentId(2).withGroupId(2)
                .withStudentName("Fillip").withStudentSurname("Kirkorov").build());

        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                .withCourseId(1).withCourseName("course one").withCourseDescription("descr one")
                .build());
        courses.add(Course.builder()
                .withCourseId(2).withCourseName("course two").withCourseDescription("descr two")
                .build());

        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder()
                .withGroupId(1).withGroupName("one")
                .build());
        groups.add(Group.builder()
                .withGroupId(2).withGroupName("two")
                .build());

        DBSource expected = new DBSource(students, groups, courses);

        when(mockedFileReader.readFile(anyString()))
                .thenReturn(studentNames).thenReturn(studentSurnames).thenReturn(coursesAndDescr);
        when(mockedStudentGenerator.generateStudents(anyList(), anyList(), anyInt(), anyInt())).thenReturn(students);
        when(mockedCourseGenerator.generateCourses(anyInt(), anyList())).thenReturn(courses);
        when(mockedGroupsGenerator.generateGroups(anyInt(), anyInt())).thenReturn(groups);

        DBSource actual = sourceGenerator.generateDBSource();

        assertThat(expected).isEqualTo(actual);
    }
}
