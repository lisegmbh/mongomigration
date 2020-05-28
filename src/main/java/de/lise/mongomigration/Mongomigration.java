package de.lise.mongomigration;

import de.lise.mongomigration.dao.ChangelogDAO;
import de.lise.mongomigration.domain.ChangeSetProcessor;
import de.lise.mongomigration.domain.ChangelogComparator;
import de.lise.mongomigration.domain.ChangelogScanner;
import de.lise.mongomigration.repository.ChangelogRepository;
import de.lise.mongomigration.repository.ChangelogRepositoryImpl;
import de.lise.mongomigration.repository.LockRepository;
import de.lise.mongomigration.repository.LockRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Mongomigration implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(Mongomigration.class);
    private final MongoTemplate mongoTemplate;
    private LockRepository lockRepository;
    private ChangelogRepository changelogRepository;
    private String changeLogsScanPackage = "";

    public Mongomigration(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.lockRepository = new LockRepositoryImpl(mongoTemplate);
        this.changelogRepository = new ChangelogRepositoryImpl(mongoTemplate);
    }


    public void setChangeLogsScanPackage(String changeLogsScanPackage) {
        this.changeLogsScanPackage = changeLogsScanPackage;
    }


    @Override
    public void afterPropertiesSet() {
        execute();
    }


    void setLockRepository(LockRepository lockRepository) {
        this.lockRepository = lockRepository;
    }


    void execute() {
        if (!lockRepository.acquireLock()) {
            logger.info("Mongomigration did not acquire process lock. Exiting.");
            return;
        }

        logger.info("Mongomigration acquired process lock, starting the data migration sequence..");

        try {
            executeMigration();
        } finally {
            logger.info("Mongomigration is releasing process lock.");
            lockRepository.releaseLock();
        }

        logger.info("Mongomigration has finished his job.");
    }


    void executeMigration() {
        ChangelogScanner scanner = new ChangelogScanner();

        List<ChangelogDAO> changelogs = scanner
                .scanForChangelogClasses(changeLogsScanPackage)
                .stream()
                .map(ChangeSetProcessor::changeSetMethods)
                .map(ChangeSetProcessor::toDaos)
                .reduce(new ArrayList<>(), this::merge);

        changelogs.sort(new ChangelogComparator());

        changelogs.forEach(changelog ->
        {
            if (!changelogRepository.findByChangeIdAndAuthor(changelog.getChangeId(), changelog.getAuthor()).isPresent()) {
                ChangelogDAO saveChangelog = changelogRepository.saveChangelog(changelog);
                try {
                    changelog.executeMethod(mongoTemplate.getDb());
                } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
                    changelogRepository.deleteById(saveChangelog.getId());
                    logger.error("Migration method {} from {} could not be invoked", changelog.getChangeSetMethod(), changelog.getChangeLogClass());
                }
            }
        });
    }


    private ArrayList<ChangelogDAO> merge(List<ChangelogDAO> lhs, List<ChangelogDAO> rhs) {
        ArrayList<ChangelogDAO> result = new ArrayList<>(lhs.size() + rhs.size());
        result.addAll(lhs);
        result.addAll(rhs);
        return result;
    }
}
