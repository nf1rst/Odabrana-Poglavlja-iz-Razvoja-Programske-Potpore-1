package hr.algebra.database;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {
    //sluzi kao posrednik da se ne zove direktno singleton tako da se mora mijenjat poziv samo na 1 mjestu

    private DBConnector(){}

    public static Connection getConnection() throws SQLException {
            return DBSingleton.INSTANCE.getConnection(); //ovog tu se uhvatit repo koji onda propagira SQL exception do servisa koji ga pretvara u custom ex za controller

    }
}
