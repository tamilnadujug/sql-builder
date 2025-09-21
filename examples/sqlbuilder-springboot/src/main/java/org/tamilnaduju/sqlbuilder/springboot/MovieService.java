package org.tamilnaduju.sqlbuilder.springboot;

import org.springframework.stereotype.Service;
import org.tamilnadujug.SqlBuilder;

import javax.sql.DataSource;
import java.sql.SQLException;

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
