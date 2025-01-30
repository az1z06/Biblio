package test.model;

import model.LivreAudio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LivreAudioTest {

    private LivreAudio livreAudio;

    @BeforeEach
    void setUp() {
        // Création d'un LivreAudio avant chaque test
        livreAudio = new LivreAudio(1, "Le Seigneur des Anneaux", 1954, "978-0261102385", 10, 20, 12.5, "Gérard Philipe");
    }

    @Test
    void testGetters() {
        assertEquals(1, livreAudio.getID_Livre());
        assertEquals("Le Seigneur des Anneaux", livreAudio.getTitre());
        assertEquals(1954, livreAudio.getAnnee_Publication());
        assertEquals("978-0261102385", livreAudio.getISBN());
        assertEquals(10, livreAudio.getID_Editeur());
        assertEquals(20, livreAudio.getID_Categorie());
        assertEquals(12.5, livreAudio.getDuree());
        assertEquals("Gérard Philipe", livreAudio.getNarrateur());
    }

    @Test
    void testSetters() {
        livreAudio.setTitre("Harry Potter");
        livreAudio.setAnnee_Publication(1997);
        livreAudio.setISBN("978-0747532699");

        assertEquals("Harry Potter", livreAudio.getTitre());
        assertEquals(1997, livreAudio.getAnnee_Publication());
        assertEquals("978-0747532699", livreAudio.getISBN());
    }

    @Test
    void testToString() {
        assertEquals("Le Seigneur des Anneaux", livreAudio.toString());
    }

    @Test
    void testAfficherDetails() {
        livreAudio.afficherDetails(); // Vérifie que l'affichage ne lève pas d'exception
    }
}
