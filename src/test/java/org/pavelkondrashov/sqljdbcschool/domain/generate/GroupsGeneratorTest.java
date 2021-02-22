package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pavelkondrashov.sqljdbcschool.entity.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupsGeneratorTest {
    @InjectMocks
    private GroupsGenerator groupsGenerator;

    @Mock
    private Random mockedRandom;

    @Test
    void generateGroupsShouldReturnListOfGroupsWhenGetAllParameters() {
        List<Group> expected = new ArrayList<>();
        int maxId = 99;
        int maxGroups = 3;

        expected.add(Group.builder().withGroupId(1).withGroupName("AA-11").build());
        expected.add(Group.builder().withGroupId(2).withGroupName("BB-22").build());
        expected.add(Group.builder().withGroupId(3).withGroupName("CC-33").build());

        when(mockedRandom.nextInt(anyInt()))
                .thenReturn(0).thenReturn(0).thenReturn(11)
                .thenReturn(1).thenReturn(1).thenReturn(22)
                .thenReturn(2).thenReturn(2).thenReturn(33);

        List<Group> actual = groupsGenerator.generateGroups(maxId, maxGroups);

        assertThat(expected).isEqualTo(actual);
    }
}
