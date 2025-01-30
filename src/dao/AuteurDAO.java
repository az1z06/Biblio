package dao;

import model.Auteur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuteurDAO {

    public boolean ajouterAuteur(Auteur auteur) {
        String query = "INSERT INTO auteur (Nom, Prenom, Date_Naissance) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, auteur.getNom());
            stmt.setString(2, auteur.getPrenom());
            stmt.setDate(3, new java.sql.Date(auteur.getDate_Naissance().getTime()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Auteur> getAuteurs() {
        List<Auteur> auteurs = new ArrayList<>();
        String query = "SELECT * FROM auteur";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                auteurs.add(new Auteur(
                        rs.getInt("ID_Auteur"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getDate("Date_Naissance")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auteurs;
    }

    public boolean mettreAJourAuteur(Auteur auteur) {
        String query = "UPDATE auteur SET Nom=?, Prenom=?, Date_Naissance=? WHERE ID_Auteur=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, auteur.getNom());
            stmt.setString(2, auteur.getPrenom());
            stmt.setDate(3, new java.sql.Date(auteur.getDate_Naissance().getTime()));
            stmt.setInt(4, auteur.getID_Auteur());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerAuteur(int idAuteur) {
        String query = "DELETE FROM auteur WHERE ID_Auteur=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idAuteur);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
