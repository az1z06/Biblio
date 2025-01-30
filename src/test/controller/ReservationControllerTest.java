package test.controller;

import controller.ReservationController;
import dao.ReservationDAO;
import model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    private ReservationController reservationController;
    private ReservationDAO reservationDAO;

    @BeforeEach
    void setUp() {
        reservationDAO = mock(ReservationDAO.class);
        reservationController = new ReservationController(reservationDAO);
    }

    @Test
    void testReserverLivre() {
        int idMembre = 1;
        int idLivre = 100;

        when(reservationDAO.reserverLivre(idMembre, idLivre)).thenReturn(true);

        boolean success = reservationController.reserverLivre(idMembre, idLivre);

        assertTrue(success);
        verify(reservationDAO, times(1)).reserverLivre(idMembre, idLivre);
    }

    @Test
    void testSupprimerReservation() {
        int idMembre = 2;
        int idLivre = 101;

        when(reservationDAO.supprimerReservation(idLivre, idMembre)).thenReturn(true);

        boolean success = reservationController.supprimerReservation(idLivre, idMembre);

        assertTrue(success);
        verify(reservationDAO, times(1)).supprimerReservation(idLivre, idMembre);
    }

    @Test
    void testGetAllReservations() {
        List<Reservation> mockReservations = Arrays.asList(
                new Reservation(100, 1, new Date(System.currentTimeMillis())),
                new Reservation(101, 2, new Date(System.currentTimeMillis()))
        );

        when(reservationDAO.getReservations()).thenReturn(mockReservations);

        List<Reservation> reservations = reservationController.getAllReservations();

        assertEquals(2, reservations.size());
        assertEquals(100, reservations.get(0).getID_Livre());
        assertEquals(101, reservations.get(1).getID_Livre());
    }

    @Test
    void testRechercherReservations() {
        List<Reservation> mockReservations = Arrays.asList(
                new Reservation(100, 1, new Date(System.currentTimeMillis()))
        );

        when(reservationDAO.rechercherReservations("100")).thenReturn(mockReservations);

        List<Reservation> result = reservationController.rechercherReservations("100");

        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getID_Livre());
    }
}
