package com.cs4485.sentencebuilder.model.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class responsible for managing the application's database connection.
 * This class implements the Singleton design pattern to ensure that only one
 * database connection instance is created and shared across the entire application.
 * It utilizes JDBC for database access.
 *
 * @author Daniel Dimitrov
 * 02/11/2026 - Initial creation
 */
public class DBConnection {
    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/cs4485";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // The sole instance of the singleton
    private static Connection connection = null;

    // Private constructor in order to prevent direct instantiation
    private DBConnection() {

    }

    // Getter to access singleton
    public static Connection getConnection() {
        try {
            // If there is no connection or has been closed, create a new one
            if (connection == null || connection.isClosed()) {
                // Establish connection and initiate singleton
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }
        return connection;
    }
}
