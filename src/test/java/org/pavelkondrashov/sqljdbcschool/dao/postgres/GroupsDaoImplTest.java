package org.pavelkondrashov.sqljdbcschool.dao.postgres;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pavelkondrashov.sqljdbcschool.dao.GroupsDao;
import org.pavelkondrashov.sqljdbcschool.domain.SQLConnector;
import org.pavelkondrashov.sqljdbcschool.domain.generate.DBGenerator;
import org.pavelkondrashov.sqljdbcschool.entity.Group;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupsDaoImplTest {
    private final SQLConnector connector = new SQLConnector("src/test/resources/database.properties");
    private final DBGenerator dbGenerator = new DBGenerator(connector);
    private final GroupsDao groupsDao = new GroupsDaoImpl(connector);

    @BeforeEach
    void createTable() {
        dbGenerator.executeSqlScript("school.sql");
    }

    @Test
    void saveShouldSaveGroupIntoDataBase() {
        Group expected = Group.builder()
                .withGroupName("group four").withGroupId(4).build();

        groupsDao.save(expected);
        Group actual = groupsDao.findById(4).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveAllShouldSaveCoursesWhenGetListOfCourses() {
        List<Group> groups = new ArrayList<>();
        Group group1 = Group.builder()
                .withGroupId(4).withGroupName("group four").build();
        Group group2 = Group.builder()
                .withGroupId(5).withGroupName("group five").build();

        groups.add(group1);
        groups.add(group2);

        groupsDao.saveAll(groups);
        List<Group> actual = groupsDao.findAll();
        assertThat(actual).contains(group1).contains(group2);
    }

    @Test
    void findShouldReturnGroupWithDesiredId() {
        Group expected = Group.builder()
                .withGroupName("group one").withGroupId(1).build();

        Group actual = groupsDao.findById(1).orElse(null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnListOfGroupsWhenGetCorrectParameters() {
        List<Group> expected = new ArrayList<>();
        expected.add(groupsDao.findById(1).orElse(null));

        List<Group> actual = groupsDao.findAll(1, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAllShouldReturnListOfAllGroupsWhenGetWrongParameters() {
        List<Group> expected = new ArrayList<>();
        expected.add(groupsDao.findById(1).orElse(null));

        List<Group> actual = groupsDao.findAll(1, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void updateShouldUpdateGroupInDataBaseWhenGettingGroupWithSameId() {
        Group expected = Group.builder()
                .withGroupName("new group").withGroupId(1).build();

        groupsDao.update(expected);
        Group actual = groupsDao.findById(1).orElse(null);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteByIdShouldRemoveGroupWhenGetId() {
        groupsDao.deleteById(2);

        Group actual = groupsDao.findById(2).orElse(null);
        assertThat(actual).isNull();
    }

    @Test
    void findAllByStudentNumberShouldReturnListOfGroupsWithSpecificCountOfStudents() {
        List<Group> expected = new ArrayList<>();
        expected.add(groupsDao.findById(2).orElse(null));

        List<Group> actual = groupsDao.findAllByStudentsNumber(2);
        assertThat(expected).isEqualTo(actual);
    }
}
