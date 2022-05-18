package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This class aims to represent the database connector
 *
 * @author Miren, Leire and Amanda
 * @version 1
 */
public class DatabaseConnector {

    private Connection connector;
    private Statement stmt;

    /**
     * Constructor of the class
     * Creates a connection with the database
     */
    public DatabaseConnector() {
        try {
            new com.mysql.cj.jdbc.Driver();

            String connectionUrl = "jdbc:mysql://dif-mysql.ehu.es:3306/dbi24";
            String connectionUser = "DBI24";
            String connectionPass = "DBI24";
            connector = DriverManager.getConnection(connectionUrl,connectionUser,connectionPass);
            stmt = connector.createStatement();

        } catch (SQLException e) {
            System.out.println("An error connecting to the database occurred. Please, try again later.");

        }
    }

    /**
     * Method to obtain the connector of the database
     * @return Connection - Connection instance of the database
     */
    public Connection getConnector(){
        return this.connector;
    }

    /**
     * Method to obtain a statement of the database
     * @return Statement - Statement created
     */
    public Statement getStatement(){
        return this.stmt;
    }

    /**
     * Closes the connection and the statement created
     * @throws SQLException if an exception is thrown in the closing
     */
    public void close() throws SQLException {
        if(connector != null){
            connector.close();
        }
        if(stmt != null){
            stmt.close();
        }
    }


}
