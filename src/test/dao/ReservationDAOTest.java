package test.dao;

import dao.DBConnection;
import dao.ReservationDAO;
import model.Reservation;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private ReservationDAO reservationDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Mock DBConnection pour retourner la connexion fictive
        DBConnection mockDbConnection = mock(DBConnection.class);
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        DBConnection.setMockInstance(mockDbConnection);

        reservationDAO = new ReservationDAO();
    }

    @AfterEach
    void tearDown() {
    
    }

    @Test
    void testReserverLivre() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = reservationDAO.reserverLivre(1, 2);

        // Assert
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 2);
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).setDate(eq(3), any(java.sql.Date.class));
    }

    @Test
    void testSupprimerReservation() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = reservationDAO.supprimerReservation(2, 1);

        // Assert
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 2);
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
    }

    @Test
    void testGetReservations() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenReturn(mock(Statement.class));
        Statement mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("ID_Livre")).thenReturn(1, 2);
        when(mockResultSet.getInt("ID_Membre")).thenReturn(3, 4);
        when(mockResultSet.getDate("Date_Reservation")).thenReturn(new Date(System.currentTimeMillis()));

        // Act
        List<Reservation> reservations = reservationDAO.getReservations();

        // Assert
        assertEquals(2, reservations.size());
        assertEquals(1, reservations.get(0).getID_Livre());
        assertEquals(4, reservations.get(1).getID_Membre());
    }

    @Test
    void testRechercherReservations() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("ID_Livre")).thenReturn(1);
        when(mockResultSet.getInt("ID_Membre")).thenReturn(3);
        when(mockResultSet.getDate("Date_Reservation")).thenReturn(new Date(System.currentTimeMillis()));

        // Act
        List<Reservation> reservations = reservationDAO.rechercherReservations("Test");

        // Assert
        assertEquals(1, reservations.size());
        assertEquals(1, reservations.get(0).getID_Livre());
        assertEquals(3, reservations.get(0).getID_Membre());
    }
}
