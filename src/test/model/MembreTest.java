package test.model;

import model.Membre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class MembreTest {

    private Membre membre;

    @BeforeEach
    void setUp() {
        // Création d'un membre avant chaque test
        membre = new Membre(1, "Dupont", "Jean", "jean.dupont@example.com", new Date());
    }

    @Test
    void testGetters() {
        assertEquals(1, membre.getID_Membre());
        assertEquals("Dupont", membre.getNom());
        assertEquals("Jean", membre.getPrenom());
        assertEquals("jean.dupont@example.com", membre.getEmail());
        assertNotNull(membre.getDate_Inscription()); // Vérifie que la date n'est pas null
    }

    @Test
    void testSetters() {
        membre.setNom("Martin");
        membre.setPrenom("Paul");
        membre.setEmail("paul.martin@example.com");

        assertEquals("Martin", membre.getNom());
        assertEquals("Paul", membre.getPrenom());
        assertEquals("paul.martin@example.com", membre.getEmail());
    }

    @Test
    void testToString() {
        assertEquals("Dupont Jean", membre.toString());
    }

    @Test
    void testUpdate() {
        // Vérifie que l'appel à update() ne provoque pas d'erreur
        assertDoesNotThrow(() -> membre.update());
    }
}
