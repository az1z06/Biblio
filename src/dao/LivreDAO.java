package dao;

import model.Livre;
import model.LivreAudio;
import model.LivreNumerique;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreDAO {

    // Ajouter un livre
    public boolean ajouterLivre(Livre livre) {
        String query = "INSERT INTO livre (Titre, Annee_Publication, ISBN, ID_Editeur, ID_Categorie, Type_Livre) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, livre.getTitre());
            stmt.setInt(2, livre.getAnnee_Publication());
            stmt.setString(3, livre.getISBN());
            stmt.setInt(4, livre.getID_Editeur());
            stmt.setInt(5, livre.getID_Categorie());

            // Déterminer le type de livre
            if (livre instanceof LivreNumerique) {
                stmt.setString(6, "Numerique");
            } else if (livre instanceof LivreAudio) {
                stmt.setString(6, "Audio");
            } else {
                stmt.setString(6, "Physique");
            }

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        livre.setID_Livre(newId);
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer tous les livres
    public List<Livre> getLivres() {
        List<Livre> livres = new ArrayList<>();
        String query = "SELECT l.ID_Livre, l.Titre, l.Annee_Publication, l.ISBN, l.ID_Editeur, e.Nom AS Nom_Editeur, " +
                       "l.ID_Categorie, c.Nom AS Nom_Categorie, l.Type_Livre " +
                       "FROM livre l " +
                       "LEFT JOIN editeur e ON l.ID_Editeur = e.ID_Editeur " +
                       "LEFT JOIN categorie c ON l.ID_Categorie = c.ID_Categorie";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString("Type_Livre");
                Livre livre;

                // Créer l'objet Livre en fonction du type
                if ("Numerique".equalsIgnoreCase(type)) {
                    livre = new LivreNumerique(
                        rs.getInt("ID_Livre"), rs.getString("Titre"), rs.getInt("Annee_Publication"),
                        rs.getString("ISBN"), rs.getInt("ID_Editeur"), rs.getInt("ID_Categorie"),
                        "PDF", 1.5 // Remplace par les valeurs réelles si présentes
                    );
                } else if ("Audio".equalsIgnoreCase(type)) {
                    livre = new LivreAudio(
                        rs.getInt("ID_Livre"), rs.getString("Titre"), rs.getInt("Annee_Publication"),
                        rs.getString("ISBN"), rs.getInt("ID_Editeur"), rs.getInt("ID_Categorie"),
                        2.0, "Narrateur inconnu" // Remplace par les valeurs réelles si présentes
                    );
                } else {
                    livre = new Livre(
                        rs.getInt("ID_Livre"), rs.getString("Titre"), rs.getInt("Annee_Publication"),
                        rs.getString("ISBN"), rs.getInt("ID_Editeur"), rs.getInt("ID_Categorie")
                    );
                }

                // Ajouter les noms pour affichage dans l'interface
                livre.setNomCategorie(rs.getString("Nom_Categorie"));
                livre.setNomEditeur(rs.getString("Nom_Editeur"));
                livres.add(livre);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }
 
    // Rechercher des livres
    public List<Livre> rechercherLivres(String query) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre WHERE Titre LIKE ? OR ISBN LIKE ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    livres.add(new Livre(
                        rs.getInt("ID_Livre"),
                        rs.getString("Titre"),
                        rs.getInt("Annee_Publication"),
                        rs.getString("ISBN"),
                        rs.getInt("ID_Editeur"),
                        rs.getInt("ID_Categorie")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    // Mettre à jour un livre
    public boolean mettreAJourLivre(Livre livre) {
        String query = "UPDATE livre SET Titre=?, Annee_Publication=?, ISBN=?, ID_Editeur=?, ID_Categorie=?, Type_Livre=? WHERE ID_Livre=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, livre.getTitre());
            statement.setInt(2, livre.getAnnee_Publication());
            statement.setString(3, livre.getISBN());
            statement.setInt(4, livre.getID_Editeur());
            statement.setInt(5, livre.getID_Categorie());

            // Déterminer le type de livre
            if (livre instanceof LivreNumerique) {
                statement.setString(6, "Numerique");
            } else if (livre instanceof LivreAudio) {
                statement.setString(6, "Audio");
            } else {
                statement.setString(6, "Physique");
            }

            statement.setInt(7, livre.getID_Livre());
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un livre
    public boolean supprimerLivre(int idLivre) {
        String query = "DELETE FROM livre WHERE ID_Livre = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, idLivre);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer un livre par ID
    public Livre getLivreById(int id) {
        String sql = "SELECT * FROM livre WHERE ID_Livre = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Livre(
                        rs.getInt("ID_Livre"),
                        rs.getString("Titre"),
                        rs.getInt("Annee_Publication"),
                        rs.getString("ISBN"),
                        rs.getInt("ID_Editeur"),
                        rs.getInt("ID_Categorie")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
