package test.controller;

import controller.LivreController;
import dao.CategorieDAO;
import dao.EditeurDAO;
import dao.LivreDAO;
import model.Livre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivreControllerTest {

    private LivreController livreController;
    private LivreDAO livreDAO;
    private CategorieDAO categorieDAO;
    private EditeurDAO editeurDAO;

    @BeforeEach
    void setUp() {
        livreDAO = mock(LivreDAO.class);
        categorieDAO = mock(CategorieDAO.class);
        editeurDAO = mock(EditeurDAO.class);

        livreController = new LivreController(livreDAO, categorieDAO, editeurDAO);
    }

    @Test
    void testAjouterLivre() {
        Livre livre = new Livre(1, "Java pour les nuls", 2023, "123456789", 2, 5);
        when(livreDAO.ajouterLivre(livre)).thenReturn(true);

        boolean success = livreController.ajouterLivre(livre);

        assertTrue(success);
        verify(livreDAO, times(1)).ajouterLivre(livre);
    }

    @Test
    void testAjouterLivreTitreVide() {
        Livre livre = new Livre(2, "", 2023, "987654321", 3, 6);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            livreController.ajouterLivre(livre);
        });

        assertEquals("Le titre ne peut pas être vide.", exception.getMessage());
        verify(livreDAO, never()).ajouterLivre(any());
    }

    @Test
    void testGetAllLivres() {
        List<Livre> mockLivres = Arrays.asList(
                new Livre(1, "Java avancé", 2022, "1234", 1, 1),
                new Livre(2, "Spring Boot Guide", 2021, "5678", 2, 2)
        );

        when(livreDAO.getLivres()).thenReturn(mockLivres);

        List<Livre> livres = livreController.getAllLivres();

        assertEquals(2, livres.size());
        assertEquals("Java avancé", livres.get(0).getTitre());
        assertEquals("Spring Boot Guide", livres.get(1).getTitre());
    }

    @Test
    void testRechercherLivres() {
        List<Livre> mockLivres = Arrays.asList(
                new Livre(1, "Programmation en Java", 2020, "112233", 3, 4)
        );

        when(livreDAO.rechercherLivres("Java")).thenReturn(mockLivres);

        List<Livre> result = livreController.rechercherLivres("Java");

        assertEquals(1, result.size());
        assertEquals("Programmation en Java", result.get(0).getTitre());
    }

    @Test
    void testMettreAJourLivre() {
        Livre livre = new Livre(1, "Java pour Experts", 2023, "223344", 5, 7);
        when(livreDAO.mettreAJourLivre(livre)).thenReturn(true);

        boolean success = livreController.mettreAJourLivre(livre);

        assertTrue(success);
        verify(livreDAO, times(1)).mettreAJourLivre(livre);
    }

    @Test
    void testSupprimerLivre() {
        when(livreDAO.supprimerLivre(1)).thenReturn(true);

        boolean success = livreController.supprimerLivre(1);

        assertTrue(success);
        verify(livreDAO, times(1)).supprimerLivre(1);
    }

    @Test
    void testGetLivreById() {
        Livre mockLivre = new Livre(1, "Spring Framework", 2021, "334455", 6, 8);
        when(livreDAO.getLivreById(1)).thenReturn(mockLivre);

        Livre livre = livreController.getLivreById(1);

        assertNotNull(livre);
        assertEquals("Spring Framework", livre.getTitre());
    }

    @Test
    void testAfficherDetailsLivre() {
        Livre mockLivre = new Livre(1, "Design Patterns", 2019, "778899", 7, 9);
        when(livreDAO.getLivreById(1)).thenReturn(mockLivre);

        livreController.afficherDetailsLivre(1);

        verify(livreDAO, times(1)).getLivreById(1);
    }

    @Test
    void testGetAllCategories() {
        List<String> categories = Arrays.asList("Informatique", "Développement", "IA");
        when(categorieDAO.getAllCategorieNames()).thenReturn(categories);

        List<String> result = livreController.getAllCategories();

        assertEquals(3, result.size());
        assertEquals("Informatique", result.get(0));
    }

    @Test
    void testGetAllEditeurs() {
        List<String> editeurs = Arrays.asList("O'Reilly", "Eyrolles", "Dunod");
        when(editeurDAO.getAllEditeurNames()).thenReturn(editeurs);

        List<String> result = livreController.getAllEditeurs();

        assertEquals(3, result.size());
        assertEquals("O'Reilly", result.get(0));
    }
}
