package de.lise.mongomigration.domain.test;

import com.mongodb.client.MongoDatabase;
import de.lise.mongomigration.domain.ChangeSet;
import de.lise.mongomigration.domain.Changelog;

@Changelog
public class A {

    @ChangeSet(order = "1", author = "unit-test", id = "2")
    public void changeSetMethod(MongoDatabase db) {

    }

    public void noChangeSetMethod(MongoDatabase db) {

    }
}
