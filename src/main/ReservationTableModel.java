package main;

import model.Reservation;
import model.Livre;
import model.Membre;
import dao.LivreDAO;
import dao.MembreDAO;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ReservationTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations;
    private LivreDAO livreDAO;
    private MembreDAO membreDAO;
    private final String[] columnNames = {"ID Livre", "Nom Livre", "ID Membre", "Nom Membre", "Date de Réservation"};

    public ReservationTableModel(List<Reservation> reservations, LivreDAO livreDAO, MembreDAO membreDAO) {
        this.reservations = reservations != null ? reservations : new ArrayList<>();
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        fireTableDataChanged();
    }

    public Reservation getReservationAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < reservations.size()) {
            return reservations.get(rowIndex);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return reservations.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Reservation reservation = reservations.get(rowIndex);
        Livre livre = livreDAO.getLivreById(reservation.getID_Livre());
        Membre membre = membreDAO.getMembreById(reservation.getID_Membre());

        switch (columnIndex) {
            case 0: return reservation.getID_Livre();
            case 1: return livre != null ? livre.getTitre() : "Inconnu";
            case 2: return reservation.getID_Membre();
            case 3: return membre != null ? membre.getNom() + " " + membre.getPrenom() : "Inconnu";
            case 4: return reservation.getDate_Reservation();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}