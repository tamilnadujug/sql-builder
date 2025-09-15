# SQL Builder

> **SQL Builder** is a lightweight alternative for **[Spring JDBC Client](https://www.baeldung.com/spring-6-jdbcclient-api)** and **[MyBatis](https://mybatis.org/mybatis-3/)** designed to provide the same essential functionality as these tools but with a focus on simplicity and readability. With SQL Builder, you can streamline your database operations without being tied down by unnecessary complexity.

## Why Use SQL Builder?

- **Simpler Fluent & Smart API:** Focused on readability, typesafe and minimal configuration.
- **Framework Independent:** It can be used in any Java framework ( [Quarkus](https://github.com/quarkusio/quarkus/discussions/38740), Spring Boot etc )
- **Lightweight:** with no third party dependencies

## Usage

Add dependency to your project (With **JDK 17+**)

### Maven
```xml
<dependency>
    <groupId>org.tamilnadujug</groupId>
    <artifactId>sql-builder</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
### Gradle
```groovy
implementation 'org.tamilnadujug:sql-builder:1.0-SNAPSHOT'
```

Thats all, You can now build and execute [Queries](#queries) , [Batch](#batch) , [Stored Procedures](#stored-procedures) and [Transactions](#transactions)

### Queries

For SQL Queries, You can use `SqlBuilder.sql` and `SqlBuilder.prepareSql` (for queries with parameters)

#### INSERT / UPDATE / DELETE

```java
int updateRows = SqlBuilder
    .sql("INSERT INTO movie(title, directed_by) VALUES ('Dunkirk', 'Nolan')")
    .execute(dataSource);
```

With Parameters

```java
int updateRows = SqlBuilder
    .prepareSql("INSERT INTO movie(title, directed_by) VALUES (?, ?)")
        .param("Dunkirk")
        .param("Nolan")
    .execute(dataSource);
```

To fetch generated key(s)
```java
long generatedId = SqlBuilder
    .prepareSql("INSERT INTO movie(title, directed_by) VALUES (?, ?)")
        .param("Interstellar")
        .param("Nolan")
    .queryGeneratedKeyForLong()
    .execute(dataSource);
```

#### SELECT
Fetch a single record,

```java
Movie movie = SqlBuilder
    .prepareSql("SELECT id, title, directed_by FROM movie WHERE id = ?")
        .param(generatedId)
    .queryForOne(this::mapRow)
    .execute(dataSource);
```
Fetch list of records,
```java
List<Movie> movies = SqlBuilder
    .prepareSql("SELECT id, title, directed_by from movie")
    .queryForList(this::mapRow)
    .execute(dataSource);
```

Check if the record exists
```java
boolean exists = SqlBuilder
    .prepareSql("SELECT 1 FROM movie WHERE id = ?")
        .param(generatedId)
    .queryForExists()
    .execute(dataSource);
```

### Batch

From SQL,

```java
int[] updatedRows = SqlBuilder
    .sql("INSERT INTO movie(title, directed_by) VALUES ('Interstellar', 'Nolan')")
        .addBatch("INSERT INTO movie(title, directed_by) VALUES ('Dunkrik', 'Nolan'),('Inception', 'Nolan')")
    .executeBatch(dataSource);
```

From Prepared SQL,

```java
int[] updatedRows = SqlBuilder
    .prepareSql("INSERT INTO movie(title, directed_by) VALUES (?, ?)")
        .param("Interstellar")
        .param("Nolan")
    .addBatch()
        .param("Dunkrik")
        .param("Nolan")
    .executeBatch(dataSource);
```

### Stored Procedures

with `IN` Parameters,

```java
SqlBuilder
    .prepareCall("CALL insert_movie_in(?, ?)")
        .param("Inception", Types.VARCHAR)
        .paramNull(Types.VARCHAR, "VARCHAR")
    .execute(dataSource);
```

with `OUT` Parameters,

```java
long id = SqlBuilder
    .prepareCall("{? = call insert_movie_fn(?, ?)}")
        .outParam(Types.BIGINT)
        .param("Inception")
        .param("Christopher Nolan")
    .queryOutParams(statement -> statement.getLong(1))
    .execute(dataSource);
```

with `INOUT` Parameters,

```java
String newTitle = SqlBuilder
    .prepareCall("CALL update_title_inout(?, ?)")
        .outParam(Types.BIGINT, id)
        .outParam(Types.VARCHAR, "Updated Title")
    .queryOutParams(statement -> statement.getString(2))
    .execute(dataSource);
```

with Batch,

```java
SqlBuilder
    .prepareCall("CALL insert_movie_in(?, ?)")
        .param("Inception", Types.VARCHAR)
        .paramNull(Types.VARCHAR, "VARCHAR")
    .addBatch()
        .param("Dunkrik")
        .param("Nolan")
    .addBatch()
        .param("Avatar")
        .param("Cameroon")
    .executeBatch(dataSource);
```

> **Note:** Batch for Stored procedures will only work with `IN` parameters—`OUT`/`INOUT` parameters are not batch-friendly.

### Transactions

```java
Transaction
    // Step 1: Insert director and return generated ID
    .begin(SqlBuilder.prepareSql("INSERT INTO director(name) VALUES (?)")
            .param("Christopher Nolan")
            .queryGeneratedKeys(rs -> rs.getLong(1)))
    // Step 2: Use directorId to fetch directorName
    .thenApply(directorId -> SqlBuilder
            .prepareSql("SELECT name FROM director WHERE id = ?")
            .param(directorId)
            .queryForString())
    // Step 3: Use directorName to insert movies
    .thenApply(directorName -> SqlBuilder
            .prepareSql("INSERT INTO movie(title, directed_by) VALUES (?, ?), (?, ?)")
            .param("Tenet").param(directorName)
            .param("Oppenheimer").param(directorName))
    // Execute as one transaction
    .execute(dataSource);
```

with Save Points, 

```java
Transaction
    // Step 1: Insert director and return generated ID
    .begin(SqlBuilder.prepareSql("INSERT INTO director(name) VALUES (?)")
            .param("Christopher Nolan")
            .queryGeneratedKeys(rs -> rs.getLong(1)))

    // Step 2: Use directorId to fetch directorName
    .thenApply(directorId -> SqlBuilder
            .prepareSql("SELECT name FROM director WHERE id = ?")
            .param(directorId)
            .queryForString())

    .savePoint("savepoint_nolan_additional_works", directorName -> Transaction
            .begin(SqlBuilder
               .prepareSql("INSERT INTO movie(title, directed_by) VALUES (?, ?), (?, ?)")
                    .param("Tenet")
                    .param(directorName)
                    .paramNull() // NOTNULL Error
                    .param(directorName))
            )
        
    // Execute as one transaction
    .execute(dataSource);
```