package test.model;

import model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private Reservation reservation;
    private Date dateReservation;

    @BeforeEach
    void setUp() {
        // Initialisation d'une date de réservation
        dateReservation = new Date(System.currentTimeMillis());

        // Création d'une réservation avant chaque test
        reservation = new Reservation(101, 202, dateReservation);
    }

    @Test
    void testGetters() {
        assertEquals(101, reservation.getID_Livre());
        assertEquals(202, reservation.getID_Membre());
        assertEquals(dateReservation, reservation.getDate_Reservation());
    }

    @Test
    void testReservationNotNull() {
        assertNotNull(reservation.getDate_Reservation(), "La date de réservation ne doit pas être null");
    }
}
