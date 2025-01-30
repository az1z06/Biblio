package main;

import controller.LivreController;
import model.Livre;
import model.LivreAudio;
import model.LivreNumerique;
import observateur.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanneauLivres extends JPanel implements Observer{
    private static final long serialVersionUID = 1L;

    private JTable tableLivres;
    private LivreController livreController;
    private JTextField searchField;

    public PanneauLivres(LivreController livreController) {
        this.livreController = livreController;
        this.livreController.addObserver(this);
        setLayout(new BorderLayout());

        // Barre de recherche
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Rechercher");
        searchButton.addActionListener(e -> rechercherLivres());
        searchPanel.add(new JLabel("Rechercher livre :"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Tableau des livres
        initTable();
        add(new JScrollPane(tableLivres), BorderLayout.CENTER);

        // Boutons CRUD
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

        btnAjouter.addActionListener(e -> ajouterLivre());
        btnModifier.addActionListener(e -> modifierLivre());
        btnSupprimer.addActionListener(e -> supprimerLivre());

        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnModifier);
        panneauBoutons.add(btnSupprimer);
        add(panneauBoutons, BorderLayout.SOUTH);

        // Charger les livres
        chargerLivres();
    }

    private void initTable() {
        String[] columnNames = {"ID", "Titre", "Type", "Année", "Editeur", "Catégorie"};
        tableLivres = new JTable(new DefaultTableModel(columnNames, 0));
    }

    private void chargerLivres() {
        List<Livre> livres = livreController.getAllLivres();
        DefaultTableModel model = (DefaultTableModel) tableLivres.getModel();
        model.setRowCount(0);

        for (Livre livre : livres) {
            String type = livre instanceof LivreNumerique ? "Numerique" :
                          livre instanceof LivreAudio ? "Audio" : "Physique";
            model.addRow(new Object[]{
                livre.getID_Livre(), livre.getTitre(), type, livre.getAnnee_Publication(),
                livre.getNomEditeur(), livre.getNomCategorie()
            });
        }
    }

    private void rechercherLivres() {
        String query = searchField.getText().trim();
        List<Livre> livres = query.isEmpty() ? livreController.getAllLivres() : livreController.rechercherLivres(query);
        DefaultTableModel model = (DefaultTableModel) tableLivres.getModel();
        model.setRowCount(0);

        for (Livre livre : livres) {
            String type = livre instanceof LivreNumerique ? "Numerique" :
                          livre instanceof LivreAudio ? "Audio" : "Physique";
            model.addRow(new Object[]{
                livre.getID_Livre(), livre.getTitre(), type, livre.getAnnee_Publication(),
                livre.getNomEditeur(), livre.getNomCategorie()
            });
        }
    }

    private JComboBox<String> createEditeurComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        List<String> editeurs = livreController.getAllEditeurs();
        for (String editeur : editeurs) {
            comboBox.addItem(editeur);
        }
        return comboBox;
    }

    private JComboBox<String> createCategorieComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        List<String> categories = livreController.getAllCategories();
        for (String categorie : categories) {
            comboBox.addItem(categorie);
        }
        return comboBox;
    }

    private void ajouterLivre() {
        JTextField titreField = new JTextField(20);
        JTextField anneeField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JComboBox<String> typeField = new JComboBox<>(new String[]{"Physique", "Numerique", "Audio"});
        JComboBox<String> editeurField = createEditeurComboBox();
        JComboBox<String> categorieField = createCategorieComboBox();

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Titre :"));
        panel.add(titreField);
        panel.add(new JLabel("Année de publication :"));
        panel.add(anneeField);
        panel.add(new JLabel("ISBN :"));
        panel.add(isbnField);
        panel.add(new JLabel("Type :"));
        panel.add(typeField);
        panel.add(new JLabel("Editeur :"));
        panel.add(editeurField);
        panel.add(new JLabel("Catégorie :"));
        panel.add(categorieField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Ajouter un livre", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String titre = titreField.getText().trim();
                int anneePublication = Integer.parseInt(anneeField.getText().trim());
                String isbn = isbnField.getText().trim();
                String type = (String) typeField.getSelectedItem();
                int idEditeur = Integer.parseInt(editeurField.getSelectedItem().toString().split(" - ")[0]);
                int idCategorie = Integer.parseInt(categorieField.getSelectedItem().toString().split(" - ")[0]);

                Livre livre;
                switch (type) {
                    case "Numerique":
                        livre = new LivreNumerique(0, titre, anneePublication, isbn, idEditeur, idCategorie, "PDF", 1.5);
                        break;
                    case "Audio":
                        livre = new LivreAudio(0, titre, anneePublication, isbn, idEditeur, idCategorie, 2.0, "Narrateur inconnu");
                        break;
                    default:
                        livre = new Livre(0, titre, anneePublication, isbn, idEditeur, idCategorie);
                }

                boolean success = livreController.ajouterLivre(livre);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Livre ajouté avec succès.");
                    chargerLivres();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Les champs numériques doivent être valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur inattendue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modifierLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow >= 0) {
            Livre livre = livreController.getAllLivres().get(selectedRow);

            JTextField titreField = new JTextField(livre.getTitre(), 20);
            JTextField anneeField = new JTextField(String.valueOf(livre.getAnnee_Publication()), 20);
            JTextField isbnField = new JTextField(livre.getISBN(), 20);
            JComboBox<String> typeField = new JComboBox<>(new String[]{"Physique", "Numerique", "Audio"});
            JComboBox<String> editeurField = createEditeurComboBox();
            JComboBox<String> categorieField = createCategorieComboBox();

            typeField.setSelectedItem(livre instanceof LivreNumerique ? "Numerique" :
                                      livre instanceof LivreAudio ? "Audio" : "Physique");

            JPanel panel = new JPanel(new GridLayout(6, 2));
            panel.add(new JLabel("Titre :"));
            panel.add(titreField);
            panel.add(new JLabel("Année de publication :"));
            panel.add(anneeField);
            panel.add(new JLabel("ISBN :"));
            panel.add(isbnField);
            panel.add(new JLabel("Type :"));
            panel.add(typeField);
            panel.add(new JLabel("Editeur :"));
            panel.add(editeurField);
            panel.add(new JLabel("Catégorie :"));
            panel.add(categorieField);

            int option = JOptionPane.showConfirmDialog(this, panel, "Modifier un livre", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String titre = titreField.getText().trim();
                    int anneePublication = Integer.parseInt(anneeField.getText().trim());
                    String isbn = isbnField.getText().trim();
                    String type = (String) typeField.getSelectedItem();
                    int idEditeur = Integer.parseInt(editeurField.getSelectedItem().toString().split(" - ")[0]);
                    int idCategorie = Integer.parseInt(categorieField.getSelectedItem().toString().split(" - ")[0]);

                    Livre updatedLivre;
                    switch (type) {
                        case "Numerique":
                            updatedLivre = new LivreNumerique(livre.getID_Livre(), titre, anneePublication, isbn, idEditeur, idCategorie, "PDF", 1.5);
                            break;
                        case "Audio":
                            updatedLivre = new LivreAudio(livre.getID_Livre(), titre, anneePublication, isbn, idEditeur, idCategorie, 2.0, "Narrateur inconnu");
                            break;
                        default:
                            updatedLivre = new Livre(livre.getID_Livre(), titre, anneePublication, isbn, idEditeur, idCategorie);
                    }

                    boolean success = livreController.mettreAJourLivre(updatedLivre);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Livre modifié avec succès.");
                        chargerLivres();
                    } else {
                        JOptionPane.showMessageDialog(this, "Erreur lors de la modification du livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Les champs numériques doivent être valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void supprimerLivre() {
        int selectedRow = tableLivres.getSelectedRow();
        if (selectedRow >= 0) {
            Livre livre = livreController.getAllLivres().get(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce livre ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = livreController.supprimerLivre(livre.getID_Livre());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Livre supprimé avec succès.");
                    chargerLivres();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du livre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un livre à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }
    @Override
    public void update() {
        
        chargerLivres();
    }
}
