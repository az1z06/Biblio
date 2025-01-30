package dao;

import model.Membre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MembreDAO {

    public boolean ajouterMembre(Membre membre) {
        String query = "INSERT INTO membre (Nom, Prenom, Email, Date_Inscription) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setDate(4, new java.sql.Date(membre.getDate_Inscription().getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        membre.setID_Membre(generatedId);
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

    public List<Membre> getMembres() {
        List<Membre> membres = new ArrayList<>();
        String query = "SELECT * FROM membre";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                membres.add(new Membre(
                    rs.getInt("ID_Membre"),
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getString("Email"),
                    rs.getDate("Date_Inscription")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membres;
    }

    public List<Membre> rechercherMembres(String query) {
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membre WHERE Nom LIKE ? OR Prenom LIKE ? OR Email LIKE ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            stmt.setString(3, "%" + query + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    membres.add(new Membre(
                        rs.getInt("ID_Membre"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Email"),
                        rs.getDate("Date_Inscription")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membres;
    }

    public boolean mettreAJourMembre(Membre membre) {
        String query = "UPDATE membre SET Nom=?, Prenom=?, Email=?, Date_Inscription=? WHERE ID_Membre=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, membre.getNom());
            stmt.setString(2, membre.getPrenom());
            stmt.setString(3, membre.getEmail());
            stmt.setDate(4, new java.sql.Date(membre.getDate_Inscription().getTime()));
            stmt.setInt(5, membre.getID_Membre());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerMembre(int idMembre) {
        String query = "DELETE FROM membre WHERE ID_Membre=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idMembre);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Membre getMembreById(int id) {
        String sql = "SELECT * FROM membre WHERE ID_Membre=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Membre(
                        rs.getInt("ID_Membre"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Email"),
                        rs.getDate("Date_Inscription")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
