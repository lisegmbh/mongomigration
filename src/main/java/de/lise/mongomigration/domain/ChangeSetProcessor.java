package de.lise.mongomigration.domain;

import de.lise.mongomigration.dao.ChangelogDAO;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeSetProcessor {
    public static List<Method> changeSetMethods(Class<?> changelog) {
        return Arrays
                .stream(changelog.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(ChangeSet.class))
                .collect(Collectors.toList());
    }


    public static List<ChangelogDAO> toDaos(List<Method> methods) {
        return methods
                .stream()
                .map(ChangelogDAO::new)
                .collect(Collectors.toList());
    }
}
