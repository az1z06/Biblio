package dao;

import composite.CategorieComposite;
import composite.LivreFeuille;
import model.Livre;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorieDAO {

    // Ajouter une catégorie
    public void ajouterCategorie(CategorieComposite categorie) {
        String query = "INSERT INTO categorie (Nom, ID_Categorie_Parent) VALUES (?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categorie.getNom());
            if (categorie.getParentId() == null) {
                stmt.setNull(2, Types.INTEGER);
            } else {
                stmt.setInt(2, categorie.getParentId());
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    categorie.setIdCategorie(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la catégorie.", e);
        }
    }

    // Supprimer une catégorie par ID
    public void supprimerCategorie(int idCategorie) {
        String deleteLivresQuery = "DELETE FROM livre WHERE ID_Categorie = ?";
        String deleteSubcategoriesQuery = "DELETE FROM categorie WHERE ID_Categorie_Parent = ?";
        String deleteCategoryQuery = "DELETE FROM categorie WHERE ID_Categorie = ?";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            conn.setAutoCommit(false); // Démarrer une transaction

            // Étape 1 : Supprimer tous les livres associés à cette catégorie
            try (PreparedStatement deleteLivresStmt = conn.prepareStatement(deleteLivresQuery)) {
                deleteLivresStmt.setInt(1, idCategorie);
                deleteLivresStmt.executeUpdate();
            }

            // Étape 2 : Supprimer les sous-catégories
            try (PreparedStatement deleteSubStmt = conn.prepareStatement(deleteSubcategoriesQuery)) {
                deleteSubStmt.setInt(1, idCategorie);
                deleteSubStmt.executeUpdate();
            }

            // Étape 3 : Supprimer la catégorie elle-même
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteCategoryQuery)) {
                deleteStmt.setInt(1, idCategorie);
                deleteStmt.executeUpdate();
            }

            conn.commit(); // Valider la transaction
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la catégorie.", e);
        }
    }



    // Construire l'arbre des catégories
    public CategorieComposite construireArbreCategories() {
        String query = "SELECT * FROM categorie";
        Map<Integer, CategorieComposite> categoriesMap = new HashMap<>();
        CategorieComposite root = new CategorieComposite(0, "Racine");
        categoriesMap.put(0, root); // Ajoute la racine au map

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID_Categorie");
                String nom = rs.getString("Nom");
                Integer parentId = rs.getInt("ID_Categorie_Parent");
                if (rs.wasNull()) parentId = null;

                CategorieComposite categorie = new CategorieComposite(id, nom);
                categoriesMap.put(id, categorie);

                if (parentId == null) {
                    root.ajouter(categorie);
                } else {
                    CategorieComposite parent = categoriesMap.get(parentId);
                    if (parent != null) {
                        parent.ajouter(categorie);
                        categorie.setParent(parent);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des catégories.", e);
        }

        return root;
    }


    // Construire l'arbre des catégories avec des livres associés
    public CategorieComposite construireArbreCategoriesAvecLivres(LivreDAO livreDAO) {
        String queryCategories = "SELECT * FROM categorie";
        Map<Integer, CategorieComposite> categoriesMap = new HashMap<>();
        CategorieComposite root = new CategorieComposite(0, "Racine");
        
        // Ajout de la racine au map
        categoriesMap.put(0, root);

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmtCategories = conn.prepareStatement(queryCategories);
             ResultSet rsCategories = stmtCategories.executeQuery()) {

            while (rsCategories.next()) {
                int id = rsCategories.getInt("ID_Categorie");
                String nom = rsCategories.getString("Nom");
                Integer parentId = rsCategories.getInt("ID_Categorie_Parent");
                if (rsCategories.wasNull()) parentId = null;

                CategorieComposite categorie = new CategorieComposite(id, nom);
                categoriesMap.put(id, categorie);

                if (parentId == null) {
                    root.ajouter(categorie);
                } else {
                    CategorieComposite parent = categoriesMap.get(parentId);
                    if (parent != null) {
                        parent.ajouter(categorie);
                        categorie.setParent(parent);
                    }
                }
            }

            // Associer les livres
            List<Livre> livres = livreDAO.getLivres();
            for (Livre livre : livres) {
                CategorieComposite categorie = categoriesMap.get(livre.getID_Categorie());
                if (categorie != null) {
                    categorie.ajouter(new LivreFeuille(livre));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des catégories et des livres.", e);
        }

        return root;
    }


    // Récupérer tous les noms des catégories
    public List<String> getAllCategorieNames() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT ID_Categorie, Nom FROM categorie";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID_Categorie");
                String nom = rs.getString("Nom");
                categories.add(id + " - " + nom);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des catégories.", e);
        }

        return categories;
    }
    


}
