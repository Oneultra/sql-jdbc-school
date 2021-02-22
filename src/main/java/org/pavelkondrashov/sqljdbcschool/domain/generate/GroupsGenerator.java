package org.pavelkondrashov.sqljdbcschool.domain.generate;

import org.pavelkondrashov.sqljdbcschool.entity.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupsGenerator {
    private static final int PREFIX_LENGTH = 2;
    private static final String HYPHEN = "-";
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private final Random random;

    public GroupsGenerator(Random random) {
        this.random = random;
    }

    public List<Group> generateGroups(int groupId, int maxGroups) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < maxGroups; i++) {
            groups.add(Group.builder()
                    .withGroupId(i + 1)
                    .withGroupName(createName(groupId))
                    .build());
        }
        return groups;
    }

    private String createName(int groupId) {
        StringBuilder name = new StringBuilder();

        for (int i = 0; i < PREFIX_LENGTH; i++) {
            name.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return name.append(HYPHEN).append(random.nextInt(groupId)).toString().toUpperCase();
    }
}
