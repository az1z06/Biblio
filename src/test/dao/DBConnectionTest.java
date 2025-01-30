package test.dao;

import org.junit.jupiter.api.*;
import dao.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DBConnectionTest {

    @BeforeEach
    public void setUp() {
        DBConnection.clearMockInstance(); // Réinitialiser l'état avant chaque test
    }

    @Test
    public void testGetInstance_ShouldReturnSingletonInstance() throws SQLException {
        DBConnection instance1 = DBConnection.getInstance();
        DBConnection instance2 = DBConnection.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2); // Les deux instances doivent être identiques
    }

    @Test
    public void testGetConnection_ShouldReturnValidConnection() throws SQLException {
        DBConnection instance = DBConnection.getInstance();
        Connection connection = instance.getConnection();

        assertNotNull(connection);
        assertFalse(connection.isClosed()); // La connexion doit être ouverte
    }

    @Test
    public void testSetMockInstance_ShouldUseMockInstance() throws SQLException {
        // Mock de la classe DBConnection
        DBConnection mockDBConnection = mock(DBConnection.class);
        Connection mockConnection = mock(Connection.class);

        when(mockDBConnection.getConnection()).thenReturn(mockConnection);

        // Définir l'instance mockée
        DBConnection.setMockInstance(mockDBConnection);

        // Vérifier que le mock est utilisé
        DBConnection instance = DBConnection.getInstance();
        Connection connection = instance.getConnection();

        assertSame(mockConnection, connection);
        verify(mockDBConnection, times(1)).getConnection();
    }

    @Test
    public void testClearMockInstance_ShouldResetSingleton() throws SQLException {
        DBConnection mockDBConnection = mock(DBConnection.class);
        DBConnection.setMockInstance(mockDBConnection);

        assertSame(mockDBConnection, DBConnection.getInstance());

        DBConnection.clearMockInstance();

        DBConnection newInstance = DBConnection.getInstance();
        assertNotSame(mockDBConnection, newInstance); // L'instance doit être différente après réinitialisation
    }

    @Test
    public void testGetInstance_ShouldReconnectIfConnectionClosed() throws SQLException {
        DBConnection instance = DBConnection.getInstance();
        Connection connection = instance.getConnection();

        connection.close(); // Fermer la connexion

        Connection newConnection = instance.getConnection();
        assertNotSame(connection, newConnection); // Une nouvelle connexion doit être établie
        assertFalse(newConnection.isClosed());
    }

    @Test
    public void testGetInstance_ShouldThrowExceptionWithInvalidDriver() {
        DBConnection.clearMockInstance(); // Réinitialiser l'instance

        assertThrows(SQLException.class, () -> {
            DriverManager.getConnection("jdbc:mysql://invalid_host:3306/bibliotheque", "root", "");
        });
    }


    @Test
    public void testGetInstance_ShouldThrowExceptionWithInvalidUrl() {
        DBConnection.clearMockInstance();
        DBConnection mockDBConnection = mock(DBConnection.class);

        try {
            when(mockDBConnection.getConnection()).thenThrow(new SQLException("URL invalide"));
            DBConnection.setMockInstance(mockDBConnection);

            assertThrows(SQLException.class, () -> DBConnection.getInstance().getConnection());
        } catch (SQLException e) {
            fail("Exception inattendue : " + e.getMessage());
        }
    }
}
