package test.dao;

import dao.DBConnection;
import dao.MembreDAO;
import model.Membre;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MembreDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private MembreDAO membreDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Mock DBConnection pour retourner la connexion fictive
        DBConnection mockDbConnection = mock(DBConnection.class);
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        DBConnection.setMockInstance(mockDbConnection);

        membreDAO = new MembreDAO();
    }

    @AfterEach
    void tearDown() {
       
    }

    @Test
    void testAjouterMembre() throws SQLException {
        // Arrange
        Membre membre = new Membre(0, "Test", "User", "test@example.com", new Date(System.currentTimeMillis()));
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatement);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(10);

        // Act
        boolean result = membreDAO.ajouterMembre(membre);

        // Assert
        assertTrue(result);
        assertEquals(10, membre.getID_Membre());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }


    @Test
    void testGetMembres() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenReturn(mock(Statement.class));
        Statement mockStatement = mock(Statement.class);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("ID_Membre")).thenReturn(1, 2);
        when(mockResultSet.getString("Nom")).thenReturn("Nom1", "Nom2");
        when(mockResultSet.getString("Prenom")).thenReturn("Prenom1", "Prenom2");
        when(mockResultSet.getString("Email")).thenReturn("email1@example.com", "email2@example.com");
        when(mockResultSet.getDate("Date_Inscription")).thenReturn(new Date(System.currentTimeMillis()));

        // Act
        List<Membre> membres = membreDAO.getMembres();

        // Assert
        assertEquals(2, membres.size());
        assertEquals("Nom1", membres.get(0).getNom());
        assertEquals("Prenom2", membres.get(1).getPrenom());
    }

    @Test
    void testRechercherMembres() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("ID_Membre")).thenReturn(1);
        when(mockResultSet.getString("Nom")).thenReturn("Test");
        when(mockResultSet.getString("Prenom")).thenReturn("User");
        when(mockResultSet.getString("Email")).thenReturn("test@example.com");
        when(mockResultSet.getDate("Date_Inscription")).thenReturn(new Date(System.currentTimeMillis()));

        // Act
        List<Membre> membres = membreDAO.rechercherMembres("Test");

        // Assert
        assertEquals(1, membres.size());
        assertEquals("Test", membres.get(0).getNom());
    }

    @Test
    void testMettreAJourMembre() throws SQLException {
        // Arrange
        Membre membre = new Membre(1, "Updated", "User", "updated@example.com", new Date(System.currentTimeMillis()));
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = membreDAO.mettreAJourMembre(membre);

        // Assert
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setString(1, "Updated");
    }

    @Test
    void testSupprimerMembre() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = membreDAO.supprimerMembre(1);

        // Assert
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    void testGetMembreById() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("ID_Membre")).thenReturn(1);
        when(mockResultSet.getString("Nom")).thenReturn("Nom Test");
        when(mockResultSet.getString("Prenom")).thenReturn("Prenom Test");
        when(mockResultSet.getString("Email")).thenReturn("test@example.com");
        when(mockResultSet.getDate("Date_Inscription")).thenReturn(new Date(System.currentTimeMillis()));

        // Act
        Membre membre = membreDAO.getMembreById(1);

        // Assert
        assertNotNull(membre);
        assertEquals("Nom Test", membre.getNom());
    }
}
