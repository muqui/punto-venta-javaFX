/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexionMYSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mq12
 */
public class ConnectionMYSQLManager {
     private static String url = "jdbc:mysql://localhost:3306/muquiventas2018";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String username = "root";
    private static String password = "Fedora12";
    private static Connection con;
   

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection." +  ex);
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        return con;
    }
}
