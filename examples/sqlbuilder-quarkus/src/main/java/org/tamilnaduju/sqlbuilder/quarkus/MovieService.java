package org.tamilnaduju.sqlbuilder.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tamilnadujug.SqlBuilder;

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

