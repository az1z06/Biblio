package test.model;

import model.Emprunt;
import model.Livre;
import model.Membre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class EmpruntTest {

    private Emprunt emprunt;
    private Livre livre;
    private Membre membre;
    private Date dateEmprunt;
    private Date dateRetourPrevue;
    private Date dateRetourEffective;

    @BeforeEach
    void setUp() {
        dateEmprunt = new Date(System.currentTimeMillis());
        dateRetourPrevue = new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000); // +7 jours
        dateRetourEffective = null;

        livre = new Livre();
        livre.setTitre("Le Petit Prince");

        membre = new Membre();
        membre.setNom("Jean Dupont");

        emprunt = new Emprunt(1, 100, 200, dateEmprunt, dateRetourPrevue, dateRetourEffective, livre, membre);
    }

    @Test
    void testGetters() {
        assertEquals(1, emprunt.getID_Emprunt());
        assertEquals(100, emprunt.getID_Livre());
        assertEquals(200, emprunt.getID_Membre());
        assertEquals(dateEmprunt, emprunt.getDate_Emprunt());
        assertEquals(dateRetourPrevue, emprunt.getDate_Retour_Prevue());
        assertNull(emprunt.getDate_Retour_Effective());
        assertEquals("Le Petit Prince", emprunt.getLivre().getTitre());
        assertEquals("Jean Dupont", emprunt.getMembre().getNom());
    }

    @Test
    void testSetters() {
        emprunt.setID_Emprunt(2);
        emprunt.setID_Livre(101);
        emprunt.setID_Membre(201);
        Date newDateRetourEffective = new Date(System.currentTimeMillis());
        emprunt.setDate_Retour_Effective(newDateRetourEffective);

        assertEquals(2, emprunt.getID_Emprunt());
        assertEquals(101, emprunt.getID_Livre());
        assertEquals(201, emprunt.getID_Membre());
        assertEquals(newDateRetourEffective, emprunt.getDate_Retour_Effective());
    }

    @Test
    void testSetLivre() {
        Livre nouveauLivre = new Livre();
        nouveauLivre.setTitre("Les Misérables");
        emprunt.setLivre(nouveauLivre);

        assertEquals("Les Misérables", emprunt.getLivre().getTitre());
    }

    @Test
    void testSetMembre() {
        Membre nouveauMembre = new Membre();
        nouveauMembre.setNom("Alice Dupont");
        emprunt.setMembre(nouveauMembre);

        assertEquals("Alice Dupont", emprunt.getMembre().getNom());
    }
}
