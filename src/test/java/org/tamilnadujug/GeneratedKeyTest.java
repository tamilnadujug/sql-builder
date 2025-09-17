package org.tamilnadujug;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PGobject;
import org.tamilnadujug.sql.Sql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class GeneratedKeyTest extends BaseTest {

    public static final SqlBuilder SQL_SINGLE =
            SqlBuilder.sql("INSERT INTO GK_TABLE VALUES (DEFAULT)");

    public static final SqlBuilder SQL_MULTI =
            SqlBuilder.sql("INSERT INTO GK_TABLE VALUES (DEFAULT),(DEFAULT)");

    // Json Test
    private static final String DEFAULT_VAL = "{\"key\": \"value\"}";

    @BeforeEach
    void init() throws SQLException {
        SqlBuilder.sql("DROP TABLE IF EXISTS GK_TABLE")
                .execute(dataSource);
    }

    @Test
    void testString() throws SQLException {
        testValue("'Ganesh'", "Ganesh", "VARCHAR(50)",
                SQL_SINGLE.queryGeneratedKeyForString(),
                SQL_MULTI.queryGeneratedKeysAsListOfString());
    }

    @Test
    void testInt() throws SQLException {
        testValue("100", 100, "INT",
                SQL_SINGLE.queryGeneratedKeyForInt(),
                SQL_MULTI.queryGeneratedKeysAsListOfInt());
    }

    @Test
    void testByte() throws SQLException {
        testValue("100", (byte) 100, "SMALLINT",
                SQL_SINGLE.queryGeneratedKeyForByte(),
                SQL_MULTI.queryGeneratedKeysAsListOfByte());
    }

    @Test
    void testShort() throws SQLException {
        testValue("100", (short) 100, "INT",
                SQL_SINGLE.queryGeneratedKeyForShort(),
                SQL_MULTI.queryGeneratedKeysAsListOfShort());
    }

    @Test
    void testLong() throws SQLException {
        testValue("9999999999", 9999999999L, "BIGINT",
                SQL_SINGLE.queryGeneratedKeyForLong(),
                SQL_MULTI.queryGeneratedKeysAsListOfLong());
    }

    @Test
    void testDouble() throws SQLException {
        testValue("123.45", 123.45, "DOUBLE PRECISION",
                SQL_SINGLE.queryGeneratedKeyForDouble(),
                SQL_MULTI.queryGeneratedKeysAsListOfDouble());
    }

    @Test
    void testFloat() throws SQLException {
        testValue("123.45", 123.45F, "DOUBLE PRECISION",
                SQL_SINGLE.queryGeneratedKeyForFloat(),
                SQL_MULTI.queryGeneratedKeysAsListOfFloat());
    }

    @Test
    void testBigDecimal() throws SQLException {
        testValue("12345.67", new BigDecimal("12345.67"), "DECIMAL(10,2)",
                SQL_SINGLE.queryGeneratedKeyForBigDecimal(),
                SQL_MULTI.queryGeneratedKeysAsListOfBigDecimal());
    }

    @Test
    void testBoolean() throws SQLException {
        testValue("TRUE", true, "BOOLEAN",
                SQL_SINGLE.queryGeneratedKeyForBoolean(),
                SQL_MULTI.queryGeneratedKeysAsListOfBoolean());
    }

    @Test
    void testDate() throws SQLException {
        Date expected = Date.valueOf("2025-09-16");
        testValue("'" + expected + "'", expected, "DATE",
                SQL_SINGLE.queryGeneratedKeyForDate(),
                SQL_MULTI.queryGeneratedKeysAsListOfDate());
    }

    @Test
    void testTime() throws SQLException {
        Time expected = Time.valueOf("12:34:56");
        testValue("'" + expected + "'", expected, "TIME",
                SQL_SINGLE.queryGeneratedKeyForTime(),
                SQL_MULTI.queryGeneratedKeysAsListOfTime());
    }

    @Test
    void testTimestamp() throws SQLException {
        Timestamp expected = Timestamp.valueOf("2025-09-16 12:34:56");
        testValue("'" + expected + "'", expected, "TIMESTAMP",
                SQL_SINGLE.queryGeneratedKeyForTimestamp(),
                SQL_MULTI.queryGeneratedKeysAsListOfTimestamp());
    }

    @Test
    void testJsonObject() throws SQLException {
        SqlBuilder.sql("CREATE TABLE GK_TABLE (val JSONB DEFAULT '%s'::jsonb);".formatted(DEFAULT_VAL))
                .execute(dataSource);

        var result = SQL_SINGLE.queryGeneratedKeyForObject()
                .execute(dataSource);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(DEFAULT_VAL, ((PGobject) result).getValue());

        var results =
                SQL_MULTI
                        .queryGeneratedKeysAsListOfObject()
                        .execute(dataSource);

        Assertions.assertEquals(2, results.size());
        Assertions.assertEquals(DEFAULT_VAL, ((PGobject) results.get(1)).getValue());
    }

    private <T> void testValue(
            String defaultValue,
            T value,
            String dataType,
            Sql<T> sql,
            Sql<List<T>> lSql
    ) throws SQLException {
        // Create table with default value
        SqlBuilder.sql("CREATE TABLE GK_TABLE(val " + dataType + " DEFAULT " + defaultValue + ")")
                .execute(dataSource);

        // Single insert check
        Assertions.assertEquals(value, sql.execute(dataSource));

        // Multi insert check (verify second element)
        List<T> results = lSql.execute(dataSource);
        Assertions.assertEquals(value, results.get(1));
    }
}
