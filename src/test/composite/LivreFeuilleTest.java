package test.composite;

import composite.LivreFeuille;
import model.Livre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class LivreFeuilleTest {

    private Livre livre;
    private LivreFeuille livreFeuille;

    @BeforeEach
    void setUp() {
        // Création d'un livre fictif
        livre = new Livre(1, "Le Petit Prince", 1943, "123456789", 2, 3);
        livreFeuille = new LivreFeuille(livre);
    }

    @Test
    void testGetNom() {
        assertEquals("Le Petit Prince", livreFeuille.getNom());
    }

    @Test
    void testGetEnfants() {
        assertTrue(livreFeuille.getEnfants().isEmpty(), "Un livre feuille ne doit pas avoir d'enfants");
    }

    @Test
    void testAjouterNonApplicable() {
        assertDoesNotThrow(() -> livreFeuille.ajouter(null), "Ajouter ne doit rien faire pour une feuille");
    }

    @Test
    void testSupprimerNonApplicable() {
        assertDoesNotThrow(() -> livreFeuille.supprimer(null), "Supprimer ne doit rien faire pour une feuille");
    }

    @Test
    void testToString() {
        assertEquals("Le Petit Prince", livreFeuille.toString());
    }

    @Test
    void testAfficherStructure() {
        assertDoesNotThrow(() -> livreFeuille.afficherStructure("  "), "Affichage de structure ne doit pas lever d'exception");
    }
}
