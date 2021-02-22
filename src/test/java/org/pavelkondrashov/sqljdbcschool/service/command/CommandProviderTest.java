package org.pavelkondrashov.sqljdbcschool.service.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pavelkondrashov.sqljdbcschool.dao.CoursesDao;
import org.pavelkondrashov.sqljdbcschool.dao.GroupsDao;
import org.pavelkondrashov.sqljdbcschool.dao.StudentsDao;
import org.pavelkondrashov.sqljdbcschool.userinterface.ViewProvider;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class CommandProviderTest {
    @InjectMocks
    private CommandProvider commandProvider;

    @Mock
    private GroupsDao mockedGroupDao;

    @Mock
    private CoursesDao mockedCourseDao;

    @Mock
    private StudentsDao mockedStudentDao;

    @Mock
    private ViewProvider mockedViewProvider;

    @Test
    void findGroupsWithStudentCountShouldPrintViewProvider5TimesWhenGet1AsCommand() {
        when(mockedViewProvider.readInt()).thenReturn(1).thenReturn(anyInt()).thenReturn(0);

        commandProvider.run();
        verify(mockedViewProvider, times(5)).print(anyString());
        verify(mockedGroupDao).findAllByStudentsNumber(anyInt());
    }

    @Test
    void findAllStudentsToCourseNameShouldPrintViewProvider5TimesAsGetting2AsCommand() {
        when(mockedViewProvider.readInt()).thenReturn(2).thenReturn(0);
        when(mockedViewProvider.readString()).thenReturn(anyString());

        commandProvider.run();
        verify(mockedViewProvider, times(6)).print(anyString());
        verify(mockedCourseDao).findAll();
        verify(mockedStudentDao).findAllByCourseName(anyString());
    }

    @Test
    void addNewStudentShouldAddAndSaveNewStudentToBaseAndPrintViewProviderWhenGet3AsCommand() {
        when(mockedViewProvider.readInt()).thenReturn(3).thenReturn(111).thenReturn(4).thenReturn(0);
        when(mockedViewProvider.readString()).thenReturn("name").thenReturn("surname");

        commandProvider.run();
        verify(mockedViewProvider, times(8)).print(anyString());
        verify(mockedStudentDao).save(any());
    }

    @Test
    void deleteStudentByIdShouldDeleteStudentFromBaseAndPrintViewProviderWhenGet4AsCommand() {
        when(mockedViewProvider.readInt()).thenReturn(4).thenReturn(5).thenReturn(0);

        commandProvider.run();
        verify(mockedViewProvider, times(5)).print(anyString());
        verify(mockedStudentDao).deleteById(anyInt());
    }

    @Test
    void addStudentToCourseShouldAssignStudentFromCourseDaoBaseAndPrintView() {
        when(mockedViewProvider.readInt()).thenReturn(5).thenReturn(2).thenReturn(3).thenReturn(0);

        commandProvider.run();
        verify(mockedViewProvider, times(6)).print(anyString());
        verify(mockedStudentDao).addToCourse(anyInt(), anyInt());
    }

    @Test
    void removeStudentFromCourseShouldDeleteStudentFromCourseDaoBaseAndPrintView() {
        when(mockedViewProvider.readInt()).thenReturn(6).thenReturn(2).thenReturn(2).thenReturn(0);

        commandProvider.run();
        verify(mockedViewProvider, times(6)).print(anyString());
        verify(mockedCourseDao).removeStudentFromCourse(anyInt(), anyInt());
    }

    @Test
    void runShouldInvokePrintErrorWhenGetWrongCommand() {
        when(mockedViewProvider.readInt()).thenReturn(7).thenReturn(1).thenReturn(1).thenReturn(0);

        commandProvider.run();
        verify(mockedViewProvider).printError();
    }
}
