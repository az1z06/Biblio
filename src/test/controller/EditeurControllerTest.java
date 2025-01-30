package test.controller;

import controller.EditeurController;
import dao.EditeurDAO;
import model.Editeur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditeurControllerTest {

    private EditeurController editeurController;
    private EditeurDAO editeurDAO;

    @BeforeEach
    void setUp() {
        editeurDAO = mock(EditeurDAO.class);
        editeurController = new EditeurController(editeurDAO);
    }

    @Test
    void testGetAllEditeurs() {
        List<Editeur> mockEditeurs = Arrays.asList(
                new Editeur(1, "Gallimard", "Paris"),
                new Editeur(2, "Hachette", "Lyon")
        );

        when(editeurDAO.getAllEditeurs()).thenReturn(mockEditeurs);

        List<Editeur> result = editeurController.getAllEditeurs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Gallimard", result.get(0).getNom());
        assertEquals("Hachette", result.get(1).getNom());

        verify(editeurDAO, times(1)).getAllEditeurs();
    }

    @Test
    void testAjouterEditeur() {
        Editeur editeur = new Editeur(3, "Flammarion", "Marseille");

        when(editeurDAO.ajouterEditeur(editeur)).thenReturn(true);

        boolean success = editeurController.ajouterEditeur(editeur);

        assertTrue(success);
        verify(editeurDAO, times(1)).ajouterEditeur(editeur);
    }

    @Test
    void testModifierEditeur() {
        Editeur editeur = new Editeur(1, "Gallimard", "Paris - Modifié");

        when(editeurDAO.modifierEditeur(editeur)).thenReturn(true);

        boolean success = editeurController.modifierEditeur(editeur);

        assertTrue(success);
        verify(editeurDAO, times(1)).modifierEditeur(editeur);
    }

    @Test
    void testSupprimerEditeur() {
        int idEditeur = 2;

        when(editeurDAO.supprimerEditeur(idEditeur)).thenReturn(true);

        boolean success = editeurController.supprimerEditeur(idEditeur);

        assertTrue(success);
        verify(editeurDAO, times(1)).supprimerEditeur(idEditeur);
    }
}
