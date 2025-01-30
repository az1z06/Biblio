package controller;

import dao.MembreDAO;
import model.Membre;
import observateur.Subject;

import java.util.List;

public class MembreController extends Subject{
    private MembreDAO membreDAO;

    public MembreController(MembreDAO membreDAO) {
        this.membreDAO = membreDAO;
    }

    public boolean ajouterMembre(Membre membre) {
        if (membre.getEmail() == null || membre.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email du membre ne peut pas être vide.");
        }
        boolean success = membreDAO.ajouterMembre(membre);
        if (success) {
            notifyObservers(); // Notifie tous les observateurs
        }
        return success;
    }

    public List<Membre> getAllMembres() {
        return membreDAO.getMembres();
    }

    public List<Membre> rechercherMembres(String query) {
        return membreDAO.rechercherMembres(query);
    }

    public boolean mettreAJourMembre(Membre membre) {
        boolean success = membreDAO.mettreAJourMembre(membre);
        if (success) {
            notifyObservers(); // Notifie en cas de succès
        }
        return success;
    }

    public boolean supprimerMembre(int idMembre) {
        boolean success = membreDAO.supprimerMembre(idMembre);
        if (success) {
            notifyObservers(); // Notifie en cas de succès
        }
        return success;
    }

    public Membre getMembreById(int id) {
        return membreDAO.getMembreById(id);
    }
}
