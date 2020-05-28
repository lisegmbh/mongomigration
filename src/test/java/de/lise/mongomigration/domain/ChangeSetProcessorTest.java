package de.lise.mongomigration.domain;

import de.lise.mongomigration.dao.ChangelogDAO;
import de.lise.mongomigration.domain.test.A;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

public class ChangeSetProcessorTest {

    @Test
    public void findChangeSetMethodOfClassA() {
        List<Method> methods = ChangeSetProcessor.changeSetMethods(A.class);

        Assertions
                .assertThat(methods)
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
        List<Method> methods = ChangeSetProcessor.changeSetMethods(A.class);
        List<ChangelogDAO> changelogDAOS = ChangeSetProcessor.toDaos(methods);

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
