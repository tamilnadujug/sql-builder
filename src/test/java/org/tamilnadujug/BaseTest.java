package org.tamilnadujug;

import org.junit.jupiter.api.BeforeEach;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Basic Setup for Test cases using PostgreSQL.
 */
class BaseTest {

    protected static PGSimpleDataSource dataSource;

    static {
        dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://localhost:5432/sampledb");
        dataSource.setUser("sampledb");
        dataSource.setPassword("sampledb");
    }

    public static Movie mapMovie(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getShort(1),
                rs.getString(2),
                rs.getString(3));
    }

    @BeforeEach
    void beforeEach() throws SQLException {
        SqlBuilder.prepareSql("TRUNCATE TABLE movie RESTART IDENTITY CASCADE")
                .execute(dataSource);
    }
}
