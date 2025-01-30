package dao;

import model.Editeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditeurDAO {

    public List<Editeur> getAllEditeurs() {
        List<Editeur> editeurs = new ArrayList<>();
        String query = "SELECT * FROM editeur";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                editeurs.add(new Editeur(
                    rs.getInt("ID_Editeur"),
                    rs.getString("Nom"),
                    rs.getString("Adresse") // Récupération de l'adresse
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return editeurs;
    }

    public boolean ajouterEditeur(Editeur editeur) {
        String query = "INSERT INTO editeur (Nom, Adresse) VALUES (?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, editeur.getNom());
            stmt.setString(2, editeur.getAdresse());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierEditeur(Editeur editeur) {
        String query = "UPDATE editeur SET Nom = ?, Adresse = ? WHERE ID_Editeur = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, editeur.getNom());
            stmt.setString(2, editeur.getAdresse());
            stmt.setInt(3, editeur.getIdEditeur());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerEditeur(int id) {
        String query = "DELETE FROM editeur WHERE ID_Editeur = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<String> getAllEditeurNames() {
        List<String> editeurNames = new ArrayList<>();
        String query = "SELECT ID_Editeur, Nom FROM editeur";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("ID_Editeur");
                String nom = rs.getString("Nom");
                editeurNames.add(id + " - " + nom); // Format ID - Nom
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return editeurNames;
    }
}
