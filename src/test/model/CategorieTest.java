package test.model;

import model.Categorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategorieTest {

    private Categorie categorie;

    @BeforeEach
    void setUp() {
        // Création d'une catégorie avec un parent
        categorie = new Categorie(1, "Fiction", 0);
    }

    @Test
    void testGetID_Categorie() {
        assertEquals(1, categorie.getID_Categorie());
    }

    @Test
    void testSetID_Categorie() {
        categorie.setID_Categorie(2);
        assertEquals(2, categorie.getID_Categorie());
    }

    @Test
    void testGetNom() {
        assertEquals("Fiction", categorie.getNom());
    }

    @Test
    void testSetNom() {
        categorie.setNom("Science-Fiction");
        assertEquals("Science-Fiction", categorie.getNom());
    }

    @Test
    void testGetID_Categorie_Parent() {
        assertEquals(0, categorie.getID_Categorie_Parent());
    }

    @Test
    void testSetID_Categorie_Parent() {
        categorie.setID_Categorie_Parent(3);
        assertEquals(3, categorie.getID_Categorie_Parent());
    }
}
