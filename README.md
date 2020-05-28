# mongomigration
Mongomigration help you to manipulate your MongoDB database during your application starts.


## Current status & how to contribute
It is partially compatible to MongoBeeJ. 
So older migrations will be detect and not executed twice.

**But attention!!** The `order` property in `@ChangeSet` annotation ist not yet implemented 
because in our current use cases it is not necessary.
Feel free to contribute this functionality if you need it :)


## Getting startet
### Add a dependency
With Maven
```
<dependency>
  <groupId>com.github.lisegmbh</groupId>
  <artifactId>mongomigration</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Usage with Spring
You need to instantiate `Mongomigration` object and provide some configuration. 
If you use Spring can be instantiated as a singleton bean in the Spring context. 
In this case the migration process will be executed automatically on startup.
```
@Bean
public Mongomigration mongomigration( MongoTemplate mongoTemplate )
{
    Mongomigration runner = new Mongomigration( mongoTemplate );
    runner.setChangeLogsScanPackage( "com.example.yourapp.changelogs" );
    return runner;
}
```

### Creating change logs
`ChangeLog` contains bunch of `ChangeSet`s. 
`ChangeSet` is a single task (set of instructions made on a database). 
In other words `ChangeLog` is a class annotated with `@ChangeLog` and 
containing methods annotated with `@ChangeSet`.
```
package com.example.yourapp.changelogs;

@ChangeLog
public class DatabaseChangelog {
  
  @ChangeSet(id = "someChangeId", author = "testAuthor")
  public void importantWorkToDo(MongoDatabase db){
     // task implementation
  }
}
```