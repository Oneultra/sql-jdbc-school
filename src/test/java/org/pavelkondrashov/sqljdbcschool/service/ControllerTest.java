package org.pavelkondrashov.sqljdbcschool.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pavelkondrashov.sqljdbcschool.dao.CoursesDao;
import org.pavelkondrashov.sqljdbcschool.dao.GroupsDao;
import org.pavelkondrashov.sqljdbcschool.dao.StudentsDao;
import org.pavelkondrashov.sqljdbcschool.domain.datasource.DBSource;
import org.pavelkondrashov.sqljdbcschool.domain.generate.DBGenerator;
import org.pavelkondrashov.sqljdbcschool.entity.Course;
import org.pavelkondrashov.sqljdbcschool.userinterface.ViewProvider;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ControllerTest {
    @InjectMocks
    private Controller controller;

    @Mock
    private DBGenerator mockedDBGenerator;
    
    @Mock
    private DBSource mockedDBSource;
    
    @Mock
    private ViewProvider mockedViewProvider;
    
    @Mock
    private GroupsDao mockedGroupDao;
    
    @Mock
    private CoursesDao mockedCourseDao;
    
    @Mock
    private StudentsDao mockedStudentDao;

    @Test
    void loadShouldInsertAllDataToDataBase() {
        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder()
                .withCourseId(1).withCourseName("course one").withCourseDescription("descr one")
                .build());
        courses.add(Course.builder()
                .withCourseId(2).withCourseName("course two").withCourseDescription("descr two")
                .build());
        courses.add(Course.builder()
                .withCourseId(3).withCourseName("course three").withCourseDescription("descr three")
                .build());

        when(mockedDBSource.getCourses()).thenReturn(courses);

        controller.load();
        verify(mockedDBGenerator).executeSqlScript(anyString());
        verify(mockedGroupDao).saveAll(anyList());
        verify(mockedCourseDao).saveAll(anyList());
        verify(mockedStudentDao).saveAll(anyList());
        verify(mockedViewProvider).print(anyString());
    }
}
