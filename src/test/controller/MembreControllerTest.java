package test.controller;

import controller.MembreController;
import dao.MembreDAO;
import model.Membre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MembreControllerTest {

    private MembreController membreController;
    private MembreDAO membreDAO;

    @BeforeEach
    void setUp() {
        membreDAO = mock(MembreDAO.class);
        membreController = new MembreController(membreDAO);
    }

    @Test
    void testAjouterMembre() {
        Membre membre = new Membre(1, "Dupont", "Jean", "jean.dupont@example.com", new Date(System.currentTimeMillis()));
        when(membreDAO.ajouterMembre(membre)).thenReturn(true);

        boolean success = membreController.ajouterMembre(membre);

        assertTrue(success);
        verify(membreDAO, times(1)).ajouterMembre(membre);
    }

    @Test
    void testAjouterMembreEmailVide() {
        Membre membre = new Membre(2, "Martin", "Paul", "", new Date(System.currentTimeMillis()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            membreController.ajouterMembre(membre);
        });

        assertEquals("L'email du membre ne peut pas être vide.", exception.getMessage());
        verify(membreDAO, never()).ajouterMembre(any());
    }

    @Test
    void testGetAllMembres() {
        List<Membre> mockMembres = Arrays.asList(
                new Membre(1, "Dupont", "Jean", "jean.dupont@example.com", new Date(System.currentTimeMillis())),
                new Membre(2, "Martin", "Paul", "paul.martin@example.com", new Date(System.currentTimeMillis()))
        );

        when(membreDAO.getMembres()).thenReturn(mockMembres);

        List<Membre> membres = membreController.getAllMembres();

        assertEquals(2, membres.size());
        assertEquals("Dupont", membres.get(0).getNom());
        assertEquals("Martin", membres.get(1).getNom());
    }

    @Test
    void testRechercherMembres() {
        List<Membre> mockMembres = Arrays.asList(
                new Membre(1, "Dupont", "Jean", "jean.dupont@example.com", new Date(System.currentTimeMillis()))
        );

        when(membreDAO.rechercherMembres("Jean")).thenReturn(mockMembres);

        List<Membre> result = membreController.rechercherMembres("Jean");

        assertEquals(1, result.size());
        assertEquals("Dupont", result.get(0).getNom());
    }

    @Test
    void testMettreAJourMembre() {
        Membre membre = new Membre(1, "Dupont", "Jean", "jean.dupont@example.com", new Date(System.currentTimeMillis()));
        when(membreDAO.mettreAJourMembre(membre)).thenReturn(true);

        boolean success = membreController.mettreAJourMembre(membre);

        assertTrue(success);
        verify(membreDAO, times(1)).mettreAJourMembre(membre);
    }

    @Test
    void testSupprimerMembre() {
        when(membreDAO.supprimerMembre(1)).thenReturn(true);

        boolean success = membreController.supprimerMembre(1);

        assertTrue(success);
        verify(membreDAO, times(1)).supprimerMembre(1);
    }

    @Test
    void testGetMembreById() {
        Membre mockMembre = new Membre(1, "Durand", "Sophie", "sophie.durand@example.com", new Date(System.currentTimeMillis()));
        when(membreDAO.getMembreById(1)).thenReturn(mockMembre);

        Membre membre = membreController.getMembreById(1);

        assertNotNull(membre);
        assertEquals("Durand", membre.getNom());
    }
}
