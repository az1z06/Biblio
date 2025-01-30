package controller;

import dao.ReservationDAO;
import model.Reservation;
import observateur.Subject;

import java.util.List;

public class ReservationController extends Subject{
    private ReservationDAO reservationDAO;

    public ReservationController(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public boolean reserverLivre(int idMembre, int idLivre) {
        boolean success = reservationDAO.reserverLivre(idMembre, idLivre);
        if (success) {
            notifyObservers(); // Notifie les observateurs d'une mise à jour
        }
        return success;
    }

    public boolean supprimerReservation(int idLivre, int idMembre) {
        boolean success = reservationDAO.supprimerReservation(idLivre, idMembre);
        if (success) {
            notifyObservers(); // Notifie les observateurs d'une mise à jour
        }
        return success;
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.getReservations();
    }

    public List<Reservation> rechercherReservations(String query) {
        return reservationDAO.rechercherReservations(query);
    }
}