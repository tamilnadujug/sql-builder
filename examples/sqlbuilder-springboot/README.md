# Spring Boot SQL Builder Integration

Sample project that demonstrates how to use SQL Builder with Spring Boot Application

## Setup

Add below dependencies, to your spring boot project (With **JDK 17+**)

### Maven
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>org.tamilnadujug</groupId>
    <artifactId>sql-builder</artifactId>
    <version>1.0</version>
</dependency>

        <!-- JDBC Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.7</version>
</dependency>
```

Configure Spring Boot Application to use your database at `src/main/resources/application.yml`

```yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/sampledb
    username: sampledb
    password: sampledb
```

Autowire DataSource to your Spring Bean

```java
@Service
public class MovieService {

    private final DataSource dataSource;

    public MovieService(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Movie create(final Movie movie) throws SQLException {
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

You can now talk to the database 

```java
@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Autowired
    MovieService movieService;

    @Override
    public void run(String... strings) throws Exception {
        Movie movie = movieService.create(new Movie(null, "Titanic", "James Cameron"));

        log.info("Movie Created {}", movie);
    }

}
```