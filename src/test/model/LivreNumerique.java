package test.model;

import model.LivreNumerique;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LivreNumeriqueTest {

    private LivreNumerique livreNumerique;

    @BeforeEach
    void setUp() {
        // Création d'un LivreNumerique avant chaque test
        livreNumerique = new LivreNumerique(1, "Clean Code", 2008, "978-0132350884", 5, 10, "PDF", 2.5);
    }

    @Test
    void testGetters() {
        assertEquals(1, livreNumerique.getID_Livre());
        assertEquals("Clean Code", livreNumerique.getTitre());
        assertEquals(2008, livreNumerique.getAnnee_Publication());
        assertEquals("978-0132350884", livreNumerique.getISBN());
        assertEquals(5, livreNumerique.getID_Editeur());
        assertEquals(10, livreNumerique.getID_Categorie());
        assertEquals("PDF", livreNumerique.getFormat());
        assertEquals(2.5, livreNumerique.getTailleFichier());
    }

    @Test
    void testSetters() {
        livreNumerique.setTitre("Refactoring");
        livreNumerique.setAnnee_Publication(1999);
        livreNumerique.setISBN("978-0201485677");

        assertEquals("Refactoring", livreNumerique.getTitre());
        assertEquals(1999, livreNumerique.getAnnee_Publication());
        assertEquals("978-0201485677", livreNumerique.getISBN());
    }

    @Test
    void testToString() {
        assertEquals("Clean Code", livreNumerique.toString());
    }

    @Test
    void testAfficherDetails() {
        livreNumerique.afficherDetails(); // Vérifie que l'affichage ne lève pas d'exception
    }
}
