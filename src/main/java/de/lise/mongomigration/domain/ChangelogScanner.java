package de.lise.mongomigration.domain;

import org.springframework.data.util.AnnotatedTypeScanner;

import java.util.Set;

public class ChangelogScanner {

    public Set<Class<?>> scanForChangelogClasses(String changeLogsBasePackage) {
        AnnotatedTypeScanner scanner = new AnnotatedTypeScanner(Changelog.class);
        return scanner.findTypes(changeLogsBasePackage);
    }
}
