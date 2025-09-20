package org.tamilnaduju.sqlbuilder.quarkus;

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
