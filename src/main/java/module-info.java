/**
 * Module definition for sql.builder. Exports core SQL builder APIs and
 * requires standard Java modules used by the implementation.
 */
module sql.builder {
    requires java.base;
    requires java.sql;
    requires java.naming;

    exports org.tamilnadujug;
    exports org.tamilnadujug.sql;
}