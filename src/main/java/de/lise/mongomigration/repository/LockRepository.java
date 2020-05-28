package de.lise.mongomigration.repository;

public interface LockRepository {
    boolean acquireLock();


    void releaseLock();


    boolean lockExists();
}
