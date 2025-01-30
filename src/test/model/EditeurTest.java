package test.model;

import model.Editeur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EditeurTest {

    private Editeur editeur;

    @BeforeEach
    void setUp() {
        // Création d'un éditeur avant chaque test
        editeur = new Editeur(1, "Hachette", "12 rue des Éditeurs, Paris");
    }

    @Test
    void testGetIdEditeur() {
        assertEquals(1, editeur.getIdEditeur());
    }

    @Test
    void testSetIdEditeur() {
        editeur.setIdEditeur(2);
        assertEquals(2, editeur.getIdEditeur());
    }

    @Test
    void testGetNom() {
        assertEquals("Hachette", editeur.getNom());
    }

    @Test
    void testSetNom() {
        editeur.setNom("Gallimard");
        assertEquals("Gallimard", editeur.getNom());
    }

    @Test
    void testGetAdresse() {
        assertEquals("12 rue des Éditeurs, Paris", editeur.getAdresse());
    }

    @Test
    void testSetAdresse() {
        editeur.setAdresse("25 boulevard Saint-Michel, Paris");
        assertEquals("25 boulevard Saint-Michel, Paris", editeur.getAdresse());
    }
}
