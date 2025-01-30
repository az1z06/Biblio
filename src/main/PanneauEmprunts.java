package main;

import controller.EmpruntController;
import model.Emprunt;
import observateur.Observer;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class PanneauEmprunts extends JPanel implements Observer{
    private static final long serialVersionUID = 1L;

    private JTable tableEmprunts;
    private EmpruntTableModel empruntTableModel;
    private EmpruntController empruntController;
    private JTextField searchField;

    public PanneauEmprunts(EmpruntController empruntController) {
        setLayout(new BorderLayout());

        this.empruntController = empruntController;
        this.empruntController.addObserver(this); // Inscription comme observateur

        // Barre de recherche
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> rechercherEmprunts());

        searchPanel.add(new JLabel("Rechercher emprunt :"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Tableau des emprunts
        empruntTableModel = new EmpruntTableModel(empruntController.getAllEmprunts());
        tableEmprunts = new JTable(empruntTableModel);
        JScrollPane scrollPane = new JScrollPane(tableEmprunts);
        add(scrollPane, BorderLayout.CENTER);

        // Boutons CRUD
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnRetour = new JButton("Date retour");

        btnAjouter.addActionListener(e -> ajouterEmprunt());
        btnModifier.addActionListener(e -> modifierEmprunt());
        btnSupprimer.addActionListener(e -> supprimerEmprunt());
        btnRetour.addActionListener(e -> mettreDateRetourEffective());

        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnModifier);
        panneauBoutons.add(btnSupprimer);
        panneauBoutons.add(btnRetour);

        add(panneauBoutons, BorderLayout.SOUTH);

        // Charger les emprunts
        chargerEmprunts();
    }

    private void chargerEmprunts() {
        List<Emprunt> emprunts = empruntController.getAllEmprunts();
        empruntTableModel.setEmprunts(emprunts);
    }

    private void rechercherEmprunts() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            chargerEmprunts();
        } else {
            List<Emprunt> resultats = empruntController.rechercherEmprunts(query);
            empruntTableModel.setEmprunts(resultats);
        }
    }

    private void ajouterEmprunt() {
        JTextField idLivreField = new JTextField(20);
        JTextField idMembreField = new JTextField(20);
        JTextField dateEmpruntField = new JTextField(20);
        JTextField dateRetourPrevueField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("ID Livre:"));
        panel.add(idLivreField);
        panel.add(new JLabel("ID Membre:"));
        panel.add(idMembreField);
        panel.add(new JLabel("Date Emprunt (AAAA-MM-JJ):"));
        panel.add(dateEmpruntField);
        panel.add(new JLabel("Date Retour Prévue (AAAA-MM-JJ):"));
        panel.add(dateRetourPrevueField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Ajouter un emprunt", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int idLivre = Integer.parseInt(idLivreField.getText().trim());
                int idMembre = Integer.parseInt(idMembreField.getText().trim());
                Date dateEmprunt = Date.valueOf(dateEmpruntField.getText().trim());
                Date dateRetourPrevue = Date.valueOf(dateRetourPrevueField.getText().trim());

                Emprunt emprunt = new Emprunt(0, idLivre, idMembre, dateEmprunt, dateRetourPrevue, null);
                empruntController.emprunter(emprunt);

                chargerEmprunts();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Les champs numériques doivent être valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifierEmprunt() {
        int selectedRow = tableEmprunts.getSelectedRow();
        if (selectedRow >= 0) {
            Emprunt emprunt = empruntTableModel.getEmpruntAt(selectedRow);

            JTextField idLivreField = new JTextField(String.valueOf(emprunt.getID_Livre()), 20);
            JTextField idMembreField = new JTextField(String.valueOf(emprunt.getID_Membre()), 20);
            JTextField dateEmpruntField = new JTextField(emprunt.getDate_Emprunt().toString(), 20);
            JTextField dateRetourPrevueField = new JTextField(emprunt.getDate_Retour_Prevue().toString(), 20);

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("ID Livre:"));
            panel.add(idLivreField);
            panel.add(new JLabel("ID Membre:"));
            panel.add(idMembreField);
            panel.add(new JLabel("Date Emprunt (AAAA-MM-JJ):"));
            panel.add(dateEmpruntField);
            panel.add(new JLabel("Date Retour Prévue (AAAA-MM-JJ):"));
            panel.add(dateRetourPrevueField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Modifier un emprunt", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int idLivre = Integer.parseInt(idLivreField.getText().trim());
                    int idMembre = Integer.parseInt(idMembreField.getText().trim());
                    Date dateEmprunt = Date.valueOf(dateEmpruntField.getText().trim());
                    Date dateRetourPrevue = Date.valueOf(dateRetourPrevueField.getText().trim());

                    Emprunt updatedEmprunt = new Emprunt(emprunt.getID_Emprunt(), idLivre, idMembre, dateEmprunt, dateRetourPrevue, null);
                    boolean success = empruntController.modifierEmprunt(updatedEmprunt);

                    if (success) {
                        JOptionPane.showMessageDialog(this, "Emprunt modifié avec succès.");
                        chargerEmprunts();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'emprunt.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Les champs numériques doivent être valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void supprimerEmprunt() {
        int selectedRow = tableEmprunts.getSelectedRow();
        if (selectedRow >= 0) {
            Emprunt emprunt = empruntTableModel.getEmpruntAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet emprunt ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = empruntController.supprimerEmprunt(emprunt.getID_Emprunt());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Emprunt supprimé avec succès.");
                    chargerEmprunts();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'emprunt.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void mettreDateRetourEffective() {
        int selectedRow = tableEmprunts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt pour mettre à jour la date de retour effective.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Emprunt emprunt = empruntTableModel.getEmpruntAt(selectedRow);

        Date now = new Date(System.currentTimeMillis());
        emprunt.setDate_Retour_Effective(now);
        empruntController.mettreAJourEmprunt(emprunt);

        empruntTableModel.fireTableRowsUpdated(selectedRow, selectedRow);
        JOptionPane.showMessageDialog(this, "Date de retour effective mise à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void update() {
        // Recharge automatiquement les emprunts en cas de modification
        chargerEmprunts();
    }
}
