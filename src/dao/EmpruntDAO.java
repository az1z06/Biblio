package dao;

import model.Emprunt;
import model.Livre;
import model.Membre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {

    // Ajouter un emprunt
    public boolean ajouterEmprunt(Emprunt emprunt) {
        String query = "INSERT INTO emprunt (ID_Livre, ID_Membre, Date_Emprunt, Date_Retour_Prevue, Date_Retour_Effective) "
                     + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             // Préparer le statement avec RETURN_GENERATED_KEYS
             PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Assigner les paramètres
            statement.setInt(1, emprunt.getID_Livre());
            statement.setInt(2, emprunt.getID_Membre());
            statement.setDate(3, emprunt.getDate_Emprunt());
            statement.setDate(4, emprunt.getDate_Retour_Prevue());
            statement.setDate(5, emprunt.getDate_Retour_Effective());

            // Exécuter l'INSERT
            int affectedRows = statement.executeUpdate();

            // Vérifier si l'insertion a réussi
            if (affectedRows > 0) {
                // Récupérer les clés générées
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1); // Récupérer l'ID (colonne 1)
                        emprunt.setID_Emprunt(generatedId); // Assigner l'ID à l'objet Emprunt
                    }
                }
                return true; // L'insertion a bien eu lieu
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer tous les emprunts
    public List<Emprunt> getEmprunts() {
        List<Emprunt> emprunts = new ArrayList<>();
        String query = "SELECT e.*, l.Titre AS Nom_Livre, m.Nom AS Nom_Membre " +
                "FROM emprunt e " +
                "JOIN livre l ON e.ID_Livre = l.ID_Livre " +
                "JOIN membre m ON e.ID_Membre = m.ID_Membre";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
            	Livre livre = new Livre();
                livre.setTitre(rs.getString("Nom_Livre"));

                Membre membre = new Membre();
                membre.setNom(rs.getString("Nom_Membre"));
                Emprunt emprunt = new Emprunt(
                    rs.getInt("ID_Emprunt"),
                    rs.getInt("ID_Livre"),
                    rs.getInt("ID_Membre"),
                    rs.getDate("Date_Emprunt"),
                    rs.getDate("Date_Retour_Prevue"),
                    rs.getDate("Date_Retour_Effective"),
                    livre,
                    membre
                );
                emprunts.add(emprunt);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprunts;
    }

    // Rechercher des emprunts
    public List<Emprunt> rechercherEmprunts(String queryParam) {
        List<Emprunt> emprunts = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection()) {

        	String query = "SELECT e.*, l.Titre AS Nom_Livre, m.Nom AS Nom_Membre " +
                    "FROM emprunt e " +
                    "JOIN livre l ON e.ID_Livre = l.ID_Livre " +
                    "JOIN membre m ON e.ID_Membre = m.ID_Membre " +
                    "WHERE l.Titre LIKE ? OR m.Nom LIKE ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, "%" + queryParam + "%");
                statement.setString(2, "%" + queryParam + "%");
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                    	Livre livre = new Livre();
                        livre.setTitre(rs.getString("Nom_Livre"));

                        Membre membre = new Membre();
                        membre.setNom(rs.getString("Nom_Membre"));
                        
                        Emprunt emprunt = new Emprunt(
                                rs.getInt("ID_Emprunt"),
                                rs.getInt("ID_Livre"),
                                rs.getInt("ID_Membre"),
                                rs.getDate("Date_Emprunt"),
                                rs.getDate("Date_Retour_Prevue"),
                                rs.getDate("Date_Retour_Effective"),
                                livre,
                                membre
                            );
                            emprunts.add(emprunt);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emprunts;
    }

    // Modifier un emprunt
    public boolean modifierEmprunt(Emprunt emprunt) {
        String sql = "UPDATE emprunt SET ID_Livre=?, ID_Membre=?, Date_Emprunt=?, Date_Retour_Prevue=?, Date_Retour_Effective=? "
                   + "WHERE ID_Emprunt=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, emprunt.getID_Livre());
            stmt.setInt(2, emprunt.getID_Membre());
            stmt.setDate(3, emprunt.getDate_Emprunt());
            stmt.setDate(4, emprunt.getDate_Retour_Prevue());
            stmt.setDate(5, emprunt.getDate_Retour_Effective());
            stmt.setInt(6, emprunt.getID_Emprunt());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un emprunt
    public boolean supprimerEmprunt(int idEmprunt) {
        String query = "DELETE FROM emprunt WHERE ID_Emprunt=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idEmprunt);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Emprunt getEmpruntById(int idEmprunt) {
        String query = "SELECT e.*, l.Titre AS Nom_Livre, m.Nom AS Nom_Membre " +
                       "FROM emprunt e " +
                       "JOIN livre l ON e.ID_Livre = l.ID_Livre " +
                       "JOIN membre m ON e.ID_Membre = m.ID_Membre " +
                       "WHERE e.ID_Emprunt = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idEmprunt);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Livre livre = new Livre();
                    livre.setTitre(rs.getString("Nom_Livre"));

                    Membre membre = new Membre();
                    membre.setNom(rs.getString("Nom_Membre"));

                    return new Emprunt(
                        rs.getInt("ID_Emprunt"),
                        rs.getInt("ID_Livre"),
                        rs.getInt("ID_Membre"),
                        rs.getDate("Date_Emprunt"),
                        rs.getDate("Date_Retour_Prevue"),
                        rs.getDate("Date_Retour_Effective")
                        
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'emprunt.", e);
        }
        return null;
    }


}
