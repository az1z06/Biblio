package main;

import controller.MembreController;
import model.Membre;
import observateur.Observer;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PanneauMembres extends JPanel implements Observer{
    private static final long serialVersionUID = 1L;

    private JTable tableMembres;
    private MembreTableModel membreTableModel;
    private MembreController membreController;
    private JTextField searchField;
    private JButton btnAjouter, btnModifier, btnSupprimer;

    public PanneauMembres(MembreController membreController) {
        setLayout(new BorderLayout());

        this.membreController = membreController;
        this.membreController.addObserver(this); // Ajoute ce panneau comme observateur

        // Barre de recherche
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> rechercherMembres());

        searchPanel.add(new JLabel("Rechercher membre :"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Tableau des membres
        membreTableModel = new MembreTableModel(membreController.getAllMembres());
        tableMembres = new JTable(membreTableModel);
        JScrollPane scrollPane = new JScrollPane(tableMembres);
        add(scrollPane, BorderLayout.CENTER);

        // Boutons CRUD
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnAjouter = new JButton("Ajouter");
        btnModifier = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        btnAjouter.addActionListener(e -> ajouterMembre());
        btnModifier.addActionListener(e -> modifierMembre());
        btnSupprimer.addActionListener(e -> supprimerMembre());

        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnModifier);
        panneauBoutons.add(btnSupprimer);

        add(panneauBoutons, BorderLayout.SOUTH);

        // Charger les membres
        chargerMembres();
    }

    private void chargerMembres() {
        List<Membre> membres = membreController.getAllMembres();
        membreTableModel.setMembres(membres);
    }

    private void rechercherMembres() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            chargerMembres();
        } else {
            List<Membre> resultats = membreController.rechercherMembres(query);
            membreTableModel.setMembres(resultats);
        }
    }

    private void ajouterMembre() {
        JTextField nomField = new JTextField(20);
        JTextField prenomField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField dateInscriptionField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Date d'Inscription (AAAA-MM-JJ):"));
        panel.add(dateInscriptionField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Ajouter un membre", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String email = emailField.getText().trim();
                String dateInscriptionStr = dateInscriptionField.getText().trim();

                Date dateInscription = new SimpleDateFormat("yyyy-MM-dd").parse(dateInscriptionStr);

                Membre membre = new Membre(0, nom, prenom, email, dateInscription);
                boolean success = membreController.ajouterMembre(membre);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Membre ajouté avec succès.");
                    chargerMembres();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du membre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Les champs numériques doivent être valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur dans la saisie des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifierMembre() {
        int selectedRow = tableMembres.getSelectedRow();
        if (selectedRow >= 0) {
            Membre membre = membreTableModel.getMembreAt(selectedRow);

            JTextField nomField = new JTextField(membre.getNom(), 20);
            JTextField prenomField = new JTextField(membre.getPrenom(), 20);
            JTextField emailField = new JTextField(membre.getEmail(), 20);
            JTextField dateInscriptionField = new JTextField(
                    new SimpleDateFormat("yyyy-MM-dd").format(membre.getDate_Inscription()), 20);

            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Nom:"));
            panel.add(nomField);
            panel.add(new JLabel("Prénom:"));
            panel.add(prenomField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);
            panel.add(new JLabel("Date d'Inscription (AAAA-MM-JJ):"));
            panel.add(dateInscriptionField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Modifier un membre", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String newNom = nomField.getText().trim();
                    String newPrenom = prenomField.getText().trim();
                    String newEmail = emailField.getText().trim();
                    String newDateInscriptionStr = dateInscriptionField.getText().trim();

                    Date newDateInscription = new SimpleDateFormat("yyyy-MM-dd").parse(newDateInscriptionStr);

                    Membre updatedMembre = new Membre(
                            membre.getID_Membre(), newNom, newPrenom, newEmail, newDateInscription
                    );

                    boolean success = membreController.mettreAJourMembre(updatedMembre);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Membre modifié avec succès.");
                        chargerMembres();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification du membre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Les champs numériques doivent être valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur dans la saisie des données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void supprimerMembre() {
        int selectedRow = tableMembres.getSelectedRow();
        if (selectedRow >= 0) {
            Membre membre = membreTableModel.getMembreAt(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce membre ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = membreController.supprimerMembre(membre.getID_Membre());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Membre supprimé avec succès.");
                    chargerMembres();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du membre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public void update() {
       try {
    	   chargerMembres(); // Rafraîchit la table des membres automatiquement
	} catch (Exception ex) {
		JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour des membres : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
	} 
    }
}
