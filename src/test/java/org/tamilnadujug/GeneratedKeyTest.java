package org.tamilnadujug;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tamilnadujug.sql.Sql;

import java.sql.SQLException;
import java.util.List;

public class GeneratedKeyTest extends BaseTest {

    public static final SqlBuilder SQL_SINGLE = SqlBuilder.sql("INSERT INTO GK_TABLE VALUES (DEFAULT)");
    public static final SqlBuilder SQL_MULTI = SqlBuilder.sql("INSERT INTO GK_TABLE VALUES (DEFAULT),(DEFAULT)");

    @BeforeEach
    void inti() throws SQLException {
        SqlBuilder.sql("DROP TABLE IF EXISTS GK_TABLE")
                .execute(dataSource);
    }

    @Test
    void testString() throws SQLException {

        testValue("'Ganesh'", "Ganesh", "VARCHAR", SQL_SINGLE
                .queryGeneratedKeyForString(), SQL_MULTI
                .queryGeneratedKeysAsListOfString());


    }

    private <T>void testValue(String defaultValue, T value, String dataType, Sql<T> sql, Sql<List<T>> lSql) throws SQLException {
        SqlBuilder.sql("CREATE TABLE GK_TABLE(val "+dataType+ " DEFAULT "+defaultValue+")")
                .execute(dataSource);

        Assertions.assertEquals(value, sql
                .execute(dataSource));

        Assertions.assertEquals(value, lSql
                .execute(dataSource).get(1));
    }
}
