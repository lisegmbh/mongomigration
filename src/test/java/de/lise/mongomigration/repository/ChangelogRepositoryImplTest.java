package de.lise.mongomigration.repository;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import de.lise.mongomigration.AbstractMongoDBTest;
import de.lise.mongomigration.dao.ChangelogDAO;
import de.lise.mongomigration.domain.ChangeSet;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Method;
import java.util.Optional;

public class ChangelogRepositoryImplTest extends AbstractMongoDBTest {

    @Test
    public void idIsNotNull() throws NoSuchMethodException {
        MongoTemplate template = getTemplateForDatabase("test");
        ChangelogRepositoryImpl repository = new ChangelogRepositoryImpl(template);

        Method method = this.getClass().getMethod("testMethod", MongoDatabase.class);
        ChangelogDAO dao = repository.saveChangelog(new ChangelogDAO(method));

        Assertions.assertThat(dao)
                .isNotNull()
                .extracting("id")
                .isNotNull();
    }


    @Test
    public void deleteById() throws NoSuchMethodException {
        ChangelogRepositoryImpl repository = new ChangelogRepositoryImpl(getTemplateForDatabase("test"));
        Method method = this.getClass().getMethod("testMethod", MongoDatabase.class);

        ChangelogDAO dao = repository.saveChangelog(new ChangelogDAO(method));
        DeleteResult deleteResult = repository.deleteById(dao.getId());
        Optional<ChangelogDAO> selectedDao = repository.findById(dao.getId());

        Assertions.assertThat(selectedDao).isEmpty();
        Assertions.assertThat(deleteResult.getDeletedCount()).isEqualTo(1);

    }


    @Test
    public void findById() throws NoSuchMethodException {
        ChangelogRepositoryImpl repository = new ChangelogRepositoryImpl(getTemplateForDatabase("test"));
        Method method = this.getClass().getMethod("testMethod", MongoDatabase.class);

        ChangelogDAO dao = repository.saveChangelog(new ChangelogDAO(method));
        Optional<ChangelogDAO> selectedDao = repository.findById(dao.getId());

        Assertions.assertThat(selectedDao)
                .isPresent()
                .get()
                .extracting("id")
                .isNotNull();

        Assertions.assertThat(selectedDao.get())
                .isEqualToIgnoringGivenFields(dao, "id", "timestamp");

    }


    @Test
    public void createIndex() {
        MongoTemplate template = getTemplateForDatabase("test");
        new ChangelogRepositoryImpl(template);

        ListIndexesIterable<Document> indexes = template.getCollection(ChangelogDAO.COLLECTION_NAME).listIndexes();

        Assertions.assertThat(indexes)
                .isNotEmpty()
                .hasSize(2)
                .element(1)
                .matches(i -> i.getBoolean("unique").equals(true))
                .matches(i ->
                {
                    Document value = (Document) i.get("key");
                    Integer changeId = value.getInteger("changeId");
                    Integer author = value.getInteger("author");

                    return changeId == 1 && author == 1;
                });
    }


    @ChangeSet(order = "1", author = "n00bi", id = "1")
    public void testMethod(MongoDatabase database) {

    }

}
