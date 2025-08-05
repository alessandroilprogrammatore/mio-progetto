package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Configurazione del database usando HikariCP.
 */
public class DatabaseConfig {
    private static HikariDataSource dataSource;

    private DatabaseConfig() {}

    /**
     * Restituisce una connessione al database.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            String url = System.getProperty("db.url", "jdbc:postgresql://localhost:5432/app");
            String user = System.getProperty("db.user", "postgres");
            String password = System.getProperty("db.password", "postgres");
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            dataSource = new HikariDataSource(config);
        }
        return dataSource.getConnection();
    }
}
