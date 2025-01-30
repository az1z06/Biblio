package dao;

import model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public boolean reserverLivre(int idMembre, int idLivre) {
        String sql = "INSERT INTO reservation (ID_Livre, ID_Membre, Date_Reservation) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLivre);
            ps.setInt(2, idMembre);
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerReservation(int idLivre, int idMembre) {
        String sql = "DELETE FROM reservation WHERE ID_Livre = ? AND ID_Membre = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLivre);
            ps.setInt(2, idMembre);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(new Reservation(
                    rs.getInt("ID_Livre"),
                    rs.getInt("ID_Membre"),
                    rs.getDate("Date_Reservation")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public List<Reservation> rechercherReservations(String query) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE ID_Livre IN (SELECT ID_Livre FROM livre WHERE Titre LIKE ?) OR ID_Membre IN (SELECT ID_Membre FROM membre WHERE Nom LIKE ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservations.add(new Reservation(
                        rs.getInt("ID_Livre"),
                        rs.getInt("ID_Membre"),
                        rs.getDate("Date_Reservation")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}
