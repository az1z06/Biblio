package test.dao;

import command.BorrowCommand;
import command.Command;
import dao.DBConnection;
import dao.EmpruntDAO;
import dao.HistoriqueCommandeDAO;
import model.Emprunt;
import model.Livre;
import model.Membre;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import static org.mockito.Mockito.*;

class HistoriqueCommandeDAOTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private EmpruntDAO mockEmpruntDAO;

    @InjectMocks
    private HistoriqueCommandeDAO historiqueCommandeDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        // Mock DBConnection pour retourner une connexion fictive
        DBConnection mockDbConnection = mock(DBConnection.class);
        when(mockDbConnection.getConnection()).thenReturn(mockConnection);
        DBConnection.setMockInstance(mockDbConnection);

        historiqueCommandeDAO = new HistoriqueCommandeDAO(mockEmpruntDAO);
    }

    @AfterEach
    void tearDown() {
     
    }

    @Test
    void testAjouterHistorique() throws SQLException {
        // Arrange
        Emprunt emprunt = new Emprunt(1, 101, 201, new Date(System.currentTimeMillis()), null, null);
        Livre livre = new Livre();
        livre.setTitre("Test Livre");
        emprunt.setLivre(livre);

        Membre membre = new Membre();
        membre.setNom("Test Membre");
        emprunt.setMembre(membre);

        Command command = mock(BorrowCommand.class);
        when(command.getEmprunt()).thenReturn(emprunt);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        historiqueCommandeDAO.ajouterHistorique(command);

        // Assert
        verify(mockPreparedStatement, times(1)).setInt(1, emprunt.getID_Emprunt());
        verify(mockPreparedStatement, times(1)).setString(2, "Test Livre");
        verify(mockPreparedStatement, times(1)).setString(3, "Test Membre");
        verify(mockPreparedStatement, times(1)).setString(4, "EN_COURS");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testMettreAJourEtatCommande() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        historiqueCommandeDAO.mettreAJourEtatCommande(1, "TERMINEE");

        // Assert
        verify(mockPreparedStatement, times(1)).setString(1, "TERMINEE");
        verify(mockPreparedStatement, times(1)).setInt(2, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetCommandByIndex() throws SQLException {
        // Arrange
        Emprunt emprunt = new Emprunt(1, 101, 201, new Date(System.currentTimeMillis()), null, null);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("ID_Emprunt")).thenReturn(1);
        when(mockResultSet.getString("Etat_Commande")).thenReturn("EN_COURS");

        when(mockEmpruntDAO.getEmpruntById(1)).thenReturn(emprunt);

        // Act
        Command command = historiqueCommandeDAO.getCommandByIndex(0);

        // Assert
        Assertions.assertNotNull(command);
        Assertions.assertTrue(command instanceof BorrowCommand);
        verify(mockPreparedStatement, times(1)).setInt(1, 0);
        verify(mockPreparedStatement, times(1)).executeQuery();
    }
}
