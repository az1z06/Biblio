package test.controller;

import controller.AuteurController;
import dao.AuteurDAO;
import model.Auteur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuteurControllerTest {

    private AuteurController auteurController;
    private AuteurDAO auteurDAO;

    @BeforeEach
    void setUp() {
        // Création du mock de AuteurDAO
        auteurDAO = mock(AuteurDAO.class);
        auteurController = new AuteurController(auteurDAO);
    }

    @Test
    void testGetAllAuteurs() {
        // Création d'une liste simulée de résultats
        List<Auteur> auteursMock = Arrays.asList(
            new Auteur(1, "Victor", "Hugo", Date.valueOf("1802-02-26")),
            new Auteur(2, "Jules", "Verne", Date.valueOf("1828-02-08"))
        );

        // Simuler le comportement du DAO
        when(auteurDAO.getAuteurs()).thenReturn(auteursMock);

        // Vérification
        List<Auteur> result = auteurController.getAllAuteurs();
        assertEquals(2, result.size());
        assertEquals("Victor", result.get(0).getNom());
        verify(auteurDAO, times(1)).getAuteurs();
    }

    @Test
    void testAjouterAuteur() {
        Auteur auteur = new Auteur(3, "George", "Orwell", Date.valueOf("1903-06-25"));

        when(auteurDAO.ajouterAuteur(auteur)).thenReturn(true);

        boolean success = auteurController.ajouterAuteur(auteur);

        assertTrue(success);
        verify(auteurDAO, times(1)).ajouterAuteur(auteur);
    }

    @Test
    void testSupprimerAuteur() {
        int idAuteur = 1;

        when(auteurDAO.supprimerAuteur(idAuteur)).thenReturn(true);

        boolean success = auteurController.supprimerAuteur(idAuteur);

        assertTrue(success);
        verify(auteurDAO, times(1)).supprimerAuteur(idAuteur);
    }

    @Test
    void testMettreAJourAuteur() {
        Auteur auteur = new Auteur(1, "Victor", "Hugo", Date.valueOf("1802-02-26"));

        when(auteurDAO.mettreAJourAuteur(auteur)).thenReturn(true);

        boolean success = auteurController.mettreAJourAuteur(auteur);

        assertTrue(success);
        verify(auteurDAO, times(1)).mettreAJourAuteur(auteur);
    }
}
