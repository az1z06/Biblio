package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private static DBConnection mockInstance; // Ajout pour les tests

    private static final String DB_URL = "jdbc:mysql://localhost:3306/bibliotheque";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Constructeur privé pour le Singleton
    private DBConnection() throws SQLException {
        connect();
    }

    // Méthode pour établir une connexion
    private void connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Explicitly load MySQL driver
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Impossible de se connecter à la base de données.", e);
        }
    }


    // Retourne l'instance unique de DBConnection
    public static synchronized DBConnection getInstance() throws SQLException {
        if (mockInstance != null) {
            return mockInstance; // Retourne le mock si défini
        }
        if (instance == null) {
            instance = new DBConnection();
        } else if (instance.getConnection() == null || instance.getConnection().isClosed()) {
            instance.connect(); // Réinitialise la connexion si elle est fermée
        }
        return instance;
    }


    // Retourne la connexion
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect(); // Rétablit la connexion si elle est fermée
        }
        return connection;
    }
    
    public static void setMockInstance(DBConnection mockInstance) {
        DBConnection.mockInstance = mockInstance;
    }
 // Réinitialiser l'instance mockée
    public static void clearMockInstance() {
        DBConnection.mockInstance = null;
        DBConnection.instance = null; // Pour garantir un nouvel état propre
    }




}
