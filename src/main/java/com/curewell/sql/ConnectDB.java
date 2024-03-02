package com.curewell.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {
    private final String databaseName = "curewell";
    private final String databaseUser = "root";
    private final String databasePassword = "";
<<<<<<< HEAD
    private final String url = "jdbc:mysql://localhost:3306/" + databaseName + "?zeroDateTimeBehavior=convertToNull";
=======
    private final String url = "jdbc:mysql://localhost:3307/" + databaseName + "?zeroDateTimeBehavior=convertToNull";
>>>>>>> b547669e9a8f206a73500ebd829b30f4e580c5c0

    public Connection conn;
    public Statement statement;

    public ConnectDB() throws SQLException, ClassNotFoundException {
        //try {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, databaseUser, databasePassword);

            statement = conn.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    public Statement getStatement() {
        return statement;
    }
}
