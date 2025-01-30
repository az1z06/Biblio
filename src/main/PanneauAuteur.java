package main;

import controller.AuteurController;
import model.Auteur;
import observateur.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class PanneauAuteur extends JPanel implements Observer{
    private static final long serialVersionUID = 1L;


    private JTable tableAuteurs;
    private AuteurController auteurController;

    public PanneauAuteur(AuteurController auteurController) {
        this.auteurController = auteurController;
        this.auteurController.addObserver(this); // S'abonne au contrôleur
        setLayout(new BorderLayout());

        // Tableau des auteurs
        initTable();
        add(new JScrollPane(tableAuteurs), BorderLayout.CENTER);

        // Boutons CRUD
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

        btnAjouter.addActionListener(e -> ajouterAuteur());
        btnModifier.addActionListener(e -> modifierAuteur());
        btnSupprimer.addActionListener(e -> supprimerAuteur());

        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnModifier);
        panneauBoutons.add(btnSupprimer);
        add(panneauBoutons, BorderLayout.SOUTH);

        chargerAuteurs();
    }

    private void initTable() {
        String[] columnNames = {"ID", "Nom", "Prénom", "Date de Naissance"};
        tableAuteurs = new JTable(new DefaultTableModel(columnNames, 0));
    }

    private void chargerAuteurs() {
        List<Auteur> auteurs = auteurController.getAllAuteurs();
        DefaultTableModel model = (DefaultTableModel) tableAuteurs.getModel();
        model.setRowCount(0);

        for (Auteur auteur : auteurs) {
            model.addRow(new Object[]{
                auteur.getID_Auteur(), auteur.getNom(), auteur.getPrenom(), auteur.getDate_Naissance()
            });
        }
    }

    private void ajouterAuteur() {
        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField dateNaissanceField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Nom :"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom :"));
        panel.add(prenomField);
        panel.add(new JLabel("Date de Naissance (YYYY-MM-DD) :"));
        panel.add(dateNaissanceField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Ajouter un auteur", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String dateNaissance = dateNaissanceField.getText().trim();

                Auteur auteur = new Auteur(0, nom, prenom, Date.valueOf(dateNaissance));
                boolean success = auteurController.ajouterAuteur(auteur);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Auteur ajouté avec succès.");
                    chargerAuteurs();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'auteur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifierAuteur() {
        int selectedRow = tableAuteurs.getSelectedRow();
        if (selectedRow >= 0) {
            Auteur auteur = auteurController.getAllAuteurs().get(selectedRow);

            JTextField nomField = new JTextField(auteur.getNom(), 20);
            JTextField prenomField = new JTextField(auteur.getPrenom(), 20);
            JTextField dateNaissanceField = new JTextField(auteur.getDate_Naissance().toString(), 20);

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Nom :"));
            panel.add(nomField);
            panel.add(new JLabel("Prénom :"));
            panel.add(prenomField);
            panel.add(new JLabel("Date de Naissance (YYYY-MM-DD) :"));
            panel.add(dateNaissanceField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Modifier un auteur", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String nom = nomField.getText().trim();
                    String prenom = prenomField.getText().trim();
                    String dateNaissance = dateNaissanceField.getText().trim();

                    Auteur updatedAuteur = new Auteur(auteur.getID_Auteur(), nom, prenom, Date.valueOf(dateNaissance));
                    boolean success = auteurController.mettreAJourAuteur(updatedAuteur);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Auteur modifié avec succès.");
                        chargerAuteurs();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'auteur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un auteur à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void supprimerAuteur() {
        int selectedRow = tableAuteurs.getSelectedRow();
        if (selectedRow >= 0) {
            Auteur auteur = auteurController.getAllAuteurs().get(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet auteur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = auteurController.supprimerAuteur(auteur.getID_Auteur());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Auteur supprimé avec succès.");
                    chargerAuteurs();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'auteur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un auteur à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public void update() {
        // Mettre à jour la table automatiquement
        chargerAuteurs();
    }
}
