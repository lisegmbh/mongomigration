package de.lise.mongomigration.domain;

import de.lise.mongomigration.domain.test.A;
import de.lise.mongomigration.domain.test.B;
import de.lise.mongomigration.domain.test.subPackage.E;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class ChangelogScannerTest {

    @Test
    public void findAnnotatedClasses() {
        ChangelogScanner scanner = new ChangelogScanner();

        Set<Class<?>> classes = scanner.scanForChangelogClasses("de.lise.mongomigration.domain.test");

        Assertions.assertThat(classes).contains(A.class, B.class, E.class);
    }
}
