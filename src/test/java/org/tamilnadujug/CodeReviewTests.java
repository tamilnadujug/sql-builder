package org.tamilnadujug;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.tamilnadujug.sql.ParamMapper;
import org.tamilnadujug.sql.RowMapper;
import org.tamilnadujug.sql.Sql;
import org.tamilnadujug.sql.StatementMapper;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * SQL Builder should not depend on any external libraries or specific jdbc
 * implementation.
 */
class CodeReviewTests {
    @Test
    void architecture_rules() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(
                        new com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests())
                .importPackages("org.tamilnadujug");

        ArchRule rule = classes().that()
                .resideInAnyPackage("org.tamilnadujug")
                .should()
                .onlyAccessClassesThat()
                .belongToAnyOf(SQLFeatureNotSupportedException.class,
                        ResultSet.class,
                        DataSource.class,
                        Connection.class,
                        Statement.class,
                        CallableStatement.class,
                        PreparedStatement.class,
                        SQLException.class,
                        Function.class,
                        Throwable.class,
                        BigDecimal.class,
                        Sql.class,
                        Transaction.class,
                        RowMapper.class,
                        StatementMapper.class,
                        ParamMapper.class,
                        SqlBuilder.class,
                        Boolean.class,
                        Integer.class,
                        Double.class,
                        Float.class,
                        Boolean.class,
                        Long.class,
                        Short.class,
                        Byte.class,
                        Object.class,
                        ArrayList.class,
                        List.class,
                        Iterator.class,
                        HashMap.class,
                        Map.class,
                        Function.class,
                        UnsupportedOperationException.class);// see next section

        rule.check(importedClasses);
    }
}
