package main;

import controller.ReservationController;
import dao.LivreDAO;
import dao.MembreDAO;
import model.Livre;
import model.Membre;
import model.Reservation;
import observateur.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanneauReservations extends JPanel implements Observer{
    private static final long serialVersionUID = 1L;

    private JTable tableReservations;
    private ReservationTableModel reservationTableModel;
    private ReservationController reservationController;
    private JTextField searchField;
    private LivreDAO livreDAO;
    private MembreDAO membreDAO;

    public PanneauReservations(ReservationController reservationController, LivreDAO livreDAO, MembreDAO membreDAO) {
        setLayout(new BorderLayout());

        this.reservationController = reservationController;
        this.reservationController.addObserver(this); // S'abonne comme observateur
        this.livreDAO = livreDAO;
        this.livreDAO = livreDAO;
        this.membreDAO = membreDAO;

        // Barre de recherche
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> rechercherReservations());

        searchPanel.add(new JLabel("Rechercher réservation :"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Tableau des réservations
        reservationTableModel = new ReservationTableModel(reservationController.getAllReservations(), livreDAO, membreDAO);
        tableReservations = new JTable(reservationTableModel);
        JScrollPane scrollPane = new JScrollPane(tableReservations);
        add(scrollPane, BorderLayout.CENTER);

        // Boutons CRUD
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAjouter = new JButton("Ajouter");
        JButton btnSupprimer = new JButton("Supprimer");

        btnAjouter.addActionListener(e -> ajouterReservation());
        btnSupprimer.addActionListener(e -> supprimerReservation());

        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnSupprimer);

        add(panneauBoutons, BorderLayout.SOUTH);

        // Charger les réservations
        chargerReservations();
    }

    private void chargerReservations() {
        List<Reservation> reservations = reservationController.getAllReservations();
        reservationTableModel.setReservations(reservations);
    }

    private void rechercherReservations() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            chargerReservations();
        } else {
            List<Reservation> resultats = reservationController.rechercherReservations(query);
            reservationTableModel.setReservations(resultats);
        }
    }

    private void ajouterReservation() {
        JComboBox<Livre> livreComboBox = new JComboBox<>();
        for (Livre livre : livreDAO.getLivres()) {
            livreComboBox.addItem(livre);
        }

        JComboBox<Membre> membreComboBox = new JComboBox<>();
        for (Membre membre : membreDAO.getMembres()) {
            membreComboBox.addItem(membre);
        }

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Livre:"));
        panel.add(livreComboBox);
        panel.add(new JLabel("Membre:"));
        panel.add(membreComboBox);

        int option = JOptionPane.showConfirmDialog(this, panel, "Ajouter une réservation", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Livre livre = (Livre) livreComboBox.getSelectedItem();
            Membre membre = (Membre) membreComboBox.getSelectedItem();

            if (livre != null && membre != null) {
                boolean success = reservationController.reserverLivre(membre.getID_Membre(), livre.getID_Livre());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Réservation ajoutée avec succès.");
                    chargerReservations();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void supprimerReservation() {
        int selectedRow = tableReservations.getSelectedRow();
        if (selectedRow >= 0) {
            Reservation reservation = reservationTableModel.getReservationAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette réservation ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = reservationController.supprimerReservation(reservation.getID_Livre(), reservation.getID_Membre());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Réservation supprimée avec succès.");
                    chargerReservations();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une réservation à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }


    @Override
    public void update() {
        try {
            chargerReservations();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour des réservations : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}