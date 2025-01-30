package test.dao;

import dao.DBConnection;
import dao.EmpruntDAO;
import model.Emprunt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmpruntDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private EmpruntDAO empruntDAO;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Mock DBConnection pour retourner la connexion simulée
        DBConnection mockDBConnection = mock(DBConnection.class);
        when(mockDBConnection.getConnection()).thenReturn(mockConnection);
        DBConnection.setMockInstance(mockDBConnection);

        empruntDAO = new EmpruntDAO();
    }

    @Test
    public void testAjouterEmprunt_Success() throws SQLException {
        // Configuration du mock
        when(mockConnection.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS)))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(123);

        // Appel de la méthode
        Emprunt emprunt = new Emprunt(0, 1, 2, Date.valueOf("2023-01-01"), Date.valueOf("2023-01-15"), null);
        boolean result = empruntDAO.ajouterEmprunt(emprunt);

        // Vérifications
        assertTrue(result);
        assertEquals(123, emprunt.getID_Emprunt());
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockPreparedStatement, times(1)).getGeneratedKeys();
    }

    @Test
    public void testGetEmprunts_Success() throws SQLException {
        // Configuration du mock
        when(mockConnection.createStatement()).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // 2 résultats
        when(mockResultSet.getInt("ID_Emprunt")).thenReturn(1, 2);
        when(mockResultSet.getInt("ID_Livre")).thenReturn(101, 102);
        when(mockResultSet.getInt("ID_Membre")).thenReturn(201, 202);
        when(mockResultSet.getDate("Date_Emprunt")).thenReturn(Date.valueOf("2023-01-01"), Date.valueOf("2023-01-02"));
        when(mockResultSet.getDate("Date_Retour_Prevue")).thenReturn(Date.valueOf("2023-01-15"), Date.valueOf("2023-01-16"));
        when(mockResultSet.getString("Nom_Livre")).thenReturn("Livre 1", "Livre 2");
        when(mockResultSet.getString("Nom_Membre")).thenReturn("Membre 1", "Membre 2");

        // Appel de la méthode
        List<Emprunt> emprunts = empruntDAO.getEmprunts();

        // Vérifications
        assertNotNull(emprunts);
        assertEquals(2, emprunts.size());
        assertEquals("Livre 1", emprunts.get(0).getLivre().getTitre());
        assertEquals("Membre 2", emprunts.get(1).getMembre().getNom());

        verify(mockPreparedStatement, times(1)).executeQuery(anyString());
    }

    @Test
    public void testModifierEmprunt_Success() throws SQLException {
        // Configuration du mock
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Appel de la méthode
        Emprunt emprunt = new Emprunt(1, 101, 201, Date.valueOf("2023-01-01"), Date.valueOf("2023-01-15"), null);
        boolean result = empruntDAO.modifierEmprunt(emprunt);

        // Vérifications
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testSupprimerEmprunt_Success() throws SQLException {
        // Configuration du mock
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Appel de la méthode
        boolean result = empruntDAO.supprimerEmprunt(1);

        // Vérifications
        assertTrue(result);
        verify(mockPreparedStatement, times(1)).setInt(1, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testRechercherEmprunts_Success() throws SQLException {
        // Configuration du mock
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false); // 1 résultat
        when(mockResultSet.getInt("ID_Emprunt")).thenReturn(1);
        when(mockResultSet.getInt("ID_Livre")).thenReturn(101);
        when(mockResultSet.getInt("ID_Membre")).thenReturn(201);
        when(mockResultSet.getDate("Date_Emprunt")).thenReturn(Date.valueOf("2023-01-01"));
        when(mockResultSet.getDate("Date_Retour_Prevue")).thenReturn(Date.valueOf("2023-01-15"));
        when(mockResultSet.getString("Nom_Livre")).thenReturn("Livre 1");
        when(mockResultSet.getString("Nom_Membre")).thenReturn("Membre 1");

        // Appel de la méthode
        List<Emprunt> emprunts = empruntDAO.rechercherEmprunts("Livre 1");

        // Vérifications
        assertNotNull(emprunts);
        assertEquals(1, emprunts.size());
        assertEquals("Livre 1", emprunts.get(0).getLivre().getTitre());

        verify(mockPreparedStatement, times(1)).executeQuery();
    }
}
