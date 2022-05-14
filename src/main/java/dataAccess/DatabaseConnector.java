package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    private Connection connector;
    private Statement stmt;

    /**
     * Constructor of the class
     * Creates connection with the database
     */
    public DatabaseConnector() {
        try {
            new com.mysql.cj.jdbc.Driver();

            String connectionUrl = "jdbc:mysql://dif-mysql.ehu.es:3306/dbi28";
            String connectionUser = "DBI28";
            String connectionPass = "DBI28";
            connector = DriverManager.getConnection(connectionUrl,connectionUser,connectionPass);
            stmt = connector.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
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
     * @throws SQLException
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
