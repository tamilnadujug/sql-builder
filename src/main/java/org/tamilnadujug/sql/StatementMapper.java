package org.tamilnadujug.sql;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * StatementMapper is an interface that defines how to map statement
 * to a Java object.
 *
 * @param <T> the type of object to map the result set to
 */
@FunctionalInterface
public interface StatementMapper<T> {
    /**
     * Extracts a value from the provided {@link CallableStatement} after
     * execution, typically by reading OUT parameters.
     * @param statement the callable statement to read from
     * @return the mapped result value
     * @throws SQLException if reading from the statement fails
     */
    T get(CallableStatement statement) throws SQLException;
}
