package de.lise.mongomigration.repository;

import com.mongodb.client.result.DeleteResult;
import de.lise.mongomigration.dao.ChangelogDAO;

import java.util.Optional;

public interface ChangelogRepository {
    ChangelogDAO saveChangelog(ChangelogDAO changelog);


    DeleteResult deleteById(String id);


    Optional<ChangelogDAO> findById(String id);

    Optional<ChangelogDAO> findByChangeIdAndAuthor(String changeId, String author);
}
