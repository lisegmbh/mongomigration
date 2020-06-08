package de.lise.mongomigration.domain;

import de.lise.mongomigration.dao.ChangelogDAO;
import de.lise.mongomigration.domain.test.A;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.data.util.Pair;

public class ChangeSetProcessorTest {

    @Test
    public void findChangeSetMethodOfClassA() {
        Pair<Class<?>, List<Method>> classListPair = ChangeSetProcessor.changeSetMethods(A.class);

        Assertions
                .assertThat(classListPair.getSecond())
                .hasSize(1)
                .first()
                .matches(m -> m.getName().equals("changeSetMethod"))
                .extracting(m -> m.getAnnotation(ChangeSet.class))
                .matches(a -> a.author().equals("unit-test"))
                .matches(a -> a.order().equals("1"))
                .matches(a -> a.id().equals("2"));
    }

    @Test
    public void createDao() {
        Pair<Class<?>, List<Method>> classListPair = ChangeSetProcessor.changeSetMethods(A.class);
        List<ChangelogDAO> changelogDAOS = ChangeSetProcessor.toDaos(classListPair);

        Assertions
                .assertThat(changelogDAOS)
                .hasSize(1)
                .first()
                .matches(m -> m.getChangeSetMethod().equals("changeSetMethod"))
                .matches(m -> m.getAuthor().equals("unit-test"))
                .matches(m -> m.getOrder().equals("1"))
                .matches(m -> m.getChangeId().equals("2"));
    }
}
