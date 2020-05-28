package de.lise.mongomigration.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import de.lise.mongomigration.dao.LockDAO;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class LockRepositoryImpl implements LockRepository {
    private static final Logger logger = LoggerFactory.getLogger(LockRepository.class);
    private static final int INDEX_SORT_ASC = 1;
    private final MongoTemplate mongoTemplate;


    public LockRepositoryImpl(MongoTemplate mongoTemplate) {
        createCollectionAndUniqueIndexIfNotExists(mongoTemplate.getDb());
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public boolean acquireLock() {
        try {
            mongoTemplate.insert(new LockDAO());
        } catch (DuplicateKeyException ex) {
            logger.warn("Duplicate key exception while acquireLock. Probably the lock has been already acquired.");
            return false;
        }
        return true;
    }


    @Override
    public void releaseLock() {
        mongoTemplate.remove(new Query(Criteria.where("id").is(LockDAO.LOCK_ENTRY_KEY_VAL)), LockDAO.class);
    }


    @Override
    public boolean lockExists() {
        MongoCollection<Document> collection = mongoTemplate.getCollection(LockDAO.COLLECTION_NAME);
        return collection.countDocuments() == 1;
    }


    void createCollectionAndUniqueIndexIfNotExists(MongoDatabase db) {
        Document indexKeys = new Document(LockDAO.KEY_PROP_NAME, INDEX_SORT_ASC);
        IndexOptions indexOptions = new IndexOptions().unique(true).name("mongomigration_key_idx");

        db.getCollection(LockDAO.COLLECTION_NAME).createIndex(indexKeys, indexOptions);
    }
}
