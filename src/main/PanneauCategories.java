package main;

import composite.CategorieComposite;
import composite.ComposantCategorie;
import controller.CategorieController;
import observateur.Observer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import java.awt.*;


public class PanneauCategories extends JPanel implements Observer{
    private static final long serialVersionUID = 1L;

    private JTree tree;
    private CategorieController categorieController;

    public PanneauCategories(CategorieController categorieController) {
        this.categorieController = categorieController;
        this.categorieController.addObserver(this); // S'abonne au contrôleur
        setLayout(new BorderLayout());

        // Construire l'arbre des catégories
        CategorieComposite racineComposite = categorieController.construireArbreCategories();

        // Créer la racine pour le JTree
        DefaultMutableTreeNode racineNode = creerNoeudArborescence(racineComposite);

        // Créer le modèle pour le JTree
        TreeModel treeModel = new DefaultTreeModel(racineNode);
        tree = new JTree(treeModel);
        tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tree.setRowHeight(25);
        tree.setFont(new Font("Arial", Font.PLAIN, 14));

        // Étendre tous les nœuds
        expandAllNodes(tree, 0, tree.getRowCount());

        // Ajouter l'arbre dans un JScrollPane
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);

        // Ajouter un menu contextuel
        JPopupMenu menuContextuel = new JPopupMenu();

        JMenuItem ajouterCategorie = new JMenuItem("Ajouter Catégorie");
        JMenuItem supprimerCategorie = new JMenuItem("Supprimer Catégorie");

        // Ajouter actions pour le menu
        ajouterCategorie.addActionListener(e -> ajouterCategorie());
        supprimerCategorie.addActionListener(e -> supprimerCategorie());

        menuContextuel.add(ajouterCategorie);
        menuContextuel.add(supprimerCategorie);

        // Ajouter le menu contextuel à l'arbre
        tree.setComponentPopupMenu(menuContextuel);
    }

    /**
     * Méthode pour ajouter une catégorie.
     */
   
    private void ajouterCategorie() {
        JTextField nomField = new JTextField(20);

        DefaultMutableTreeNode nodeSelectionne = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (nodeSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une catégorie ou la racine.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Détecter si la racine est sélectionnée
        boolean isRoot = nodeSelectionne.isRoot();
        Integer parentId = null;

        if (!isRoot) {
            CategorieComposite parentCategorie = (CategorieComposite) nodeSelectionne.getUserObject();
            parentId = parentCategorie.getIdCategorie(); // ID du parent
        }

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Nom de la catégorie :"));
        panel.add(nomField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter une catégorie", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nom = nomField.getText().trim();
                if (nom.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Le nom de la catégorie ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ajouter la catégorie avec ou sans parent
                categorieController.ajouterCategorie(nom, parentId);
                JOptionPane.showMessageDialog(this, "Catégorie ajoutée avec succès.");
                rafraichirArbre();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Méthode pour supprimer une catégorie.
     */
    private void supprimerCategorie() {
        DefaultMutableTreeNode nodeSelectionne = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (nodeSelectionne == null || nodeSelectionne.isRoot()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une catégorie valide à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Récupérer l'objet catégorie
        CategorieComposite categorie = (CategorieComposite) nodeSelectionne.getUserObject();

        // Vérifier si la catégorie contient des enfants
        if (!categorie.getEnfants().isEmpty()) {
            StringBuilder enfants = new StringBuilder();
            for (ComposantCategorie enfant : categorie.getEnfants()) {
                enfants.append("- ").append(enfant.getNom()).append("\n");
            }

            JOptionPane.showMessageDialog(
                this,
                "Impossible de supprimer la catégorie \"" + categorie.getNom() + "\" car elle contient les sous-catégories ou livres suivants :\n" + enfants,
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Confirmation de la suppression
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Supprimer cette catégorie : " + categorie.getNom() + " ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                categorieController.supprimerCategorie(categorie.getIdCategorie());
                JOptionPane.showMessageDialog(this, "Catégorie supprimée avec succès.");
                rafraichirArbre();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Méthode récursive pour créer les nœuds de l’arbre.
     */
    private DefaultMutableTreeNode creerNoeudArborescence(ComposantCategorie composant) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(composant);

        for (ComposantCategorie enfant : composant.getEnfants()) {
            node.add(creerNoeudArborescence(enfant));
        }
        return node;
    }

    /**
     * Étendre tous les nœuds de l’arbre.
     */
    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; i++) {
            tree.expandRow(i);
        }
        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    /**
     * Rafraîchir l’arbre des catégories.
     */
    private void rafraichirArbre() {
        CategorieComposite racineComposite = categorieController.construireArbreCategories();
        DefaultMutableTreeNode racineNode = creerNoeudArborescence(racineComposite);
        tree.setModel(new DefaultTreeModel(racineNode));
        expandAllNodes(tree, 0, tree.getRowCount());
    }
    
    @Override
    public void update() {
        // Met à jour l'arbre automatiquement
        rafraichirArbre();
    }
}
