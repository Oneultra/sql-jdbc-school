package org.pavelkondrashov.sqljdbcschool.dao;

import org.pavelkondrashov.sqljdbcschool.entity.Group;

import java.util.List;

public interface GroupsDao extends CrudDao<Group, Integer> {
    List<Group> findAllByStudentsNumber(int number);
}
