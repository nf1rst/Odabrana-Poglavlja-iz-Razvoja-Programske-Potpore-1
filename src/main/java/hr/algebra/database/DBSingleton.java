package hr.algebra.database;
import hr.algebra.exceptions.InitializationException;
import hr.algebra.util.LoggingUtil;
import hr.algebra.xml.AppConfig;
import hr.algebra.xml.ConfigReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public enum DBSingleton {
    INSTANCE;

    private static Connection connection;
    private static AppConfig config;  // static varijabla za config

    static {
        try {
            config = ConfigReader.read();

            connection = DriverManager.getConnection(
                    config.getDbUrl(),
                    config.getDbUsername(),
                    config.getDbPassword()
            );

            tryInitialize();
            LoggingUtil.logInfo("Database initialized successfully");

        } catch (Exception e) {
            LoggingUtil.logError("DB initialization error - tables already exist", e);
        }
    }

    private static void tryInitialize() {
        try{
            initSchema();
        }
        catch (InitializationException e) {
            LoggingUtil.logError("Script not found", e);
        }
        catch (IOException e) {
            LoggingUtil.logError("DB already initialized", e); //probact ce create table i nece moc ako vidi da tablice vec postoje
        }
        catch (SQLException e){
            LoggingUtil.logError("Procedure not found", e);
        }
    }

    private DBSingleton() {}

    private static void initSchema() throws IOException, InitializationException, SQLException {

        var resourceStream = DBSingleton.class.getResourceAsStream("/hr/algebra/sql/init.sql");
        if (resourceStream == null) {
            throw new InitializationException("init.sql not found");
        }

        String ddl = new String(resourceStream.readAllBytes());

        if (ddl.startsWith("\uFEFF")) { // da ako pocinje s razmakom da se ne srusi
            ddl = ddl.substring(1);
        }

        try (Statement statement = connection.createStatement()) {
            String[] statements = ddl.split("(?mi)^go\\s*$"); //da gleda samo "go" a ne kad je u sklopu druge rijeci

            for (String sql : statements) {
                String trimmedSQL = sql.trim();
                if (!trimmedSQL.isEmpty()) {
                    statement.execute(trimmedSQL);
                }
            }
        }

        LoggingUtil.logInfo("Database schema initialized");
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    config.getDbUrl(),
                    config.getDbUsername(),
                    config.getDbPassword()
            );
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            LoggingUtil.logInfo("Database connection closed");
        }
    }
}