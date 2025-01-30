package test.model;

import model.Livre;
import observateur.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LivreTest {

    private Livre livre;
    private Observer observerMock;

    @BeforeEach
    void setUp() {
        // Initialisation d'un livre
        livre = new Livre(1, "Le Petit Prince", 1943, "123-456-789", 10, 20);
        
        // Création d'un mock pour l'observateur
        observerMock = mock(Observer.class);
    }

    @Test
    void testGetters() {
        assertEquals(1, livre.getID_Livre());
        assertEquals("Le Petit Prince", livre.getTitre());
        assertEquals(1943, livre.getAnnee_Publication());
        assertEquals("123-456-789", livre.getISBN());
        assertEquals(10, livre.getID_Editeur());
        assertEquals(20, livre.getID_Categorie());
        assertTrue(livre.isDisponible());
    }

    @Test
    void testSetters() {
        livre.setID_Livre(2);
        livre.setTitre("Les Misérables");
        livre.setAnnee_Publication(1862);
        livre.setISBN("987-654-321");
        livre.setID_Editeur(30);
        livre.setID_Categorie(40);
        livre.setDisponible(false);

        assertEquals(2, livre.getID_Livre());
        assertEquals("Les Misérables", livre.getTitre());
        assertEquals(1862, livre.getAnnee_Publication());
        assertEquals("987-654-321", livre.getISBN());
        assertEquals(30, livre.getID_Editeur());
        assertEquals(40, livre.getID_Categorie());
        assertFalse(livre.isDisponible());
    }

    @Test
    void testObserverNotificationOnAvailabilityChange() {
        // Ajouter un observateur
        livre.addObserver(observerMock);

        // Vérifier que la notification est envoyée quand le livre devient disponible
        livre.setDisponible(true);
        verify(observerMock, times(1)).update();

        // Vérifier qu'aucune notification n'est envoyée si le livre devient indisponible
        livre.setDisponible(false);
        verify(observerMock, times(1)).update();  // Toujours 1 appel (pas de notification pour indisponible)
    }

    @Test
    void testAddAndRemoveObserver() {
        livre.addObserver(observerMock);
        livre.notifyObservers();

        verify(observerMock, times(1)).update();

        // Supprimer l'observateur et tester qu'il ne reçoit plus de notification
        livre.removeObserver(observerMock);
        livre.notifyObservers();

        verify(observerMock, times(1)).update(); // Pas de nouvelles notifications
    }

    @Test
    void testAfficherDetails() {
        // Vérifier que `afficherDetails` ne lève pas d'erreur
        livre.afficherDetails();
    }

    @Test
    void testToString() {
        assertEquals("Le Petit Prince", livre.toString());
    }
}
