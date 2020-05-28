package de.lise.mongomigration.domain;

import de.lise.mongomigration.dao.ChangelogDAO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChangelogComparatorTest {

    @Test
    public void leftSideIsGreaterThanRightSide() {
        ChangelogComparator comparator = new ChangelogComparator();

        int compare = comparator.compare(newDaoWithOrder("2"), newDaoWithOrder("1"));

        Assertions.assertThat(compare).isPositive();
    }

    @Test
    public void leftSideIsLessThanRightSide() {
        ChangelogComparator comparator = new ChangelogComparator();

        int compare = comparator.compare(newDaoWithOrder("2"), newDaoWithOrder("3"));

        Assertions.assertThat(compare).isNegative();
    }

    @Test
    public void leftSideIsEqualToRightSide() {
        ChangelogComparator comparator = new ChangelogComparator();

        int compare = comparator.compare(newDaoWithOrder("2"), newDaoWithOrder("2"));

        Assertions.assertThat(compare).isZero();
    }

    private ChangelogDAO newDaoWithOrder(String order) {
        ChangelogDAO dao = new ChangelogDAO();
        dao.setOrder(order);
        return dao;
    }
}
