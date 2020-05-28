package de.lise.mongomigration.domain;

import de.lise.mongomigration.dao.ChangelogDAO;

import java.util.Comparator;

public class ChangelogComparator implements Comparator<ChangelogDAO> {
    @Override
    public int compare(ChangelogDAO lhs, ChangelogDAO rhs) {
        return lhs.getOrder().compareTo(rhs.getOrder());
    }
}
