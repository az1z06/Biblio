package dao;

import command.BorrowCommand;
import command.Command;
import command.ReturnCommand;
import model.Emprunt;
import java.sql.*;

public class HistoriqueCommandeDAO {
	private EmpruntDAO empruntDAO;
	
    public HistoriqueCommandeDAO(EmpruntDAO empruntDAO) {
        this.empruntDAO = empruntDAO;
    }

    public HistoriqueCommandeDAO() {
        
    }
    public void ajouterHistorique(Command cmd) {
        String query = "INSERT INTO historique_commandes (ID_Emprunt, Nom_Livre, Nom_Membre, Etat_Commande) " +
                       "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            Emprunt emprunt = cmd.getEmprunt();
            if (emprunt.getLivre() == null || emprunt.getMembre() == null) {
                throw new IllegalArgumentException("Le livre ou le membre n'est pas correctement initialisé pour l'emprunt.");
            }

            stmt.setInt(1, emprunt.getID_Emprunt());
            stmt.setString(2, emprunt.getLivre().getTitre());
            stmt.setString(3, emprunt.getMembre().getNom());
            stmt.setString(4, "EN_COURS");

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'historique des commandes.", e);
        }
    }


    public void mettreAJourEtatCommande(int idEmprunt, String etat) {
        String query = "UPDATE historique_commandes SET Etat_Commande = ? WHERE ID_Emprunt = ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, etat);
            stmt.setInt(2, idEmprunt);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'état de la commande.", e);
        }
    }

    public ResultSet getHistorique() {
        String query = "SELECT * FROM historique_commandes";

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            return stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'historique des commandes.", e);
        }
    }
    
    public Command getCommandByIndex(int index) {
        String query = "SELECT * FROM historique_commandes LIMIT ?, 1";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, index);

            // Variables pour stocker les données du ResultSet
            int idEmprunt = -1;
            String etatCommande = null;

            // Récupérer les données depuis le ResultSet
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idEmprunt = rs.getInt("ID_Emprunt");
                    etatCommande = rs.getString("Etat_Commande");
                }
            }

            // Si aucune commande trouvée
            if (idEmprunt == -1 || etatCommande == null) {
                throw new RuntimeException("Aucune commande trouvée pour l'index : " + index);
            }

            // Récupérer l'objet Emprunt après avoir fermé le ResultSet
            Emprunt emprunt = empruntDAO.getEmpruntById(idEmprunt);

            // Créer et retourner la commande appropriée
            return etatCommande.equals("EN_COURS")
                ? new BorrowCommand(emprunt, new EmpruntDAO())
                : new ReturnCommand(emprunt, new EmpruntDAO());

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la commande.", e);
        }
    }


}
