package org.tamilnadujug.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Maps and binds a parameter to a JDBC PreparedStatement.
 * Implementations set the value for a specific parameter index.
 */
public interface ParamMapper {

    /**
     * Binds a parameter value to the given PreparedStatement at the specified
     * index.
     *
     * @param ps    the PreparedStatement to bind to
     * @param index the parameter index (1-based)
     * @throws SQLException if binding fails or a database error occurs
     */
    void set(PreparedStatement ps, int index) throws SQLException;
}
