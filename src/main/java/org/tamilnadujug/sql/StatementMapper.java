package org.tamilnadujug.sql;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Maps a JDBC CallableStatement to a Java object of type T.
 *
 * @param <T> the type of object to map from the statement
 */
@FunctionalInterface
public interface StatementMapper<T> {
    /**
     * Extracts a value from the given CallableStatement.
     *
     * @param statement the CallableStatement to extract from
     * @return mapped result object
     * @throws SQLException if an error occurs while extracting
     */
    T get(CallableStatement statement) throws SQLException;
}
