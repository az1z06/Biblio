package test.controller;

import controller.EmpruntController;
import dao.EmpruntDAO;
import dao.LivreDAO;
import dao.MembreDAO;
import model.Emprunt;
import model.Livre;
import model.Membre;
import command.BorrowCommand;
import command.ReturnCommand;
import command.CommandManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpruntControllerTest {

    private EmpruntController empruntController;
    private EmpruntDAO empruntDAO;
    private LivreDAO livreDAO;
    private MembreDAO membreDAO;
    private CommandManager commandManager;

    @BeforeEach
    void setUp() {
        empruntDAO = mock(EmpruntDAO.class);
        livreDAO = mock(LivreDAO.class);
        membreDAO = mock(MembreDAO.class);
        commandManager = mock(CommandManager.class);

        empruntController = new EmpruntController(empruntDAO, livreDAO, membreDAO, commandManager);
    }

    @Test
    void testEmprunter() {
        Emprunt emprunt = new Emprunt(1, 101, 201, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000), null);
        Livre livre = new Livre(101, "Java Programming", 2020, "12345", 10, 5);
        Membre membre = new Membre(201, "Doe", "John", "john.doe@email.com", new Date(System.currentTimeMillis()));

        when(livreDAO.getLivreById(101)).thenReturn(livre);
        when(membreDAO.getMembreById(201)).thenReturn(membre);
        when(empruntDAO.ajouterEmprunt(emprunt)).thenReturn(true);

        empruntController.emprunter(emprunt);

        verify(commandManager, times(1)).executeCommand(any(BorrowCommand.class));
        assertEquals("Java Programming", emprunt.getLivre().getTitre());
        assertEquals("Doe", emprunt.getMembre().getNom());
    }

    @Test
    void testRetourner() {
        Emprunt emprunt = new Emprunt(1, 101, 201, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000), null);
        Livre livre = new Livre(101, "Java Programming", 2020, "12345", 10, 5);

        when(livreDAO.getLivreById(101)).thenReturn(livre);
        when(empruntDAO.modifierEmprunt(emprunt)).thenReturn(true);

        empruntController.retourner(emprunt);

        verify(commandManager, times(1)).executeCommand(any(ReturnCommand.class));
        verify(livreDAO, times(1)).getLivreById(101);
    }

    @Test
    void testRechercherEmprunts() {
        List<Emprunt> mockEmprunts = Arrays.asList(
                new Emprunt(1, 101, 201, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000), null),
                new Emprunt(2, 102, 202, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000), null)
        );

        when(empruntDAO.rechercherEmprunts("Java")).thenReturn(mockEmprunts);

        List<Emprunt> result = empruntController.rechercherEmprunts("Java");

        assertEquals(2, result.size());
        verify(empruntDAO, times(1)).rechercherEmprunts("Java");
    }

    @Test
    void testModifierEmprunt() {
        Emprunt emprunt = new Emprunt(1, 101, 201, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000), null);

        when(empruntDAO.modifierEmprunt(emprunt)).thenReturn(true);

        boolean success = empruntController.modifierEmprunt(emprunt);

        assertTrue(success);
        verify(empruntDAO, times(1)).modifierEmprunt(emprunt);
    }

    @Test
    void testSupprimerEmprunt() {
        when(empruntDAO.supprimerEmprunt(1)).thenReturn(true);

        boolean success = empruntController.supprimerEmprunt(1);

        assertTrue(success);
        verify(empruntDAO, times(1)).supprimerEmprunt(1);
    }

    @Test
    void testMettreDateRetourEffective() {
        Emprunt emprunt = new Emprunt(1, 101, 201, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000), null);
        Livre livre = new Livre(101, "Java Programming", 2020, "12345", 10, 5);
        Membre membre = new Membre(201, "Doe", "John", "john.doe@email.com", new Date(System.currentTimeMillis()));

        when(empruntDAO.getEmpruntById(1)).thenReturn(emprunt);
        when(livreDAO.getLivreById(101)).thenReturn(livre);
        when(membreDAO.getMembreById(201)).thenReturn(membre);

        empruntController.mettreDateRetourEffective(1);

        assertNotNull(emprunt.getDate_Retour_Effective());
        verify(empruntDAO, times(1)).modifierEmprunt(emprunt);
    }
}
