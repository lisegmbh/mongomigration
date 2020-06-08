package de.lise.mongomigration.domain;

import de.lise.mongomigration.dao.ChangelogDAO;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.util.Pair;

public class ChangeSetProcessor {
    public static Pair<Class<?>,List<Method>> changeSetMethods(Class<?> changelog) {
        return Pair.of(
                changelog,
                Arrays
                .stream(changelog.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(ChangeSet.class))
                .collect(Collectors.toList())
        );
    }


    public static List<ChangelogDAO> toDaos(Pair<Class<?>,List<Method>> methodsOfClass) {
        return methodsOfClass.getSecond()
                .stream()
                .map(m-> new ChangelogDAO(methodsOfClass.getFirst(), m))
                .collect(Collectors.toList());
    }
}
