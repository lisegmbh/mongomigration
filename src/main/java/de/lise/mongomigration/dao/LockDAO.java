package de.lise.mongomigration.dao;

import de.lise.mongomigration.domain.LockStatus;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = LockDAO.COLLECTION_NAME)
public class LockDAO {
    public static final String COLLECTION_NAME = "mongomigrationlock";
    public static final String KEY_PROP_NAME = "id";
    public static final String LOCK_ENTRY_KEY_VAL = "LOCK";

    private String id = LOCK_ENTRY_KEY_VAL;
    private Instant timestamp = Instant.now();
    private LockStatus lockStatus = LockStatus.LOCK_HELD;


    public LockDAO() {
    }


    public String getId() {
        return id;
    }


    public Instant getTimestamp() {
        return timestamp;
    }


    public void setLockStatus(LockStatus lockStatus) {
        this.lockStatus = lockStatus;
    }
}
