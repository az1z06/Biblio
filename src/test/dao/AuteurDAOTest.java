package test.dao;

import model.Auteur;
import org.junit.jupiter.api.*;
import org.mockito.Mock;

import dao.AuteurDAO;
import dao.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuteurDAOTest {

    private AuteurDAO auteurDAO;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeAll
    public void setup() {
        auteurDAO = new AuteurDAO();
    }

    
    @BeforeEach
    public void setupMocks() throws Exception {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);

        // Mock DBConnection.getInstance().getConnection()
        DBConnection dbConnection = mock(DBConnection.class);
        when(dbConnection.getConnection()).thenReturn(mockConnection);
        DBConnection.setMockInstance(dbConnection);
    }

    @Test
    public void testAjouterAuteur() throws Exception {
        Auteur auteur = new Auteur(0, "Hugo", "Victor", Date.valueOf("1802-02-26"));

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = auteurDAO.ajouterAuteur(auteur);

        assertTrue(result);
        verify(mockPreparedStatement).setString(1, "Hugo");
        verify(mockPreparedStatement).setString(2, "Victor");
        verify(mockPreparedStatement).setDate(3, Date.valueOf("1802-02-26"));
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetAuteurs() throws Exception {
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // Une seule ligne de résultat
        when(mockResultSet.getInt("ID_Auteur")).thenReturn(1);
        when(mockResultSet.getString("Nom")).thenReturn("Hugo");
        when(mockResultSet.getString("Prenom")).thenReturn("Victor");
        when(mockResultSet.getDate("Date_Naissance")).thenReturn(Date.valueOf("1802-02-26"));

        List<Auteur> auteurs = auteurDAO.getAuteurs();

        assertEquals(1, auteurs.size());
        Auteur auteur = auteurs.get(0);
        assertEquals(1, auteur.getID_Auteur());
        assertEquals("Hugo", auteur.getNom());
        assertEquals("Victor", auteur.getPrenom());
        assertEquals(Date.valueOf("1802-02-26"), auteur.getDate_Naissance());

        verify(mockStatement).executeQuery("SELECT * FROM auteur");
    }

    
    @Test
    public void testMettreAJourAuteur() throws Exception {
        Auteur auteur = new Auteur(1, "Hugo", "Victor", Date.valueOf("1802-02-26"));

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = auteurDAO.mettreAJourAuteur(auteur);

        assertTrue(result);
        verify(mockPreparedStatement).setString(1, "Hugo");
        verify(mockPreparedStatement).setString(2, "Victor");
        verify(mockPreparedStatement).setDate(3, Date.valueOf("1802-02-26"));
        verify(mockPreparedStatement).setInt(4, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testSupprimerAuteur() throws Exception {
        int idAuteur = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = auteurDAO.supprimerAuteur(idAuteur);

        assertTrue(result);
        verify(mockPreparedStatement).setInt(1, idAuteur);
        verify(mockPreparedStatement).executeUpdate();
    }

    @AfterEach
    public void tearDownMocks() throws Exception {
        reset(mockConnection, mockPreparedStatement, mockStatement, mockResultSet);
    }
}
