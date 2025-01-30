package test.composite;

import composite.CategorieComposite;
import composite.ComposantCategorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategorieCompositeTest {

    private CategorieComposite categorieParent;
    private CategorieComposite sousCategorie1;
    private CategorieComposite sousCategorie2;

    @BeforeEach
    void setUp() {
        // Création des catégories
        categorieParent = new CategorieComposite(1, "Science");
        sousCategorie1 = new CategorieComposite(2, "Physique");
        sousCategorie2 = new CategorieComposite(3, "Chimie");
    }

    @Test
    void testAjoutCategorie() {
        categorieParent.ajouter(sousCategorie1);
        categorieParent.ajouter(sousCategorie2);

        List<ComposantCategorie> enfants = categorieParent.getEnfants();

        assertEquals(2, enfants.size());
        assertTrue(enfants.contains(sousCategorie1));
        assertTrue(enfants.contains(sousCategorie2));
    }

    @Test
    void testSuppressionCategorie() {
        categorieParent.ajouter(sousCategorie1);
        categorieParent.supprimer(sousCategorie1);

        List<ComposantCategorie> enfants = categorieParent.getEnfants();
        assertEquals(0, enfants.size());
    }

    @Test
    void testAjouterCategorieAvecNomVide() {
        assertThrows(IllegalArgumentException.class, () -> new CategorieComposite(4, ""));
    }

    @Test
    void testAjoutBoucle() {
        categorieParent.ajouter(sousCategorie1);
        assertThrows(IllegalArgumentException.class, () -> sousCategorie1.ajouter(categorieParent));
    }

    @Test
    void testGetParentId() {
        categorieParent.ajouter(sousCategorie1);
        assertEquals(1, sousCategorie1.getParentId());
    }

    @Test
    void testAffichageStructure() {
        categorieParent.ajouter(sousCategorie1);
        categorieParent.ajouter(sousCategorie2);

        // Vérifie que l'affichage ne génère pas d'erreurs (pas de NullPointerException)
        assertDoesNotThrow(() -> categorieParent.afficherStructure(""));
    }
}
