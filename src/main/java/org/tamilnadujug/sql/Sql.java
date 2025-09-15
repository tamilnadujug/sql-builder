package org.tamilnadujug.sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a generic SQL operation that can be executed using a JDBC
 * Connection or DataSource. This interface allows you to define SQL
 * operations that return a result of type R.
 *
 * @param <R> the type of result returned by the SQL operation
 */
public interface Sql<R> {
    /**
     * Executes the SQL operation using the given JDBC connection.
     *
     * @param connection JDBC connection to use for execution
     * @return result of the SQL operation
     * @throws SQLException if an error occurs during execution
     */
    R execute(Connection connection) throws SQLException;

    /**
     * Executes the SQL operation using the given JDBC DataSource.
     *
     * @param dataSource JDBC DataSource to use for execution
     * @return result of the SQL operation
     * @throws SQLException if an error occurs during execution
     */
    default R execute(final DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return execute(conn);
        }
    }
}
