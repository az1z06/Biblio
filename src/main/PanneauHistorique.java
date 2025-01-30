package main;

import command.Command;
import command.CommandManager;
import dao.DBConnection;
import dao.HistoriqueCommandeDAO;
import observateur.Observer;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PanneauHistorique extends JPanel implements Observer {
    private static final long serialVersionUID = 1L;

    private HistoriqueCommandeDAO historiqueCommandeDAO;
    private DefaultListModel<String> listModel;
    private JList<String> commandeList;

    public PanneauHistorique(HistoriqueCommandeDAO historiqueCommandeDAO, CommandManager commandManager) {
        this.historiqueCommandeDAO = historiqueCommandeDAO;
        commandManager.addObserver(this);  // Inscription comme observateur du CommandManager
        setLayout(new BorderLayout());

        // Liste des commandes
        listModel = new DefaultListModel<>();
        commandeList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(commandeList);

        // Boutons pour les actions
        JButton btnAnnuler = new JButton("Annuler Commande");
        JButton btnRejouer = new JButton("Rejouer Commande");

        btnAnnuler.addActionListener(e -> annulerCommande());
        btnRejouer.addActionListener(e -> rejouerCommande());

        JPanel boutonsPanel = new JPanel();
        boutonsPanel.add(btnAnnuler);
        boutonsPanel.add(btnRejouer);

        add(new JLabel("Historique des commandes"), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(boutonsPanel, BorderLayout.SOUTH);

        rafraichirHistorique();
    }

    private void annulerCommande() {
        int selectedIndex = commandeList.getSelectedIndex();
        if (selectedIndex >= 0) {
            Command command = historiqueCommandeDAO.getCommandByIndex(selectedIndex);
            command.annule();
            historiqueCommandeDAO.mettreAJourEtatCommande(command.getEmprunt().getID_Emprunt(), "ANNULEE");
            rafraichirHistorique();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à annuler.");
        }
    }

    private void rejouerCommande() {
        int selectedIndex = commandeList.getSelectedIndex();
        if (selectedIndex >= 0) {
            Command command = historiqueCommandeDAO.getCommandByIndex(selectedIndex);
            command.execute();
            historiqueCommandeDAO.mettreAJourEtatCommande(command.getEmprunt().getID_Emprunt(), "EN_COURS");
            rafraichirHistorique();
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande à rejouer.");
        }
    }

    public void rafraichirHistorique() {
        listModel.clear();

        String query = "SELECT * FROM historique_commandes";

        try (Connection conn = DBConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String commande = "ID Emprunt: " + rs.getInt("ID_Emprunt") +
                                  " - Livre: " + rs.getString("Nom_Livre") +
                                  " - Membre: " + rs.getString("Nom_Membre") +
                                  " - État: " + rs.getString("Etat_Commande") +
                                  " - Date: " + rs.getTimestamp("Date_Commande").toString();
                listModel.addElement(commande);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du rafraîchissement de l'historique : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);

        }
    }
    
    @Override
    public void update() {
        rafraichirHistorique();
    }

}
