package hr.algebra.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hr.algebra.exceptions.InitializationException;
import hr.algebra.util.LoggingUtil;
import hr.algebra.xml.AppConfig;
import hr.algebra.xml.ConfigReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

//https://www.baeldung.com/hikaricp
public class DataSource {
    private static final HikariConfig hikariCOnfig = new  HikariConfig();
    private static HikariDataSource ds;
    private static AppConfig configSettings;

    static{
        try{
            configSettings = ConfigReader.read();
            hikariCOnfig.setJdbcUrl(configSettings.getDbUrl());
            hikariCOnfig.setUsername(configSettings.getDbUsername());
            hikariCOnfig.setPassword(configSettings.getDbPassword());
            ds = new HikariDataSource(hikariCOnfig);
            tryInitialize();

        }
        catch (Exception e){
            LoggingUtil.logError("Hikari static error", e);
        }

    }

    private DataSource(){}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
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
            LoggingUtil.logError("Command already executed", e);
        }
    }


    private static void initSchema() throws IOException, InitializationException, SQLException {

        var resourceStream = DataSource.class.getResourceAsStream("/hr/algebra/sql/init.sql");
        if (resourceStream == null) {
            throw new InitializationException("init.sql not found");
        }

        String ddl = new String(resourceStream.readAllBytes());

        if (ddl.startsWith("\uFEFF")) { // byte order mark, BOM
            ddl = ddl.substring(1);
        }

        try (Connection con = ds.getConnection();
                Statement statement =  con.createStatement()) {
            String[] statements = ddl.split("(?mi)^go\\s*$"); //da gleda samo "go" a ne kad je u sklopu druge rijeci, regex
            //multi-line mode, casesensitive, pocetak linije, prepoznavanje s " ", kraj linije odmah
            for (String sql : statements) {
                String trimmedSQL = sql.trim();
                if (!trimmedSQL.isEmpty()) {
                    statement.execute(trimmedSQL);
                }
            }
        }
        LoggingUtil.logInfo("Database schema initialized");
    }

    public static void closeConnection() {
        ds.close();
    }
}
