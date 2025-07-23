// src/main/java/com/example/sistemanomina/db/DatabaseConnection.java
package com.example.sistemanomina.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Cambia estos valores según tu configuración
    private final String url = "jdbc:mysql://localhost:3306/db_nomina";
    private final String username = "usuario_nomina";
    private final String password = "clave_nomina";

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("MySQL JDBC Driver no encontrado.", ex);
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
