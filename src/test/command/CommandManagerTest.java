package test.command;

import command.Command;
import command.CommandManager;
import dao.HistoriqueCommandeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

class CommandManagerTest {

    private CommandManager commandManager;
    private Command mockCommand;
    private HistoriqueCommandeDAO mockHistoriqueCommandeDAO;

    @BeforeEach
    void setUp() {
        // Mock de l'HistoriqueCommandeDAO
        mockHistoriqueCommandeDAO = mock(HistoriqueCommandeDAO.class);
        
        // Initialisation du CommandManager avec le mock du DAO
        commandManager = new CommandManager(mockHistoriqueCommandeDAO);
        
        // Mock d'une commande
        mockCommand = mock(Command.class);
        when(mockCommand.isExecuted()).thenReturn(false);
    }

    @Test
    void testExecuteCommand() {
        // Exécution de la commande
        commandManager.executeCommand(mockCommand);

        // Vérifier que la commande a bien été exécutée
        verify(mockCommand, times(1)).execute();

        // Vérifier que la commande a bien été ajoutée à l'historique
        assertEquals(1, commandManager.getHistorique().size());

        // Vérifier que l'ajout à l'historique en base a été appelé
        verify(mockHistoriqueCommandeDAO, times(1)).ajouterHistorique(mockCommand);
    }

    @Test
    void testUndoLastCommand() {
        commandManager.executeCommand(mockCommand);
        when(mockCommand.isExecuted()).thenReturn(true);

        commandManager.undoLastCommand();

        // Vérifier que l'annulation a bien été appelée
        verify(mockCommand, times(1)).annule();

        // Vérifier que la commande a été supprimée de l'historique
        assertEquals(0, commandManager.getHistorique().size());
    }

    @Test
    void testUndoSpecificCommand() {
        commandManager.executeCommand(mockCommand);
        when(mockCommand.isExecuted()).thenReturn(true);

        commandManager.undoSpecificCommand(0);

        // Vérifier que l'annulation a bien été appelée
        verify(mockCommand, times(1)).annule();

        // Vérifier que l'historique est vide après l'annulation
        assertEquals(0, commandManager.getHistorique().size());
    }

    @Test
    void testRedoCommand() {
        commandManager.redoCommand(mockCommand);

        // Vérifier que la commande est bien exécutée
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testGetHistorique() {
        commandManager.executeCommand(mockCommand);
        
        // Vérifier que l'historique contient la commande exécutée
        List<Command> historique = commandManager.getHistorique();
        assertEquals(1, historique.size());
        assertTrue(historique.contains(mockCommand));
    }

    @Test
    void testRechercherCommandes() {
        commandManager.executeCommand(mockCommand);
        when(mockCommand.toString()).thenReturn("Test Command");

        List<Command> resultats = commandManager.rechercherCommandes("Test");

        // Vérifier que la recherche retourne la bonne commande
        assertEquals(1, resultats.size());
        assertEquals(mockCommand, resultats.get(0));
    }
}
