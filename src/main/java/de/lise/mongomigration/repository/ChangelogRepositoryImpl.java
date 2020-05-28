package de.lise.mongomigration.repository;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.result.DeleteResult;
import de.lise.mongomigration.dao.ChangelogDAO;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Optional;

public class ChangelogRepositoryImpl implements ChangelogRepository {
    private static final int INDEX_SORT_ASC = 1;
    private final MongoTemplate mongoTemplate;


    public ChangelogRepositoryImpl(MongoTemplate mongoTemplate) {
        createCollectionAndUniqueIndexIfNotExists(mongoTemplate.getDb());
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public ChangelogDAO saveChangelog(ChangelogDAO changelog) {
        return mongoTemplate.insert(changelog);
    }


    @Override
    public DeleteResult deleteById(String id) {
        return mongoTemplate.remove(new Query(Criteria.where("id").is(id)), ChangelogDAO.class);
    }


    @Override
    public Optional<ChangelogDAO> findById(String id) {
        ChangelogDAO dao = mongoTemplate.findById(id, ChangelogDAO.class, ChangelogDAO.COLLECTION_NAME);
        return Optional.ofNullable(dao);
    }

    @Override
    public Optional<ChangelogDAO> findByChangeIdAndAuthor(String changeId, String author) {
        ChangelogDAO dao = mongoTemplate.findOne(
                new Query(
                        Criteria.where(ChangelogDAO.KEY_CHANGEID).is(changeId)
                                .and(ChangelogDAO.KEY_AUTHOR).is(author)
                ), ChangelogDAO.class);

        return Optional.ofNullable(dao);
    }

    void createCollectionAndUniqueIndexIfNotExists(MongoDatabase db) {
        db
                .getCollection(ChangelogDAO.COLLECTION_NAME)
                .createIndex(
                        new Document()
                                .append(ChangelogDAO.KEY_CHANGEID, INDEX_SORT_ASC)
                                .append(ChangelogDAO.KEY_AUTHOR, INDEX_SORT_ASC),
                        new IndexOptions().unique(true));
    }
}
