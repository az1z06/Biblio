package test.controller;

import composite.CategorieComposite;
import controller.CategorieController;
import dao.CategorieDAO;
import dao.LivreDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategorieControllerTest {

    private CategorieController categorieController;
    private CategorieDAO categorieDAO;
    private LivreDAO livreDAO;

    @BeforeEach
    void setUp() {
        // Création des mocks pour les DAO
        categorieDAO = mock(CategorieDAO.class);
        livreDAO = mock(LivreDAO.class);

        categorieController = new CategorieController(categorieDAO, livreDAO);

        // Évite les exceptions pour les méthodes void
        doNothing().when(categorieDAO).ajouterCategorie(any(CategorieComposite.class));
        doNothing().when(categorieDAO).supprimerCategorie(anyInt());
    }

    @Test
    void testConstruireArbreCategories() {
        // Simuler un arbre de catégories retourné par le DAO
        CategorieComposite categorieMock = new CategorieComposite(1, "Fiction");

        when(categorieDAO.construireArbreCategories()).thenReturn(categorieMock);

        // Vérification
        CategorieComposite result = categorieController.construireArbreCategories();
        assertNotNull(result);
        assertEquals("Fiction", result.getNom());

        verify(categorieDAO, times(1)).construireArbreCategories();
    }

    @Test
    void testConstruireArbreCategoriesAvecLivres() {
        // Simuler un arbre de catégories avec livres
        CategorieComposite categorieMock = new CategorieComposite(2, "Science-Fiction");

        when(categorieDAO.construireArbreCategoriesAvecLivres(livreDAO)).thenReturn(categorieMock);

        // Vérification
        CategorieComposite result = categorieController.construireArbreCategoriesAvecLivres();
        assertNotNull(result);
        assertEquals("Science-Fiction", result.getNom());

        verify(categorieDAO, times(1)).construireArbreCategoriesAvecLivres(livreDAO);
    }

    @Test
    void testAjouterCategorie() {
        String nomCategorie = "Histoire"; // Nom valide
        Integer parentId = 1; // ID parent valide

        // Exécution de la méthode
        categorieController.ajouterCategorie(nomCategorie, parentId);

        // Vérification : la méthode DAO doit être appelée avec une catégorie valide
        verify(categorieDAO, times(1)).ajouterCategorie(any(CategorieComposite.class));
    }


    @Test
    void testAjouterCategorieSansParent() {
        String nomCategorie = "Philosophie";

        // Appel de la méthode avec parentId = null
        categorieController.ajouterCategorie(nomCategorie, null);

        // Vérifier que l'ajout est bien effectué même sans parent
        verify(categorieDAO, times(1)).ajouterCategorie(any(CategorieComposite.class));
    }

    @Test
    void testAjouterCategorie_NomVide() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categorieController.ajouterCategorie("", null); // Nom vide
        });

        assertEquals("Le nom de la catégorie ne peut pas être vide.", exception.getMessage());
    }

    @Test
    void testSupprimerCategorie() {
        int idCategorie = 3;

        // Simuler une suppression réussie
        doNothing().when(categorieDAO).supprimerCategorie(idCategorie);

        // Appel de la méthode
        categorieController.supprimerCategorie(idCategorie);

        // Vérifier que la suppression est bien appelée
        verify(categorieDAO, times(1)).supprimerCategorie(idCategorie);
    }

    @Test
    void testSupprimerCategorie_IdInvalide() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categorieController.supprimerCategorie(-1);
        });

        assertEquals("L'ID de la catégorie est invalide.", exception.getMessage());
    }

    @Test
    void testSupprimerCategorieInexistante() {
        int idCategorie = 999; // Supposons que cette catégorie n'existe pas

        // Simuler une suppression sans effet
        doNothing().when(categorieDAO).supprimerCategorie(idCategorie);

        // Appel de la méthode
        categorieController.supprimerCategorie(idCategorie);

        // Vérification : même pour une catégorie inexistante, la méthode doit être appelée
        verify(categorieDAO, times(1)).supprimerCategorie(idCategorie);
    }
}
