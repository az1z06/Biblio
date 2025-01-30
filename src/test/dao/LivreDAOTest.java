package test.dao;

import dao.DBConnection;
import dao.LivreDAO;
import model.Livre;
import model.LivreNumerique;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LivreDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private LivreDAO livreDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Mock DBConnection pour retourner la connexion fictive
        DBConnection mockDbConnection = mock(DBConnection.class);
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        DBConnection.setMockInstance(mockDbConnection);

        livreDAO = new LivreDAO();
    }

    @AfterEach
    void tearDown() {
        
    }

    @Test
    void testAjouterLivre() throws SQLException {
        // Arrange
        Livre livre = new Livre(0, "Test Livre", 2022, "1234567890", 1, 2);
        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
            .thenReturn(mockPreparedStatement);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(10);

        // Act
        boolean result = livreDAO.ajouterLivre(livre);

        // Assert
        assertTrue(result);
        assertEquals(10, livre.getID_Livre());
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetLivres() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("ID_Livre")).thenReturn(1, 2);
        when(mockResultSet.getString("Titre")).thenReturn("Livre 1", "Livre 2");
        when(mockResultSet.getInt("Annee_Publication")).thenReturn(2020, 2021);
        when(mockResultSet.getString("ISBN")).thenReturn("123", "456");
        when(mockResultSet.getInt("ID_Editeur")).thenReturn(1, 2);
        when(mockResultSet.getInt("ID_Categorie")).thenReturn(3, 4);
        when(mockResultSet.getString("Type_Livre")).thenReturn("Physique", "Numerique");

        // Act
        List<Livre> livres = livreDAO.getLivres();

        // Assert
        assertEquals(2, livres.size());
        assertEquals("Livre 1", livres.get(0).getTitre());
        assertTrue(livres.get(1) instanceof LivreNumerique);
    }

    @Test
    void testRechercherLivres() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("ID_Livre")).thenReturn(1);
        when(mockResultSet.getString("Titre")).thenReturn("Livre Recherche");
        when(mockResultSet.getInt("Annee_Publication")).thenReturn(2022);
        when(mockResultSet.getString("ISBN")).thenReturn("999");
        when(mockResultSet.getInt("ID_Editeur")).thenReturn(2);
        when(mockResultSet.getInt("ID_Categorie")).thenReturn(3);

        // Act
        List<Livre> livres = livreDAO.rechercherLivres("Recherche");

        // Assert
        assertEquals(1, livres.size());
        assertEquals("Livre Recherche", livres.get(0).getTitre());
    }

   

    @Test
    void testMettreAJourLivre() throws SQLException {
        // Arrange
        Livre livre = new Livre(1, "Livre Modifié", 2022, "999", 2, 3);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = livreDAO.mettreAJourLivre(livre);

        // Assert
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setString(1, "Livre Modifié");
    }

    @Test
    void testSupprimerLivre() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = livreDAO.supprimerLivre(1);

        // Assert
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
    }

    @Test
    void testGetLivreById() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("ID_Livre")).thenReturn(1);
        when(mockResultSet.getString("Titre")).thenReturn("Livre By ID");
        when(mockResultSet.getInt("Annee_Publication")).thenReturn(2022);
        when(mockResultSet.getString("ISBN")).thenReturn("12345");
        when(mockResultSet.getInt("ID_Editeur")).thenReturn(1);
        when(mockResultSet.getInt("ID_Categorie")).thenReturn(2);

        // Act
        Livre livre = livreDAO.getLivreById(1);

        // Assert
        assertNotNull(livre);
        assertEquals("Livre By ID", livre.getTitre());
    }
}
