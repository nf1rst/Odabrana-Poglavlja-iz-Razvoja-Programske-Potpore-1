package hr.algebra.database;
import hr.algebra.exceptions.DBConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {
    //sluzi kao posrednik da ne zoves direktno singleton tako da ako se nesto mijenjat ne moras mijenjat na 100 mjesta

    private DBConnector(){}

    public static Connection getConnection(){
        try{
            return DBSingleton.INSTANCE.getConnection();
        }
        catch(SQLException _) {
            throw new DBConnectionException("DBConnector failed");// je li ovdje potrebno bacati novu gresku i jel se treba zapisat u logove
        }
    }
}
