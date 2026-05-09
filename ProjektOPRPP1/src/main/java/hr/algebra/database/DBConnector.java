package hr.algebra.database;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {
    //sluzi kao posrednik da ne zoves direktno singleton tako da ako se nesto mijenjat ne moras mijenjat na 100 mjesta

    private DBConnector(){}

    public static Connection getConnection(){
        try{
            return DBSingleton.getInstance().getConnection();
        }
        catch(SQLException e) {
            throw new RuntimeException("Cannot connect to database, try again later", e); // je li ovdje potrebno bacati novu gresku i jel se treba zapisat u logove
        }
    }
}
