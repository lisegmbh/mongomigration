package de.lise.mongomigration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.mongodb.core.MongoTemplate;

public abstract class AbstractMongoDBTest {
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    private MongodExecutable _mongodExe;
    private MongodProcess _mongod;

    private MongoClient _mongo;


    @BeforeEach
    protected void setUp() throws Exception {

        _mongodExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("localhost", 12345, Network.localhostIsIPv6()))
                .build());
        _mongod = _mongodExe.start();

        MongoClientSettings options = MongoClientSettings
                .builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:12345"))
                .build();

        _mongo = MongoClients.create(options);
    }


    @AfterEach
    protected void tearDown() throws Exception {
        _mongod.stop();
        _mongodExe.stop();
    }


    public MongoClient getMongo() {
        return _mongo;
    }


    public MongoTemplate getTemplateForDatabase(String database) {
        return new MongoTemplate(getMongo(), database);
    }

}
