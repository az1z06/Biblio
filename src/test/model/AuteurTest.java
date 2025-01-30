package test.model;

import model.Auteur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class AuteurTest {

    private Auteur auteur;

    @BeforeEach
    void setUp() {
        // Création d'un Auteur avec java.sql.Date
        auteur = new Auteur(1, "Hugo", "Victor", Date.valueOf("1802-02-26"));
    }

    @Test
    void testGetID_Auteur() {
        assertEquals(1, auteur.getID_Auteur());
    }

    @Test
    void testSetID_Auteur() {
        auteur.setID_Auteur(2);
        assertEquals(2, auteur.getID_Auteur());
    }

    @Test
    void testGetNom() {
        assertEquals("Hugo", auteur.getNom());
    }

    @Test
    void testSetNom() {
        auteur.setNom("Zola");
        assertEquals("Zola", auteur.getNom());
    }

    @Test
    void testGetPrenom() {
        assertEquals("Victor", auteur.getPrenom());
    }

    @Test
    void testSetPrenom() {
        auteur.setPrenom("Emile");
        assertEquals("Emile", auteur.getPrenom());
    }

    @Test
    void testGetDate_Naissance() {
        assertEquals(Date.valueOf("1802-02-26"), auteur.getDate_Naissance());
    }

    @Test
    void testSetDate_Naissance() {
        Date nouvelleDate = Date.valueOf("1840-03-15");
        auteur.setDate_Naissance(nouvelleDate);
        assertEquals(nouvelleDate, auteur.getDate_Naissance());
    }
}
