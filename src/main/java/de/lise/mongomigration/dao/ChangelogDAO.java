package de.lise.mongomigration.dao;

import com.mongodb.client.MongoDatabase;
import de.lise.mongomigration.domain.ChangeSet;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;

@Document(collection = ChangelogDAO.COLLECTION_NAME)
public class ChangelogDAO {
    public static final String COLLECTION_NAME = "dbchangelog";
    public static final String KEY_CHANGEID = "changeId";
    public static final String KEY_AUTHOR = "author";

    @Id
    private String id;
    private String changeId;
    private String author;
    private String order;
    private Instant timestamp;
    private String changeLogClass;
    private String changeSetMethod;

    @Transient
    private Method method;


    public ChangelogDAO() {
    }


    public ChangelogDAO(Method method) {
        ChangeSet methodAnnotation = method.getAnnotation(ChangeSet.class);
        this.changeId = methodAnnotation.id();
        this.author = methodAnnotation.author();
        this.order = methodAnnotation.order();
        this.changeSetMethod = method.getName();
        this.changeLogClass = method.getClass().getName();
        this.timestamp = Instant.now();
        this.method = method;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getChangeId() {
        return changeId;
    }


    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }


    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }


    public Instant getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }


    public String getChangeLogClass() {
        return changeLogClass;
    }


    public void setChangeLogClass(String changeLogClass) {
        this.changeLogClass = changeLogClass;
    }


    public String getChangeSetMethod() {
        return changeSetMethod;
    }


    public void setChangeSetMethod(String changeSetMethod) {
        this.changeSetMethod = changeSetMethod;
    }


    public void executeMethod(MongoDatabase mongoDatabase)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        method.invoke(method.getClass().getDeclaredConstructor().newInstance(), mongoDatabase);
    }
}
