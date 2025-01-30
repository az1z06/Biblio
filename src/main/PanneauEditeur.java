package main;

import controller.EditeurController;
import model.Editeur;
import observateur.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanneauEditeur extends JPanel implements Observer{
    private static final long serialVersionUID = 1L;

    private JTable tableEditeurs;
    private EditeurController editeurController;

    public PanneauEditeur(EditeurController editeurController) {
        this.editeurController = editeurController;
        this.editeurController.addObserver(this);
        setLayout(new BorderLayout());

        // Tableau des éditeurs
        initTable();
        add(new JScrollPane(tableEditeurs), BorderLayout.CENTER);

        // Boutons CRUD
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

        btnAjouter.addActionListener(e -> ajouterEditeur());
        btnModifier.addActionListener(e -> modifierEditeur());
        btnSupprimer.addActionListener(e -> supprimerEditeur());

        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnModifier);
        panneauBoutons.add(btnSupprimer);
        add(panneauBoutons, BorderLayout.SOUTH);

        chargerEditeurs();
    }

    private void initTable() {
        String[] columnNames = {"ID", "Nom", "Adresse"};
        tableEditeurs = new JTable(new DefaultTableModel(columnNames, 0));
    }

    private void chargerEditeurs() {
        List<Editeur> editeurs = editeurController.getAllEditeurs();
        DefaultTableModel model = (DefaultTableModel) tableEditeurs.getModel();
        model.setRowCount(0);

        for (Editeur editeur : editeurs) {
            model.addRow(new Object[]{editeur.getIdEditeur(), editeur.getNom(), editeur.getAdresse()});
        }
    }

    private void ajouterEditeur() {
        JTextField nomField = new JTextField(20);
        JTextField adresseField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Nom :"));
        panel.add(nomField);
        panel.add(new JLabel("Adresse :"));
        panel.add(adresseField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Ajouter un éditeur", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nom = nomField.getText().trim();
            String adresse = adresseField.getText().trim();
            if (!nom.isEmpty() && !adresse.isEmpty()) {
                boolean success = editeurController.ajouterEditeur(new Editeur(0, nom, adresse));
                if (success) {
                    JOptionPane.showMessageDialog(this, "Éditeur ajouté avec succès.");
                    chargerEditeurs();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void modifierEditeur() {
        int selectedRow = tableEditeurs.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableEditeurs.getValueAt(selectedRow, 0);
            String nom = (String) tableEditeurs.getValueAt(selectedRow, 1);
            String adresse = (String) tableEditeurs.getValueAt(selectedRow, 2);

            JTextField nomField = new JTextField(nom, 20);
            JTextField adresseField = new JTextField(adresse, 20);

            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Nom :"));
            panel.add(nomField);
            panel.add(new JLabel("Adresse :"));
            panel.add(adresseField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Modifier un éditeur", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String newNom = nomField.getText().trim();
                String newAdresse = adresseField.getText().trim();
                if (!newNom.isEmpty() && !newAdresse.isEmpty()) {
                    boolean success = editeurController.modifierEditeur(new Editeur(id, newNom, newAdresse));
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Éditeur modifié avec succès.");
                        chargerEditeurs();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un éditeur à modifier.");
        }
    }

    private void supprimerEditeur() {
        int selectedRow = tableEditeurs.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableEditeurs.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cet éditeur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = editeurController.supprimerEditeur(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Éditeur supprimé avec succès.");
                    chargerEditeurs();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un éditeur à supprimer.");
        }
    }
    
    @Override
    public void update() {
        // Recharge automatiquement les éditeurs
        chargerEditeurs();
    }
}
