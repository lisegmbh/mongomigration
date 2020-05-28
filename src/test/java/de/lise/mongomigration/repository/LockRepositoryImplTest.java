package de.lise.mongomigration.repository;

import com.mongodb.client.ListIndexesIterable;
import de.lise.mongomigration.AbstractMongoDBTest;
import de.lise.mongomigration.dao.LockDAO;
import org.assertj.core.api.Assertions;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

public class LockRepositoryImplTest extends AbstractMongoDBTest
{

	@Test
	public void acquireLock()
	{
		MongoTemplate template = getTemplateForDatabase( "test" );
		LockRepositoryImpl repository = new LockRepositoryImpl( template );

		boolean b = repository.acquireLock();

		Assertions.assertThat( b ).isTrue();
	}


	@Test
	public void acquireLock_failedTwice()
	{
		MongoTemplate template = getTemplateForDatabase( "test" );
		LockRepositoryImpl repository = new LockRepositoryImpl( template );

		boolean bFirst = repository.acquireLock();
		boolean bSecond = repository.acquireLock();

		Assertions.assertThat( bFirst ).isTrue();
		Assertions.assertThat( bSecond ).isFalse();
	}


	@Test
	public void releaseLock_successTwice()
	{
		MongoTemplate template = getTemplateForDatabase( "test" );
		LockRepositoryImpl repository = new LockRepositoryImpl( template );

		boolean bFirst = repository.acquireLock();
		repository.releaseLock();
		boolean bSecond = repository.acquireLock();

		Assertions.assertThat( bFirst ).isTrue();
		Assertions.assertThat( bSecond ).isTrue();
	}


	@Test
	public void lockDoesntExists()
	{
		MongoTemplate template = getTemplateForDatabase( "test" );
		LockRepositoryImpl repository = new LockRepositoryImpl( template );

		boolean lockExists = repository.lockExists();

		Assertions.assertThat( lockExists ).isFalse();
	}


	@Test
	public void lockExists()
	{
		MongoTemplate template = getTemplateForDatabase( "test" );
		LockRepositoryImpl repository = new LockRepositoryImpl( template );

		repository.acquireLock();
		boolean lockExists = repository.lockExists();

		Assertions.assertThat( lockExists ).isTrue();
	}


	@Test
	public void createIndex()
	{
		MongoTemplate template = getTemplateForDatabase( "test" );
		new LockRepositoryImpl( template );

		ListIndexesIterable<Document> indexes = template.getCollection( LockDAO.COLLECTION_NAME ).listIndexes();

		Assertions.assertThat( indexes )
				.isNotEmpty()
				.hasSize( 2 )
				.element( 1 )
				.matches( i -> i.getBoolean( "unique" ).equals( true ) );
	}
}
