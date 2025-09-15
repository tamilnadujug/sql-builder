package org.tamilnadujug;

import org.tamilnadujug.sql.Sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.function.Function;

/**
 * Represents a database transaction as a composable unit of work.
 * Implements the {@link Sql} interface, allowing execution of SQL
 * operations within a transactional context. You can chain multiple SQL
 * operations together, passing the output of one as input to the next.
 *
 * <p>Usage Example:</p>
 * <pre>
 * Transaction tx = Transaction.begin(insertUserSql)
 *     .thenApply(userId -> updateAccountSql(userId))
 *     .thenApply(accountId -> fetchDetailsSql(accountId));
 * tx.execute(dataSource);
 * </pre>
 *
 * <p>Transaction Management:</p>
 * <ul>
 *   <li>Disables auto-commit before execution</li>
 *   <li>Executes the SQL chain atomically</li>
 *   <li>Commits after successful execution</li>
 *   <li>Caller handles rollback on exceptions</li>
 *   <li>Restores auto-commit at the end</li>
 * </ul>
 *
 * @param <T> the result type of the SQL operation at the current stage
 */
public class Transaction<T> implements Sql<T> {

    /**
     * The SQL operation to be executed in this transaction stage.
     */
    private final Sql<T> sql;

    /**
     * Constructs a transaction stage with the given SQL operation.
     *
     * @param theSql the SQL operation to be executed
     */
    protected Transaction(final Sql<T> theSql) {
        this.sql = theSql;
    }

    /**
     * Begins a new transaction with the initial SQL operation.
     *
     * @param sql the first SQL operation
     * @param <T> the result type of the SQL operation
     * @return a new {@code Transaction} instance
     */
    public static <T> Transaction<T> begin(final Sql<T> sql) {
        return new Transaction<>(sql);
    }

    /**
     * Chains another SQL operation to be executed after this transaction
     * stage.
     *
     * <p>The result of the current SQL operation is passed into the
     * provided function, which returns the next SQL operation to execute.</p>
     *
     * @param tSqlFunction function mapping the result of this SQL to next SQL
     * @param <R>          the result type of the next SQL operation
     * @return a new {@code Transaction} instance
     */
    public <R> Transaction<R> thenApply(
            final Function<T, Sql<R>> tSqlFunction) {
        return new Transaction<>(connection -> {
            T t = sql.execute(connection);
            return tSqlFunction.apply(t).execute(connection);
        });
    }

    /**
     * Executes the transaction on the given database connection.
     *
    * <p>Manages transaction boundaries automatically:</p>
    * <ul>
    *   <li>Disables auto-commit before execution</li>
    *   <li>Commits the transaction upon success</li>
    *   <li>Restores auto-commit afterward</li>
    * </ul>
     *
     * @param connection the database connection
     * @return the result of the SQL execution
     * @throws SQLException if a database error occurs
     */
    @Override
    public T execute(final Connection connection) throws SQLException {
        connection.setAutoCommit(false);
        T t = sql.execute(connection);
        connection.commit();
        connection.setAutoCommit(true);
        return t;
    }

    /**
     * Creates a savepoint within the current transaction.
     * <p>
     * Savepoints allow you to mark a specific point in a transaction that you
     * can roll back to later without affecting the entire transaction. This is
     * useful when you want to group a set of operations together, but still
     * have the option to undo them if needed, while keeping earlier successful
     * operations intact.
     * </p>
     *
     * <pre>
     * Transaction
     *     .begin(...)
     *     .savePoint("sp1")
     *     .thenApply(...)
     *     .rollBackTo("sp1")
     *     .execute(dataSource);
     * </pre>
     *
     * @param savePointId   a unique identifier for the savepoint
     * @param transactionFn function mapping the result of this Transaction to
     *                      next Transaction
     * @param <R>           the result type of the next SQL operation
     * @return the current {@link Transaction} instance for fluent chaining
     */
    public <R> Transaction<R> savePoint(final String savePointId,
                                        final Function<T,
                                        Transaction<R>> transactionFn) {
        return new Transaction<>(connection -> {
            T t = sql.execute(connection);
            Savepoint savepoint = connection.setSavepoint(savePointId);
            R r = null;
            try {
                r = transactionFn.apply(t).sql.execute(connection);
            } catch (SQLException sqlException) {
                connection.rollback(savepoint);
            }
            return r;
        });
    }

}
