# Spring Boot SQL Builder Integration

This sample project demonstrates how to integrate **SQL Builder** with a Spring Boot application.

## Setup

Add the following dependencies to your Spring Boot project (**JDK 17+ required**):

### Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>org.tamilnadujug</groupId>
    <artifactId>sql-builder</artifactId>
    <version>${sql-builder.version}</version>
</dependency>

<!-- JDBC Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>${postgresql.version}</version>
</dependency>
```

### Configuration

Set up your database connection in `src/main/resources/application.yml`:

```yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/sampledb
    username: sampledb
    password: sampledb
```

### Example Service

```java
@Service
public class MovieService {

    private final DataSource dataSource;

    public MovieService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private MovieService movieService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Movie movie = movieService.create(new Movie(null, "Titanic", "James Cameron"));
        log.info("Movie Created: {}", movie);
    }
}
```
