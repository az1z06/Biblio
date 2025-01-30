package test.dao;

import dao.DBConnection;
import dao.EditeurDAO;
import model.Editeur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EditeurDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private EditeurDAO editeurDAO;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Mock DBConnection to return the mocked Connection
        DBConnection mockDBConnection = mock(DBConnection.class);
        when(mockDBConnection.getConnection()).thenReturn(mockConnection);
        DBConnection.setMockInstance(mockDBConnection);

        editeurDAO = new EditeurDAO();
    }

    @Test
    public void testGetAllEditeurs_Success() throws SQLException {
        // Configure mock result set
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // 2 rows
        when(mockResultSet.getInt("ID_Editeur")).thenReturn(1, 2);
        when(mockResultSet.getString("Nom")).thenReturn("Editeur 1", "Editeur 2");
        when(mockResultSet.getString("Adresse")).thenReturn("Adresse 1", "Adresse 2");

        // Appeler la méthode
        List<Editeur> editeurs = editeurDAO.getAllEditeurs();

        // Vérifier les résultats
        assertNotNull(editeurs);
        assertEquals(2, editeurs.size());
        assertEquals("Editeur 1", editeurs.get(0).getNom());
        assertEquals("Adresse 2", editeurs.get(1).getAdresse());

        // Vérifications Mockito
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(3)).next();
    }

    @Test
    public void testAjouterEditeur_Success() throws SQLException {
        // Mock PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Appeler la méthode
        Editeur editeur = new Editeur(0, "Editeur Test", "Adresse Test");
        boolean result = editeurDAO.ajouterEditeur(editeur);

        // Vérifications
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setString(1, "Editeur Test");
        verify(mockPreparedStatement, times(1)).setString(2, "Adresse Test");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testModifierEditeur_Success() throws SQLException {
        // Mock PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Appeler la méthode
        Editeur editeur = new Editeur(1, "Editeur Modifié", "Nouvelle Adresse");
        boolean result = editeurDAO.modifierEditeur(editeur);

        // Vérifications
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setString(1, "Editeur Modifié");
        verify(mockPreparedStatement, times(1)).setString(2, "Nouvelle Adresse");
        verify(mockPreparedStatement, times(1)).setInt(3, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testSupprimerEditeur_Success() throws SQLException {
        // Mock PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Appeler la méthode
        boolean result = editeurDAO.supprimerEditeur(1);

        // Vérifications
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetAllEditeurNames_Success() throws SQLException {
        // Mock result set
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // 2 rows
        when(mockResultSet.getInt("ID_Editeur")).thenReturn(1, 2);
        when(mockResultSet.getString("Nom")).thenReturn("Editeur 1", "Editeur 2");

        // Appeler la méthode
        List<String> editeurNames = editeurDAO.getAllEditeurNames();

        // Vérifications
        assertNotNull(editeurNames);
        assertEquals(2, editeurNames.size());
        assertEquals("1 - Editeur 1", editeurNames.get(0));
        assertEquals("2 - Editeur 2", editeurNames.get(1));

        // Vérifications Mockito
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(3)).next();
    }
}
