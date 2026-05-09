package hr.algebra.database;
import hr.algebra.xml.AppConfig;
import hr.algebra.xml.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBSingleton {
    //ovaj se brine da postoji samo jedna koncekcija prema bazi jer je to skupa operacija
    //lazy jer se konekcija otvara tek kad ju netko zatrazi a ne kad se aplikacije pokrene


    private static DBSingleton INSTANCE;
    private Connection connection;

    private static final AppConfig CONFIG = ConfigReader.read();

    //ovo treba stavit u config.xml umjesto ovjde u kodu na otvorenom
    private static final String URL="jdbc:sqlserver://localhost:1433;databaseName=HeadphonesDB;encrypt=false;trustServerCertificate=true";
    private static final String USER ="sa";
    private static final String PASSWORD ="SQL";

    private DBSingleton() throws SQLException {
        this.connection = DriverManager.getConnection(
                CONFIG.getDbUrl(),
                CONFIG.getDbUsername(),
                CONFIG.getDbPassword());
    }

    public static DBSingleton getInstance() throws SQLException {
        if(INSTANCE == null || INSTANCE.connection.isClosed()){
            INSTANCE=new DBSingleton();
        }
        return INSTANCE;
    }
    //sto ako ima vise dretvi, sto nije da enum osigurava da je DBSingleton onda samo jedna instanca

    public Connection getConnection() {return connection;}

    public void closeConnection() throws SQLException {
        if(connection!=null && !connection.isClosed()){
            connection.close();
        }
    }
}
