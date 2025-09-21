package org.tamilnaduju.sqlbuilder.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

        log.info("\n\n\nMovie Created {}\n\n\n", movie);
    }

}
