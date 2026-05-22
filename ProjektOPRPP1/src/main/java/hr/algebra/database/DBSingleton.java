package hr.algebra.database;
import hr.algebra.exceptions.DBConnectionException;
import hr.algebra.xml.AppConfig;
import hr.algebra.xml.ConfigReader;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DBSingleton {
    INSTANCE;

    private Connection connection;
    private final AppConfig CONFIG = ConfigReader.read(); //ovo ne moze bit static

    DBSingleton()  {
        try{
            this.connection = DriverManager.getConnection(
                    CONFIG.getDbUrl(),
                    CONFIG.getDbUsername(),
                    CONFIG.getDbPassword()
            );
        }
        catch (SQLException _){
            throw new DBConnectionException("Initialization hasn't gone as planned");
        }
    }

    public Connection getConnection() throws SQLException {
        if(connection==null || connection.isClosed()){ // da ako je konekcija zavtrena da se ponovno otvori
            this.connection= DriverManager.getConnection(
                    CONFIG.getDbUrl(),
                    CONFIG.getDbUsername(),
                    CONFIG.getDbPassword()
            );
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }
}
