# Quarkus SQL Builder Integration

This sample project demonstrates how to integrate **SQL Builder** with a Quarkus application.

## Setup

Add the following dependencies to your Quarkus project (**JDK 17+ required**):

### Maven

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jdbc-postgresql</artifactId>
</dependency>

<dependency>
    <groupId>org.tamilnadujug</groupId>
    <artifactId>sql-builder</artifactId>
    <version>${sql-builder.version}</version>
</dependency>
```

### Configuration

Set up your database connection in `src/main/resources/application.properties`:

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=sampledb
quarkus.datasource.password=sampledb
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/sampledb
```

### Example Service

```java
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

@ApplicationScoped
public class MovieService {

    @Inject
    DataSource dataSource;

    public Movie create(Movie movie) throws SQLException {
        return SqlBuilder
                .sql("INSERT INTO movie(title, directed_by) VALUES ('Interstellar', 'Nolan') RETURNING id, title, directed_by")
                .queryForOne(rs -> new Movie(
                        rs.getShort(1),
                        rs.getString(2),
                        rs.getString(3)))
                .execute(dataSource);
    }
}
```

### Example Application

```java
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@QuarkusMain
public class Application implements QuarkusApplication {

    private static final Logger log = Logger.getLogger(Application.class);

    @Inject
    MovieService movieService;

    public static void main(String... args) {
        Quarkus.run(Application.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        Movie movie = movieService.create(new Movie(null, "Titanic", "James Cameron"));
        log.info("Movie Created: " + movie);
        return 0;
    }
}
```