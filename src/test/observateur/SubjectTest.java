package test.observateur;

import observateur.Observer;
import observateur.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.mockito.Mockito.*;

class SubjectTest {

    private Subject subject;
    private Observer observerMock1;
    private Observer observerMock2;

    @BeforeEach
    void setUp() {
        subject = new Subject(); // Création de l'objet à tester
        observerMock1 = mock(Observer.class); // Création de faux observateurs
        observerMock2 = mock(Observer.class);
    }

    @Test
    void testAjouterObserver() {
        subject.addObserver(observerMock1);
        subject.addObserver(observerMock2);

        subject.notifyObservers();

        // Vérification que chaque observateur a bien reçu la notification
        verify(observerMock1, times(1)).update();
        verify(observerMock2, times(1)).update();
    }

    @Test
    void testSupprimerObserver() {
        subject.addObserver(observerMock1);
        subject.addObserver(observerMock2);

        subject.removeObserver(observerMock1);
        subject.notifyObservers();

        // Vérification : observerMock1 ne reçoit pas la notification, mais observerMock2 oui
        verify(observerMock1, never()).update();
        verify(observerMock2, times(1)).update();
    }

    @Test
    void testNotifierSansObservateurs() {
        subject.notifyObservers();
        // Aucune erreur ne doit se produire, test réussi si rien ne plante
    }
}
